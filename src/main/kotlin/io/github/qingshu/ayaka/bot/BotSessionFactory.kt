package io.github.qingshu.ayaka.bot

import io.github.qingshu.ayaka.config.HttpPostProperties
import io.github.qingshu.ayaka.config.WebsocketProperties
import io.github.qingshu.ayaka.utils.ProtocolHelper
import org.springframework.stereotype.Component

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Component
class BotSessionFactory(
    private val helper: ProtocolHelper,
    private val httpCfg: HttpPostProperties,
    private val websocketProperties: WebsocketProperties
) {

    fun <T> createSession(session: T): BotSession =
        BotSessionImpl(session, helper, httpCfg, websocketProperties.echoTimeout)
}