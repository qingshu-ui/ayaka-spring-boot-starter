package io.github.qingshu.ayaka.dto.general

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.qingshu.ayaka.utils.EMPTY_STRING

data class RespList<T>(
    @JsonProperty("status") val status: String = EMPTY_STRING,
    @JsonProperty("retcode") val retCode: Int = 0,
    @JsonProperty("data") val data: List<T> = emptyList(),
)
