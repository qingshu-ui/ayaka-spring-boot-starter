package io.github.qingshu.ayaka.boot

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.qingshu.ayaka.bot.BotContainer
import io.github.qingshu.ayaka.bot.BotFactory
import io.github.qingshu.ayaka.config.AyakaProperties
import io.github.qingshu.ayaka.config.WebsocketProperties
import io.github.qingshu.ayaka.config.WebsocketServerProperties
import io.github.qingshu.ayaka.dto.event.EventFactory
import io.github.qingshu.ayaka.handler.WebsocketClientHandler
import io.github.qingshu.ayaka.handler.WebsocketServerHandler
import io.github.qingshu.ayaka.task.ScheduledTask
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
class AyakaBeans(
    private val ayakaP: AyakaProperties,
    private val botContainer: BotContainer,
    private val botFactory: BotFactory,
    private val wsP: WebsocketProperties,
    private val wssP: WebsocketServerProperties,
    private val scheduledTask: ScheduledTask,
    private val jsonParser: ObjectMapper,
    private val eventFactory: EventFactory
) {

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = ["ayaka.ws.client.enable"], havingValue = "true")
    fun websocketClientHandler(): WebsocketClientHandler {
        return WebsocketClientHandler(
            wsp = wsP,
            botContainer = botContainer,
            botFactory = botFactory,
            eventFactory = eventFactory,
        )
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = ["ayaka.ws.server.enable"], havingValue = "true")
    fun websocketServerHandler(): WebsocketServerHandler {
        return WebsocketServerHandler(
            ayaka = ayakaP,
            botContainer = botContainer,
            botFactory = botFactory,
            scheduledTask = scheduledTask,
            websocketProperties = wsP,
            eventFactory = eventFactory
        )
    }

    @Bean
    @ConditionalOnMissingBean
    @Lazy
    fun createWebSocketServerContainer(): ServletServerContainerFactoryBean? {
        val container = ServletServerContainerFactoryBean()
        container.setMaxTextMessageBufferSize(wsP.maxTextMessageBufferSize)
        container.setMaxBinaryMessageBufferSize(wsP.maxBinaryMessageBufferSize)
        container.setMaxSessionIdleTimeout(wssP.maxSessionIdleTimeout)
        return container
    }


}