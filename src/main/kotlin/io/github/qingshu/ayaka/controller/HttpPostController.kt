package io.github.qingshu.ayaka.controller

import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.qingshu.ayaka.annotation.HmacCheck
import io.github.qingshu.ayaka.bot.BotFactory
import io.github.qingshu.ayaka.bot.BotSessionFactory
import io.github.qingshu.ayaka.dto.event.EventFactory
import io.github.qingshu.ayaka.utils.mapper
import jakarta.servlet.http.HttpServletRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@RestController
@ConditionalOnProperty(prefix = "ayaka.http", name = ["enable"], havingValue = "true", matchIfMissing = false)
class HttpPostController(
    private val eventFactory: EventFactory,
    private val coroutineScope: CoroutineScope,
    private val botFactory: BotFactory,
    private val botSessionFactory: BotSessionFactory,
) {

    companion object {
        private val log = LoggerFactory.getLogger(HttpPostController::class.java)
    }

    @HmacCheck
    @RequestMapping("\${ayaka.http.endpoint}", method = [RequestMethod.GET, RequestMethod.POST])
    fun post(
        @RequestBody payload: String,
        @RequestHeader headers: Map<String, Any>,
        request: HttpServletRequest,
    ): ResponseEntity<Map<String, Any>> {
        log.debug("Received: {}", request)
        val remoteHost = request.remoteHost
        coroutineScope.launch {
            val xSelfId = (headers["x-self-id"] as? String)?.toLong() ?: return@launch
            val botSession = botSessionFactory.createSession(remoteHost)
            val bot = botFactory.createBot(xSelfId, botSession)
            eventFactory.postEvent(bot, mapper.readTree(payload) as ObjectNode)
        }
        return ResponseEntity.noContent().build()
    }
}