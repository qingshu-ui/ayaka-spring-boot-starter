package io.github.qingshu.ayaka.aop

import io.github.qingshu.ayaka.annotation.HmacCheck
import io.github.qingshu.ayaka.annotation.Slf4j
import io.github.qingshu.ayaka.annotation.Slf4j.Companion.log
import io.github.qingshu.ayaka.config.HttpPostProperties
import io.github.qingshu.ayaka.exception.HmacException
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.text.Charsets.UTF_8

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Slf4j
@Aspect
@Component
class HmacCheckAop(
    private val httpCfg: HttpPostProperties,
) {

    @Around("@annotation(hmacCheck)")
    fun hmacCheck(joinPoint: ProceedingJoinPoint, hmacCheck: HmacCheck): Any {
        return try {
            if (httpCfg.secret.isEmpty()) {
                return joinPoint.proceed()
            }

            val clientSignature = (joinPoint.args[1] as Map<*, *>)["x-signature"] as String
            val payload = joinPoint.args[0] as String

            val serverSignature = calculateHMAC(payload, httpCfg.secret)
            if (serverSignature == clientSignature.split("=")[1]) {
                joinPoint.proceed()
            } else {
                throw HmacException("HMAC signature invalid")
            }
        } catch (e: Throwable) {
            if (e is HmacException) {
                throw e
            }
            log.error("{}", e.message)
            throw HmacException(e.message ?: "Server error")
        }
    }

    private fun calculateHMAC(data: String, secret: String): String {
        val algorithm = "HmacSHA1"
        val secretKeySpec = SecretKeySpec(secret.toByteArray(UTF_8), algorithm)
        val mac = Mac.getInstance(algorithm)
        mac.init(secretKeySpec)
        return mac.doFinal(data.toByteArray(UTF_8)).joinToString("") {
            "%02x".format(it)
        }
    }
}