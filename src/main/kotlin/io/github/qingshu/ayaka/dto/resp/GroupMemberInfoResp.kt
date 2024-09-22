package io.github.qingshu.ayaka.dto.resp

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.qingshu.ayaka.utils.EMPTY_STRING

data class GroupMemberInfoResp(
    @JsonProperty("group_id") val groupId: Long = 0,
    @JsonProperty("user_id") val userId: Long = 0,
    @JsonProperty("nickname") val nickname: String = EMPTY_STRING,
    @JsonProperty("card") val card: String = EMPTY_STRING,
    @JsonProperty("sex") val sex: String = EMPTY_STRING,
    @JsonProperty("age") val age: Int = 0,
    @JsonProperty("area") val area: String = EMPTY_STRING,
    @JsonProperty("join_time") val joinTime: Int = 0,
    @JsonProperty("last_sent_time") val lastSentTime: Int = 0,
    @JsonProperty("level") val level: String = EMPTY_STRING,
    @JsonProperty("role") val role: String = EMPTY_STRING,
    @JsonProperty("unfriendly") val unfriendly: Boolean = false,
    @JsonProperty("title") val title: String = EMPTY_STRING,
    @JsonProperty("title_expire_time") val titleExpireTime: Long = 0,
    @JsonProperty("card_changeable") val cardChangeable: Boolean = false,
)
