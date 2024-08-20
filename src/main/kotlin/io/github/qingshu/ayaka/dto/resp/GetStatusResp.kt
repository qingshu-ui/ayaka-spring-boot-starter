package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class GetStatusResp(
    @JSONField(name = "online") val online: Boolean?,
    @JSONField(name = "good") val good: Boolean?,
)
