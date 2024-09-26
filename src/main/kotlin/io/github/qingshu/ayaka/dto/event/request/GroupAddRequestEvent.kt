package io.github.qingshu.ayaka.dto.event.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.qingshu.ayaka.dto.constant.ParamsKey.POST_TYPE
import io.github.qingshu.ayaka.dto.constant.ParamsKey.REQUEST_TYPE
import io.github.qingshu.ayaka.dto.event.GeneralEvent

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
class GroupAddRequestEvent : GeneralEvent() {
    @JsonProperty("request_type")
    lateinit var requestType: String

    @JsonProperty("user_id")
    var userId: Long = 0

    @JsonProperty("comment")
    lateinit var comment: String

    @JsonProperty("flag")
    lateinit var flag: String

    @JsonProperty("sub_type")
    lateinit var subType: String

    @JsonProperty("group_id")
    var groupId: Long = 0

    @JsonProperty("invitor_id")
    var invitorId: Long = 0

    override fun setCancelled(cancelled: Boolean) {
        this.block = cancelled
    }

    override fun isCancelled(): Boolean = this.block

    companion object {
        init {
            events.add(GroupAddRequestEvent::class)
        }

        fun canHandle(json: ObjectNode) =
            "request" == json[POST_TYPE].asText() && "group" == json[REQUEST_TYPE].asText()
    }
}