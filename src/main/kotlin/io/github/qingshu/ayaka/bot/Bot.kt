package io.github.qingshu.ayaka.bot

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.TypeReference
import io.github.qingshu.ayaka.bot.compatible.OneBot
import io.github.qingshu.ayaka.bot.compatible.OpenShamrock
import io.github.qingshu.ayaka.dto.constant.ActionPathEnum
import io.github.qingshu.ayaka.dto.constant.ParamsKey
import io.github.qingshu.ayaka.dto.general.GeneralRawResp
import io.github.qingshu.ayaka.dto.general.GeneralRespData
import io.github.qingshu.ayaka.dto.general.GeneralRespList
import io.github.qingshu.ayaka.dto.general.ProtocolBody
import io.github.qingshu.ayaka.dto.resp.GetMsgResp
import io.github.qingshu.ayaka.dto.resp.GroupInfoResp
import io.github.qingshu.ayaka.dto.resp.MsgId
import io.github.qingshu.ayaka.event.message.AnyMessageEvent
import io.github.qingshu.ayaka.utils.ProtocolHelper
import org.springframework.web.socket.WebSocketSession

class Bot(
    var selfId: Long,
    var session: WebSocketSession,
    private var helper: ProtocolHelper,
) : OneBot, OpenShamrock {

    /**
     * 发送私聊消息
     * @param userId 对方 QQ 号
     * @param msg 消息内容
     * @param autoEscape 消息内容是否作为纯文本发送，不解析 CQ 码
     * @return result [GeneralRespData] of [MsgId]
     */
    override fun sendPrivateMsg(userId: Long, msg: String, autoEscape: Boolean): GeneralRespData<MsgId> {
        return sendPrivateMsg(null, userId, msg, autoEscape)
    }

    /**
     * 发送私聊消息
     * @param groupId 主动发起临时会话群号, 机器人本身必须是管理员/群主
     * @param userId 对方 QQ 号
     * @param msg 消息内容
     * @param autoEscape 消息内容是否作为纯文本发送，不解析 CQ 码
     * @return result [GeneralRespData] of [MsgId]
     */
    override fun sendPrivateMsg(
        groupId: Long?, userId: Long, msg: String, autoEscape: Boolean
    ): GeneralRespData<MsgId> {
        val params = JSONObject()
        if (groupId != null) {
            params[ParamsKey.GROUP_ID] = groupId
        }
        params[ParamsKey.USER_ID] = userId
        params[ParamsKey.MESSAGE] = msg
        params[ParamsKey.AUTO_ESCAPE] = autoEscape
        val api = ProtocolBody(
            action = ActionPathEnum.SEND_PRIVATE_MSG.path,
            params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRespData<MsgId>>() {})
        }
    }

    /**
     * 发送群消息
     * @param groupId 群号
     * @param msg 消息内容
     * @param autoEscape 消息内容是否作为纯文本发送，不解析 CQ 码
     * @return result [GeneralRespData] of [MsgId]
     */
    override fun sendGroupMsg(groupId: Long, msg: String, autoEscape: Boolean): GeneralRespData<MsgId> {
        val params = JSONObject()
        params[ParamsKey.GROUP_ID] = groupId
        params[ParamsKey.MESSAGE] = msg
        params[ParamsKey.AUTO_ESCAPE] = autoEscape
        val api = ProtocolBody(
            action = ActionPathEnum.SEND_GROUP_MSG.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRespData<MsgId>>() {})
        }
    }

    /**
     * 获取群列表
     * @return result [GeneralRespList] of [GroupInfoResp]
     */
    override fun getGroupList(): GeneralRespList<GroupInfoResp> {
        val api = ProtocolBody(
            action = ActionPathEnum.GET_GROUP_LIST.path, params = JSONObject()
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRespList<GroupInfoResp>>() {})
        }
    }

    /**
     * 发送消息
     * @param event [AnyMessageEvent]
     * @param msg 消息内容
     * @param autoEscape 消息内容是否作为纯文本发送，不解析 CQ 码
     * @return result [GeneralRespData] of [MsgId]
     */
    override fun sendMsg(event: AnyMessageEvent, msg: String, autoEscape: Boolean): GeneralRespData<MsgId> {
        return when (event.messageType) {
            ParamsKey.GROUP -> sendGroupMsg(event.groupId!!, msg, autoEscape)
            ParamsKey.PRIVATE -> sendPrivateMsg(event.groupId!!, msg, autoEscape)
            else -> GeneralRespData(
                status = "failed", data = null, retCode = -1
            )
        }
    }

    /**
     * 撤回消息
     *
     * @param msgId 消息 ID
     * @return result [GeneralRawResp]
     */
    override fun deleteMsg(msgId: Int): GeneralRawResp {
        val params = JSONObject()
        params[ParamsKey.MESSAGE_ID] = msgId
        val api = ProtocolBody(
            action = ActionPathEnum.DELETE_MSG.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRawResp>() {})
        }
    }

    /**
     * 获取消息
     *
     * @param msgId 消息 ID
     * @return result [GeneralRespData] of [GetMsgResp]
     */
    override fun getMsg(msgId: Int): GeneralRespData<GetMsgResp> {
        val params = JSONObject()
        params[ParamsKey.MESSAGE_ID] = msgId
        val api = ProtocolBody(
            action = ActionPathEnum.GET_MSG.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRespData<GetMsgResp>>() {})
        }
    }

    /**
     * 好友点赞
     *
     * @param userId 目标用户
     * @param times  点赞次数（每个好友每天最多 10 次，机器人为 Super VIP 则提高到 20次）
     * @return result [GeneralRawResp]
     */
    override fun sendLike(userId: Long, times: Int): GeneralRawResp {
        val params = JSONObject()
        params[ParamsKey.USER_ID] = userId
        params[ParamsKey.TIMES] = times
        val api = ProtocolBody(
            action = ActionPathEnum.SEND_LIKE.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRawResp>() {})
        }
    }

    /**
     * 群打卡，来自 OpenShamrock 在其他 Bot 实现可能无效
     * @param groupId 群号
     * @return result [GeneralRawResp]
     */
    override fun sendGroupSign(groupId: Long): GeneralRawResp {
        val params = JSONObject()
        params[ParamsKey.GROUP_ID] = groupId
        val api = ProtocolBody(
            action = ActionPathEnum.SEND_GROUP_SIGN.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRawResp>() {})
        }
    }
}
