package io.github.qingshu.ayaka.boot

import com.alibaba.fastjson2.JSONObject
import com.fasterxml.jackson.databind.util.LRUMap
import io.github.qingshu.ayaka.config.AsyncTaskProperties
import io.github.qingshu.ayaka.config.PluginProperties
import meteordevelopment.orbit.EventBus
import meteordevelopment.orbit.IEventBus
import meteordevelopment.orbit.listeners.LambdaListener
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
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
@Configuration
class AyakaBeanProvider {

    @Bean
    @ConditionalOnMissingBean
    fun echoMap(): LRUMap<String, CompletableFuture<JSONObject>> {
        return LRUMap(128, 1024)
    }


    @Bean("ayakaTaskExecutor")
    @ConditionalOnProperty(value = ["ayaka.task.enable-async"], havingValue = "true", matchIfMissing = true)
    fun createExecutorService(
        poolProperties: AsyncTaskProperties,
    ) = ThreadPoolTaskExecutor().apply {
        corePoolSize = poolProperties.corePoolSize
        maxPoolSize = poolProperties.maxPoolSize
        queueCapacity = poolProperties.workQueueSize
        keepAliveSeconds = poolProperties.keepAliveTime
        setThreadNamePrefix(poolProperties.threadNamePrefix)
        setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy())
        setWaitForTasksToCompleteOnShutdown(true)
        initialize()
    }

    @Bean
    @ConditionalOnMissingBean
    fun createEventBus(pluginProperties: PluginProperties): IEventBus {

        val lambdaFactory: LambdaListener.Factory = LambdaListener.Factory { lookupInMethod, klass ->
            lookupInMethod.invoke(null, klass, MethodHandles.lookup()) as MethodHandles.Lookup
        }

        val bus = EventBus()
        if (pluginProperties.pluginPackage.isNotEmpty()) {
            bus.registerLambdaFactory(pluginProperties.pluginPackage, lambdaFactory)
        } else {
            log.warn("No event scan package found")
        }
        return bus
    }

    companion object {
        private val log = LoggerFactory.getLogger(AyakaBeanProvider::class.java)
    }

}