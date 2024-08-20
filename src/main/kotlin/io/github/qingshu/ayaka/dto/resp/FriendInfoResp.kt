package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class FriendInfoResp(
    @JSONField(name = "user_id") val userId: Long?,
    @JSONField(name = "nickname") val nickname: String?,
    @JSONField(name = "remark") val remark: String?,
)
