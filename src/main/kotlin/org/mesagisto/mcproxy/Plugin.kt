package org.mesagisto.mcproxy

import kotlinx.coroutines.CoroutineScope
import org.meowcat.mesagisto.client.Logger
import org.meowcat.mesagisto.client.MesagistoConfig
import org.meowcat.mesagisto.client.Server
import org.meowcat.mesagisto.client.utils.ConfigKeeper
import org.mesagisto.mcproxy.handlers.Receive
import org.mesagisto.mcproxy.platforms.BungeePlugin
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object Plugin : CoroutineScope {
  lateinit var bungee: BungeePlugin

  private var closed: Boolean = false
  override val coroutineContext: CoroutineContext = EmptyCoroutineContext
  private val CONFIG_KEEPER = ConfigKeeper.create(File("plugins/mesagisto/config.yml").toPath()) { RootConfig() }

  val CONFIG = CONFIG_KEEPER.value

  suspend fun onLoad(bungee: BungeePlugin) {
    this.bungee = bungee
    Logger.bridgeToStd(bungee.logger)
    CONFIG_KEEPER.save()
    Template.init()
  }
  suspend fun onEnable() {
    if (closed) {
      throw IllegalStateException("hot reload error")
    }
    if (!CONFIG.enable) {
      Logger.info { "Mesagisto信使未启用" }
      return
    }
    MesagistoConfig.builder {
      name = "bukkit"
      natsAddress = CONFIG.nats
      cipherKey = CONFIG.cipher.key
    }.apply()
    Receive.recover()
    bungee.proxy.pluginManager.registerListener(bungee, Listener)
    bungee.proxy.pluginManager.registerCommand(bungee, Command)
    Logger.info { "Mesagisto信使启用成功" }
  }
  suspend fun onDisable() {
    CONFIG_KEEPER.save()
    if (CONFIG.enable) {
      Server.close()
    }
    closed = true
  }
}
