package io.github.qingshu.ayaka.utils

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the MIT License.
 * See the LICENSE file for details.
 */
class CacheUtils(
    private val groupsMap:HashMap<Int, LongArray> = hashMapOf(),
    private val sendersMap: HashMap<Int, LongArray> = hashMapOf(),
) {

    fun getSortedSenders(senders: LongArray): LongArray {
        return getSortedArray(senders, sendersMap)
    }

    fun getSortedGroups(groups: LongArray): LongArray {
        return getSortedArray(groups, groupsMap)
    }

    private fun getSortedArray(array: LongArray, map: HashMap<Int, LongArray>): LongArray {
        val key = array.contentHashCode()
        return map[key]?:array.clone().apply {
            sort()
            map[key] = this
        }
    }

}