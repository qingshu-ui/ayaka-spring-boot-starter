package io.github.qingshu.ayaka.dto.event.notice

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.qingshu.ayaka.dto.constant.ParamsKey.NOTICE_TYPE
import io.github.qingshu.ayaka.dto.constant.ParamsKey.POST_TYPE

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
class GroupFileUploadEvent : NoticeEvent() {

    @JsonProperty("group_id")
    var groupId: Long = 0

    @JsonProperty("file")
    lateinit var file: File

    class File {
        @JsonProperty("id")
        lateinit var id: String

        @JsonProperty("name")
        lateinit var name: String

        @JsonProperty("size")
        var size: Long = 0

        @JsonProperty("busid")
        var busid: Long = 0

        @JsonProperty("url")
        lateinit var url: String
    }

    override fun setCancelled(cancelled: Boolean) {
        this.block = cancelled
    }

    override fun isCancelled(): Boolean {
        return this.block
    }

    companion object{
        init {
            events.add(GroupFileUploadEvent::class)
        }

        fun canHandle(json: ObjectNode) =
            "notice" == json[POST_TYPE].asText() && "group_upload" == json[NOTICE_TYPE].asText()
    }
}