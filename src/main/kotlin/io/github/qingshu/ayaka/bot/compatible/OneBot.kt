package io.github.qingshu.ayaka.bot.compatible

import io.github.qingshu.ayaka.dto.general.Anonymous
import io.github.qingshu.ayaka.dto.general.GeneralRawResp
import io.github.qingshu.ayaka.dto.general.GeneralRespData
import io.github.qingshu.ayaka.dto.general.GeneralRespList
import io.github.qingshu.ayaka.dto.resp.*
import io.github.qingshu.ayaka.dto.event.message.AnyMessageEvent

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
    fun sendPrivateMsg(groupId: Long?, userId: Long, msg: String, autoEscape: Boolean): GeneralRespData<MsgId>

    /**
     * 发送群消息
     * @param groupId 群号
     * @param msg 消息内容
     * @param autoEscape 消息内容是否作为纯文本发送，不解析 CQ 码
     * @return result [GeneralRespData] of [MsgId]
     */
    fun sendGroupMsg(groupId: Long, msg: String, autoEscape: Boolean): GeneralRespData<MsgId>

    /**
     * 获取群列表
     * @return result [GeneralRespList] of [GroupInfoResp]
     */
    fun getGroupList(): GeneralRespList<GroupInfoResp>

    /**
     * 发送消息
     * @param event [AnyMessageEvent]
     * @param msg 消息内容
     * @param autoEscape 消息内容是否作为纯文本发送，不解析 CQ 码
     * @return result [GeneralRespData] of [MsgId]
     */
    fun sendMsg(event: AnyMessageEvent, msg:String, autoEscape: Boolean): GeneralRespData<MsgId>

    /**
     * 撤回消息
     *
     * @param msgId 消息 ID
     * @return result [GeneralRawResp]
     */
    fun deleteMsg(msgId: Int): GeneralRawResp

    /**
     * 获取消息
     *
     * @param msgId 消息 ID
     * @return result [GeneralRespData] of [GetMsgResp]
     */
    fun getMsg(msgId: Int): GeneralRespData<GetMsgResp>

    /**
     * 好友点赞
     *
     * @param userId 目标用户
     * @param times  点赞次数（每个好友每天最多 10 次，机器人为 Super VIP 则提高到 20次）
     * @return result [GeneralRawResp]
     */
    fun sendLike(userId: Long, times: Int = 10): GeneralRawResp

    /**
     * 群组踢人
     *
     * @param groupId 群号
     * @param userId 要踢的 QQ 号
     * @param rejectAddRequest 拒绝此人的加群请求 (默认false)
     * @return result [GeneralRawResp]
     */
    fun setGroupKick(groupId: Long, userId: Long, rejectAddRequest: Boolean = false): GeneralRawResp

    /**
     * 群组单人禁言
     *
     * @param groupId 群号
     * @param userId 要禁言的 QQ 号
     * @param duration 禁言时长, 单位秒, 0 表示取消禁言 (默认30 * 60)
     * @return result [GeneralRawResp]
     */
    fun setGroupBan(groupId: Long, userId: Long, duration: Int): GeneralRawResp

    /**
     * 群组匿名用户禁言
     *
     * @param groupId 群号
     * @param anonymous 要禁言的匿名用户对象（群消息上报的 anonymous 字段）
     * @param duration 禁言时长，单位秒，无法取消匿名用户禁言
     * @return result [GeneralRawResp]
     */
    fun setGroupAnonymousBan(groupId: Long, anonymous: Anonymous, duration: Int): GeneralRawResp

    /**
     * 全体禁言
     *
     * @param groupId 群号
     * @param enable  是否禁言（默认True,False为取消禁言）
     * @return result [GeneralRawResp]
     */
    fun setGroupWholeBan(groupId: Long, enable: Boolean): GeneralRawResp

    /**
     * 群组设置管理员
     *
     * @param groupId 群号
     * @param userId  要设置管理员的 QQ 号
     * @param enable  true 为设置，false 为取消
     * @return result [GeneralRawResp]
     */
    fun setGroupAdmin(groupId: Long, userId: Long, enable: Boolean): GeneralRawResp

    /**
     * 群组匿名
     *
     * @param groupId 群号
     * @param enable  是否允许匿名聊天
     * @return result [GeneralRawResp]
     */
    fun setGroupAnonymous(groupId: Long, enable: Boolean): GeneralRawResp

    /**
     * 设置群名片（群备注）
     *
     * @param groupId 群号
     * @param userId  要设置的 QQ 号
     * @param card    群名片内容，不填或空字符串表示删除群名片
     * @return result [GeneralRawResp]
     */
    fun setGroupCard(groupId: Long, userId: Long, card: String): GeneralRawResp

    /**
     * 设置群名
     *
     * @param groupId   群号
     * @param groupName 新群名
     * @return result [GeneralRawResp]
     */
    fun setGroupName(groupId: Long, groupName: String): GeneralRawResp

    /**
     * 退出群组
     *
     * @param groupId   群号
     * @param isDismiss 是否解散, 如果登录号是群主, 则仅在此项为 true 时能够解散
     * @return result [GeneralRawResp]
     */
    fun setGroupLeave(groupId: Long, isDismiss: Boolean): GeneralRawResp

    /**
     * 设置群组专属头衔
     * @param groupId 群号
     * @param userId QQ号
     * @param title 专属头衔，不填或空字符串表示删除专属头衔
     * @return result [GeneralRawResp]
     */
    fun setGroupSpecialTitle(groupId: Long, userId: Long, title: String = ""): GeneralRawResp

    /**
     * 处理加好友请求
     *
     * @param flag    加好友请求的 flag（需从上报的数据中获得）
     * @param approve 是否同意请求(默认为true)
     * @param remark  添加后的好友备注（仅在同意时有效）
     * @return result [GeneralRawResp]
     */
    fun setFriendAddRequest(flag: String, approve: Boolean, remark: String): GeneralRawResp

    /**
     * 处理加群请求／邀请
     *
     * @param flag    加群请求的 flag（需从上报的数据中获得）
     * @param subType add 或 invite，请求类型（需要和上报消息中的 sub_type 字段相符）
     * @param approve 是否同意请求／邀请
     * @param reason  拒绝理由（仅在拒绝时有效）
     * @return result [GeneralRawResp]
     */
    fun setGroupAddRequest(flag: String, subType: String, approve: Boolean, reason: String): GeneralRawResp


    /**
     * 获取登录号信息
     *
     * @return result [GeneralRespData] of [GetLoginInfoResp]
     */
    fun getLoginInfo(): GeneralRespData<GetLoginInfoResp>

    /**
     * 获取陌生人信息
     *
     * @param userId  QQ 号
     * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
     * @return result [GeneralRespData] of [StrangerInfoResp]
     */
    fun getStrangerInfo(userId: Long, noCache: Boolean): GeneralRespData<StrangerInfoResp>

    /**
     * 获取好友列表
     *
     * @return result [GeneralRespData] of [FriendInfoResp]
     */
    fun getFriendList(): GeneralRespData<FriendInfoResp>

    /**
     * 获取群信息
     *
     * @param groupId 群号
     * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
     * @return result [GeneralRespData] of [GroupInfoResp]
     */
    fun getGroupInfo(groupId: Long, noCache: Boolean): GeneralRespData<GroupInfoResp>

    /**
     * 获取群成员信息
     *
     * @param groupId 群号
     * @param userId  QQ 号
     * @param noCache 是否不使用缓存（使用缓存可能更新不及时，但响应更快）
     * @return result [GeneralRespData] of [GroupMemberInfoResp]
     */
    fun getGroupMemberInfo(groupId: Long, userId: Long, noCache: Boolean): GeneralRespData<GroupMemberInfoResp>

    /**
     * 获取群成员列表
     *
     * @param groupId 群号
     * @return result [GeneralRespList] of [GroupMemberInfoResp]
     */
    fun getGroupMemberList(groupId: Long): GeneralRespList<GroupMemberInfoResp>

    /**
     * 获取群荣誉信息
     *
     * @param groupId 群号
     * @param type    要获取的群荣誉类型, 可传入 talkative performer legend strong_newbie emotion 以分别获取单个类型的群荣誉数据, 或传入 all 获取所有数据
     * @return result [GeneralRespData] of [GroupHonorInfoResp]
     */
    fun getGroupHonorInfo(groupId: Long, type:String): GeneralRespData<GroupHonorInfoResp>

    /**
     * 检查是否可以发送图片
     *
     * @return result [GeneralRespData] of [BooleanResp]
     */
    fun canSendImage(): GeneralRespData<BooleanResp>

    /**
     * 检查是否可以发送语音
     *
     * @return result [GeneralRespData] of [BooleanResp]
     */
    fun canSendRecord(): GeneralRespData<BooleanResp>

    /**
     * 获取状态
     *
     * @return result [GeneralRespData] of [GetStatusResp]
     */
    fun getStatus(): GeneralRespData<GetStatusResp>

    /* Unrealized API for onebot v11
    fun getForwardMsg(): GeneralRespData<JSONObject>
    fun getCookies(): GeneralRespData<GetCooliesResp>
    fun getCsrfToken(): GeneralRespData<CsrfTokenResp>
    fun getCredentials(): GeneralRespData<CredentialsResp>
    fun getRecord(): GeneralRespData<GetResourceResp>
    fun getImage(): GeneralRespData<GetResourceResp>
    fun getVersionInfo(): GeneralRespData<VersionInfoResp>
    fun setRestart(): GeneralRawResp
    fun cleanCache(): GeneralRawResp
    */
}