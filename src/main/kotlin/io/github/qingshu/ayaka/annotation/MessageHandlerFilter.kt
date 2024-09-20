package io.github.qingshu.ayaka.annotation

import io.github.qingshu.ayaka.dto.constant.AtEnum
import io.github.qingshu.ayaka.dto.constant.MsgTypeEnum
import io.github.qingshu.ayaka.dto.constant.ReplyEnum

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MessageHandlerFilter(
    val cmd: String = "",
    val at: AtEnum = AtEnum.OFF,
    val reply: ReplyEnum = ReplyEnum.OFF,
    val types: Array<MsgTypeEnum> = [],
    val groups: LongArray = [],
    val senders: LongArray = [],
    val startWith: Array<String> = [],
    val endWith: Array<String> = [],
    val invert: Boolean = false,
)
