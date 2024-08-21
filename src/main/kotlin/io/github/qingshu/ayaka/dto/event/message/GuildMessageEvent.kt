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
class GuildMessageEvent: MessageEvent() {

    @JSONField(name = "message_id") var messageId: String? = null
    @JSONField(name = "sub_type") var subType: String? = null
    @JSONField(name = "guild_id") var guildId: String? = null
    @JSONField(name = "channel_id") var channelId: String? = null
    @JSONField(name = "self_tiny_id") var selfTinyId: String? = null
    @JSONField(name = "sender") var sender: Sender? = null

    class Sender {
        @JSONField(name = "user_id") var userId: Long? = null
        @JSONField(name = "tiny_id") var tinyId: String? = null
        @JSONField(name = "nickname") var nickname: String? = null
    }

    companion object{
        init {
            events.add(GuildMessageEvent::class)
        }

        fun canHandle(json: JSONObject): Boolean{
            return when {
                "message" == json["post_type"] -> "guild" == json["message_type"]
                else -> false
            }
        }
    }

}