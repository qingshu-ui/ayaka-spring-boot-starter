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
@ConfigurationProperties(prefix = "ayaka.ws.client")
class WebsocketClientProperties {
    /**
     * 是否启用正向 websocket，ayaka 作为 client
     */
    var enable = false

    /**
     * ws 地址
     * 如： ws://localhost:5800
     */
    var url = ""

    /**
     * 自动重连间隔，单位（秒）
     */
    var reconnectInterval = 5
}