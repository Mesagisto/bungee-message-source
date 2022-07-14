package org.mesagisto.mcproxy.handlers

import com.github.jknack.handlebars.Context
import io.nats.client.impl.NatsMessage
import org.meowcat.mesagisto.client.Base64
import org.meowcat.mesagisto.client.Server
import org.meowcat.mesagisto.client.data.Either
import org.meowcat.mesagisto.client.data.Message
import org.meowcat.mesagisto.client.data.MessageType
import org.meowcat.mesagisto.client.data.Packet
import org.mesagisto.mcproxy.MultiServer
import org.mesagisto.mcproxy.Plugin.CONFIG
import org.mesagisto.mcproxy.Template

object Receive {
  suspend fun recover() {
    CONFIG.bindings.forEach {
      runCatching {
        Server.recv(it.key, it.value) handler@{ msg, id ->
          return@handler mainHandler(msg as NatsMessage, id)
        }
      }
    }
  }
  suspend fun add(target: String, address: String) {
    Server.recv(target, address) handler@{ msg, server ->
      return@handler mainHandler(msg as NatsMessage, server)
    }
  }
  suspend fun change(target: String, address: String) {
    Server.unsub(target)
    add(target, address)
  }
  fun del(target: String) {
    Server.unsub(target)
  }
}
fun mainHandler(
  message: NatsMessage,
  server: String
): Result<Unit> = runCatching {
  when (val packet = Packet.fromCbor(message.data).getOrThrow()) {
    is Either.Left -> {
      leftSubHandler(packet.value, server).getOrThrow()
    }
    is Either.Right -> {
      packet.value
    }
  }
}
fun leftSubHandler(
  message: Message,
  server: String
): Result<Unit> = runCatching fn@{
  val senderName = with(message.profile) { nick ?: username ?: Base64.encodeToString(id) }
  val msgList = message.chain.filterIsInstance<MessageType.Text>()
  msgList.forEach {
    val text = renderMessage(senderName, it.content)
    MultiServer.broadcastOn(server, text)
  }
}

private fun renderMessage(sender: String, content: String): String {
  val module = HashMap<String, String>(2)
  module.apply {
    put("sender", sender)
    put("content", content)
  }
  val context = Context.newContext(module)
  return Template.apply("message", context)
}
