package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class VersionInfoResp(
    @JSONField(name = "app_name") val appName: String?,
    @JSONField(name = "app_version") val appVersion: String?,
    @JSONField(name = "protocol_version") val protocolVersion: String?,
)
