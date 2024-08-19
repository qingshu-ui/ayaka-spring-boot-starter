package io.github.qingshu.ayaka.utils

import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.core.type.filter.AssignableTypeFilter
import kotlin.reflect.KClass

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
object RefectionUtils {

    fun findSubClasses(baseClass: KClass<*>): List<Class<*>> {
        val scanner = ClassPathScanningCandidateComponentProvider(false)
        scanner.addIncludeFilter(AssignableTypeFilter(baseClass.java))
        val basePackage = baseClass.java.packageName
        return scanner.findCandidateComponents(basePackage)
            .map { Class.forName(it.beanClassName) }
            .filter { baseClass.java.isAssignableFrom(it) && it != baseClass.java }
    }

    fun initStaticFun(baseClass: KClass<*>) {
        findSubClasses(baseClass)
    }
}