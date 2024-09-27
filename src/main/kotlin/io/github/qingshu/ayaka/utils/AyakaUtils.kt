package io.github.qingshu.ayaka.utils

import com.fasterxml.jackson.annotation.JsonSetter
import com.fasterxml.jackson.annotation.Nulls
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.qingshu.ayaka.dto.ArrayMsg
import io.github.qingshu.ayaka.dto.constant.MsgTypeEnum
import io.github.qingshu.ayaka.dto.event.message.MessageEvent
import org.slf4j.LoggerFactory

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
class AyakaUtils

private val log = LoggerFactory.getLogger(AyakaUtils::class.java)
val mapper = ObjectMapper().apply {
    configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    setDefaultSetterInfo(JsonSetter.Value.forValueNulls(Nulls.AS_EMPTY))
    registerKotlinModule()
}
const val EMPTY_STRING = ""

private const val CQ_CODE_SPLIT = "(?<=\\[CQ:[^]]{1,99999}])|(?=\\[CQ:[^]]{1,99999}])"
private const val CQ_CODE_REGEX = "\\[CQ:([^,\\[\\]]+)((?:,[^,=\\[\\]]+=[^,\\[\\]]*)*)]"

/**
 * 消息编码
 * @param str 消息内容
 * @return [String]
 */
fun escape(str: String) = str
    .replace("&", "&amp;")
    .replace(",", "&#44;")
    .replace("[", "&#91;")
    .replace("]", "&#93;")

/**
 * 消息解码
 * @param str 需要解码的内容
 * @return [String]
 */
fun unescape(str: String) = str
    .replace("&#44;", ",")
    .replace("&#91;", "[")
    .replace("&#93;", "]")
    .replace("&amp;", "&")

/**
 * 判断是否为全体at
 * @param msg 消息
 * @return [Boolean]
 */
fun isAtAll(msg: String) = msg.contains("[CQ:at,qq=all]")

/**
 * 创建自定义消息合并转发
 * @param uin      发送者QQ号
 * @param name     发送者显示名字
 * @param contents 消息列表，每个元素视为一个消息节点
 * @return 消息结构
 */
fun generateForwardMsg(uin: Long, name: String, contents: List<String>): List<Map<String, Any>> {
    val nodes = ArrayList<Map<String, Any>>()
    contents.forEach { msg ->
        val node = HashMap<String, Any>()
        node["type"] = "node"
        val data = HashMap<String, Any>()
        data["name"] = name
        data["uin"] = uin
        data["content"] = msg
        node["data"] = data
        nodes.add(node)
    }
    return nodes
}

/**
 * rawMessage to ArrayMsg
 * @param rawMsg
 * @return [List]
 */
fun raw2ArrayMsg(rawMsg: String): List<ArrayMsg> {
    val chain = ArrayList<ArrayMsg>()
    try {
        rawMsg.split(Regex(CQ_CODE_SPLIT)).filter { it.isNotEmpty() }.forEach { s ->
            val matches = RegexUtils.matcher(CQ_CODE_REGEX, s)
            val item = ArrayMsg()
            val data = mutableMapOf<String, String>()

            if (matches == null) {
                item.setType(MsgTypeEnum.TEXT)
                data["text"] = unescape(s)
                item.data = data
            } else {

                val type = MsgTypeEnum.typeOf(matches.group(1))
                val params = matches.group(2).split(",")
                item.setType(type)
                params.filter { it.isNotEmpty() }.forEach { args ->
                    val (k, v) = args.split("=", limit = 2)
                    data[k] = unescape(v)
                }
                item.data = data
            }
            chain.add(item)
        }
    } catch (e: Exception) {
        log.error("Conversion failed: ${e.message}")
    }
    return chain
}

/**
 * 从 ArrayMsg 生成 CQ Code
 * @param arrayMsg [ArrayMsg]
 * @return [String]
 */
fun arrayMsg2Code(arrayMsg: List<ArrayMsg>): String {
    return buildString {
        arrayMsg.forEach { item ->
            if (MsgTypeEnum.TEXT != item.getType()) {
                append("[CQ:")
                append(item.getType().path)
                item.data.forEach { (k, v) ->
                    append(",$k=${unescape(v.toString())}")
                }
                append("]")
            } else {
                append(escape(item.data[MsgTypeEnum.TEXT.path]?.toString()!!))
            }
        }
    }
}

/**
 * 获取消息内所有图片链接
 * @param msgList 消息链
 * @return [List]
 */
fun getImgUrlInMsg(msgList: List<ArrayMsg>): List<String> =
    msgList.filter { MsgTypeEnum.IMAGE == it.getType() }.map { it.data["url"]?.toString() ?: "" }.toList()

/**
 * 获取消息内所有视频链接
 * @param msgList 消息链
 * @return [List]
 */
fun getVideoUrlInMsg(msgList: List<ArrayMsg>): List<String> =
    msgList.filter { MsgTypeEnum.VIDEO == it.getType() }.map { it.data["url"]?.toString() ?: "" }.toList()

/**
 * 获取消息内所有at对象账号（不包含全体 at）
 * @param msgList 消息链
 * @return [List]
 */
fun getAtList(msgList: List<ArrayMsg>): List<Long> {
    return msgList.filter { MsgTypeEnum.AT == it.getType() && "all" != (it.data["qq"] ?: "") }
        .map { it.data["qq"]?.toString()?.toLong() ?: 0L }.toList()
}

fun rowConvert(msg: String, event: MessageEvent) {
    if (msg.isValidJsonArr(mapper)) {
        val type = mapper.typeFactory.constructParametricType(List::class.java, ArrayMsg::class.java)
        val arrayMsg: List<ArrayMsg> = mapper.readValue(msg, type)
        event.arrayMsg = arrayMsg
        event.message = arrayMsg2Code(arrayMsg)
        return
    }
    event.arrayMsg = raw2ArrayMsg(msg)
}

fun String.isValidJsonObj(mapper: ObjectMapper): Boolean {
    return try {
        mapper.readTree(this)
        true
    } catch (_: JsonProcessingException) {
        false
    }
}

fun String.isValidJsonArr(mapper: ObjectMapper): Boolean {
    return try {
        val node = mapper.readTree(this)
        node.isArray
    } catch (_: JsonProcessingException) {
        false
    }
}