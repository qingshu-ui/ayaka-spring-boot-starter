package io.github.qingshu.ayaka.utils

import java.util.regex.Matcher

data class CheckResult(
    var result: Boolean = false,
    var matcher: Matcher? = null,
) {
    fun changeResult() {
        result = !result
    }
}
