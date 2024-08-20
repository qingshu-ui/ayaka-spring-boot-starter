package io.github.qingshu.ayaka.dto.general

import com.alibaba.fastjson2.annotation.JSONField

data class GeneralRespList<T>(
    @JSONField(name = "status") private val status: String?,
    @JSONField(name = "ret_code") private val retCode: Int?,
    @JSONField(name = "data") private val data: List<T>?,
)
