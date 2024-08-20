package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class CsrfTokenResp(
    @JSONField(name = "token") private val token: Int?
)
