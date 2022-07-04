plugins {
    java
    `java-library`
    id("net.minecrell.plugin-yml.bukkit") version "0.5.0"
    id("xyz.jpenilla.run-paper") version "1.0.6"
    id("com.github.johnrengelman.shadow") version "7.0.0"
}

group = "org.gepron1x"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://papermc.io/repo/repository/maven-public/") }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.17.1-R0.1-SNAPSHOT")
    implementation("org.takes:takes:1.20")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

var libraryPackage = "org.gepron1x.resourcepacks.libraries"

tasks {
    shadowJar {
        relocate("org.takes", "$libraryPackage.takes")

    }

    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion("1.18.2")
        jvmArgs("-Xms128M", "-Xmx1024M")
    }

    compileJava {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release.set(8)
        options.compilerArgs.add("-parameters")
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
}

bukkit {
    name = "PacksServer"
    main = "org.gepron1x.resourcepacks.PacksServerPlugin"
    description = "A file server for your resource packs"
    apiVersion = "1.13"
    authors = listOf("gepron1x")
}
