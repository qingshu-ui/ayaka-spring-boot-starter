package io.github.qingshu.ayaka.dto.general

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
data class Anonymous(
    @JsonProperty("id") var id: Long = 0,
    @JsonProperty("name") val name: String = "",
    @JsonProperty("flag") val flag: String = "",
)