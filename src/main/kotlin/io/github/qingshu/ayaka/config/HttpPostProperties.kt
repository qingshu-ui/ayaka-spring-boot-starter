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
@ConfigurationProperties(prefix = "ayaka.http")
data class HttpPostProperties(

    /**
     * HttpPost 上报路径
     */
    var endpoint: String = "/ayaka",

    /**
     * OneBot api 端口，地址通过 post 中的 request.remoteHost 获取
     */
    var apiPort: Long = 5800,

    /**
     * 是否启用 http 功能，启用将可以通过 http 向 OneBot 通信
     */
    var enable: Boolean = false,
)