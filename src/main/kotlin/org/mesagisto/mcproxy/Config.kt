package org.mesagisto.mcproxy

import com.fasterxml.jackson.annotation.JsonAlias
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

data class RootConfig(
  val enable: Boolean = false,
  val bindings: MutableMap<String, String> = HashMap(),
  val cipher: CipherConfig = CipherConfig(),
  @JsonAlias("id_counter")
  val idCounter: MutableMap<String, AtomicInteger> = ConcurrentHashMap(),
  val nats: String = "nats://nats.mesagisto.org:4222",
  val template: TemplateConfig = TemplateConfig()
)

data class CipherConfig(
  val key: String = "your-key"
)

data class TemplateConfig(
  val message: String = "§7<{{sender}}> {{content}}"
)
