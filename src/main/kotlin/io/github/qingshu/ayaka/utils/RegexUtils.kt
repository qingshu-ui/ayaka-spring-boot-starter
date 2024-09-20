package io.github.qingshu.ayaka.utils

import java.util.Collections.synchronizedMap
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
object RegexUtils {

    private val cache = synchronizedMap(mutableMapOf<String, Pattern>())

    /**
     * 正则匹配
     * @param regex 正则表达式
     * @param text  匹配内容
     * @return [MatchResult]
     */
    fun matcher(regex: String, text: String): Matcher? {
        val pattern = cache.computeIfAbsent(regex) { Pattern.compile(it) }
        val matcher = pattern.matcher(text)
        return if (matcher.matches()) matcher else null
    }
}