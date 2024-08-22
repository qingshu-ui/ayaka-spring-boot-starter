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
@ConfigurationProperties("ayaka")
data class AyakaProperties(
    /**
     * 当发生掉线后, 等待重新连接的时间, 如果小于或等于0, 则不等待, 单位为秒数
     */
    var reConnectInterval: Int = 0
)