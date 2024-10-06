package io.github.qingshu.ayaka.dto

import io.github.qingshu.ayaka.dto.constant.MsgTypeEnum

@Suppress("MemberVisibilityCanBePrivate")
class ArrayMsg {
    lateinit var type: String
    lateinit var data: Map<String, Any>

    fun getType(): MsgTypeEnum = MsgTypeEnum.valueOf(type.uppercase())

    fun setType(typeEnum: MsgTypeEnum?): ArrayMsg {
        type = if (null == typeEnum || !MsgTypeEnum.valid(typeEnum))
            MsgTypeEnum.UNKNOWN.path else typeEnum.path
        return this
    }

    fun getRawType(): String = type

    fun setRawType(type: String): ArrayMsg {
        this.type = type
        return this
    }

    fun toCQCode(): String {
        if ("text".equals(type, true)) {
            return data.getOrDefault("text", "").toString()
        }
        return buildString {
            append("[CQ:")
            append(getRawType())
            data.forEach { (key, value) ->
                append(",")
                append(key)
                append("=")
                append(value)
            }
            append("]")
        }
    }
}
