package io.github.qingshu.ayaka.bot

import io.github.qingshu.ayaka.bot.compatible.OneBot
import io.github.qingshu.ayaka.dto.general.GeneralRespData
import io.github.qingshu.ayaka.dto.resp.MsgId
import org.springframework.web.socket.WebSocketSession

class Bot(
    var selfId: Long,
    var session: WebSocketSession,
) : OneBot {

    /**
     * 发送私聊消息
     * @param userId 对方 QQ 号
     * @param msg 消息内容
     * @param autoEscape 消息内容是否作为纯文本发送，不解析 CQ 码
     * @return result [GeneralRespData] of [MsgId]
     */
    override fun sendPrivateMsg(userId: Long, msg: String, autoEscape: Boolean): GeneralRespData<MsgId> {
        TODO("Not yet implemented")
    }

    /**
     * 发送私聊消息
     * @param groupId 主动发起临时会话群号, 机器人本身必须是管理员/群主
     * @param userId 对方 QQ 号
     * @param msg 消息内容
     * @param autoEscape 消息内容是否作为纯文本发送，不解析 CQ 码
     * @return result [GeneralRespData] of [MsgId]
     */
    override fun sendPrivateMsg(groupId: Long, userId: Long, msg: String, autoEscape: Boolean): GeneralRespData<MsgId> {
        TODO("Not yet implemented")
    }

}
