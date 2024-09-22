package io.github.qingshu.ayaka.dto.resp

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.qingshu.ayaka.utils.EMPTY_STRING

data class GetResourceResp(
    @JsonProperty("file") val file: String = EMPTY_STRING,
)
