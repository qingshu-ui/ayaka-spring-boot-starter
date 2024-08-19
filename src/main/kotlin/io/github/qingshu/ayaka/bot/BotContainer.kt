package io.github.qingshu.ayaka.bot

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
data class BotContainer(
    val bots: ConcurrentHashMap<Long, Bot> = ConcurrentHashMap()
)
