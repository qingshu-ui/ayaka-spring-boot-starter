package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class GetLoginInfoResp(
    @JSONField(name = "nickname") private val nickname: String?,
    @JSONField(name = "user_id") private val userId: Long?
)
