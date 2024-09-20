package io.github.qingshu.ayaka.service.impl

import io.github.qingshu.ayaka.service.ScheduledTask
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Service
import java.util.concurrent.ScheduledThreadPoolExecutor

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the MIT License.
 * See the LICENSE file for details.
 */
@Service
class ScheduledTaskImpl(
    @Qualifier("ayakaTaskExecutor") private val ayakaExecutor: ThreadPoolTaskExecutor
): ScheduledTask {

    private var executor: ScheduledThreadPoolExecutor? = null

    override fun executor(): ScheduledThreadPoolExecutor {
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