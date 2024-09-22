package io.github.qingshu.ayaka.dto.event.message

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import io.github.qingshu.ayaka.dto.ArrayMsg
import io.github.qingshu.ayaka.dto.event.GeneralEvent
import io.github.qingshu.ayaka.utils.ArrayJsonDeserializer
import java.util.regex.Matcher

open class MessageEvent: GeneralEvent(){
    @JsonProperty("message_type")
    open lateinit var messageType: String

    @JsonProperty("user_id")
    open var userId: Long = 0

    @JsonDeserialize(using = ArrayJsonDeserializer::class)
    @JsonProperty("message")
    open lateinit var message: String

    @JsonProperty("raw_message")
    open lateinit var rawMessage: String

    @JsonProperty("font")
    open var font: Int = 0

    @JsonIgnore
    open var matcher: Matcher? = null

    open var arrayMsg: List<ArrayMsg> = emptyList()

    override fun setCancelled(cancelled: Boolean) {
        this.block = cancelled
    }

    override fun isCancelled(): Boolean {
        return this.block
    }

    /**
     * 消息是否提及某人
     * @param userId 某人
     * @return [Boolean]
     */
    fun isAt(userId: Long): Boolean {
        val atPattern = "\\[CQ:at,qq=$userId(,[^]]*)?\\]".toRegex()
        return atPattern.find(rawMessage) != null
    }
}
