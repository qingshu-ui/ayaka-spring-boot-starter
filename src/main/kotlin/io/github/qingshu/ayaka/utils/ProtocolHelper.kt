package io.github.qingshu.ayaka.utils

import com.alibaba.fastjson2.JSONObject
import com.fasterxml.jackson.databind.util.LRUMap
import io.github.qingshu.ayaka.config.WebsocketProperties
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.future.await
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.*
import java.util.concurrent.CompletableFuture

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Component
class ProtocolHelper @Autowired constructor(
    private val echoMap: LRUMap<String, CompletableFuture<JSONObject>>,
    private val websocketProperties: WebsocketProperties
) {

    fun send(session: WebSocketSession, message:JSONObject): JSONObject {
        val uuid = UUID.randomUUID().toString()
        val futureResp = CompletableFuture<JSONObject>()
        echoMap[uuid] = futureResp
        message["echo"] = uuid
        session.sendMessage(TextMessage(message.toString()))
        val result = runBlocking{
            try {
                withTimeout(websocketProperties.echoTimeout) {
                    futureResp.await()
                }
            }catch (e: TimeoutCancellationException){
                log.warn("Action {} failed, reason: {}", message["action"], e.message)
                val result = JSONObject()
                result["status"] = "failed"
                result["retcode"] = -1
                result
            }
        }
        return result?: JSONObject()
    }

    companion object{
        private val log = LoggerFactory.getLogger(ProtocolHelper::class.java)
    }
}

private operator fun <K, V> LRUMap<K, V>.set(uuid: K, value: V) {
    this.put(uuid, value)
}
