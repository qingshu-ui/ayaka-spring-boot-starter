package io.github.qingshu.ayaka.dto.event.meta

import com.alibaba.fastjson2.JSONObject
import com.alibaba.fastjson2.annotation.JSONField
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
    @JSONField(name = "time")
    var interval: Long? = null

    @JSONField(name = "status")
    var status: Status? = null

    class Status {
        @JSONField(name = "app_initialized")
        var appInitialized: Boolean? = null

        @JSONField(name = "app_enabled")
        var appEnabled: Boolean? = null

        @JSONField(name = "app_good")
        var appIsGood: Boolean? = null

        @JSONField(name = "plugins_good")
        var pluginsIsGood: Boolean? = null

        @JSONField(name = "online")
        var online: Boolean? = null

        @JSONField(name = "stat")
        var stat: StatusStatistics? = null
    }

    class StatusStatistics {
        @JSONField(name = "packet_received")
        var packetReceived: Long? = null

        @JSONField(name = "packet_sent")
        var packetSent: Long? = null

        @JSONField(name = "packet_lost")
        var packetLost: Long? = null

        @JSONField(name = "message_received")
        var messageReceived: Long? = null

        @JSONField(name = "message_sent")
        var messageSent: Long? = null

        @JSONField(name = "disconnect_times")
        var disconnectTimes: Long? = null

        @JSONField(name = "lost_times")
        var lostTimes: Long? = null

        @JSONField(name = "last_message_time")
        var lastMessageTime: Long? = null
    }

    companion object{
        init {
            events.add(HeartbeatMetaEvent::class)
        }

        fun canHandle(json: JSONObject): Boolean {
            return when{
                "meta_event" == json[POST_TYPE] -> "heartbeat" == json[META_EVENT_TYPE]
                else -> false
            }
        }
    }
}