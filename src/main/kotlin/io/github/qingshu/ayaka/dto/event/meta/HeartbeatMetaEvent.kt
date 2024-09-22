package io.github.qingshu.ayaka.dto.event.meta

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.node.ObjectNode
import io.github.qingshu.ayaka.dto.constant.ParamsKey.META_EVENT_TYPE
import io.github.qingshu.ayaka.dto.constant.ParamsKey.POST_TYPE
import io.github.qingshu.ayaka.dto.event.GeneralEvent

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
class HeartbeatMetaEvent: GeneralEvent() {
    @JsonProperty("time")
    var interval: Long = 0

    @JsonProperty("status")
    lateinit var status: Status

    class Status {
        @JsonProperty("app_initialized")
        var appInitialized: Boolean = false

        @JsonProperty("app_enabled")
        var appEnabled: Boolean = false

        @JsonProperty("app_good")
        var appIsGood: Boolean = false

        @JsonProperty("plugins_good")
        var pluginsIsGood: Boolean = false

        @JsonProperty("online")
        var online: Boolean = false

        @JsonProperty("stat")
        lateinit var stat: StatusStatistics
    }

    class StatusStatistics {
        @JsonProperty("packet_received")
        var packetReceived: Long = 0

        @JsonProperty("packet_sent")
        var packetSent: Long = 0

        @JsonProperty("packet_lost")
        var packetLost: Long = 0

        @JsonProperty("message_received")
        var messageReceived: Long = 0

        @JsonProperty("message_sent")
        var messageSent: Long = 0

        @JsonProperty("disconnect_times")
        var disconnectTimes: Long = 0

        @JsonProperty("lost_times")
        var lostTimes: Long = 0

        @JsonProperty("last_message_time")
        var lastMessageTime: Long = 0
    }

    override fun setCancelled(cancelled: Boolean) {
        this.block = cancelled
    }

    override fun isCancelled(): Boolean {
        return this.block
    }

    companion object{
        init {
            events.add(HeartbeatMetaEvent::class)
        }

        fun canHandle(json: ObjectNode) =
            "meta_event" == json[POST_TYPE].asText() && "heartbeat" == json[META_EVENT_TYPE].asText()
    }
}