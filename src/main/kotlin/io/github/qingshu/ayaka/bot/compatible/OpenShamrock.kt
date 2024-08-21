package io.github.qingshu.ayaka.bot.compatible

import io.github.qingshu.ayaka.dto.general.GeneralRawResp

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
interface OpenShamrock {
    /**
     * 群打卡，来自 OpenShamrock 在其他 Bot 实现可能无效
     * @param groupId 群号
     * @return result [GeneralRawResp]
     */
    fun sendGroupSign(groupId: Long): GeneralRawResp
}