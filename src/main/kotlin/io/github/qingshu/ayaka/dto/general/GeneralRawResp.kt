package io.github.qingshu.ayaka.dto.general

import com.alibaba.fastjson2.annotation.JSONField

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
data class GeneralRawResp(
    @JSONField(name = "status") val status: String?,
    @JSONField(name = "retcode") val retCode: Int?,
    @JSONField(name = "echo") val echo: String?,
)