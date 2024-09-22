package io.github.qingshu.ayaka.annotation

import io.github.qingshu.ayaka.dto.constant.AtEnum
import io.github.qingshu.ayaka.dto.constant.MsgTypeEnum
import io.github.qingshu.ayaka.dto.constant.ReplyEnum
import java.lang.annotation.Inherited

/**
 * 此注解只对 MessageEvent 生效
 */
@MustBeDocumented
@Inherited
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class MessageHandlerFilter(

    /**
     * 触发命令，支持正则
     * 注: 仅用于消息校验, 不会返回 matcher (理论上可以做到, 但是会冲突
     *
     * @return 正则表达式
     */
    val cmd: String = "",

    /**
     * 检查是否被at
     * 如果值为 NEED        只处理带有at机器人的消息
     * 如果值为 NOT_NEED    若消息中at了机器人此条消息会被忽略
     *
     * @return at 枚举
     */
    val at: AtEnum = AtEnum.OFF,

    /**
     * 检测是否包含回复
     * OFF              不处理
     * NONE             不包括回复
     * REPLY_ME         回复 bot 的消息
     * REPLY_OTHER      回复任意其他人的消息
     * REPLY_ALL        任意包括回复的消息
     *
     * @return reply 枚举
     */
    val reply: ReplyEnum = ReplyEnum.OFF,

    /**
     * 消息中包含某一类型的
     * 注0: reply 如果设为 REPLY_XXX, types 默认增加一条额外的 type.reply, types 为空不受影响
     * 注1: 若 reply 为 NONE, types 包含 type.reply, 则本规则的 type.reply 条件无效
     */
    val types: Array<MsgTypeEnum> = [],

    /**
     * 仅注解指明的群组会触发, 如果为空则任意群组都可以触发
     * 注, 私聊消息无效
     *
     * @return 群组 ID
     */
    val groups: LongArray = [],

    /**
     * 仅注解指明的 qq 发送会触发, 如果为空则任意消息都可以触发
     *
     * @return 群组 ID
     */
    val senders: LongArray = [],

    /**
     * 若指明前缀, 则仅消息头部匹配前缀的消息才可以触发, 判断条件为or, 如果为空则任意消息都可以触发
     *
     * @return 前缀, 可多选
     */
    val startWith: Array<String> = [],

    /**
     * 若指明后缀, 则仅消息尾部匹配后缀的消息才可以触发, 判断条件为or, 如果为空则任意消息都可以触发
     *
     * @return 后缀缀, 可多选
     */
    val endWith: Array<String> = [],

    /**
     * 将过滤器反转, 即所有**不为默认值/非空**的过滤条件反转, 当某条件未设置时反转无效
     * 例如 指明`senders`后, 只有指明的 qq 发送的消息会触发, 反转过滤器后, 指明的 qq 则不会触发
     *
     * @return true 则反转
     */
    val invert: Boolean = false,
)
