package io.github.qingshu.ayaka.event.message

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
data class AnyMessageEvent(
    override var messageType: String? = null,
    override var groupId: Long? = null,
    override var userId: Long? = null,
) : GroupMessageEvent()