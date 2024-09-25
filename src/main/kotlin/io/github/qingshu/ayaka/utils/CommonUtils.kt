package io.github.qingshu.ayaka.utils

import io.github.qingshu.ayaka.annotation.MessageHandlerFilter
import io.github.qingshu.ayaka.dto.ArrayMsg
import io.github.qingshu.ayaka.dto.constant.AtEnum
import io.github.qingshu.ayaka.dto.constant.CommonEnum
import io.github.qingshu.ayaka.dto.constant.MsgTypeEnum
import io.github.qingshu.ayaka.dto.constant.ReplyEnum
import io.github.qingshu.ayaka.dto.event.message.GroupMessageEvent
import io.github.qingshu.ayaka.dto.event.message.MessageEvent
import java.util.regex.Matcher

object CommonUtils {

    private val cache = CacheUtils()

    fun msgExtract(msg: String, arrayMsg: List<ArrayMsg>, atEnum: AtEnum, selfId: Long): String {
        if (!listOf(AtEnum.NEED, AtEnum.BOTH).contains(atEnum)) {
            return msg
        }
        val item = atParse(arrayMsg, selfId)
        if (null != item) {
            val code = arrayMsg2Code(listOf(arrayMsg[arrayMsg.indexOf(item)]))
            return msg.replace(code, "").trim()
        }
        return msg
    }

    private fun atParse(arrayMsg: List<ArrayMsg>, selfId: Long): ArrayMsg? {
        if (arrayMsg.isEmpty()) return null
        var index = 0
        var item = arrayMsg[index]
        var rawTarget = item.data.getOrDefault("qq", "0").toString()
        var target = if (CommonEnum.AT_ALL.value == rawTarget) 0L else rawTarget.toLong()
        index = arrayMsg.size - 1
        if ((target == 0L || target != selfId) && index >= 0) {
            item = arrayMsg[index]
            index = arrayMsg.size - 2
            if (MsgTypeEnum.TEXT == item.getType() && index >= 0) {
                item = arrayMsg[index]
            }
            rawTarget = item.data.getOrDefault("qq", "0").toString()
            target = if (CommonEnum.AT_ALL.value == rawTarget) 0L else rawTarget.toLong()
            if (target == 0L || target != selfId) {
                return null
            }
        }
        return item
    }

    fun atCheck(arrayMsg: List<ArrayMsg>, selfId: Long, at: AtEnum): Boolean {
        val opt = atParse(arrayMsg, selfId)
        return when (at) {
            AtEnum.NEED -> opt?.let { item ->
                val target = item.data["qq"]?.toString()?.toLongOrNull() ?: 0L
                target == 0L || target != selfId
            } ?: true

            AtEnum.BOTH -> opt?.let { item ->
                val target = item.data["qq"]?.toString()?.toLongOrNull() ?: 0L
                target == selfId
            } ?: false

            else -> false
        }
    }

    fun allFilterCheck(event: MessageEvent, filter: MessageHandlerFilter): CheckResult {
        val result = filterCheck(event = event, filter = filter)
        if (filter.invert) {
            result.changeResult()
            result.matcher = null
        }
        return result
    }

    private fun filterCheck(event: MessageEvent, filter: MessageHandlerFilter): CheckResult {
        var matcher: Matcher? = null
        val rawMessage = msgExtract(event.message, event.arrayMsg, filter.at, event.selfId)

        // check regex
        if (filter.cmd.isNotBlank() && RegexUtils.matcher(filter.cmd, rawMessage).also { matcher = it } == null) {
            return CheckResult()
        }

        // check @at
        if (CommonEnum.GROUP.value == event.messageType && filter.at != AtEnum.OFF && atCheck(
                event.arrayMsg,
                event.selfId, filter.at
            )
        ) {
            return CheckResult()
        }

        // check reply
        if (filter.reply != ReplyEnum.OFF) {
            val reply = event.arrayMsg.firstOrNull { it.getType() == MsgTypeEnum.REPLY }

            val flag = when (filter.reply) {
                ReplyEnum.NONE -> reply == null
                ReplyEnum.REPLY_ALL -> reply != null
                ReplyEnum.REPLY_ME -> reply?.data?.get("qq") == event.selfId.toString()
                ReplyEnum.REPLY_OTHER -> reply?.data?.get("qq") != event.selfId.toString()
                else -> false
            }
            if (!flag) return CheckResult()
        }

        // check contain types
        if (filter.types.isNotEmpty()) {
            val flag = event.arrayMsg.any { e -> filter.types.binarySearch(e.getType()) >= 0 }
            if (!flag) return CheckResult()
        }

        // check group msg
        if (filter.groups.isNotEmpty() && CommonEnum.GROUP.value == event.messageType) {
            val groupMsgEvent = event as GroupMessageEvent
            val flag = cache.getSortedGroups(filter.groups).binarySearch(groupMsgEvent.groupId) >= 0
            if (!flag) return CheckResult()
        }

        // check sender
        if (filter.senders.isNotEmpty()) {
            val senders = filter.senders
            senders.sort()
            val flag = cache.getSortedSenders(filter.senders).binarySearch(event.userId) >= 0
            if (!flag) return CheckResult()
        }

        // startWith
        if (filter.startWith.isNotEmpty()) {
            var flag = false
            for (start in filter.startWith) {
                flag = rawMessage.startsWith(start)
                if (flag) {
                    if (null == matcher) {
                        matcher = RegexUtils.matcher("($start)(.*)", rawMessage)
                    }
                    break
                }
            }
            if (!flag) return CheckResult()
        }

        // endWith
        if (filter.endWith.isNotEmpty()) {
            var flag = false
            for (end in filter.endWith) {
                flag = rawMessage.endsWith(end)
                if (flag) {
                    if (null == matcher) {
                        matcher = RegexUtils.matcher("(.*)($end)", rawMessage)
                    }
                    break
                }
            }
            if (!flag) return CheckResult()
        }

        return CheckResult(result = true, matcher = matcher)
    }

}