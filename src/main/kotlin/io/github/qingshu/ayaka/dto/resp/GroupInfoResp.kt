package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class GroupInfoResp(
    @JSONField(name = "group_id") private val groupId: Long?,
    @JSONField(name = "group_name") private val groupName: String?,
    @JSONField(name = "group_memo") private val groupMemo: String?,
    @JSONField(name = "group_create_time") private val groupCreateTime: Int?,
    @JSONField(name = "group_level") private val groupLevel: Int?,
    @JSONField(name = "member_count") private val memberCount: Int?,
    @JSONField(name = "max_member_count") private val maxMemberCount: Int?,
)
