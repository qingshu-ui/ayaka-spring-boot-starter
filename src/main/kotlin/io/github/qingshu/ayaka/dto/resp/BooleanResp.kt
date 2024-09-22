package io.github.qingshu.ayaka.dto.resp

import com.fasterxml.jackson.annotation.JsonProperty

data class BooleanResp(
    @JsonProperty("yes") val yes: Boolean = false,
)
