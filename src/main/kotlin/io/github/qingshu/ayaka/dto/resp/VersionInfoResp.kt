package io.github.qingshu.ayaka.dto.resp

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.qingshu.ayaka.utils.EMPTY_STRING

data class VersionInfoResp(
    @JsonProperty("app_name") val appName: String = EMPTY_STRING,
    @JsonProperty("app_version") val appVersion: String = EMPTY_STRING,
    @JsonProperty("protocol_version") val protocolVersion: String = EMPTY_STRING,
)
