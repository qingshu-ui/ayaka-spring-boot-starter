package io.github.qingshu.ayaka.dto.resp

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.qingshu.ayaka.utils.EMPTY_STRING

data class StrangerInfoResp(
    @JsonProperty("user_id") val userId: Long = 0,
    @JsonProperty("nickname") val nickname: String = EMPTY_STRING,
    @JsonProperty("sex") val sex: String = EMPTY_STRING,
    @JsonProperty("age") val age: Int = 0,
    @JsonProperty("qid") val qid: String = EMPTY_STRING,
    @JsonProperty("level") val level: Int = 0,
    @JsonProperty("login_days") val loginDays: Int = 0,
)
