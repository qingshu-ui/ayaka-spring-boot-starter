package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class VersionInfoResp(
    @JSONField(name = "app_name") private val appName: String?,
    @JSONField(name = "app_version") private val appVersion: String?,
    @JSONField(name = "protocol_version") private val protocolVersion: String?,
)
