package io.github.qingshu.ayaka.service

import java.util.concurrent.ScheduledThreadPoolExecutor

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the MIT License.
 * See the LICENSE file for details.
 */
interface ScheduledTask {
    fun executor(): ScheduledThreadPoolExecutor
}