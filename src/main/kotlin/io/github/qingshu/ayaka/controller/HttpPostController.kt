package io.github.qingshu.ayaka.controller

import com.alibaba.fastjson2.JSONObject
import io.github.qingshu.ayaka.annotation.HmacCheck
import io.github.qingshu.ayaka.bot.BotFactory
import io.github.qingshu.ayaka.bot.BotSessionFactory
import io.github.qingshu.ayaka.dto.event.EventFactory
import jakarta.servlet.http.HttpServletRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Controller
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
    ): ResponseEntity<String> {
        log.debug("Received: {}", request)
        coroutineScope.launch {
            val xSelfId = (headers["x-self-id"] as? String)?.toLong() ?: return@launch
            val botSession = botSessionFactory.createSession(request.remoteHost)
            val bot = botFactory.createBot(xSelfId, botSession)
            eventFactory.postEvent(bot, JSONObject.parseObject(payload))
        }
        return ResponseEntity.noContent().build()
    }
}