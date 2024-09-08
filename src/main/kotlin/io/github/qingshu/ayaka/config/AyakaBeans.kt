package io.github.qingshu.ayaka.config

import io.github.qingshu.ayaka.bot.BotContainer
import io.github.qingshu.ayaka.bot.BotFactory
import io.github.qingshu.ayaka.dto.event.EventFactory
import io.github.qingshu.ayaka.handler.WebsocketClientHandler
import io.github.qingshu.ayaka.handler.WebsocketServerHandler
import io.github.qingshu.ayaka.propreties.AyakaProperties
import io.github.qingshu.ayaka.propreties.WebsocketProperties
import io.github.qingshu.ayaka.propreties.WebsocketServerProperties
import io.github.qingshu.ayaka.task.ScheduledTask
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Configuration
class AyakaBeans {

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
    ) = WebsocketClientHandler(
        wsp = websocketProperties,
        botContainer = botContainer,
        botFactory = botFactory,
        eventFactory = eventFactory,
        coroutine = coroutine,
        dispatcher = dispatcher,
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
    ) = WebsocketServerHandler(
        ayaka = ayakaProperties,
        botContainer = botContainer,
        botFactory = botFactory,
        scheduledTask = scheduledTask,
        websocketProperties = websocketProperties,
        eventFactory = eventFactory,
        coroutine = coroutine,
        dispatcher = dispatcher,
        )
    @Bean
    @ConditionalOnMissingBean
    @Lazy
    fun createWebSocketServerContainer(
        websocketProperties: WebsocketProperties, websocketServerProperties: WebsocketServerProperties
    ) = ServletServerContainerFactoryBean().apply {
        setMaxTextMessageBufferSize(websocketProperties.maxTextMessageBufferSize)
        setMaxBinaryMessageBufferSize(websocketProperties.maxBinaryMessageBufferSize)
        setMaxSessionIdleTimeout(websocketServerProperties.maxSessionIdleTimeout)
    }

}