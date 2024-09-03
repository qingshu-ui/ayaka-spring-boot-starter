package io.github.qingshu.ayaka.dto.event.notice

import com.alibaba.fastjson2.annotation.JSONField
import io.github.qingshu.ayaka.dto.event.GeneralEvent

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
open class NoticeEvent : GeneralEvent() {
    @JSONField(name = "notice_type")
    open var noticeType: String? = null

    @JSONField(name = "user_id")
    open var userId: Long? = null

    override fun setCancelled(cancelled: Boolean) {
        this.block = cancelled
    }

    override fun isCancelled(): Boolean {
        return this.block
    }
}