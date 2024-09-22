package io.github.qingshu.ayaka.dto.general

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.qingshu.ayaka.utils.EMPTY_STRING
import io.github.qingshu.ayaka.utils.mapper

data class ProtocolBody(
    @JsonProperty("action") val action: String = EMPTY_STRING,
    @JsonProperty("params") val params: ObjectNode = mapper.createObjectNode(),
)
