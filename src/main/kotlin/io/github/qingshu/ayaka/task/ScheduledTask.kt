package io.github.qingshu.ayaka.task

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import java.util.concurrent.ScheduledThreadPoolExecutor

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Component
class ScheduledTask @Autowired constructor(
    @Qualifier("ayakaTaskExecutor") private val ayakaExecutor: ThreadPoolTaskExecutor
) {
    private var executor: ScheduledThreadPoolExecutor? = null

    fun executor(): ScheduledThreadPoolExecutor {
        return executor ?: run {
            val newExecutor = ScheduledThreadPoolExecutor(
                ayakaExecutor.corePoolSize, ayakaExecutor.threadPoolExecutor.threadFactory
            ).apply {
                removeOnCancelPolicy = true
            }
            executor = newExecutor
            newExecutor
        }
    }
}