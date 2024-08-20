package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class GetStatusResp(
    @JSONField(name = "online") private val online: Boolean?,
    @JSONField(name = "good") private val good: Boolean?,
)
