package io.github.qingshu.ayaka.utils

import io.github.qingshu.ayaka.dto.ArrayMsg

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
class ArrayMsgUtils {
    private val builder: ArrayList<ArrayMsg> = arrayListOf()

    companion object {
        fun builder() = ArrayMsgUtils()
    }

    private fun getJsonData(type: String, consumer: (MutableMap<String, String>) -> Unit) = ArrayMsg().apply {
        this.setRawType(type)
        this.data = mutableMapOf<String, String>().apply(consumer)
    }

    fun text(text: String) = apply {
        builder.add(getJsonData("text") { it["text"] = text })
    }

    fun img(img: String) = apply {
        builder.add(getJsonData("image") { it["file"] = escape(img) })
    }

    fun video(video: String, cover: String) = apply {
        builder.add(getJsonData("video") {
            it["file"] = escape(video)
            it["cover"] = escape(cover)
        })
    }

    fun flashImg(img: String) = apply {
        builder.add(getJsonData("image") {
            it["flash"] = "flash"
            it["file"] = escape(img)
        })
    }

    fun face(id: Int) = apply {
        builder.add(getJsonData("face") { it["id"] = id.toString() })
    }

    fun voice(voice: String) = apply {
        builder.add(getJsonData("record") { it["file"] = escape(voice) })
    }

    fun at(userId: Long) = apply {
        builder.add(getJsonData("at") { it["qq"] = userId.toString() })
    }

    fun atAll() = apply {
        builder.add(getJsonData("at") { it["qq"] = "all" })
    }

    fun poke(userId: Long) = apply {
        builder.add(getJsonData("poke") { it["qq"] = userId.toString() })
    }

    fun reply(msgId: Int) = apply {
        builder.add(getJsonData("reply") { it["id"] = msgId.toString() })
    }

    fun reply(msgId: String) = apply {
        builder.add(getJsonData("reply") { it["id"] = msgId })
    }

    fun gift(userId: Long, giftId: Int) = apply {
        builder.add(getJsonData("gift") {
            it["qq"] = userId.toString()
            it["id"] = giftId.toString()
        })
    }

    fun tts(text: String) = apply {
        builder.add(getJsonData("tts") { it["text"] = text })
    }

    fun xml(data: String) = apply {
        builder.add(getJsonData("xml") { it["data"] = data })
    }

    fun xml(data: String, resId: Int) = apply {
        builder.add(getJsonData("xml") {
            it["data"] = data
            it["resid"] = resId.toString()
        })
    }

    fun json(data: String) = apply {
        builder.add(getJsonData("json") { it["data"] = escape(data) })
    }

    fun json(data: String, resId: Int) = apply {
        builder.add(getJsonData("json") {
            it["data"] = data
            it["resid"] = resId.toString()
        })
    }

    fun cardImage(file: String) = apply {
        builder.add(getJsonData("cardimage") { it["file"] = file })
    }

    fun cardImage(file: String, minW: Long, minH: Long, maxW: Long, maxH: Long, source: String, icon: String) = apply {
        builder.add(getJsonData("cardimage") {
            it["file"] = escape(file)
            it["minwidth"] = minW.toString()
            it["minheight"] = minH.toString()
            it["maxwidth"] = maxW.toString()
            it["maxheight"] = maxH.toString()
            it["source"] = escape(source)
            it["icon"] = escape(icon)
        })
    }

    fun music(type: String, id: Long) = apply {
        builder.add(getJsonData("music") {
            it["type"] = type
            it["id"] = id.toString()
        })
    }

    fun customMusic(url: String, audio: String, title: String, content: String, image: String) = apply {
        builder.add(getJsonData("music") {
            it["type"] = "custom"
            it["url"] = escape(url)
            it["audio"] = escape(audio)
            it["title"] = escape(title)
            it["content"] = escape(content)
            it["image"] = escape(image)
        })
    }

    fun customMusic(url: String, audio: String, title: String) = apply {
        builder.add(getJsonData("music") {
            it["type"] = "custom"
            it["url"] = escape(url)
            it["audio"] = escape(audio)
            it["title"] = escape(title)
        })
    }

    fun rps(value: Int) = apply {
        builder.add(getJsonData("rps") { it["value"] = value.toString() })
    }

    fun longMsg(id: String) {
        builder.add(getJsonData("longmsg") { it["id"] = id })
    }

    fun forward(id: String) = apply {
        builder.add(getJsonData("forward") { it["id"] = id })
    }

    fun markdown(content: String) = apply {
        builder.add(getJsonData("markdown") {
            val map = hashMapOf<String, String>()
            map["content"] = content
            it["content"] = mapper.writeValueAsString(map)
        })
    }

    fun buildCQ() = builder.joinToString("") { it.toCQCode() }

    fun build() = builder
}