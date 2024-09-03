package io.github.qingshu.ayaka.dto.event.notice

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.annotation.JSONField
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

    @JSONField(name = "group_id")
    var groupId: Long? = null

    @JSONField(name = "file")
    var file: File? = null

    class File {
        @JSONField(name = "id")
        var id: String? = null

        @JSONField(name = "name")
        var name: String? = null

        @JSONField(name = "size")
        var size: Long? = null

        @JSONField(name = "busid")
        var busid: Long? = null

        @JSONField(name = "url")
        var url: String? = null
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

        fun canHandle(json: JSONObject): Boolean {
            return when{
                "notice" == json[POST_TYPE] -> "group_upload" == json[NOTICE_TYPE]
                else -> false
            }
        }
    }
}