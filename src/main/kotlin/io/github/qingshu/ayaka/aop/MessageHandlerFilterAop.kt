package io.github.qingshu.ayaka.aop

import io.github.qingshu.ayaka.annotation.MessageHandlerFilter
import io.github.qingshu.ayaka.annotation.Slf4j.Companion.log
import io.github.qingshu.ayaka.dto.event.message.MessageEvent
import io.github.qingshu.ayaka.utils.CommonUtils
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component

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

    @Around("@annotation(filter)")
    fun messageFilter(joinPoint: ProceedingJoinPoint, filter: MessageHandlerFilter): Any? {
        return try {
            val event = joinPoint.args.singleOrNull() as? MessageEvent ?: return joinPoint.proceed()
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

