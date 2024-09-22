package io.github.qingshu.ayaka.dto.resp

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.qingshu.ayaka.utils.EMPTY_STRING

data class GetLoginInfoResp(
    @JsonProperty("nickname") val nickname: String = EMPTY_STRING,
    @JsonProperty("user_id") val userId: Long = 0,
)
