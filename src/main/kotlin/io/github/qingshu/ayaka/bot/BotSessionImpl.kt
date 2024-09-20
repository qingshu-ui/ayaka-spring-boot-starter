package io.github.qingshu.ayaka.bot

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.JSONWriter
import io.github.qingshu.ayaka.config.HttpPostProperties
import io.github.qingshu.ayaka.utils.NetUtils
import io.github.qingshu.ayaka.utils.ProtocolHelper
import org.slf4j.LoggerFactory
import org.springframework.web.socket.WebSocketSession
import java.net.Inet6Address
import java.net.InetAddress

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

        private val FAILED_RESPONSE = JSONObject().apply {
            put("status", "failed")
            put("retcode", -1)
            put("data", null)
        }
    }

    private fun isValidIPv6(url: String): Boolean {
        return try {
            InetAddress.getByName(url) is Inet6Address
        } catch (_: Exception) {
            false
        }
    }

    private fun formatUrl(protocol: String, url: String, port: Long, path: String): String {
        if (isValidIPv6(url)) {
            return "$protocol://[$url]:$port/$path"
        }
        return "$protocol://$url:$port/$path"
    }

    override fun sendMessage(message: JSONObject): JSONObject {
        return when (session) {
            is WebSocketSession -> helper.send(session, message)

            is String -> {
                val params = message["params"] as JSONObject
                val api = message["action"] as String
                val url = formatUrl(
                    protocol = "http",
                    url = session,
                    port = httpPost.apiPort,
                    path = api
                )
                val resp = NetUtils.post(url, params.toJSONString(JSONWriter.Feature.LargeObject))
                resp.use {
                    val respStr = resp.body?.string()
                    if (respStr.isNullOrEmpty()) {
                        log.warn("The $api has been sent successfully, but the response content is empty")
                        return FAILED_RESPONSE
                    }
                    return JSONObject.parseObject(respStr) ?: FAILED_RESPONSE
                }
            }

            else -> {
                log.warn("The session is an invalid: {}", session)
                FAILED_RESPONSE
            }
        }
    }
}