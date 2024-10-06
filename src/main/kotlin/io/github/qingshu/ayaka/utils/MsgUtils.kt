package io.github.qingshu.ayaka.utils

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Suppress("unused")
class MsgUtils {

    private val builder = StringBuilder()

    /**
     * 文本内容
     * @param text 内容
     * @return [MsgUtils]
     */
    fun text(text: String): MsgUtils {
        builder.append(text)
        return this
    }

    /**
     * 图片
     * 支持本地图片，网络图片，Base64
     * @param img 图片
     * @return [MsgUtils]
     */
    fun img(img: String): MsgUtils {
        builder.append("[CQ:image,file=${escape(img)}]")
        return this
    }

    /**
     * 短视频
     * @param video 视频地址, 支持 http 和 file 发送
     * @param cover 视频封面, 支持 http, file 和 base64 发送, 格式必须为 jpg
     * @return [MsgUtils]
     */
    fun video(video: String, cover: String): MsgUtils {
        builder.append("[CQ:video,file=${escape(video)},cover=${escape(cover)}]")
        return this
    }

    /**
     * 闪照
     * @param img 图片
     * @return [MsgUtils]
     */
    fun flashImg(img: String): MsgUtils {
        builder.append("[CQ:image,type=flash,file=${escape(img)}]")
        return this
    }

    /**
     * QQ 表情
     * @param id QQ 表情 ID
     * @return [MsgUtils]
     */
    fun face(id: Int): MsgUtils {
        builder.append("[CQ:face,id=$id]")
        return this
    }

    /**
     * 语音
     * @param voice 语音, 支持本地文件和 URL
     * @return [MsgUtils]
     */
    fun voice(voice: String): MsgUtils {
        builder.append("[CQ:record,file=${escape(voice)}]")
        return this
    }

    /**
     * at 某人
     * @param userId at 的 QQ 号
     * @return [MsgUtils]
     */
    fun at(userId: Long): MsgUtils {
        builder.append("[CQ:at,qq=$userId]")
        return this
    }

    /**
     * at 全体成员
     * @return [MsgUtils]
     */
    fun atAll(): MsgUtils {
        builder.append("[CQ:at,qq=all]")
        return this
    }

    /**
     * 戳一戳
     * @param userId 需要戳的成员
     * @return [MsgUtils]
     */
    fun poke(userId: Long): MsgUtils {
        builder.append("[CQ:poke,qq=$userId]")
        return this
    }

    /**
     * 回复
     * @param msgId 回复时所引用的消息 id, 必须为本群消息.
     * @return [MsgUtils]
     */
    fun reply(msgId: Int): MsgUtils {
        builder.append("[CQ:reply,id=$msgId]")
        return this
    }

    /**
     * 回复-频道
     * @param msgId 回复时所引用的消息 id, 必须为本频道消息.
     * @return [MsgUtils]
     */
    fun reply(msgId: String): MsgUtils {
        builder.append("[CQ:reply,id=\"$msgId\"]")
        return this
    }

    /**
     * 礼物
     * 仅支持免费礼物, 发送群礼物消息 无法撤回, 返回的 message id 恒定为 0
     *
     * @param userId 接收礼物的成员
     * @param giftId 礼物的类型
     * @return [MsgUtils]
     */
    fun gift(userId: Long, giftId: Int): MsgUtils {
        builder.append("[CQ:gift,qq=$userId,id=$giftId]")
        return this
    }

    /**
     * 文本转语音
     * 通过腾讯的 TTS 接口, 采用的音源与登录账号的性别有关
     * @param text 内容
     * @return [MsgUtils]
     */
    fun tts(text: String): MsgUtils {
        builder.append("[CQ:tts,text=${escape(text)}]")
        return this
    }

    /**
     * XML 消息
     * @param data xml内容, xml 中的 value 部分, 记得实体化处理
     * @return [MsgUtils]
     */
    fun xml(data: String): MsgUtils {
        builder.append("[CQ:xml,data=${escape(data)}]")
        return this
    }

    /**
     * XML 消息
     * @param data xml内容, xml 中的 value 部分, 记得实体化处理
     * @param resId 可以不填
     * @return [MsgUtils]
     */
    fun xml(data: String, resId: Int): MsgUtils {
        builder.append("[CQ:xml,data=${escape(data)},resid=$resId]")
        return this
    }

    /**
     * JSON 消息
     * @param data json 内容, json 的所有字符串记得实体化处理
     * @return [MsgUtils]
     */
    fun json(data: String): MsgUtils {
        builder.append("[CQ:json,data=${escape(data)}]")
        return this
    }

    /**
     * JSON 消息
     * @param data json 内容, json 的所有字符串记得实体化处理
     * @param resId 默认不填为 0, 走小程序通道, 填了走富文本通道发送
     * @return [MsgUtils]
     */
    fun json(data: String, resId: Int = 0): MsgUtils {
        builder.append("[CQ:json,data=${escape(data)},resid=$resId]")
        return this
    }

    /**
     * 一种 xml 的图片消息
     * xml 接口的消息都存在风控风险, 请自行兼容发送失败后的处理 ( 可以失败后走普通图片模式 )
     * @param file 和 image 的 file 字段对齐, 支持也是一样的
     * @return [MsgUtils]
     */
    fun cardImage(file: String): MsgUtils {
        builder.append("[CQ:cardimage,file=${escape(file)}]")
        return this
    }

    /**
     * 一种 xml 的图片消息
     * xml 接口的消息都存在风控风险, 请自行兼容发送失败后的处理 ( 可以失败后走普通图片模式 )
     * @param file      和 image 的 file 字段对齐, 支持也是一样的
     * @param minWidth  默认不填为 400, 最小 width
     * @param minHeight 默认不填为 400, 最小 height
     * @param maxWidth  默认不填为 500, 最大 width
     * @param maxHeight 默认不填为 1000, 最大 height
     * @param source    分享来源的名称, 可以留空
     * @param icon      分享来源的 icon 图标 url, 可以留空
     * @return [MsgUtils]
     */
    fun cardImage(
        file: String, minWidth: Int, minHeight: Int, maxWidth: Int,
        maxHeight: Int, source: String, icon: String,
    ): MsgUtils {
        builder.append(
            "[CQ:cardimage,file=${escape(file)},minwidth=$minWidth,minheight=$minHeight," +
                    "maxwidth=$maxWidth,maxheight=$maxHeight,source=${escape(source)},icon=${escape(icon)}]"
        )
        return this
    }

    /**
     * 音乐分享
     * @param type qq 163 xm (分别表示使用 QQ 音乐、网易云音乐、虾米音乐)
     * @param id   歌曲 ID
     * @return [MsgUtils]
     */
    fun music(type: String, id: Int): MsgUtils {
        builder.append("[CQ:music,type=${escape(type)},id=$id]")
        return this
    }

    /**
     * 音乐自定义分享
     * @param url     点击后跳转目标 URL
     * @param audio   音乐 URL
     * @param title   标题
     * @param content 发送时可选，内容描述
     * @param image   发送时可选，图片 URL
     * @return [MsgUtils]
     */
    fun customMusic(url: String, audio: String, title: String, content: String, image: String): MsgUtils {
        builder.append(
            "[CQ:music,type=custom,url=${escape(url)},audio=${escape(audio)},title=${escape(title)},content=${
                escape(
                    content
                )
            },image=${escape(image)}]"
        )
        return this
    }

    /**
     * 音乐自定义分享
     * @param url   点击后跳转目标 URL
     * @param audio 音乐 URL
     * @param title 标题
     * @return [MsgUtils]
     */
    fun customMusic(url: String, audio: String, title: String): MsgUtils {
        builder.append("[CQ:music,type=custom,url=${escape(url)},audio=${escape(audio)},title=${escape(title)}]")
        return this
    }

    /**
     * 发送猜拳消息
     * @param value 0石头 1剪刀 2布
     * @return [MsgUtils]
     */
    fun rps(value: Int): MsgUtils {
        builder.append("[CQ:rps,value=$value]")
        return this
    }

    /**
     * 发送长消息
     * @param id 长消息Id
     * @return [MsgUtils]
     */
    fun longMsg(id: String): MsgUtils {
        builder.append("[CQ:longmsg,id=$id]")
        return this
    }

    /**
     * 发送合并转发消息
     * @param id 合并转发消息Id
     * @return [MsgUtils]
     */
    fun forward(id: String): MsgUtils {
        builder.append("[CQ:forward,id=$id]")
        return this
    }

    /**
     * 构建消息链
     * @return [String]
     */
    fun build(): String = builder.toString()

    companion object {
        fun builder() = MsgUtils()

        fun create(initializer: MsgUtils.() -> Unit) =
            MsgUtils().apply(initializer).build()
    }
}

fun msgBuilder(initializer: MsgUtils.() -> Unit) = lazy {
    MsgUtils().apply(initializer).build()
}