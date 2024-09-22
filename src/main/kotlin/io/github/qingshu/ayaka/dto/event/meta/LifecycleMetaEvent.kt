package io.github.qingshu.ayaka.dto.event.meta

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.node.ObjectNode
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

    @JsonProperty("sub_type")
    lateinit var subType: String

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

        fun canHandle(json: ObjectNode) =
            "meta_event" == json[POST_TYPE].asText() && "lifecycle" == json[META_EVENT_TYPE].asText()
    }
}