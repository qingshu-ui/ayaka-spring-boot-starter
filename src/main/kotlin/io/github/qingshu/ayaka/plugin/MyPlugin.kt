package io.github.qingshu.ayaka.plugin

import io.github.qingshu.ayaka.event.message.PrivateMessageEvent
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
@Component
class MyPlugin: BotPlugin {

    @EventHandler
    fun exampleListener(event: PrivateMessageEvent){
        val bot = event.bot
        val msg = event.messageId
        log.info("This a example plugin by {} handle", Thread.currentThread().name)
    }

    companion object{
        private val log = LoggerFactory.getLogger(MyPlugin::class.java)
    }
}