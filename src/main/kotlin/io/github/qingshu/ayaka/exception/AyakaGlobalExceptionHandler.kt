package io.github.qingshu.ayaka.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@ControllerAdvice
class AyakaGlobalExceptionHandler {

    @ExceptionHandler(HmacException::class)
    fun handleHmacException(e: HmacException): ResponseEntity<Map<String, Any>> {
        val body = mapOf("message" to (e.message ?: ""))
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body)
    }
}