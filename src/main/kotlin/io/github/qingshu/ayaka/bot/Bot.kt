package io.github.qingshu.ayaka.bot

import org.springframework.web.socket.WebSocketSession

class Bot(
    var selfId: Long,
    var session: WebSocketSession,
) {

}
