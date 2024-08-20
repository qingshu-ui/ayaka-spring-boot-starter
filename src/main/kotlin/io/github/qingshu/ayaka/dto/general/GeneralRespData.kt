package io.github.qingshu.ayaka.dto.general

import com.alibaba.fastjson2.annotation.JSONField

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
data class GeneralRespData<T>(
    @JSONField(name = "status") private val status: String?,
    @JSONField(name = "ret_code") private val retCode: Int?,
    @JSONField(name = "data") private val data: T?,
)