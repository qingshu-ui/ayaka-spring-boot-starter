package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class GetResourceResp(
    @JSONField(name = "file") private val file: String?,
)
