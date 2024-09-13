package io.github.qingshu.ayaka.utils

import io.github.qingshu.ayaka.bot.Bot
import io.github.qingshu.ayaka.bot.BotFactory
import io.github.qingshu.ayaka.bot.BotSessionFactory
import io.github.qingshu.ayaka.bot.BotSessionImpl
import io.github.qingshu.ayaka.dto.constant.AdapterEnum
import io.github.qingshu.ayaka.dto.constant.Connection
import io.github.qingshu.ayaka.dto.constant.SessionStatusEnum
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

private class ConnectionUtils

private val log: Logger = LoggerFactory.getLogger(ConnectionUtils::class.java)

fun checkToken(session: WebSocketSession, token: String): Boolean {
    if (token.isEmpty()) {
        return true
    }
    val clientToken = session.handshakeHeaders["authorization"]?.get(0)
    if (clientToken.isNullOrEmpty()) {
        return false
    }
    return token.equals(clientToken, ignoreCase = true)
}

fun handleFirstConnect(
    xSelfId: Long,
    session: WebSocketSession,
    botFactory: BotFactory,
    botSessionFactory: BotSessionFactory
): Bot {
    log.info("{} connected", xSelfId)
    val botSession = botSessionFactory.createSession(session)
    val bot = botFactory.createBot(xSelfId, botSession)
    return bot
}

fun handleReConnect(bot: Bot, xSelfId: Long, session: WebSocketSession, botSessionFactory: BotSessionFactory) {
    log.info("Received reconnect from {}", xSelfId)
    lateinit var oldContext: MutableMap<String, Any>
    if (bot.session is BotSessionImpl<*>) {
        val botSession = (bot.session as BotSessionImpl<*>).session
        if (botSession is WebSocketSession) {
            oldContext = botSession.attributes
        }
    }
//    val oldContext = bot.session.attributes
    val status = oldContext[Connection.SESSION_STATUS_KEY] as? SessionStatusEnum
    if (status == SessionStatusEnum.ONLINE) {
        session.close()
        return
    }
    oldContext.computeIfPresent(Connection.FUTURE_KEY) { _, obj ->
        val future = obj as ScheduledFuture<*>
        if (future.getDelay(TimeUnit.MILLISECONDS) > 0) {
            future.cancel(false)
        }
        null
    }
    oldContext.clear()
    bot.session = botSessionFactory.createSession(session)
}

fun parseSelfId(session: WebSocketSession): Long {
    val xSelfIdStr = session.handshakeHeaders["x-self-id"]?.get(0) ?: session.attributes["x-self-id"].toString()
    return try {
        xSelfIdStr.toLong()
    }catch (e: NumberFormatException) {
        0L
    }
}

fun getAdapter(session: WebSocketSession): AdapterEnum {
    val context = session.attributes
    return when (val adapterObj = context[Connection.ADAPTER_KEY]) {
        is AdapterEnum -> adapterObj
        else -> throw UnsupportedOperationException("adapter type wrong")
    }
}

fun getSessionStatus(session: WebSocketSession): SessionStatusEnum {
    val context = session.attributes
    return when (val statusObj = context.getOrDefault(Connection.SESSION_STATUS_KEY, SessionStatusEnum.DIE)) {
        is SessionStatusEnum -> statusObj
        else -> throw UnsupportedOperationException("session status type wrong")
    }
}