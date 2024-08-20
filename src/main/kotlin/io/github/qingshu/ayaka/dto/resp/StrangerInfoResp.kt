package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

data class StrangerInfoResp(
    @JSONField(name = "user_id") private val userId: Long?,
    @JSONField(name = "nickname") private val nickname: String?,
    @JSONField(name = "sex") private val sex: String?,
    @JSONField(name = "age") private val age: Int?,
    @JSONField(name = "qid") private val qid: String?,
    @JSONField(name = "level") private val level: Int?,
    @JSONField(name = "login_days") private val loginDays: Int?,
)
