package io.github.qingshu.ayaka.dto.resp

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.qingshu.ayaka.utils.EMPTY_STRING

data class CredentialsResp(
    @JsonProperty("cookies") val cookies: String = EMPTY_STRING,
    @JsonProperty("csrf_token") val csrfToken: Int = 0,
)
