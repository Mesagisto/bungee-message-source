package org.meowcat.mesagisto.bungeecord

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object Plugin : CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = EmptyCoroutineContext
  suspend fun enable() {

  }

}
