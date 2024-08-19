package io.github.qingshu.ayaka.boot

import io.github.qingshu.ayaka.config.WebsocketClientProperties
import io.github.qingshu.ayaka.config.WebsocketProperties
import io.github.qingshu.ayaka.config.WebsocketServerProperties
import io.github.qingshu.ayaka.handler.WebsocketClientHandler
import io.github.qingshu.ayaka.handler.WebsocketServerHandler
import io.github.qingshu.ayaka.task.ScheduledTask
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.client.WebSocketConnectionManager
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import java.util.concurrent.TimeUnit
import kotlin.system.exitProcess

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@EnableAsync
@Configuration
@EnableWebSocket
@ComponentScan("io.github.qingshu.ayaka")
class AyakaAutoConfiguration @Autowired constructor(
    private val wsP: WebsocketProperties,
    private val wssP: WebsocketServerProperties,
    private val wscP: WebsocketClientProperties,
    @Autowired(required = false) private val wssH: WebsocketServerHandler?,
    @Autowired(required = false) private val wscH: WebsocketClientHandler?,
    private val scheduledTask: ScheduledTask,
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        if (wssP.enable && wscP.enable) {
            log.error("Cannot enable ws client and server simultaneously")
            exitProcess(0)
        }

        if (wssP.enable) {
            registry.addHandler(wssH as WebSocketHandler, wssP.url).setAllowedOrigins("*")
        }

        if (wscP.enable) {
            createWebsocketClient()
        }
    }

    private fun createWebsocketClient() {
        val client = StandardWebSocketClient()
        val headers = WebSocketHttpHeaders()
        if (wsP.accessToken != "") {
            headers["Authorization"] = wsP.accessToken()
        }
        val manager = WebSocketConnectionManager(client, wscH as WebSocketHandler, wscP.url)
        manager.headers = headers
        manager.isAutoStartup = true

        scheduledTask.executor().scheduleAtFixedRate({
            if (!manager.isConnected) {
                manager.startInternal()
            }
        }, 0, wscP.reconnectInterval.toLong(), TimeUnit.SECONDS)
    }

    companion object {
        private val log = LoggerFactory.getLogger(AyakaAutoConfiguration::class.java)
    }

}