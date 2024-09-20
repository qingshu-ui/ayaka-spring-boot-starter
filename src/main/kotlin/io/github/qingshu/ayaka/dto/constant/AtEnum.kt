package io.github.qingshu.ayaka.dto.constant

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the MIT License.
 * See the LICENSE file for details.
 */
enum class AtEnum {

    /**
     * 默认值
     */
    OFF,

    /**
     * 只处理带有 at 机器人的消息
     */
    NEED,

    /**
     * 若消息中 at 了机器人此条消息会被忽略
     */
    NOT_NEED,

    /**
     * AtEnum.NEED 和 AtEnum.NOT_NEED 的合并
     */
    BOTH,
}