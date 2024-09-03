package io.github.qingshu.ayaka.utils

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
class AyakaUtils

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
