package io.github.qingshu.ayaka.dto.general

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import io.github.qingshu.ayaka.utils.EMPTY_STRING

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
data class RawResp(
    @JsonProperty("status") val status: String = EMPTY_STRING,
    @JsonProperty("retcode") val retCode: Int = 0,
    @JsonSetter(nulls = Nulls.AS_EMPTY)
    @JsonProperty("echo") val echo: String = EMPTY_STRING,
)
/*
fun main(args: Array<String>) {
    val jsonStr = """
        {
            "status": "ok",
            "retcode": 0,
            "echo": null
        }
    """.trimIndent()

    val resp = mapper.readValue(jsonStr, RawResp::class.java)
    println(resp)
}
 */