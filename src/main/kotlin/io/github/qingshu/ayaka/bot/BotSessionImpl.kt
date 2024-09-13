package io.github.qingshu.ayaka.bot

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONWriter
import io.github.qingshu.ayaka.config.HttpPostProperties
import io.github.qingshu.ayaka.utils.NetUtils
import io.github.qingshu.ayaka.utils.ProtocolHelper
import org.slf4j.LoggerFactory
import org.springframework.web.socket.WebSocketSession

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
class BotSessionImpl<T>(
    val session: T,
    private val helper: ProtocolHelper,
    private val httpPost: HttpPostProperties,
) : BotSession {

    companion object {
        private val log = LoggerFactory.getLogger(BotSessionImpl::class.java)
    }

    override fun sendMessage(message: JSONObject): JSONObject {
        if (session is WebSocketSession) {
            return helper.send(session, message)
        }
        if (session is String) {
            val api = message["action"] as String
            val params = message["params"] as JSONObject
            val url = "http://$session:${httpPost.apiPort}/$api"
            val resp = NetUtils.post(url, params.toJSONString(JSONWriter.Feature.LargeObject))
            val respStr = resp.body?.string() ?: "{\"status\":\"failed\",\"retcode\":-1,\"data\":null}"
            val respJson = JSONObject.parseObject(respStr)
            return respJson
        }
        log.warn("The session is an invalid: {}", session)
        val invalid = JSONObject()
        invalid["status"] = "failed"
        invalid["retcode"] = -1
        invalid["data"] = null
        return invalid
    }
}