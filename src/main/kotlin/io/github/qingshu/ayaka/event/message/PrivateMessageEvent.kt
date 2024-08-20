package io.github.qingshu.ayaka.event.message

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.annotation.JSONField
import io.github.qingshu.ayaka.bot.Bot
import io.github.qingshu.ayaka.event.GeneralEvent

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
data class PrivateMessageEvent(
    override val postType: String?,
    override val time: Long?,
    override val selfId: String?,
    override var bot: Bot?,

    @JSONField(name = "message_id") val messageId: Int?,
    @JSONField(name = "sub_type") val subType: String?,
    @JSONField(name = "sender") val sender: PrivateSender?,
    @JSONField(name = "temp_source") val tempSource: Int?,
) : GeneralEvent(postType, time, selfId, bot) {

    data class PrivateSender(
        @JSONField(name = "group_id") val groupId: Long?,
        @JSONField(name = "user_id") val userId: Long?,
        @JSONField(name = "nickname") val nickname: String?,
        @JSONField(name = "sex") val sex: String?,
        @JSONField(name = "age") val age: Int?,
    )

    companion object {
        init {
            events.add(PrivateMessageEvent::class)
        }

        fun canHandle(json: JSONObject): Boolean {
            if(json.containsKey("post_type") && json["post_type"] == "message") {
                return json.containsKey("message_type") && json["message_type"] == "private"
            }
            return false
        }
    }
}