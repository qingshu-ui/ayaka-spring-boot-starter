package io.github.qingshu.ayaka.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Configuration
@ConfigurationProperties(prefix = "ayaka.ws.server")
class WebsocketServerProperties {
    val maxSessionIdleTimeout: Long = 15 * 60000L
    val url = "/ws/ayaka"
    var enable = false
}