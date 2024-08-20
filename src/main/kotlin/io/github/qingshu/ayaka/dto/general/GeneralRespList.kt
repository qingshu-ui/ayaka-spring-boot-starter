package io.github.qingshu.ayaka.dto.general

import com.alibaba.fastjson2.annotation.JSONField

data class GeneralRespList<T>(
    @JSONField(name = "status") val status: String?,
    @JSONField(name = "retcode") val retCode: Int?,
    @JSONField(name = "data") val data: List<T>?,
)
