package io.github.qingshu.ayaka.bot.compatible

import io.github.qingshu.ayaka.dto.general.GeneralRespData
import io.github.qingshu.ayaka.dto.resp.MsgId

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
interface OneBot {
    /**
     * 发送私聊消息
     * @param userId 对方 QQ 号
     * @param msg 消息内容
     * @param autoEscape 消息内容是否作为纯文本发送，不解析 CQ 码
     * @return result [GeneralRespData] of [MsgId]
     */
    fun sendPrivateMsg(userId: Long, msg: String, autoEscape: Boolean): GeneralRespData<MsgId>

    /**
     * 发送私聊消息
     * @param groupId 主动发起临时会话群号, 机器人本身必须是管理员/群主
     * @param userId 对方 QQ 号
     * @param msg 消息内容
     * @param autoEscape 消息内容是否作为纯文本发送，不解析 CQ 码
     * @return result [GeneralRespData] of [MsgId]
     */
    fun sendPrivateMsg(groupId: Long, userId: Long, msg: String, autoEscape: Boolean): GeneralRespData<MsgId>

    /*
    fun sendGroupMsg(): GeneralRespData<MsgId>
    fun sendMsg(): GeneralRespData<MsgId>
    fun deleteMsg(): GeneralRawResp
    fun getMsg(): GeneralRespData<GetMsgResp>
    fun getForwardMsg(): GeneralRespData<JSONObject>
    fun sendLike(): GeneralRawResp
    fun setGroupKick(): GeneralRawResp
    fun setGroupBan(): GeneralRawResp
    fun setGroupAnonymousBan(): GeneralRawResp
    fun setGroupWholeBan(): GeneralRawResp
    fun setGroupAdmin(): GeneralRawResp
    fun setGroupAnonymous(): GeneralRawResp
    fun setGroupCard(): GeneralRawResp
    fun setGroupName(): GeneralRawResp
    fun setGroupLeave(): GeneralRawResp
    fun setGroupSpecialTitle(): GeneralRawResp
    fun setFriendAddRequest(): GeneralRawResp
    fun setGroupAddRequest(): GeneralRawResp
    fun getLoginInfo(): GeneralRespData<GetLoginInfoResp>
    fun getStrangerInfo(): GeneralRespData<StrangerInfoResp>
    fun getFriendList(): GeneralRespData<FriendInfoResp>
    fun getGroupInfo(): GeneralRespData<GroupInfoResp>
    fun getGroupList(): GeneralRespList<GroupInfoResp>
    fun getGroupMemberInfo(): GeneralRespData<GroupMemberInfoResp>
    fun getGroupMemberList(): GeneralRespList<GroupMemberInfoResp>
    fun getGroupHonorInfo(): GeneralRespData<GroupHonorInfoResp>
    fun getCookies(): GeneralRespData<GetCooliesResp>
    fun getCsrfToken(): GeneralRespData<CsrfTokenResp>
    fun getCredentials(): GeneralRespData<CredentialsResp>
    fun getRecord(): GeneralRespData<GetResourceResp>
    fun getImage(): GeneralRespData<GetResourceResp>
    fun canSendImage(): GeneralRespData<BooleanResp>
    fun canSendRecord(): GeneralRespData<BooleanResp>
    fun getStatus(): GeneralRespData<GetStatusResp>
    fun getVersionInfo(): GeneralRespData<VersionInfoResp>
    fun setRestart(): GeneralRawResp
    fun cleanCache(): GeneralRawResp
    */
}