package io.github.qingshu.ayaka.bot

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
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
    private val respWaitTimeout: Long,
) : BotSession {

    companion object {
        private val log = LoggerFactory.getLogger(BotSessionImpl::class.java)
        private val mapper = ObjectMapper().apply {
            configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        }

        private val FAILED_RESPONSE = mapper.createObjectNode().apply {
            put("status", "failed")
            put("retcode", -1)
            putNull("data")
        }
    }

    private fun isValidIPv6(url: String): Boolean {
        return try {
            InetAddress.getByName(url) is Inet6Address
        } catch (_: Exception) {
            false
        }
    }

    @Suppress("SameParameterValue")
    private fun formatUrl(protocol: String, url: String, port: Long, path: String): String {
        if (isValidIPv6(url)) {
            return "$protocol://[$url]:$port/$path"
        }
        return "$protocol://$url:$port/$path"
    }

    override fun sendMessage(message: ObjectNode): ObjectNode {
        return when (session) {
            is WebSocketSession -> helper.send(session, message)

            is String -> {
                val params = message["params"] as ObjectNode
                val api = message["action"].asText()
                val url = formatUrl(
                    protocol = "http",
                    url = session,
                    port = httpPost.apiPort,
                    path = api
                )
                val resp = NetUtils.post(url, mapper.writeValueAsString(params), respWaitTimeout)
                resp.use {
                    val respStr = it.body?.string()
                    if (respStr.isNullOrEmpty()) {
                        log.warn("The $api has been sent successfully, but the response content is empty")
                        return FAILED_RESPONSE
                    }
                    return mapper.readTree(respStr) as? ObjectNode ?: FAILED_RESPONSE
                }
            }

            else -> {
                log.warn("The session is an invalid: {}", session)
                FAILED_RESPONSE
            }
        }
    }
}