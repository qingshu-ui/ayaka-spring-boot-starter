package io.github.qingshu.ayaka.dto.event.notice

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.qingshu.ayaka.dto.constant.ParamsKey.NOTICE_TYPE
import io.github.qingshu.ayaka.dto.constant.ParamsKey.POST_TYPE

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
class GroupDecreaseEvent : NoticeEvent() {

    @JsonProperty("sub_type")
    lateinit var subType: String

    @JsonProperty("group_id")
    var groupId: Long = 0

    @JsonProperty("operator_id")
    var operatorId: Long = 0

    override fun setCancelled(cancelled: Boolean) {
        this.block = cancelled
    }

    override fun isCancelled(): Boolean {
        return this.block
    }

    companion object {
        init {
            events.add(GroupDecreaseEvent::class)
        }

        fun canHandle(json: ObjectNode) =
            "notice" == json[POST_TYPE].asText() && "group_decrease" == json[NOTICE_TYPE].asText()
    }

}