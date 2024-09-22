package io.github.qingshu.ayaka.dto.resp

import com.fasterxml.jackson.annotation.JsonProperty

data class CsrfTokenResp(
    @JsonProperty("token") val token: Int = 0,
)
