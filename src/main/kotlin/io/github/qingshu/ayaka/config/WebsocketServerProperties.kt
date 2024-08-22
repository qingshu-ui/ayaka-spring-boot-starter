package io.github.qingshu.ayaka.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Component
@ConfigurationProperties(prefix = "ayaka.ws.server")
class WebsocketServerProperties {
    /**
     * 空闲时间，超过这个时间将关闭会话
     */
    var maxSessionIdleTimeout: Long = 15 * 60000L

    /**
     * ws 地址
     * 如：/ws/ayaka, 连接时使用 ws://localhost:port/ws/ayaka
     */
    var url = "/ws/ayaka"

    /**
     * 是否启用反向 websocket，ayaka 作为 server
     */
    var enable = false
}