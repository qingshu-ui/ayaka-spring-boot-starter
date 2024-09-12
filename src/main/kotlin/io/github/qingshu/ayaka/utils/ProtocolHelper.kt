package io.github.qingshu.ayaka.utils

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONWriter
import com.fasterxml.jackson.databind.util.LRUMap
import io.github.qingshu.ayaka.dto.constant.AdapterEnum
import io.github.qingshu.ayaka.dto.constant.SessionStatusEnum
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
import java.util.concurrent.ConcurrentHashMap

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Component
class ProtocolHelper @Autowired constructor(
    private val echoMap: ConcurrentHashMap<String, CompletableFuture<JSONObject>>,
    private val websocketProperties: WebsocketProperties
) {

    fun send(session: WebSocketSession, message:JSONObject): JSONObject {

        if (getAdapter(session) == AdapterEnum.SERVER) {
            val status = getSessionStatus(session)
            if (SessionStatusEnum.ONLINE != status) {
                when (status) {
                    SessionStatusEnum.DIE -> throw Exception("session been closed.")
                    else -> throw Exception("session been closed, but you can attempt again later.")
                }
            }
        }

        val uuid = UUID.randomUUID().toString()
        val futureResp = CompletableFuture<JSONObject>()
        echoMap[uuid] = futureResp
        message["echo"] = uuid
        val jsonStr = message.toJSONString(JSONWriter.Feature.LargeObject)
        synchronized(session){
            session.sendMessage(TextMessage(jsonStr))
        }
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
            }finally {
                echoMap.remove(uuid)
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
