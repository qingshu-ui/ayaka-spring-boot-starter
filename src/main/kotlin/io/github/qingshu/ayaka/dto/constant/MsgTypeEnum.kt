package io.github.qingshu.ayaka.dto.constant

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Suppress("unused")
enum class MsgTypeEnum(val path: String) {

    /**
     * at 某人
     */
    AT("at"),

    /**
     * 文本类型
     */
    TEXT("text"),

    /**
     * 表情
     */
    FACE("face"),

    /**
     * 商城大表情消息
     */
    MFACE("mface"),

    /**
     * 商城大表情消息
     */
    MARKETFACE("marketface"),

    /**
     * 投篮表情
     */
    BASKETBALL("basketball"),

    /**
     * 语音
     */
    RECORD("record"),

    /**
     * 短视频
     */
    VIDEO("video"),

    /**
     * 猜拳魔法表情
     */
    RPS("rps"),

    /**
     * 新猜拳表情
     */
    NEW_RPS("new_rps"),

    /**
     * 掷骰子魔法表情
     */
    DICE("dice"),

    /**
     * 新掷骰子表情
     */
    NEW_DICE("new_dice"),

    /**
     * 窗口抖动（戳一戳）
     */
    SHAKE("shake"),

    /**
     * 匿名发消息
     */
    ANONYMOUS("anonymous"),

    /**
     * 链接分享
     */
    SHARE("share"),

    /**
     * 推荐好友/群
     */
    CONTACT("contact"),

    /**
     * 位置
     */
    LOCATION("location"),

    /**
     * 音乐分享
     */
    MUSIC("music"),

    /**
     * 图片
     */
    IMAGE("image"),

    /**
     * 回复
     */
    REPLY("reply"),

    /**
     * 红包
     */
    REDBAG("redbag"),

    /**
     * 戳一戳
     */
    POKE("poke"),

    /**
     * 礼物
     */
    GIFT("gift"),

    /**
     * 合并转发
     */
    FORWARD("forward"),

    /**
     * 富文本消息
     */
    MARKDOWN("markdown"),

    /**
     * 富文本下的按钮
     */
    KEYBOARD("keyboard"),

    /**
     * 合并转发消息节点
     */
    NODE("node"),

    /**
     * XML 消息
     */
    XML("xml"),

    /**
     * JSON 消息
     */
    JSON("json"),

    /**
     * 一种 XML 的图片消息
     */
    CARDIMAGE("cardimage"),

    /**
     * 文本转语音
     */
    TTS("tts"),

    /**
     * 长消息
     */
    LONGMSG("longmsg"),

    /**
     * 未知类型
     */
    UNKNOWN("unknown");

    companion object {

        fun typeOf(type: String): MsgTypeEnum {
            return entries.find { it.name.equals(type, true) } ?: UNKNOWN
        }

        fun valid(type: MsgTypeEnum): Boolean {
            return entries.contains(type)
        }
    }
}