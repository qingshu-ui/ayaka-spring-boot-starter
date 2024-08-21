package io.github.qingshu.ayaka.dto.event.message

import com.alibaba.fastjson2.annotation.JSONField
import io.github.qingshu.ayaka.dto.event.GeneralEvent

open class MessageEvent: GeneralEvent(){
    @JSONField(name = "message_type") open var messageType: String? = null
    @JSONField(name = "user_id") open var userId: Long? = null
    @JSONField(name = "message") open var message: String? = null
    @JSONField(name = "raw_message") open var rawMessage: String? = null
    @JSONField(name = "font") open var font: Int? = null
}
