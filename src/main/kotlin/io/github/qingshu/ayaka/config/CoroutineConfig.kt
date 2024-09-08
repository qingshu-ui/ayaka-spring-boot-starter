package io.github.qingshu.ayaka.config

import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Configuration
class CoroutineConfig {

    @Bean
    fun coroutineScope(): CoroutineScope {
        return CoroutineScope(Dispatchers.IO + SupervisorJob())
    }

    @Bean
    fun dispatcher(@Qualifier("ayakaTaskExecutor") executor: ThreadPoolTaskExecutor): CoroutineDispatcher {
        return executor.asCoroutineDispatcher()
    }
}