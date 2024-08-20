package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class GroupMemberInfoResp(
    @JSONField(name = "group_id") private val groupId: Long?,
    @JSONField(name = "user_id") private val userId: Long?,
    @JSONField(name = "nickname") private val nickname: String?,
    @JSONField(name = "card") private val card: String?,
    @JSONField(name = "sex") private val sex: String?,
    @JSONField(name = "age") private val age: Int?,
    @JSONField(name = "area") private val area: String?,
    @JSONField(name = "join_time") private val joinTime: Int?,
    @JSONField(name = "last_sent_time") private val lastSentTime: Int?,
    @JSONField(name = "level") private val level: String?,
    @JSONField(name = "role") private val role: String?,
    @JSONField(name = "unfriendly") private val unfriendly: Boolean?,
    @JSONField(name = "title") private val title: String?,
    @JSONField(name = "title_expire_time") private val titleExpireTime: Long?,
    @JSONField(name = "card_changeable") private val cardChangeable: Boolean?,
)
