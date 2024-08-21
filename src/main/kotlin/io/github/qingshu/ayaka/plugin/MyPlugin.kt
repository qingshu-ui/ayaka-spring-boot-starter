package io.github.qingshu.ayaka.plugin

import io.github.qingshu.ayaka.bot.BotContainer
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Copyright (c) 2024 qingshu.
 * This file is part of the ayaka-spring-boot-starter project.
 *
 * This project is licensed under the GPL-3.0 License.
 * See the LICENSE file for details.
 */
@Component
class MyPlugin @Autowired constructor(val botContainer: BotContainer): BotPlugin {

    /*
    @EventHandler
    fun exampleListener(event: PrivateMessageEvent){
        val bot = event.bot!!
        val msg = event.messageId
        log.info("This a example plugin by {} handle", Thread.currentThread().name)
        val userId = event.sender!!.userId!!
        val result = bot.sendPrivateMsg(userId, "This a example", false)
        log.info("Sent message successfully, result: {}", result)
    }

    @EventHandler
    fun getGroupList(event: PrivateMessageEvent){
        val bot = event.bot!!
        val msg = event.rawMessage!!
        val userId = event.sender!!.userId!!
        if(msg == "group"){
            val result = bot.getGroupList()
            log.info("Get group list: {}", result)
        }
    }

    @EventHandler
    fun delMsg(event: PrivateMessageEvent){
        val bot = event.bot!!
        val msg = event.rawMessage!!
        val msgId = event.messageId!!
        if(msg == "del"){
            val result = bot.deleteMsg(msgId)
            log.info("Recall msg successfully, result: {}", result)
        }
    }

    @EventHandler
    fun testGetMsg(event: PrivateMessageEvent){
        val bot = event.bot!!
        val msg = event.rawMessage!!
        val userId = event.sender!!.userId!!
        val messageId = event.messageId!!
        if(msg == "get"){
            val result = bot.getMsg(messageId)
            log.info("Get message: {}", result)
        }
    }

    @EventHandler
    fun testSendLike(event: PrivateMessageEvent){
        val bot = event.bot!!
        val msg = event.rawMessage!!
        val userId = event.sender!!.userId!!
        if(msg == "like"){
            val result = bot.sendLike(userId, 10)
            log.info("Like message: {}", result)
        }
    }
     */

    @Scheduled(cron = "5 0 0 * * ?")
    fun taskGroupSign(){
        val groupList = listOf(
            244427991L,
            335783090L,
            737472779L
        )
        val botId = 2639125193
        val bot = botContainer.bots[botId]
        if(bot != null){
            groupList.forEach(bot::sendGroupSign)
        }else{
            log.warn("Task failed, bot is null")
        }
    }


    companion object{
        private val log = LoggerFactory.getLogger(MyPlugin::class.java)
    }
}