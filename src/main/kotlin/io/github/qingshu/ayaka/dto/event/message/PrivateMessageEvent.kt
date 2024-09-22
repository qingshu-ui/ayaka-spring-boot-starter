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
class PrivateMessageEvent : MessageEvent() {
    @JsonProperty("message_id")
    var messageId: Int = 0

    @JsonProperty("sub_type")
    lateinit var subType: String

    @JsonProperty("sender")
    lateinit var sender: PrivateSender

    @JsonProperty("temp_source")
    var tempSource: Int = 0

    class PrivateSender {
        @JsonProperty("group_id")
        var groupId: Long = 0

        @JsonProperty("user_id")
        var userId: Long = 0

        @JsonProperty("nickname")
        lateinit var nickname: String

        @JsonProperty("sex")
        lateinit var sex: String

        @JsonProperty("age")
        var age: Int = 0
    }

    companion object {
        init {
            events.add(PrivateMessageEvent::class)
        }

        fun canHandle(json: ObjectNode) =
            "message" == json["post_type"].asText() && "private" == json["message_type"].asText()
    }

    override fun setCancelled(cancelled: Boolean) {
        this.block = cancelled
    }

    override fun isCancelled(): Boolean {
        return this.block
    }
}