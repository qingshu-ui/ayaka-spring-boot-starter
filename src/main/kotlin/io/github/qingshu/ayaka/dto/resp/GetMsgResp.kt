package io.github.qingshu.ayaka.dto.resp

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.qingshu.ayaka.utils.EMPTY_STRING

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
data class GetMsgResp(
    @JsonProperty("time") val time: Int = 0,
    @JsonProperty("message_type") val messageType: String = EMPTY_STRING,
    @JsonProperty("message_id") val messageId: Int = 0,
    @JsonProperty("real_id") val realId: Int = 0,
    @JsonProperty("message") val message: String = EMPTY_STRING,
    @JsonProperty("raw_message") val rawMessage: String = EMPTY_STRING,
    @JsonProperty("sender") val sender: Sender = Sender(),
) {
    data class Sender(
        @JsonProperty("user_id") val userId: Long = 0,
        @JsonProperty("nickname") val nickname: String = EMPTY_STRING,
        @JsonProperty("sex") val sex: String = EMPTY_STRING,
        @JsonProperty("age") val age: Int = 0,
        @JsonProperty("card") val card: String = EMPTY_STRING,
        @JsonProperty("area") val area: String = EMPTY_STRING,
        @JsonProperty("level") val level: String = EMPTY_STRING,
        @JsonProperty("role") val role: String = EMPTY_STRING,
        @JsonProperty("title") val title: String = EMPTY_STRING,
    )
}