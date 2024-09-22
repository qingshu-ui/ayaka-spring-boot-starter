package io.github.qingshu.ayaka.dto.resp

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import io.github.qingshu.ayaka.utils.EMPTY_STRING

data class GroupInfoResp @JsonCreator constructor(
    @JsonProperty("group_id") val groupId: Long = 0,
    @JsonProperty("group_name") val groupName: String = EMPTY_STRING,
    @JsonSetter
    @JsonProperty("group_memo") val groupMemo: String = EMPTY_STRING,
    @JsonProperty("group_create_time") val groupCreateTime: Int = 0,
    @JsonProperty("group_level") val groupLevel: Int = 0,
    @JsonProperty("member_count") val memberCount: Int = 0,
    @JsonProperty("max_member_count") val maxMemberCount: Int = 0,
)
