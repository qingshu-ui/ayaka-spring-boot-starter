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
@ConfigurationProperties(prefix = "ayaka.plugin")
data class PluginProperties(
    /**
     * 插件所在的包全路径，此项必须在 application.yml 或者 .properties 中配置
     */
    var pluginPackage: String = ""
)