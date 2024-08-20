package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class CredentialsResp(
    @JSONField(name = "cookies") val cookies: String?,
    @JSONField(name = "csrf_token") val csrfToken: Int?,
)
