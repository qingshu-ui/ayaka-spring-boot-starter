package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class BooleanResp(
    @JSONField(name = "yes") val yes: Boolean?
)
