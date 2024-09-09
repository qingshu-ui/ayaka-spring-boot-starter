package io.github.qingshu.ayaka.dto.event

import com.alibaba.fastjson2.JSONObject
import io.github.qingshu.ayaka.bot.BotContainer
import io.github.qingshu.ayaka.plugin.BotPlugin
import io.github.qingshu.ayaka.utils.RefectionUtils
import meteordevelopment.orbit.IEventBus
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ConcurrentHashMap
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
    private val echoMap: ConcurrentHashMap<String, CompletableFuture<JSONObject>>,
    private val botContainer: BotContainer,
    listener: List<BotPlugin>,
    private val bus: IEventBus
) {

    init {
        listener.forEach(bus::subscribe)
    }

    private fun getEvent(json: JSONObject): List<GeneralEvent> {
        val events = arrayListOf<GeneralEvent>()
        GeneralEvent.events.forEach { event ->
            val canHandleFun = event.companionObject?.memberFunctions?.find { it.name == "canHandle" }
            val canHandle = canHandleFun?.call(event.companionObjectInstance, json) as Boolean
            if (canHandle) {
                events.add(json.to(event.java))
            }
        }
        return events
    }

    /**
     * 将上报内容传递给事件监听器进行处理
     * @param xSelfId 接收上报消息的 QQ 号
     * @param resp [JSONObject] of fastjson2
     */
    suspend fun postEvent(xSelfId: Long, resp: JSONObject) {
        log.debug("{}", resp)
        resp["echo"]?.let {
            echoMap[it]?.complete(resp)
            return
        }

        val bot = botContainer.bots[xSelfId] ?: run {
            log.warn("[bot] is null for xSelfId: $xSelfId")
            return
        }

        getEvent(resp).run {
            if(isEmpty()){
                log.warn("[event] is null for response: $resp")
                return
            }
            forEach {
                it.bot = bot
                bus.post(it)
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