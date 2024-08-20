package io.github.qingshu.ayaka.dto.general

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.annotation.JSONField

data class ProtocolBody(
    @JSONField(name = "action") val action: String,
    @JSONField(name = "params") val params: JSONObject,
)
