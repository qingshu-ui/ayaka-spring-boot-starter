package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class GetCooliesResp(
    @JSONField(name = "cookies") val cookies: String?
)
