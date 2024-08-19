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
@ConfigurationProperties("ayaka.task")
data class AsyncTaskProperties(
    var corePoolSize: Int = 10,
    var maxPoolSize: Int = 50,
    var keepAliveTime: Int = 10,
    var workQueueSize: Int = 10000,
    var threadNamePrefix: String = "ayaka-",
    var enableAsync: Boolean = true
)