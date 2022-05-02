package org.meowcat.mesagisto.bungeecord

import kotlinx.coroutines.runBlocking
import net.md_5.bungee.api.plugin.Plugin

class BungeecordAdapter : Plugin() {
  override fun onEnable() = runBlocking {
    org.meowcat.mesagisto.bungeecord.Plugin.enable()
    proxy.pluginManager.registerListener(this@BungeecordAdapter, PluginListener)
  }

  override fun onDisable() = runBlocking {
    // Plugin shutdown logic
  }
}
