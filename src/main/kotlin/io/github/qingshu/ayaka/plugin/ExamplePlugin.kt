package io.github.qingshu.ayaka.plugin

import io.github.qingshu.ayaka.dto.event.message.AnyMessageEvent
import meteordevelopment.orbit.EventHandler
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
//@Component
class ExamplePlugin: BotPlugin {

    companion object {
        private val log = LoggerFactory.getLogger(ExamplePlugin::class.java)
    }

    @EventHandler
    fun handler(e: AnyMessageEvent) {
        val bot = e.bot!!
        val resp = bot.sendMsg(e, e.rawMessage ?: "hello")
        log.info("Received: {}", resp)
    }
}