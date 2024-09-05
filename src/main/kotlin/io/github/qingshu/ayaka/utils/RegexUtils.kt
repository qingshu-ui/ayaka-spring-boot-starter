package io.github.qingshu.ayaka.utils

import java.util.Collections.synchronizedMap

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
object RegexUtils {

    private val cache = synchronizedMap(mutableMapOf<String, Regex>())

    /**
     * 正则匹配
     * @param regex 正则表达式
     * @param text  匹配内容
     * @return [MatchResult]
     */
    fun matcher(regex: String, text: String): MatchResult? {
        val pattern = cache.getOrPut(regex) { Regex(regex) }
        return pattern.matchEntire(text)
    }
}