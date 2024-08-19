package io.github.qingshu.ayaka.boot

import com.alibaba.fastjson2.JSONObject
import com.fasterxml.jackson.databind.util.LRUMap
import io.github.qingshu.ayaka.config.AsyncTaskProperties
import meteordevelopment.orbit.EventBus
import meteordevelopment.orbit.IEventBus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component
import java.lang.invoke.MethodHandles
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ThreadPoolExecutor


/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Component
class AyakaBeanProvider @Autowired constructor(
    private val poolProperties: AsyncTaskProperties
) {

    @Bean
    @ConditionalOnMissingBean
    fun echoMap() : LRUMap<String, CompletableFuture<JSONObject>> {
        return LRUMap(128, 1024)
    }


    @Bean("ayakaTaskExecutor")
    @ConditionalOnProperty(value = ["ayaka.task.enableAsync"], havingValue = "true", matchIfMissing = true)
    fun createExecutorService(): ThreadPoolTaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = poolProperties.corePoolSize
        executor.maxPoolSize = poolProperties.maxPoolSize
        executor.queueCapacity = poolProperties.workQueueSize
        executor.keepAliveSeconds = poolProperties.keepAliveTime
        executor.setThreadNamePrefix(poolProperties.threadNamePrefix)
        executor.setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy())
        executor.setWaitForTasksToCompleteOnShutdown(true)
        executor.initialize()
        return executor
    }


}