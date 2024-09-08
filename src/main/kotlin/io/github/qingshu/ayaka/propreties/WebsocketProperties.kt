package io.github.qingshu.ayaka.propreties

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
@ConfigurationProperties(prefix = "ayaka.ws")
data class WebsocketProperties(
    /**
     * 访问令牌, 部署在公网服务器上是设置，避免被滥用
     */
    var accessToken: String = "",

    /**
     * 认证约定
     */
    var authSchema: String = "Bearer",

    /**
     * 是否启用约定
     */
    var enableAuthSchema: Boolean = true,

    /**
     * 最大文本消息缓冲
     */
    var maxTextMessageBufferSize: Int = 512000,

    /**
     * 最大二进制消息缓冲
     */
    var maxBinaryMessageBufferSize: Int = 512000,

    /**
     * 使用 bot 发送协议带了 echo，对方响应时应该带上此 echo,
     * 等待 echo 时间，超时则放弃
     */
    var echoTimeout: Long = 60000L
) {
    fun accessToken(): String {
        if (enableAuthSchema && accessToken != "") {
            return "$authSchema $accessToken"
        }
        return accessToken
    }
}