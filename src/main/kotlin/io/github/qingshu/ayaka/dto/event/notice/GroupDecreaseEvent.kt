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
class GroupDecreaseEvent : NoticeEvent() {

    @JSONField(name = "sub_type")
    var subType: String? = null

    @JSONField(name = "group_id")
    var groupId: Long? = null

    @JSONField(name = "operator_id")
    var operatorId: Long? = null

    override fun setCancelled(cancelled: Boolean) {
        this.block = cancelled
    }

    override fun isCancelled(): Boolean {
        return this.block
    }

    companion object {
        init {
            events.add(GroupDecreaseEvent::class)
        }

        fun canHandle(json: JSONObject): Boolean {
            return when {
                "notice" == json[POST_TYPE] -> "group_decrease" == json[NOTICE_TYPE]
                else -> false
            }
        }
    }

}