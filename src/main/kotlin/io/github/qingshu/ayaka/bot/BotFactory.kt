package io.github.qingshu.ayaka.bot

import io.github.qingshu.ayaka.utils.ProtocolHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketSession

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Component
class BotFactory @Autowired constructor(
    private val sender: ProtocolHelper,
) {
    fun createBot(xSelfId: Long, session: WebSocketSession): Bot {
        return Bot(xSelfId, session, sender)
    }
}