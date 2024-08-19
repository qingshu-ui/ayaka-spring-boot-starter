package io.github.qingshu.ayaka.bot

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
class BotFactory {
    fun createBot(xSelfId: Long, session: WebSocketSession): Bot {
        return Bot(xSelfId, session)
    }
}