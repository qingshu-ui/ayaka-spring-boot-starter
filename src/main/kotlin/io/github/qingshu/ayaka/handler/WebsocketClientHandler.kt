package io.github.qingshu.ayaka.handler

import com.alibaba.fastjson2.JSON
import io.github.qingshu.ayaka.bot.BotContainer
import io.github.qingshu.ayaka.bot.BotFactory
import io.github.qingshu.ayaka.config.WebsocketProperties
import io.github.qingshu.ayaka.dto.constant.AdapterEnum
import io.github.qingshu.ayaka.dto.constant.Connection
import io.github.qingshu.ayaka.dto.event.EventFactory
import io.github.qingshu.ayaka.utils.parseSelfId
import org.slf4j.LoggerFactory
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
class WebsocketClientHandler(
    private val wsp: WebsocketProperties,
    private val botFactory: BotFactory,
    private val botContainer: BotContainer,
    private val eventFactory: EventFactory
) : TextWebSocketHandler() {

    override fun afterConnectionEstablished(session: WebSocketSession) {
        session.textMessageSizeLimit = wsp.maxTextMessageBufferSize
        session.binaryMessageSizeLimit = wsp.maxBinaryMessageBufferSize
        session.attributes[Connection.ADAPTER_KEY] = AdapterEnum.CLIENT
        val xSelfId = parseSelfId(session)
        if (xSelfId == 0L) {
            return
        }
        val bot = botFactory.createBot(xSelfId, session)
        botContainer.bots[xSelfId] = bot
        log.info("{} connected", xSelfId)
    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        botContainer.bots.entries.firstOrNull()?.let { bot ->
            log.warn("{} disconnected", bot.key)
            botContainer.bots.clear()
        }
    }

    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        var xSelfId = parseSelfId(session)
        if (xSelfId == 0L) {
            val valid = JSON.isValid(message.payload)
            if (valid) {
                val selfId = JSON.parseObject(message.payload).getOrDefault("self_id", "").toString()
                session.attributes["x-self-id"] = selfId
                xSelfId = parseSelfId(session)
            }
            if (!botContainer.bots.containsKey(xSelfId)) {
                afterConnectionEstablished(session)
            }
        }
        val result = JSON.parseObject(message.payload)
        eventFactory.postEvent(xSelfId, result)
    }

    companion object {
        private val log = LoggerFactory.getLogger(WebsocketClientHandler::class.java)
    }

}