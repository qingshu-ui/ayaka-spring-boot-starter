package io.github.qingshu.ayaka.bot

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.ArrayNode
import io.github.qingshu.ayaka.bot.action.GoCQHTTPExtend
import io.github.qingshu.ayaka.bot.action.OneBot
import io.github.qingshu.ayaka.dto.constant.ActionPathEnum
import io.github.qingshu.ayaka.dto.constant.ParamsKey
import io.github.qingshu.ayaka.dto.event.message.GroupMessageEvent
import io.github.qingshu.ayaka.dto.event.message.MessageEvent
import io.github.qingshu.ayaka.dto.event.message.PrivateMessageEvent
import io.github.qingshu.ayaka.dto.general.*
import io.github.qingshu.ayaka.dto.resp.*
import io.github.qingshu.ayaka.utils.mapper

class Bot(
    var selfId: Long,
    var session: BotSession,
) : OneBot, GoCQHTTPExtend {

    /**
     * 发送私聊消息
     * @param userId 对方 QQ 号
     * @param msg 消息内容
     * @param autoEscape 消息内容是否作为纯文本发送，不解析 CQ 码
     * @return result [RespData] of [MsgId]
     */
    override fun sendPrivateMsg(userId: Long, msg: String, autoEscape: Boolean): RespData<MsgId> =
        sendPrivateMsg(0, userId, msg, autoEscape)

    /**
     * 发送私聊消息
     * @param groupId 主动发起临时会话群号, 机器人本身必须是管理员/群主
     * @param userId 对方 QQ 号
     * @param msg 消息内容
     * @param autoEscape 消息内容是否作为纯文本发送，不解析 CQ 码
     * @return result [RespData] of [MsgId]
     */
    override fun sendPrivateMsg(
        groupId: Long, userId: Long, msg: String, autoEscape: Boolean
    ): RespData<MsgId> {
        val params = mapper.createObjectNode()
        if (groupId != 0L) {
            params.put(ParamsKey.GROUP_ID, groupId)
        }
        params.put(ParamsKey.USER_ID, userId)
        params.put(ParamsKey.MESSAGE, msg)
        params.put(ParamsKey.AUTO_ESCAPE, autoEscape)

        val api = ProtocolBody(
            action = ActionPathEnum.SEND_PRIVATE_MSG.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespData<MsgId>>() {})
        }
    }

    /**
     * 发送群消息
     * @param groupId 群号
     * @param msg 消息内容
     * @param autoEscape 消息内容是否作为纯文本发送，不解析 CQ 码
     * @return result [RespData] of [MsgId]
     */
    override fun sendGroupMsg(groupId: Long, msg: String, autoEscape: Boolean): RespData<MsgId> {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        params.put(ParamsKey.MESSAGE, msg)
        params.put(ParamsKey.AUTO_ESCAPE, autoEscape)
        val api = ProtocolBody(
            action = ActionPathEnum.SEND_GROUP_MSG.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespData<MsgId>>() {})
        }
    }

    /**
     * 获取群列表
     * @return result [RespList] of [GroupInfoResp]
     */
    override fun getGroupList(): RespList<GroupInfoResp> {
        val api = ProtocolBody(
            action = ActionPathEnum.GET_GROUP_LIST.path, params = mapper.createObjectNode()
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespList<GroupInfoResp>>() {})
        }
    }

    /**
     * 发送消息
     * @param event [MessageEvent]
     * @param msg 消息内容
     * @param autoEscape 消息内容是否作为纯文本发送，不解析 CQ 码
     * @return result [RespData] of [MsgId]
     */
    override fun sendMsg(event: MessageEvent, msg: String, autoEscape: Boolean): RespData<MsgId> {
        return when (event) {
            is GroupMessageEvent -> sendGroupMsg(event.groupId, msg, autoEscape)
            is PrivateMessageEvent -> sendPrivateMsg(event.userId, msg, autoEscape)
            else -> RespData(
                status = "failed", data = MsgId(-1), retCode = -1
            )
        }
    }

    /**
     * 撤回消息
     *
     * @param msgId 消息 ID
     * @return result [RawResp]
     */
    override fun deleteMsg(msgId: Int): RawResp {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.MESSAGE_ID, msgId)
        val api = ProtocolBody(
            action = ActionPathEnum.DELETE_MSG.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RawResp>() {})
        }
    }

    /**
     * 获取消息
     *
     * @param msgId 消息 ID
     * @return result [RespData] of [GetMsgResp]
     */
    override fun getMsg(msgId: Int): RespData<GetMsgResp> {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.MESSAGE_ID, msgId)
        val api = ProtocolBody(
            action = ActionPathEnum.GET_MSG.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespData<GetMsgResp>>() {})
        }
    }

    /**
     * 好友点赞
     *
     * @param userId 目标用户
     * @param times  点赞次数（每个好友每天最多 10 次，机器人为 Super VIP 则提高到 20次）
     * @return result [RawResp]
     */
    override fun sendLike(userId: Long, times: Int): RawResp {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.USER_ID, userId)
        params.put(ParamsKey.TIMES, times)
        val api = ProtocolBody(
            action = ActionPathEnum.SEND_LIKE.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RawResp>() {})
        }
    }

    /**
     * 群打卡
     * @param groupId 群号
     * @return result [RawResp]
     */
    override fun sendGroupSign(groupId: Long): RawResp {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        val api = ProtocolBody(
            action = ActionPathEnum.SEND_GROUP_SIGN.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RawResp>() {})
        }
    }

    /**
     * 群组踢人
     *
     * @param groupId 群号
     * @param userId 要踢的 QQ 号
     * @param rejectAddRequest 拒绝此人的加群请求 (默认false)
     * @return result [RawResp]
     */
    override fun setGroupKick(groupId: Long, userId: Long, rejectAddRequest: Boolean): RawResp {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        params.put(ParamsKey.USER_ID, userId)
        params.put(ParamsKey.REJECT_ADD_REQUEST, rejectAddRequest)
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_KICK.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RawResp>() {})
        }
    }

    /**
     * 群组单人禁言
     *
     * @param groupId 群号
     * @param userId 要禁言的 QQ 号
     * @param duration 禁言时长, 单位秒, 0 表示取消禁言 (默认30 * 60)
     * @return result [RawResp]
     */
    override fun setGroupBan(groupId: Long, userId: Long, duration: Int): RawResp {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        params.put(ParamsKey.USER_ID, userId)
        params.put(ParamsKey.DURATION, duration)
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_BAN.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RawResp>() {})
        }
    }

    /**
     * 群组匿名用户禁言
     *
     * @param groupId 群号
     * @param anonymous 要禁言的匿名用户对象（群消息上报的 anonymous 字段）
     * @param duration 禁言时长，单位秒，无法取消匿名用户禁言
     * @return result [RawResp]
     */
    override fun setGroupAnonymousBan(groupId: Long, anonymous: Anonymous, duration: Int): RawResp {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        params.set<JsonNode>(ParamsKey.ANONYMOUS, mapper.valueToTree(anonymous))
        params.put(ParamsKey.DURATION, duration)
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_ANONYMOUS_BAN.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RawResp>() {})
        }
    }

    /**
     * 全体禁言
     *
     * @param groupId 群号
     * @param enable  是否禁言（默认True,False为取消禁言）
     * @return result [RawResp]
     */
    override fun setGroupWholeBan(groupId: Long, enable: Boolean): RawResp {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        params.put(ParamsKey.ENABLE, enable)
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_WHOLE_BAN.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RawResp>() {})
        }
    }

    /**
     * 群组设置管理员
     *
     * @param groupId 群号
     * @param userId  要设置管理员的 QQ 号
     * @param enable  true 为设置，false 为取消
     * @return result [RawResp]
     */
    override fun setGroupAdmin(groupId: Long, userId: Long, enable: Boolean): RawResp {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        params.put(ParamsKey.USER_ID, userId)
        params.put(ParamsKey.ENABLE, enable)
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_ADMIN.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RawResp>() {})
        }
    }

    /**
     * 群组匿名
     *
     * @param groupId 群号
     * @param enable  是否允许匿名聊天
     * @return result [RawResp]
     */
    override fun setGroupAnonymous(groupId: Long, enable: Boolean): RawResp {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        params.put(ParamsKey.ENABLE, enable)
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_ANONYMOUS.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RawResp>() {})
        }
    }

    /**
     * 设置群名片（群备注）
     *
     * @param groupId 群号
     * @param userId  要设置的 QQ 号
     * @param card    群名片内容，不填或空字符串表示删除群名片
     * @return result [RawResp]
     */
    override fun setGroupCard(groupId: Long, userId: Long, card: String): RawResp {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        params.put(ParamsKey.USER_ID, userId)
        params.put(ParamsKey.CARD, card)
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_CARD.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RawResp>() {})
        }
    }

    /**
     * 设置群名
     *
     * @param groupId   群号
     * @param groupName 新群名
     * @return result [RawResp]
     */
    override fun setGroupName(groupId: Long, groupName: String): RawResp {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        params.put(ParamsKey.GROUP_NAME, groupName)
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_NAME.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RawResp>() {})
        }
    }

    /**
     * 退出群组
     *
     * @param groupId   群号
     * @param isDismiss 是否解散, 如果登录号是群主, 则仅在此项为 true 时能够解散
     * @return result [RawResp]
     */
    override fun setGroupLeave(groupId: Long, isDismiss: Boolean): RawResp {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        params.put(ParamsKey.IS_DISMISS, isDismiss)
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_LEAVE.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RawResp>() {})
        }
    }

    /**
     * 设置群组专属头衔
     * @param groupId 群号
     * @param userId QQ号
     * @param title 专属头衔，不填或空字符串表示删除专属头衔
     * @return result [RawResp]
     */
    override fun setGroupSpecialTitle(groupId: Long, userId: Long, title: String): RawResp {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        params.put(ParamsKey.USER_ID, userId)
        params.put(ParamsKey.SPECIAL_TITLE, title)
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_SPECIAL_TITLE.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RawResp>() {})
        }
    }

    /**
     * 处理加好友请求
     *
     * @param flag    加好友请求的 flag（需从上报的数据中获得）
     * @param approve 是否同意请求(默认为true)
     * @param remark  添加后的好友备注（仅在同意时有效）
     * @return result [RawResp]
     */
    override fun setFriendAddRequest(flag: String, approve: Boolean, remark: String): RawResp {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.FLAG, flag)
        params.put(ParamsKey.APPROVE, approve)
        params.put(ParamsKey.REMARK, remark)
        val api = ProtocolBody(
            action = ActionPathEnum.SET_FRIEND_ADD_REQUEST.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RawResp>() {})
        }
    }

    /**
     * 处理加群请求／邀请
     *
     * @param flag    加群请求的 flag（需从上报的数据中获得）
     * @param subType add 或 invite，请求类型（需要和上报消息中的 sub_type 字段相符）
     * @param approve 是否同意请求／邀请
     * @param reason  拒绝理由（仅在拒绝时有效）
     * @return result [RawResp]
     */
    override fun setGroupAddRequest(flag: String, subType: String, approve: Boolean, reason: String): RawResp {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.FLAG, flag)
        params.put(ParamsKey.SUB_TYPE, subType)
        params.put(ParamsKey.APPROVE, approve)
        params.put(ParamsKey.REASON, reason)
        val api = ProtocolBody(
            action = ActionPathEnum.SET_GROUP_ADD_REQUEST.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RawResp>() {})
        }
    }


    /**
     * 获取登录号信息
     *
     * @return result [RespData] of [GetLoginInfoResp]
     */
    override fun getLoginInfo(): RespData<GetLoginInfoResp> {
        val api = ProtocolBody(
            action = ActionPathEnum.GET_LOGIN_INFO.path, params = mapper.createObjectNode()
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespData<GetLoginInfoResp>>() {})
        }
    }

    /**
     * 获取陌生人信息
     *
     * @param userId  QQ 号
     * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
     * @return result [RespData] of [StrangerInfoResp]
     */
    override fun getStrangerInfo(userId: Long, noCache: Boolean): RespData<StrangerInfoResp> {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.USER_ID, userId)
        params.put(ParamsKey.NO_CACHE, noCache)
        val api = ProtocolBody(
            action = ActionPathEnum.GET_STRANGER_INFO.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespData<StrangerInfoResp>>() {})
        }
    }

    /**
     * 获取好友列表
     *
     * @return result [RespData] of [FriendInfoResp]
     */
    override fun getFriendList(): RespData<FriendInfoResp> {
        val api = ProtocolBody(
            action = ActionPathEnum.GET_FRIEND_LIST.path, params = mapper.createObjectNode()
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespData<FriendInfoResp>>() {})
        }
    }

    /**
     * 获取群信息
     *
     * @param groupId 群号
     * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
     * @return result [RespData] of [GroupInfoResp]
     */
    override fun getGroupInfo(groupId: Long, noCache: Boolean): RespData<GroupInfoResp> {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        params.put(ParamsKey.NO_CACHE, noCache)
        val api = ProtocolBody(
            action = ActionPathEnum.GET_GROUP_INFO.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespData<GroupInfoResp>>() {})
        }
    }

    /**
     * 获取群成员信息
     *
     * @param groupId 群号
     * @param userId  QQ 号
     * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
     * @return result [RespData] of [GroupMemberInfoResp]
     */
    override fun getGroupMemberInfo(
        groupId: Long, userId: Long, noCache: Boolean
    ): RespData<GroupMemberInfoResp> {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        params.put(ParamsKey.USER_ID, userId)
        params.put(ParamsKey.NO_CACHE, noCache)
        val api = ProtocolBody(
            action = ActionPathEnum.GET_GROUP_MEMBER_INFO.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespData<GroupMemberInfoResp>>() {})
        }
    }

    /**
     * 获取群成员列表
     *
     * @param groupId 群号
     * @return result [RespList] of [GroupMemberInfoResp]
     */
    override fun getGroupMemberList(groupId: Long): RespList<GroupMemberInfoResp> {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        val api = ProtocolBody(
            action = ActionPathEnum.GET_GROUP_MEMBER_LIST.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespList<GroupMemberInfoResp>>() {})
        }
    }

    /**
     * 获取群荣誉信息
     *
     * @param groupId 群号
     * @param type    要获取的群荣誉类型, 可传入 talkative performer legend strong_newbie emotion 以分别获取单个类型的群荣誉数据, 或传入 all 获取所有数据
     * @return result [RespData] of [GroupHonorInfoResp]
     */
    override fun getGroupHonorInfo(groupId: Long, type: String): RespData<GroupHonorInfoResp> {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        params.put(ParamsKey.TYPE, type)
        val api = ProtocolBody(
            action = ActionPathEnum.GET_GROUP_HONOR_INFO.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespData<GroupHonorInfoResp>>() {})
        }
    }

    /**
     * 检查是否可以发送图片
     *
     * @return result [RespData] of [BooleanResp]
     */
    override fun canSendImage(): RespData<BooleanResp> {
        val api = ProtocolBody(
            action = ActionPathEnum.CAN_SEND_IMAGE.path, params = mapper.createObjectNode()
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespData<BooleanResp>>() {})
        }
    }

    /**
     * 检查是否可以发送语音
     *
     * @return result [RespData] of [BooleanResp]
     */
    override fun canSendRecord(): RespData<BooleanResp> {
        val api = ProtocolBody(
            action = ActionPathEnum.CAN_SEND_RECORD.path, params = mapper.createObjectNode()
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespData<BooleanResp>>() {})
        }
    }

    /**
     * 获取状态
     *
     * @return result [RespData] of [GetStatusResp]
     */
    override fun getStatus(): RespData<GetStatusResp> {
        val api = ProtocolBody(
            action = ActionPathEnum.GET_STATUS.path, params = mapper.createObjectNode()
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespData<GetStatusResp>>() {})
        }
    }

    /**
     * 发送合并转发
     * @param event 事件
     * @param msg   自定义转发消息
     * @return [RespData]
     */
    override fun sendForwardMsg(event: MessageEvent, msg: List<Map<String, Any>>): RespData<MsgId> {
        val params = mapper.createObjectNode()
        when (event) {
            is GroupMessageEvent -> params.put(ParamsKey.GROUP_ID, event.groupId)
            is PrivateMessageEvent -> params.put(ParamsKey.USER_ID, event.userId)
        }
        params.set<ArrayNode>(ParamsKey.MESSAGES, mapper.valueToTree(msg))
        val api = ProtocolBody(
            action = ActionPathEnum.SEND_FORWARD_MSG.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespData<MsgId>>() {})
        }
    }

    /**
     * 发送合并转发 (私聊)
     * @param userId 目标用户
     * @param msg    自定义转发消息
     * @return [RespData]
     */
    override fun sendPrivateForwardMsg(userId: Long, msg: List<Map<String, Any>>): RespData<MsgId> {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.USER_ID, userId)
        params.set<ArrayNode>(ParamsKey.MESSAGES, mapper.valueToTree(msg))
        val api = ProtocolBody(
            action = ActionPathEnum.SEND_PRIVATE_FORWARD_MSG.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespData<MsgId>>() {})
        }
    }

    /**
     * 发送合并转发 (群聊)
     * @param groupId 群号
     * @param msg    自定义转发消息
     * @return [RespData]
     */
    override fun sendGroupForwardMsg(groupId: Long, msg: List<Map<String, Any>>): RespData<MsgId> {
        val params = mapper.createObjectNode()
        params.put(ParamsKey.GROUP_ID, groupId)
        params.set<ArrayNode>(ParamsKey.MESSAGES, mapper.valueToTree(msg))
        val api = ProtocolBody(
            action = ActionPathEnum.SEND_GROUP_FORWARD_MSG.path, params = params
        )
        val result = session.sendMessage(mapper.valueToTree(api))
        return result.let {
            mapper.treeToValue(it, object : TypeReference<RespData<MsgId>>() {})
        }
    }
}
