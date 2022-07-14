package org.mesagisto.mcproxy.platforms

import kotlinx.coroutines.* // ktlint-disable no-wildcard-imports
import org.mesagisto.mcproxy.Plugin

typealias BungeePlugin = net.md_5.bungee.api.plugin.Plugin

class BungeeAdapter : BungeePlugin(), CoroutineScope {
  override val coroutineContext = CoroutineScope(Dispatchers.Default).coroutineContext

  private val inner = Plugin
  override fun onLoad() {
    launch {
      coroutineContext.ensureActive()
      inner.coroutineContext.ensureActive()
      inner.onLoad(this@BungeeAdapter)
    }
  }
  override fun onEnable() {
    launch {
      inner.onEnable()
    }
  }

  override fun onDisable() {
    runBlocking {
      inner.onDisable()
    }
    runCatching {
      inner.coroutineContext.cancel()
      coroutineContext.cancel()
    }
  }
}
