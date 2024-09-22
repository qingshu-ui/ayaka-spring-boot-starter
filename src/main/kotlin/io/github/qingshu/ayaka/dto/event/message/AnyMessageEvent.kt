package io.github.qingshu.ayaka.dto.event.message

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.node.ObjectNode

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
data class AnyMessageEvent(
    @JsonProperty("message_type") override var messageType: String,
    @JsonProperty("group_id") override var groupId: Long,
    @JsonProperty("user_id") override var userId: Long,
) : GroupMessageEvent() {

    override fun setCancelled(cancelled: Boolean) {
        this.block = cancelled
    }

    override fun isCancelled(): Boolean {
        return this.block
    }

    companion object{
        init {
            events.add(AnyMessageEvent::class)
        }

        fun canHandle(json: ObjectNode) =
            "message" == json["post_type"].asText()
    }
}