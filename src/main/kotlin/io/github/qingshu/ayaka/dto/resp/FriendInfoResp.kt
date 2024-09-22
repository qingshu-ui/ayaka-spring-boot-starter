package io.github.qingshu.ayaka.dto.resp

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.qingshu.ayaka.utils.EMPTY_STRING

data class FriendInfoResp(
    @JsonProperty("user_id") val userId: Long = 0,
    @JsonProperty("nickname") val nickname: String = EMPTY_STRING,
    @JsonProperty("remark") val remark: String = EMPTY_STRING,
)
