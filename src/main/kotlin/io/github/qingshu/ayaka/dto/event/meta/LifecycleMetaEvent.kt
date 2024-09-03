package io.github.qingshu.ayaka.dto.event.meta

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.annotation.JSONField
import io.github.qingshu.ayaka.dto.constant.ParamsKey.META_EVENT_TYPE
import io.github.qingshu.ayaka.dto.constant.ParamsKey.POST_TYPE
import io.github.qingshu.ayaka.dto.event.GeneralEvent

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
class LifecycleMetaEvent : GeneralEvent() {

    @JSONField(name = "sub_type")
    var subType: String? = null

    override fun setCancelled(cancelled: Boolean) {
        this.block = cancelled
    }

    override fun isCancelled(): Boolean {
        return this.block
    }

    companion object {
        init {
            events.add(LifecycleMetaEvent::class)
        }

        fun canHandle(json: JSONObject): Boolean {
            return when {
                "meta_event" == json[POST_TYPE] -> "lifecycle" == json[META_EVENT_TYPE]
                else -> false
            }
        }
    }
}