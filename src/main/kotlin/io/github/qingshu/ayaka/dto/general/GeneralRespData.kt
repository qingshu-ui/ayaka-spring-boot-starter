package io.github.qingshu.ayaka.dto.general

import com.fasterxml.jackson.annotation.JsonProperty
import io.github.qingshu.ayaka.utils.EMPTY_STRING

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
data class GeneralRespData<T>(
    @JsonProperty("status") val status: String = EMPTY_STRING,
    @JsonProperty("retcode") val retCode: Int = 0,
    @JsonProperty("data") val data: T,
)