package io.github.qingshu.ayaka.config

import meteordevelopment.orbit.EventBus
import meteordevelopment.orbit.IEventBus
import meteordevelopment.orbit.listeners.LambdaListener
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.lang.invoke.MethodHandles

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Configuration
@ConfigurationProperties(prefix = "ayaka.plugin")
data class PluginProperties(
    var pluginPackage: String = ""
) {

    @Bean
    @ConditionalOnMissingBean
    fun createEventBus(): IEventBus {

        val lambdaFactory: LambdaListener.Factory = LambdaListener.Factory { lookupInMethod, klass ->
            lookupInMethod.invoke(null, klass, MethodHandles.lookup()) as MethodHandles.Lookup
        }

        val bus: EventBus = EventBus()
        if (pluginPackage.isNotEmpty()) {
            bus.registerLambdaFactory(pluginPackage, lambdaFactory)
        } else {
            log.warn("No event scan package found")
        }
        return bus
    }

    companion object {
        private val log = LoggerFactory.getLogger(PluginProperties::class.java)
    }
}