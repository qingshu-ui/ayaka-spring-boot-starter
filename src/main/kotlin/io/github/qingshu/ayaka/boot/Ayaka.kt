package io.github.qingshu.ayaka.boot

import com.alibaba.fastjson2.JSONObject
import io.github.qingshu.ayaka.bot.BotContainer
import io.github.qingshu.ayaka.bot.BotFactory
import io.github.qingshu.ayaka.bot.BotSessionFactory
import io.github.qingshu.ayaka.config.*
import io.github.qingshu.ayaka.dto.event.EventFactory
import io.github.qingshu.ayaka.handler.WebsocketClientHandler
import io.github.qingshu.ayaka.handler.WebsocketServerHandler
import io.github.qingshu.ayaka.task.ScheduledTask
import kotlinx.coroutines.*
import meteordevelopment.orbit.EventBus
import meteordevelopment.orbit.IEventBus
import meteordevelopment.orbit.listeners.LambdaListener
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean
import java.lang.invoke.MethodHandles
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
class Ayaka {

    companion object {
        private val log = LoggerFactory.getLogger(Ayaka::class.java)
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = ["ayaka.ws.client.enable"], havingValue = "true")
    fun websocketClientHandler(
        websocketProperties: WebsocketProperties,
        botContainer: BotContainer,
        botFactory: BotFactory,
        eventFactory: EventFactory,
        coroutine: CoroutineScope,
        dispatcher: CoroutineDispatcher,
        botSessionFactory: BotSessionFactory,
    ) = WebsocketClientHandler(
        wsp = websocketProperties,
        botContainer = botContainer,
        botFactory = botFactory,
        eventFactory = eventFactory,
        coroutine = coroutine,
        dispatcher = dispatcher,
        botSessionFactory = botSessionFactory,
    )

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = ["ayaka.ws.server.enable"], havingValue = "true")
    fun websocketServerHandler(
        ayakaProperties: AyakaProperties,
        botContainer: BotContainer,
        botFactory: BotFactory,
        scheduledTask: ScheduledTask,
        websocketProperties: WebsocketProperties,
        eventFactory: EventFactory,
        coroutine: CoroutineScope,
        dispatcher: CoroutineDispatcher,
        botSessionFactory: BotSessionFactory,
    ) = WebsocketServerHandler(
        ayaka = ayakaProperties,
        botContainer = botContainer,
        botFactory = botFactory,
        scheduledTask = scheduledTask,
        websocketProperties = websocketProperties,
        eventFactory = eventFactory,
        coroutine = coroutine,
        dispatcher = dispatcher,
        botSessionFactory = botSessionFactory,
    )

    @Bean
    @ConditionalOnMissingBean
    fun createWebSocketServerContainer(
        websocketProperties: WebsocketProperties, websocketServerProperties: WebsocketServerProperties
    ) = ServletServerContainerFactoryBean().apply {
        setMaxTextMessageBufferSize(websocketProperties.maxTextMessageBufferSize)
        setMaxBinaryMessageBufferSize(websocketProperties.maxBinaryMessageBufferSize)
        setMaxSessionIdleTimeout(websocketServerProperties.maxSessionIdleTimeout)
    }

    @Bean
    @ConditionalOnMissingBean
    fun echoMap(): ConcurrentHashMap<String, CompletableDeferred<JSONObject>> {
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

    @Bean
    fun coroutineScope(dispatcher: CoroutineDispatcher): CoroutineScope {
        return CoroutineScope(dispatcher + SupervisorJob())
    }

    @Bean
    fun dispatcher(@Qualifier("ayakaTaskExecutor") executor: ThreadPoolTaskExecutor): CoroutineDispatcher {
        return executor.asCoroutineDispatcher()
    }
}