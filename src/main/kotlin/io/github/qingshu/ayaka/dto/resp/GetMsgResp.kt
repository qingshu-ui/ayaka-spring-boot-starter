package io.github.qingshu.ayaka.dto.resp

import com.alibaba.fastjson2.annotation.JSONField

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
data class GetMsgResp(
    @JSONField(name = "time") private val time: Int?,
    @JSONField(name = "message_type") private val messageType: String?,
    @JSONField(name = "message_id") private val messageId: Int?,
    @JSONField(name = "real_id") private val realId: Int?,
    @JSONField(name = "message") private val message: String?,
    @JSONField(name = "raw_message") private val rawMessage: String?,
    @JSONField(name = "sender") private val sender: Sender?,
) {
    data class Sender(
        @JSONField(name = "user_id") private val userId: Long?,
        @JSONField(name = "nickname") private val nickname: String?,
        @JSONField(name = "sex") private val sex: String?,
        @JSONField(name = "age") private val age: Int?,
        @JSONField(name = "card") private val card: String?,
        @JSONField(name = "area") private val area: String?,
        @JSONField(name = "level") private val level: String?,
        @JSONField(name = "role") private val role: String?,
        @JSONField(name = "title") private val title: String?,
    )
}