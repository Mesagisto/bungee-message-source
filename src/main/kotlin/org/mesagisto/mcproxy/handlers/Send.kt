package org.mesagisto.mcproxy.handlers

import net.md_5.bungee.api.event.ChatEvent
import org.meowcat.mesagisto.client.Server
import org.meowcat.mesagisto.client.data.* // ktlint-disable no-wildcard-imports
import org.meowcat.mesagisto.client.toByteArray
import org.mesagisto.mcproxy.MultiServer
import org.mesagisto.mcproxy.Plugin.CONFIG
import org.mesagisto.mcproxy.asBytes
import java.util.concurrent.atomic.AtomicInteger

suspend fun send(
  event: ChatEvent
) {
  val serverName = MultiServer.getServerName(event.receiver) ?: return
  val sender = MultiServer.getPlayer(event.sender) ?: return
  val channel = CONFIG.bindings[serverName] ?: return
  val msgId = CONFIG.idCounter
    .getOrPut(serverName) { AtomicInteger(0) }
    .getAndIncrement()
  val chain = listOf<MessageType>(
    MessageType.Text(event.message)
  )
  val message = Message(
    profile = Profile(
      sender.uniqueId.asBytes(),
      sender.name,
      sender.displayName
    ),
    id = msgId.toByteArray(),
    chain = chain
  )
  val packet = Packet.from(message.left())
  Server.send(serverName, channel, packet)
}
