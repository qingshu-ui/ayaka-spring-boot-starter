package io.github.qingshu.ayaka.handler

import com.alibaba.fastjson2.JSONObject
import io.github.qingshu.ayaka.bot.BotContainer
import io.github.qingshu.ayaka.bot.BotFactory
import io.github.qingshu.ayaka.config.AyakaProperties
import io.github.qingshu.ayaka.config.WebsocketProperties
import io.github.qingshu.ayaka.dto.constant.AdapterEnum
import io.github.qingshu.ayaka.dto.constant.Connection
import io.github.qingshu.ayaka.dto.constant.SessionStatusEnum
import io.github.qingshu.ayaka.event.EventFactory
import io.github.qingshu.ayaka.task.ScheduledTask
import io.github.qingshu.ayaka.utils.checkToken
import io.github.qingshu.ayaka.utils.handleFirstConnect
import io.github.qingshu.ayaka.utils.handleReConnect
import io.github.qingshu.ayaka.utils.parseSelfId
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.concurrent.TimeUnit

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
class WebsocketServerHandler(
    private val ayaka: AyakaProperties,
    private val botContainer: BotContainer,
    private val botFactory: BotFactory,
    private val scheduledTask: ScheduledTask,
    private val websocketProperties: WebsocketProperties,
    private val eventFactory: EventFactory,
) : TextWebSocketHandler() {

    override fun afterConnectionEstablished(session: WebSocketSession) {
        session.attributes[Connection.ADAPTER_KEY] = AdapterEnum.SERVER
        val xSelfId = parseSelfId(session)
        if (xSelfId == 0L) {
            session.close()
            return
        }
        if (!checkToken(session, websocketProperties.accessToken)) {
            session.close()
            return
        }
        val context = session.attributes
        context[Connection.SESSION_STATUS_KEY] = SessionStatusEnum.ONLINE

        if (ayaka.reConnectInterval <= 0) {
            if (botContainer.bots.containsKey(xSelfId)) {
                context.clear()
                session.close()
            } else {
                val bot = handleFirstConnect(xSelfId, session, botFactory)
                botContainer.bots[xSelfId] = bot
            }
            return
        }

        botContainer.bots.compute(xSelfId) { _, bot ->
            bot ?: return@compute handleFirstConnect(xSelfId, session, botFactory)
            handleReConnect(bot, xSelfId, session)
            bot
        }

    }

    override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
        val xSelfId = session.handshakeHeaders["x-self-id"]?.get(0)?.toLong() ?: 0L
        if (xSelfId != 0L || !botContainer.bots.containsKey(xSelfId)) {
            return
        }

        if (ayaka.reConnectInterval <= 0) {
            botContainer.bots.remove(xSelfId)
            log.warn("{} is disconnected", xSelfId)
            return
        }
        val schedule = scheduledTask.executor().schedule({
            if (botContainer.bots.containsKey(xSelfId)) {
                botContainer.bots.remove(xSelfId)
            }
        }, ayaka.reConnectInterval.toLong(), TimeUnit.SECONDS)
        session.attributes[Connection.SESSION_STATUS_KEY] = SessionStatusEnum.OFFLINE
        session.attributes[Connection.FUTURE_KEY] = schedule
    }


    override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
        val xSelfId = parseSelfId(session)
        val result = JSONObject.parseObject(message.payload)
        eventFactory.postEvent(xSelfId, result)
    }

    companion object {
        val log: Logger = LoggerFactory.getLogger(WebsocketServerHandler::class.java)
    }
}
