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
    @JSONField(name = "time") val time: Int?,
    @JSONField(name = "message_type") val messageType: String?,
    @JSONField(name = "message_id") val messageId: Int?,
    @JSONField(name = "real_id") val realId: Int?,
    @JSONField(name = "message") val message: String?,
    @JSONField(name = "raw_message") val rawMessage: String?,
    @JSONField(name = "sender") val sender: Sender?,
) {
    data class Sender(
        @JSONField(name = "user_id") val userId: Long?,
        @JSONField(name = "nickname") val nickname: String?,
        @JSONField(name = "sex") val sex: String?,
        @JSONField(name = "age") val age: Int?,
        @JSONField(name = "card") val card: String?,
        @JSONField(name = "area") val area: String?,
        @JSONField(name = "level") val level: String?,
        @JSONField(name = "role") val role: String?,
        @JSONField(name = "title") val title: String?,
    )
}