package io.github.qingshu.ayaka.bot

import com.alibaba.fastjson2.JSON
import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.TypeReference
import io.github.qingshu.ayaka.bot.compatible.OneBot
import io.github.qingshu.ayaka.bot.compatible.OpenShamrock
import io.github.qingshu.ayaka.dto.constant.ActionPathEnum
import io.github.qingshu.ayaka.dto.constant.ParamsKey
import io.github.qingshu.ayaka.dto.event.message.AnyMessageEvent
import io.github.qingshu.ayaka.dto.general.*
import io.github.qingshu.ayaka.dto.resp.*
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
            action = ActionPathEnum.SEND_PRIVATE_MSG.path, params = params
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

    /**
     * 群组踢人
     *
     * @param groupId 群号
     * @param userId 要踢的 QQ 号
     * @param rejectAddRequest 拒绝此人的加群请求 (默认false)
     * @return result [GeneralRawResp]
     */
    override fun setGroupKick(groupId: Long, userId: Long, rejectAddRequest: Boolean): GeneralRawResp {
        val params = JSONObject()
        params[ParamsKey.GROUP_ID] = groupId
        params[ParamsKey.USER_ID] = userId
        params[ParamsKey.REJECT_ADD_REQUEST] = rejectAddRequest
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_KICK.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRawResp>() {})
        }
    }

    /**
     * 群组单人禁言
     *
     * @param groupId 群号
     * @param userId 要禁言的 QQ 号
     * @param duration 禁言时长, 单位秒, 0 表示取消禁言 (默认30 * 60)
     * @return result [GeneralRawResp]
     */
    override fun setGroupBan(groupId: Long, userId: Long, duration: Int): GeneralRawResp {
        val params = JSONObject()
        params[ParamsKey.GROUP_ID] = groupId
        params[ParamsKey.USER_ID] = userId
        params[ParamsKey.DURATION] = duration
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_BAN.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRawResp>() {})
        }
    }

    /**
     * 群组匿名用户禁言
     *
     * @param groupId 群号
     * @param anonymous 要禁言的匿名用户对象（群消息上报的 anonymous 字段）
     * @param duration 禁言时长，单位秒，无法取消匿名用户禁言
     * @return result [GeneralRawResp]
     */
    override fun setGroupAnonymousBan(groupId: Long, anonymous: Anonymous, duration: Int): GeneralRawResp {
        val params = JSONObject()
        params[ParamsKey.GROUP_ID] = groupId
        params[ParamsKey.ANONYMOUS] = anonymous
        params[ParamsKey.DURATION] = duration
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_ANONYMOUS_BAN.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRawResp>() {})
        }
    }

    /**
     * 全体禁言
     *
     * @param groupId 群号
     * @param enable  是否禁言（默认True,False为取消禁言）
     * @return result [GeneralRawResp]
     */
    override fun setGroupWholeBan(groupId: Long, enable: Boolean): GeneralRawResp {
        val params = JSONObject()
        params[ParamsKey.GROUP_ID] = groupId
        params[ParamsKey.ENABLE] = enable
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_WHOLE_BAN.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRawResp>() {})
        }
    }

    /**
     * 群组设置管理员
     *
     * @param groupId 群号
     * @param userId  要设置管理员的 QQ 号
     * @param enable  true 为设置，false 为取消
     * @return result [GeneralRawResp]
     */
    override fun setGroupAdmin(groupId: Long, userId: Long, enable: Boolean): GeneralRawResp {
        val params = JSONObject()
        params[ParamsKey.GROUP_ID] = groupId
        params[ParamsKey.USER_ID] = userId
        params[ParamsKey.ENABLE] = enable
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_ADMIN.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRawResp>() {})
        }
    }

    /**
     * 群组匿名
     *
     * @param groupId 群号
     * @param enable  是否允许匿名聊天
     * @return result [GeneralRawResp]
     */
    override fun setGroupAnonymous(groupId: Long, enable: Boolean): GeneralRawResp {
        val params = JSONObject()
        params[ParamsKey.GROUP_ID] = groupId
        params[ParamsKey.ENABLE] = enable
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_ANONYMOUS.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRawResp>() {})
        }
    }

    /**
     * 设置群名片（群备注）
     *
     * @param groupId 群号
     * @param userId  要设置的 QQ 号
     * @param card    群名片内容，不填或空字符串表示删除群名片
     * @return result [GeneralRawResp]
     */
    override fun setGroupCard(groupId: Long, userId: Long, card: String): GeneralRawResp {
        val params = JSONObject()
        params[ParamsKey.GROUP_ID] = groupId
        params[ParamsKey.USER_ID] = userId
        params[ParamsKey.CARD] = card
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_CARD.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRawResp>() {})
        }
    }

    /**
     * 设置群名
     *
     * @param groupId   群号
     * @param groupName 新群名
     * @return result [GeneralRawResp]
     */
    override fun setGroupName(groupId: Long, groupName: String): GeneralRawResp {
        val params = JSONObject()
        params[ParamsKey.GROUP_ID] = groupId
        params[ParamsKey.GROUP_NAME] = groupName
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_NAME.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRawResp>() {})
        }
    }

    /**
     * 退出群组
     *
     * @param groupId   群号
     * @param isDismiss 是否解散, 如果登录号是群主, 则仅在此项为 true 时能够解散
     * @return result [GeneralRawResp]
     */
    override fun setGroupLeave(groupId: Long, isDismiss: Boolean): GeneralRawResp {
        val params = JSONObject()
        params[ParamsKey.GROUP_ID] = groupId
        params[ParamsKey.IS_DISMISS] = isDismiss
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_LEAVE.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRawResp>() {})
        }
    }

    /**
     * 设置群组专属头衔
     * @param groupId 群号
     * @param userId QQ号
     * @param title 专属头衔，不填或空字符串表示删除专属头衔
     * @return result [GeneralRawResp]
     */
    override fun setGroupSpecialTitle(groupId: Long, userId: Long, title: String): GeneralRawResp {
        val params = JSONObject()
        params[ParamsKey.GROUP_ID] = groupId
        params[ParamsKey.USER_ID] = userId
        params[ParamsKey.SPECIAL_TITLE] = title
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_SPECIAL_TITLE.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRawResp>() {})
        }
    }

    /**
     * 处理加好友请求
     *
     * @param flag    加好友请求的 flag（需从上报的数据中获得）
     * @param approve 是否同意请求(默认为true)
     * @param remark  添加后的好友备注（仅在同意时有效）
     * @return result [GeneralRawResp]
     */
    override fun setFriendAddRequest(flag: String, approve: Boolean, remark: String): GeneralRawResp {
        val params = JSONObject()
        params[ParamsKey.FLAG] = flag
        params[ParamsKey.APPROVE] = approve
        params[ParamsKey.REMARK] = remark
        val api = ProtocolBody(
            action = ActionPathEnum.SET_FRIEND_ADD_REQUEST.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRawResp>() {})
        }
    }

    /**
     * 处理加群请求／邀请
     *
     * @param flag    加群请求的 flag（需从上报的数据中获得）
     * @param subType add 或 invite，请求类型（需要和上报消息中的 sub_type 字段相符）
     * @param approve 是否同意请求／邀请
     * @param reason  拒绝理由（仅在拒绝时有效）
     * @return result [GeneralRawResp]
     */
    override fun setGroupAddRequest(flag: String, subType: String, approve: Boolean, reason: String): GeneralRawResp {
        val params = JSONObject()
        params[ParamsKey.FLAG] = flag
        params[ParamsKey.SUB_TYPE] = subType
        params[ParamsKey.APPROVE] = approve
        params[ParamsKey.REASON] = reason
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_ADD_REQUEST.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRawResp>() {})
        }
    }


    /**
     * 获取登录号信息
     *
     * @return result [GeneralRespData] of [GetLoginInfoResp]
     */
    override fun getLoginInfo(): GeneralRespData<GetLoginInfoResp> {
        val api = ProtocolBody(
            action = ActionPathEnum.GET_LOGIN_INFO.path, params = JSONObject()
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRespData<GetLoginInfoResp>>() {})
        }
    }

    /**
     * 获取陌生人信息
     *
     * @param userId  QQ 号
     * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
     * @return result [GeneralRespData] of [StrangerInfoResp]
     */
    override fun getStrangerInfo(userId: Long, noCache: Boolean): GeneralRespData<StrangerInfoResp> {
        val params = JSONObject()
        params[ParamsKey.USER_ID] = userId
        params[ParamsKey.NO_CACHE] = noCache
        val api = ProtocolBody(
            action = ActionPathEnum.GET_STRANGER_INFO.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRespData<StrangerInfoResp>>() {})
        }
    }

    /**
     * 获取好友列表
     *
     * @return result [GeneralRespData] of [FriendInfoResp]
     */
    override fun getFriendList(): GeneralRespData<FriendInfoResp> {
        val api = ProtocolBody(
            action = ActionPathEnum.GET_FRIEND_LIST.path, params = JSONObject()
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRespData<FriendInfoResp>>() {})
        }
    }

    /**
     * 获取群信息
     *
     * @param groupId 群号
     * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
     * @return result [GeneralRespData] of [GroupInfoResp]
     */
    override fun getGroupInfo(groupId: Long, noCache: Boolean): GeneralRespData<GroupInfoResp> {
        val params = JSONObject()
        params[ParamsKey.GROUP_ID] = groupId
        params[ParamsKey.NO_CACHE] = noCache
        val api = ProtocolBody(
            action = ActionPathEnum.GET_GROUP_INFO.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRespData<GroupInfoResp>>() {})
        }
    }

    /**
     * 获取群成员信息
     *
     * @param groupId 群号
     * @param userId  QQ 号
     * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
     * @return result [GeneralRespData] of [GroupMemberInfoResp]
     */
    override fun getGroupMemberInfo(
        groupId: Long, userId: Long, noCache: Boolean
    ): GeneralRespData<GroupMemberInfoResp> {
        val params = JSONObject()
        params[ParamsKey.GROUP_ID] = groupId
        params[ParamsKey.USER_ID] = userId
        params[ParamsKey.NO_CACHE] = noCache
        val api = ProtocolBody(
            action = ActionPathEnum.GET_GROUP_MEMBER_INFO.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRespData<GroupMemberInfoResp>>() {})
        }
    }

    /**
     * 获取群成员列表
     *
     * @param groupId 群号
     * @return result [GeneralRespList] of [GroupMemberInfoResp]
     */
    override fun getGroupMemberList(groupId: Long): GeneralRespList<GroupMemberInfoResp> {
        val params = JSONObject()
        params[ParamsKey.GROUP_ID] = groupId
        val api = ProtocolBody(
            action = ActionPathEnum.GET_GROUP_MEMBER_LIST.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRespList<GroupMemberInfoResp>>() {})
        }
    }

    /**
     * 获取群荣誉信息
     *
     * @param groupId 群号
     * @param type    要获取的群荣誉类型, 可传入 talkative performer legend strong_newbie emotion 以分别获取单个类型的群荣誉数据, 或传入 all 获取所有数据
     * @return result [GeneralRespData] of [GroupHonorInfoResp]
     */
    override fun getGroupHonorInfo(groupId: Long, type: String): GeneralRespData<GroupHonorInfoResp> {
        val params = JSONObject()
        params[ParamsKey.GROUP_ID] = groupId
        params[ParamsKey.TYPE] = type
        val api = ProtocolBody(
            action = ActionPathEnum.GET_GROUP_HONOR_INFO.path, params = params
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRespData<GroupHonorInfoResp>>() {})
        }
    }

    /**
     * 检查是否可以发送图片
     *
     * @return result [GeneralRespData] of [BooleanResp]
     */
    override fun canSendImage(): GeneralRespData<BooleanResp> {
        val api = ProtocolBody(
            action = ActionPathEnum.CAN_SEND_IMAGE.path, params = JSONObject()
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRespData<BooleanResp>>() {})
        }
    }

    /**
     * 检查是否可以发送语音
     *
     * @return result [GeneralRespData] of [BooleanResp]
     */
    override fun canSendRecord(): GeneralRespData<BooleanResp> {
        val api = ProtocolBody(
            action = ActionPathEnum.CAN_SEND_RECORD.path, params = JSONObject()
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRespData<BooleanResp>>() {})
        }
    }

    /**
     * 获取状态
     *
     * @return result [GeneralRespData] of [GetStatusResp]
     */
    override fun getStatus(): GeneralRespData<GetStatusResp> {
        val api = ProtocolBody(
            action = ActionPathEnum.GET_STATUS.path, params = JSONObject()
        )
        val result = helper.send(session, JSON.toJSON(api) as JSONObject)
        return result.let {
            JSON.parseObject(it.toJSONString(), object : TypeReference<GeneralRespData<GetStatusResp>>() {})
        }
    }
}
