package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class StrangerInfoResp(
    @JSONField(name = "user_id") val userId: Long?,
    @JSONField(name = "nickname") val nickname: String?,
    @JSONField(name = "sex") val sex: String?,
    @JSONField(name = "age") val age: Int?,
    @JSONField(name = "qid") val qid: String?,
    @JSONField(name = "level") val level: Int?,
    @JSONField(name = "login_days") val loginDays: Int?,
)
