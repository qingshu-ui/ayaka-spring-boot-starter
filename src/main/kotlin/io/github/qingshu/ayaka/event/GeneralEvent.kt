package io.github.qingshu.ayaka.event

import com.alibaba.fastjson2.annotation.JSONField
import io.github.qingshu.ayaka.bot.Bot
import kotlin.reflect.KClass

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
open class GeneralEvent(
    @JSONField(name = "post_type") open val postType: String?,

    @JSONField(name = "time") open val time: Long?,

    @JSONField(name = "self_id") open val selfId: String?,

    @JSONField(serialize = false, deserialize = false) open var bot: Bot?
){
    companion object{
        val events = mutableListOf<KClass<out GeneralEvent>>()
    }
}