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

    private fun findSubClasses(baseClass: KClass<*>): List<Class<*>> {
        val scanner = ClassPathScanningCandidateComponentProvider(false)
        scanner.addIncludeFilter(AssignableTypeFilter(baseClass.java))
        val basePackage = baseClass.java.packageName
        return scanner.findCandidateComponents(basePackage)
            .map { Class.forName(it.beanClassName) }
            .filter { baseClass.java.isAssignableFrom(it) && it != baseClass.java }
    }

    /**
     * 扫描指定类型的所有子类，使其静态成员被初始化
     * 应该在 spring 应用启动时完成调用此函数
     * 主要用于 不受 IOC 容器管理的 bean 对象
     */
    fun initStaticFun(baseClass: KClass<*>) {
        findSubClasses(baseClass)
    }
}