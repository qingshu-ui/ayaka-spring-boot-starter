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
class GuildMessageEvent: MessageEvent() {

    @JsonProperty("message_id")
    lateinit var messageId: String

    @JsonProperty("sub_type")
    lateinit var subType: String

    @JsonProperty("guild_id")
    lateinit var guildId: String

    @JsonProperty("channel_id")
    lateinit var channelId: String

    @JsonProperty("self_tiny_id")
    lateinit var selfTinyId: String

    @JsonProperty("sender")
    lateinit var sender: Sender

    class Sender {
        @JsonProperty("user_id")
        var userId: Long = 0

        @JsonProperty("tiny_id")
        lateinit var tinyId: String

        @JsonProperty("nickname")
        lateinit var nickname: String
    }

    override fun setCancelled(cancelled: Boolean) {
        this.block = cancelled
    }

    override fun isCancelled(): Boolean {
        return this.block
    }

    companion object{
        init {
            events.add(GuildMessageEvent::class)
        }

        fun canHandle(json: ObjectNode) =
            "message" == json["post_type"].asText() && "guild" == json["message_type"].asText()
    }

}