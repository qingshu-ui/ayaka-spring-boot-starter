package io.github.qingshu.ayaka.dto.event.message

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.annotation.JSONField

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
class PrivateMessageEvent : MessageEvent() {
    @JSONField(name = "message_id") var messageId: Int? = null
    @JSONField(name = "sub_type") var subType: String? = null
    @JSONField(name = "sender") var sender: PrivateSender? = null
    @JSONField(name = "temp_source") var tempSource: Int? = null

    class PrivateSender {
        @JSONField(name = "group_id") var groupId: Long? = null
        @JSONField(name = "user_id") var userId: Long? = null
        @JSONField(name = "nickname") var nickname: String? = null
        @JSONField(name = "sex") var sex: String? = null
        @JSONField(name = "age") var age: Int? = null
    }

    companion object {
        init {
            events.add(PrivateMessageEvent::class)
        }

        fun canHandle(json: JSONObject): Boolean {
            return when {
                "message" == json["post_type"] -> "private" == json["message_type"]
                else -> false
            }
        }
    }

    override fun setCancelled(cancelled: Boolean) {
        this.block = cancelled
    }

    override fun isCancelled(): Boolean {
        return this.block
    }
}