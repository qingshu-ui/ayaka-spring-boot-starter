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
@ConfigurationProperties("ayaka.task")
data class AsyncTaskProperties(

    /**
     * 核心线程持数量
     */
    var corePoolSize: Int = 5,

    /**
     * 最大线程数量
     */
    var maxPoolSize: Int = 10,

    /**
     * 空闲线程保持时间
     */
    var keepAliveTime: Int = 10,

    /**
     * 缓冲队列大小
     */
    var workQueueSize: Int = 10000,

    /**
     * 线程前缀名称
     */
    var threadNamePrefix: String = "ayaka-",

    /**
     * 是否启用 ayaka 线程池
     */
    var enableAsync: Boolean = true
)