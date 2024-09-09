package io.github.qingshu.ayaka.config

import com.alibaba.fastjson2.JSONObject
import io.github.qingshu.ayaka.propreties.AsyncTaskProperties
import io.github.qingshu.ayaka.propreties.PluginProperties
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
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ThreadPoolExecutor
import kotlin.system.exitProcess


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
    fun echoMap(): ConcurrentHashMap<String, CompletableFuture<JSONObject>> {
        return ConcurrentHashMap()
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
            log.warn("The 'BotPlugin' package path is not specified.")
            exitProcess(-1)
        }
        return bus
    }

    companion object {
        private val log = LoggerFactory.getLogger(AyakaBeanProvider::class.java)
    }

}