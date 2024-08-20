package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class GroupInfoResp(
    @JSONField(name = "group_id") val groupId: Long?,
    @JSONField(name = "group_name") val groupName: String?,
    @JSONField(name = "group_memo") val groupMemo: String?,
    @JSONField(name = "group_create_time") val groupCreateTime: Int?,
    @JSONField(name = "group_level") val groupLevel: Int?,
    @JSONField(name = "member_count") val memberCount: Int?,
    @JSONField(name = "max_member_count") val maxMemberCount: Int?,
)
