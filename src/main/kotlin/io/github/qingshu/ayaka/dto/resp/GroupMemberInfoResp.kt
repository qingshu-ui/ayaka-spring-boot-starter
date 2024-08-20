package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class GroupMemberInfoResp(
    @JSONField(name = "group_id") val groupId: Long?,
    @JSONField(name = "user_id") val userId: Long?,
    @JSONField(name = "nickname") val nickname: String?,
    @JSONField(name = "card") val card: String?,
    @JSONField(name = "sex") val sex: String?,
    @JSONField(name = "age") val age: Int?,
    @JSONField(name = "area") val area: String?,
    @JSONField(name = "join_time") val joinTime: Int?,
    @JSONField(name = "last_sent_time") val lastSentTime: Int?,
    @JSONField(name = "level") val level: String?,
    @JSONField(name = "role") val role: String?,
    @JSONField(name = "unfriendly") val unfriendly: Boolean?,
    @JSONField(name = "title") val title: String?,
    @JSONField(name = "title_expire_time") val titleExpireTime: Long?,
    @JSONField(name = "card_changeable") val cardChangeable: Boolean?,
)
