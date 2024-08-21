package io.github.qingshu.ayaka.dto.event.message

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.annotation.JSONField
import io.github.qingshu.ayaka.dto.general.Anonymous

open class GroupMessageEvent : MessageEvent() {
    @JSONField(name = "message_id") open var messageId: Int? = null
    @JSONField(name = "sub_type") open var subType: String? = null
    @JSONField(name = "avatar") open var avatar: String? = null
    @JSONField(name = "real_message_type") open var realMessageType: String? = null
    @JSONField(name = "is_binded_group_id") open var isBindedGroupId: Boolean? = null
    @JSONField(name = "group_id") open var groupId: Long? = null
    @JSONField(name = "anonymous") open var anonymous: Anonymous? = null
    @JSONField(name = "sender") open var sender: GroupSender? = null
    @JSONField(name = "is_binded_user_id") open var isBindedUserId: Boolean? = null

    class GroupSender {
        @JSONField(name = "user_id") var userId: Long? = null
        @JSONField(name = "nickname") var nickname: String? = null
        @JSONField(name = "card") var card: String? = null
        @JSONField(name = "sex") var sex: String? = null
        @JSONField(name = "age") var age: Int? = null
        @JSONField(name = "area") var area: String? = null
        @JSONField(name = "level") var level: String? = null
        @JSONField(name = "role") var role: String? = null
        @JSONField(name = "title") var title: String? = null
    }

    companion object{
        init {
            events.add(GroupMessageEvent::class)
        }

        fun canHandle(json: JSONObject): Boolean{
            return when {
                "message" == json["post_type"] -> "group" == json["message_type"]
                else -> false
            }
        }
    }
}
