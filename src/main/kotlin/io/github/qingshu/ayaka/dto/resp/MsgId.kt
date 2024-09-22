package io.github.qingshu.ayaka.dto.resp

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
data class MsgId(
    @JsonProperty("message_id") val messageId: Int = 0,
)