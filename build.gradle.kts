plugins {
    id("java")
}

group = "com.rpmtw"
version = "0.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    mavenCentral()
    maven("https://maven.fabricmc.net")
    maven("https://files.minecraftforge.net/maven")
    maven("https://libraries.minecraft.net")
}

dependencies {
    compileOnly("org.jetbrains:annotations:24.0.1")
    implementation("org.apache.logging.log4j:log4j-api:2.22.1")
    implementation("org.apache.logging.log4j:log4j-core:2.22.1")
    implementation("com.google.code.gson:gson:2.10.1")

    implementation("net.fabricmc:fabric-loader:0.15.6") // Fabric Loader
    implementation("cpw.mods:modlauncher:8.1.3") // Forge 1.13.2+
    implementation("net.minecraft:launchwrapper:1.12") // FML/LiteLoader 1.6 ~ 1.12.2
}

tasks {
    processResources {
        filesMatching("fabric.mod.json") {
            expand(
                    "version" to project.version,
            )
        }

        filesMatching("META-INF/mods.toml") {
            expand(
                    "version" to project.version,
            )
        }
    }

    jar {
        manifest {
            attributes("TweakClass" to "com.rpmtw.rtranslator_lite.loaders.LegacyMinecraftMod")
        }
    }
}
