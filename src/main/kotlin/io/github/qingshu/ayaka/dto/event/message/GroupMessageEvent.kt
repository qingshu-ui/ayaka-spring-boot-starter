package io.github.qingshu.ayaka.dto.event.message

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.qingshu.ayaka.dto.general.Anonymous

open class GroupMessageEvent : MessageEvent() {
    @JsonProperty("message_id")
    open var messageId: Int = 0

    @JsonProperty("sub_type")
    open lateinit var subType: String

    @JsonProperty("avatar")
    open lateinit var avatar: String

    @JsonProperty("real_message_type")
    open lateinit var realMessageType: String

    @JsonProperty("is_binded_group_id")
    open var isBindedGroupId: Boolean = false

    @JsonProperty("group_id")
    open var groupId: Long = 0

    @JsonProperty("anonymous")
    open lateinit var anonymous: Anonymous

    @JsonProperty("sender")
    open lateinit var sender: GroupSender

    @JsonProperty("is_binded_user_id")
    open var isBindedUserId: Boolean = false

    class GroupSender {
        @JsonProperty("user_id")
        var userId: Long = 0

        @JsonProperty("nickname")
        lateinit var nickname: String

        @JsonProperty("card")
        lateinit var card: String

        @JsonProperty("sex")
        lateinit var sex: String

        @JsonProperty("age")
        var age: Int = 0

        @JsonProperty("area")
        lateinit var area: String

        @JsonProperty("level")
        lateinit var level: String

        @JsonProperty("role")
        lateinit var role: String

        @JsonProperty("title")
        lateinit var title: String
    }

    override fun setCancelled(cancelled: Boolean) {
        this.block = cancelled
    }

    override fun isCancelled(): Boolean {
        return this.block
    }

    companion object{
        init {
            events.add(GroupMessageEvent::class)
        }

        fun canHandle(json: ObjectNode) =
            "message" == json["post_type"].asText() && "group" == json["message_type"].asText()
    }
}
