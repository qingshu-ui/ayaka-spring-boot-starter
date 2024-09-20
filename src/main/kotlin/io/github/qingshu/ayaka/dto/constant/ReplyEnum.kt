package io.github.qingshu.ayaka.dto.constant

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the MIT License.
 * See the LICENSE file for details.
 */
enum class ReplyEnum {
    /**
     * 不处理
     */
    OFF,
    /**
     * 不带回复
     */
    NONE,
    /**
     * 回复 bot 的消息
     */
    REPLY_ME,
    /**
     * 回复任意其他人的消息
     */
    REPLY_OTHER,
    /**
     * 任意包括回复的消息
     */
    REPLY_ALL,
}