package io.github.qingshu.ayaka.event

import com.alibaba.fastjson2.JSONObject
import com.fasterxml.jackson.databind.util.LRUMap
import io.github.qingshu.ayaka.bot.BotContainer
import io.github.qingshu.ayaka.plugin.BotPlugin
import io.github.qingshu.ayaka.utils.RefectionUtils
import meteordevelopment.orbit.IEventBus
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import kotlin.reflect.full.companionObject
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.memberFunctions

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Component
class EventFactory @Autowired constructor(
    private val echoMap: LRUMap<String, CompletableFuture<JSONObject>>,
    private val botContainer: BotContainer,
    listener: List<BotPlugin>,
    private val bus: IEventBus
) {

    init {
        listener.forEach(bus::subscribe)
    }

    fun getEvent(json: JSONObject): GeneralEvent? {
        GeneralEvent.events.forEach { event ->
            val canHandleFun = event.companionObject?.memberFunctions?.find { it.name == "canHandle" }
            val canHandle = canHandleFun?.call(event.companionObjectInstance, json) as Boolean
            if (canHandle) {
                return json.to(event.java)
            }
        }
        return null
    }


    @Async("ayakaTaskExecutor")
    fun postEvent(xSelfId: Long, resp: JSONObject) {
        if (resp.containsKey("echo")) {
            echoMap[resp["echo"]].complete(resp)
        } else {
            val event = getEvent(resp)
            if(event != null) {
                event.bot = botContainer.bots[xSelfId]!!
                bus.post(event)
            }
        }
    }

    companion object {
        init {
            RefectionUtils.initStaticFun(GeneralEvent::class)
        }
        private val log = LoggerFactory.getLogger(EventFactory::class.java)
    }
}