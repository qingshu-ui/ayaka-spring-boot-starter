package io.github.qingshu.ayaka.dto.event

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.qingshu.ayaka.bot.Bot
import meteordevelopment.orbit.ICancellable
import kotlin.reflect.KClass

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
abstract class GeneralEvent : ICancellable {

    @JsonProperty("post_type") open lateinit var postType: String
    @JsonProperty("time") open var time: Long = 0
    @JsonProperty("self_id") open var selfId: Long = 0
    open lateinit var bot: Bot

    protected var block = false

    companion object{
        val events = mutableListOf<KClass<out GeneralEvent>>()
    }
}