package io.github.qingshu.ayaka.dto.event

import com.alibaba.fastjson2.annotation.JSONField
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

    @JSONField(name = "post_type") open var postType: String? = null
    @JSONField(name = "time") open var time: Long? = null
    @JSONField(name = "self_id") open var selfId: Long? = null
    @JSONField(serialize = false, deserialize = false) open var bot: Bot? = null

    protected var block = false

    companion object{
        val events = mutableListOf<KClass<out GeneralEvent>>()
    }
}