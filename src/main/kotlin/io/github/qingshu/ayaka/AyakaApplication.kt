package io.github.qingshu.ayaka

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@EnableScheduling
@SpringBootApplication
class AyakaApplication

fun main(args: Array<String>) {
    runApplication<AyakaApplication>(*args)
}