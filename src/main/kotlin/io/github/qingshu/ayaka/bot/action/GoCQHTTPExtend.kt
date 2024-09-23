package io.github.qingshu.ayaka.bot.action

import io.github.qingshu.ayaka.dto.event.message.AnyMessageEvent
import io.github.qingshu.ayaka.dto.general.RawResp
import io.github.qingshu.ayaka.dto.general.RespData
import io.github.qingshu.ayaka.dto.resp.MsgId

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
interface GoCQHTTPExtend {

    /**
     * 群打卡
     * @param groupId 群号
     * @return result [RawResp]
     */
    fun sendGroupSign(groupId: Long): RawResp

    /**
     * 发送合并转发
     * @param event 事件
     * @param msg   自定义转发消息
     * @return [RespData]
     */
    fun sendForwardMsg(event: AnyMessageEvent, msg: List<Map<String, Any>>): RespData<MsgId>

    /**
     * 发送合并转发 (私聊)
     * @param userId 目标用户
     * @param msg    自定义转发消息
     * @return [RespData]
     */
    fun sendPrivateForwardMsg(userId: Long, msg: List<Map<String, Any>>): RespData<MsgId>

    /**
     * 发送合并转发 (群聊)
     * @param groupId 群号
     * @param msg    自定义转发消息
     * @return [RespData]
     */
    fun sendGroupForwardMsg(groupId: Long, msg: List<Map<String, Any>>): RespData<MsgId>
}