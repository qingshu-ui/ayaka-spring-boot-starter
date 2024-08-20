package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class CsrfTokenResp(
    @JSONField(name = "token") val token: Int?
)
