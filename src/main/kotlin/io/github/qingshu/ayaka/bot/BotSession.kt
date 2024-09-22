package io.github.qingshu.ayaka.bot

import com.fasterxml.jackson.databind.node.ObjectNode

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
interface BotSession {
    fun sendMessage(message: ObjectNode): ObjectNode
}