package net.yukulab.template

import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer
import net.fabricmc.api.ModInitializer
import net.yukulab.template.network.PayloadRegistry
import org.slf4j.LoggerFactory

object YukulabTemplate : ModInitializer {
    const val MOD_ID = "yukulabtemplate"
    private val logger = LoggerFactory.getLogger(MOD_ID)

    override fun onInitialize() {
        AutoConfig.register(ModConfig::class.java, ::Toml4jConfigSerializer)
        PayloadRegistry.registerAll()
        logger.info("Hello Fabric world!")
    }
}