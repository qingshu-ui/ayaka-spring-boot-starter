package io.github.qingshu.ayaka.utils

import com.fasterxml.jackson.annotation.JsonProperty


/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the APGL-3.0 License.
 * See the LICENSE file for details.
 */

@Suppress("unused", "MemberVisibilityCanBePrivate")
class Keyboard {

    @Suppress("MemberVisibilityCanBePrivate")
    companion object {
        const val STYLE_GREY = 0
        const val STYLE_BLUE = 1
        const val ACTION_TYPE_JUMP = 0
        const val ACTION_TYPE_CALLBACK = 1
        const val ACTION_TYPE_CMD = 2
        const val PERMISSION_TYPE_USER = 0
        const val PERMISSION_TYPE_MANAGER = 1
        const val PERMISSION_TYPE_ALL = 2
        const val PERMISSION_TYPE_ROLE = 3
        const val ANCHOR_NONE = 0
        const val ANCHOR_SELECT = 1

        fun builder() = Keyboard()
        fun builder(initializer: Keyboard.() -> Unit) = Keyboard().apply(initializer)
        fun callButtonBuilder(initializer: ButtonBuilder.() -> Unit) =
            ButtonBuilder().apply {
                actionType = ACTION_TYPE_CALLBACK
                initializer()
            }

        fun textButtonBuilder(initializer: ButtonBuilder.() -> Unit) =
            ButtonBuilder().apply {
                actionType = ACTION_TYPE_CMD
                initializer()
            }

        fun urlButtonBuilder(initializer: ButtonBuilder.() -> Unit) =
            ButtonBuilder().apply {
                actionType = ACTION_TYPE_JUMP
                initializer()
            }
    }

    val type = "keyboard"
    val data = Data()

    fun build() = this

    fun addButton(button: Button) = apply {
        val rows = data.content.rows
        if (rows.isEmpty()) {
            addRow()
        }
        rows[rows.size - 1].addButton(button)
        return this
    }

    fun addRow() = apply {
        val rows = data.content.rows
        if (rows.isNotEmpty() && rows.last().buttons.isEmpty()) {
            return this
        }
        rows.add(Row())
        return this
    }


    data class Data(
        val content: Content = Content(),
    )

    data class Content(
        val rows: ArrayList<Row> = arrayListOf(),
    )

    data class Row(
        val buttons: ArrayList<Button> = arrayListOf(),
    ) {
        fun addButton(button: Button) = this.buttons.add(button)
    }

    data class Button(
        var id: String = "",
        @JsonProperty("render_data")
        val renderData: RenderData = RenderData(),
        @JsonProperty("action")
        val action: Action = Action(),
    )

    data class RenderData(
        var label: String = "",
        @JsonProperty("visited_label")
        var visitedLabel: String = "",
        var style: Int = 0,
    )

    data class Action(
        var type: Int = 0,
        val permission: Permission = Permission(),
        var data: String = "",
        var reply: Boolean = false,
        var enter: Boolean = false,
        var anchor: Int = 0,
        @JsonProperty("unsupport_tips")
        var unSupportTips: String = "",
    )

    data class Permission(
        var type: Int = 0,
        var specifyUserIds: List<String> = emptyList(),
        var specifyRoleIds: List<String> = emptyList(),
    )

    @Suppress("MemberVisibilityCanBePrivate")
    class ButtonBuilder(
        var id: String = "",
        var label: String = "Button",
        var visitedLabel: String = "",
        var style: Int = STYLE_BLUE,
        var actionType: Int = ACTION_TYPE_CMD,
        var data: String = "",
        var reply: Boolean = false,
        var enter: Boolean = false,
        var permissionType: Int = PERMISSION_TYPE_ALL,
        var specifyUserIds: List<String> = emptyList(),
        var specifyRoleIds: List<String> = emptyList(),
        var anchor: Int = ANCHOR_NONE,
        var unSupportTips: String = "Unsupported current version",
    ) {

        fun build() = Button().apply {
            id = this@ButtonBuilder.id
            renderData.apply {
                label = this@ButtonBuilder.label
                visitedLabel = this@ButtonBuilder.visitedLabel
                style = this@ButtonBuilder.style
            }
            action.apply {
                type = this@ButtonBuilder.actionType
                data = this@ButtonBuilder.data
                reply = this@ButtonBuilder.reply
                enter = this@ButtonBuilder.enter
                anchor = this@ButtonBuilder.anchor
                unSupportTips = this@ButtonBuilder.unSupportTips
            }
            action.permission.apply {
                type = this@ButtonBuilder.permissionType
                specifyUserIds = this@ButtonBuilder.specifyUserIds
                specifyRoleIds = this@ButtonBuilder.specifyRoleIds
            }
        }

    }

}