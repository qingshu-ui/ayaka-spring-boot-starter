package io.github.qingshu.ayaka.bot

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.TypeReference
import io.github.qingshu.ayaka.bot.compatible.OneBot
import io.github.qingshu.ayaka.dto.constant.ActionPathEnum
import io.github.qingshu.ayaka.dto.general.GeneralRespData
import io.github.qingshu.ayaka.dto.general.ProtocolBody
import io.github.qingshu.ayaka.dto.resp.MsgId
import io.github.qingshu.ayaka.utils.ProtocolHelper
import org.springframework.web.socket.WebSocketSession

class Bot(
    var selfId: Long,
    var session: WebSocketSession,
    private var sender: ProtocolHelper,
) : OneBot {

    /**
     * 发送私聊消息
     * @param userId 对方 QQ 号
     * @param msg 消息内容
     * @param autoEscape 消息内容是否作为纯文本发送，不解析 CQ 码
     * @return result [GeneralRespData] of [MsgId]
     */
    override fun sendPrivateMsg(userId: Long, msg: String, autoEscape: Boolean): GeneralRespData<MsgId> {
        val params = JSONObject()
        params["user_id"] = userId
        params["message"] = msg
        params["auto_escape"] = autoEscape
        val api = ProtocolBody(
            action = ActionPathEnum.SEND_PRIVATE_MSG.path,
            params = params
        )
        val result = sender.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRespData<MsgId>>() {})
        }
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
