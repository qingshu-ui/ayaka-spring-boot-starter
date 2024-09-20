package io.github.qingshu.ayaka.aop

import io.github.qingshu.ayaka.annotation.MessageHandlerFilter
import io.github.qingshu.ayaka.annotation.Slf4j.Companion.log
import io.github.qingshu.ayaka.dto.event.message.MessageEvent
import io.github.qingshu.ayaka.utils.CommonUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import org.springframework.stereotype.Component
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.kotlinFunction

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the MIT License.
 * See the LICENSE file for details.
 */
@Aspect
@Component
class MessageHandlerFilterAop {

    @Pointcut("@annotation(io.github.qingshu.ayaka.annotation.MessageHandlerFilter)")
    fun messageFilterMethod() {
    }

    @Around("messageFilterMethod()")
    fun messageFilter(joinPoint: ProceedingJoinPoint): Any? {
        return try {
            val event = joinPoint.args.singleOrNull() as? MessageEvent ?: return null
            val method = (joinPoint.signature as? MethodSignature)?.method?.kotlinFunction ?: return null
            val filter = method.findAnnotation<MessageHandlerFilter>() ?: return null
            val checkResult = CommonUtils.allFilterCheck(event, filter)
            if (checkResult.result) {
                event.matcher = checkResult.matcher
                joinPoint.proceed()
            } else null
        } catch (e: Exception) {
            log.error(e.message)
            null
        }
    }

}

