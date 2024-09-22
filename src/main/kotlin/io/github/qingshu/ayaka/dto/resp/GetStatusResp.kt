package io.github.qingshu.ayaka.dto.resp

import com.fasterxml.jackson.annotation.JsonProperty

data class GetStatusResp(
    @JsonProperty("online") val online: Boolean = false,
    @JsonProperty("good") val good: Boolean = false,
)
