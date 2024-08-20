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
@ConfigurationProperties(prefix = "ayaka.ws")
data class WebsocketProperties(
    var accessToken: String = "",
    var authSchema: String = "Bearer",
    var enableAuthSchema: Boolean = true,
    var maxTextMessageBufferSize: Int = 512000,
    var maxBinaryMessageBufferSize: Int = 512000,
    var echoTimeout: Long = 60000L
) {
    fun accessToken(): String {
        if (enableAuthSchema && accessToken != "") {
            return "$authSchema $accessToken"
        }
        return accessToken
    }
}