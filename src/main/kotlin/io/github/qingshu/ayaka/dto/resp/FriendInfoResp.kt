package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class FriendInfoResp(
    @JSONField(name = "user_id") private val userId: Long?,
    @JSONField(name = "nickname") private val nickname: String?,
    @JSONField(name = "remark") private val remark: String?,
)
