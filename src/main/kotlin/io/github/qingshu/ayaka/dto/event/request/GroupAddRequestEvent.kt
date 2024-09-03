package io.github.qingshu.ayaka.dto.event.request

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.annotation.JSONField
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
    @JSONField(name = "request_type")
    var requestType: String? = null

    @JSONField(name = "user_id")
    var userId: Long? = null

    @JSONField(name = "comment")
    var comment: String? = null

    @JSONField(name = "flag")
    var flag: String? = null

    @JSONField(name = "sub_type")
    var subType: String? = null

    @JSONField(name = "group_id")
    var groupId: Long? = null

    @JSONField(name = "invitor_id")
    var invitorId: Long? = null

    override fun setCancelled(cancelled: Boolean) {
        this.block = cancelled
    }

    override fun isCancelled(): Boolean {
        return this.block
    }

    companion object {
        init {
            events.add(GroupAddRequestEvent::class)
        }

        fun canHandle(json: JSONObject): Boolean {
            return when {
                "request" == json[POST_TYPE] -> "group" == json[REQUEST_TYPE]
                else -> false
            }
        }
    }
}