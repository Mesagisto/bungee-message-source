import io.itsusinn.pkg.pkgIn

plugins {
  java
  kotlin("jvm") version "1.6.0"
  kotlin("plugin.serialization") version "1.6.0"
  id("com.github.johnrengelman.shadow") version "7.1.1"
  id("io.itsusinn.pkg") version "1.2.0"
}

group = "org.meowcat"
version = "1.0.0-rc"

repositories {
  mavenCentral()
  mavenLocal()

  maven("https://oss.sonatype.org/content/repositories/snapshots/")
  maven("https://oss.sonatype.org/content/groups/public/")
  maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
  maven("https://jitpack.io")
}
pkg {
  excludePath("META-INF/*.kotlin_module")
  excludePath("*.md")
  excludePath("DebugProbesKt.bin")
  excludePathStartWith("META-INF/maven")
  shadowJar {
    minimize()
    mergeServiceFiles()
  }
  relocateKotlinStdlib()
  relocateKotlinxLib()
}
dependencies {
  compileOnly("net.md-5:bungeecord-api:1.18-R0.1-SNAPSHOT")
  compileOnly("org.jetbrains.kotlin:kotlin-stdlib")
  pkgIn("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
  pkgIn("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.2.2")
  pkgIn("io.nats:jnats:2.14.0")
  pkgIn("org.meowcat:mesagisto-client-jvm:1.3.0")
}
java {
  targetCompatibility = JavaVersion.VERSION_1_8
  sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
  processResources {
    inputs.property("version", project.version)
    filesMatching("bungee.yml") {
      expand(mutableMapOf("version" to project.version))
    }
  }
  compileKotlin {
    kotlinOptions {
      jvmTarget = "1.8"
      freeCompilerArgs = listOf("-Xinline-classes", "-Xopt-in=kotlin.RequiresOptIn")
    }
    sourceCompatibility = "1.8"
  }
}
