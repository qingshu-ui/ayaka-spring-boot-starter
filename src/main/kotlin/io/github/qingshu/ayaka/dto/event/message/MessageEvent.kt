package io.github.qingshu.ayaka.dto.event.message

import com.alibaba.fastjson2.annotation.JSONField
import io.github.qingshu.ayaka.dto.ArrayMsg
import io.github.qingshu.ayaka.dto.event.GeneralEvent
import java.util.regex.Matcher

open class MessageEvent: GeneralEvent(){
    @JSONField(name = "message_type") open var messageType: String? = null
    @JSONField(name = "user_id") open var userId: Long? = null
    @JSONField(name = "message") open var message: String? = null
    @JSONField(name = "raw_message") open var rawMessage: String? = null
    @JSONField(name = "font") open var font: Int? = null

    @JSONField(serialize = false, deserialize = false)
    open var matcher: Matcher? = null

    @JSONField(serialize = false, deserialize = false)
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
        return atPattern.find(rawMessage ?: "") != null
    }
}
