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
class GroupMessageRecallEvent: NoticeEvent() {

    @JSONField(name = "group_id")
    var groupId: Long? = null

    @JSONField(name = "operator_id")
    var operatorId: Long? = null

    @JSONField(name = "message_id")
    var messageId: Int? = null

    companion object {
        init {
            events.add(GroupMessageRecallEvent::class)
        }

        fun canHandle(json: JSONObject): Boolean {
            return when {
                "notice" == json[POST_TYPE] -> "group_recall" == json[NOTICE_TYPE]
                else -> false
            }
        }
    }
}