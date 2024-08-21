package io.github.qingshu.ayaka.dto.event.notice

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.annotation.JSONField
import io.github.qingshu.ayaka.dto.constant.ParamsKey.NOTICE_TYPE
import io.github.qingshu.ayaka.dto.constant.ParamsKey.POST_TYPE
import io.github.qingshu.ayaka.dto.constant.ParamsKey.SUB_TYPE

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
class PokeNoticeEvent : NoticeEvent() {

    @JSONField(name = "sub_type")
    var subType: String? = null

    @JSONField(name = "sender_id")
    var senderId: Long? = null

    @JSONField(name = "target_id")
    var targetId: Long? = null

    @JSONField(name = "group_id")
    var groupId: Long? = null

    companion object {
        init {
            events.add(PokeNoticeEvent::class)
        }

        fun canHandle(json: JSONObject): Boolean {
            return when {
                "notice" == json[POST_TYPE] -> "notify" == json[NOTICE_TYPE] && "poke" == json[SUB_TYPE]
                else -> false
            }
        }
    }

}