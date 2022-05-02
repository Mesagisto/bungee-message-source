package org.meowcat.mesagisto.bungeecord

import net.md_5.bungee.api.event.ChatEvent
import net.md_5.bungee.api.plugin.Listener
import net.md_5.bungee.event.EventHandler

object PluginListener : Listener {
  @EventHandler
  fun onChat(event: ChatEvent) {
    println(event.toString())
  }
}
