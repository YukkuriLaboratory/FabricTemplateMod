package net.yukulab.template

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.Toml4jConfigSerializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents
import net.yukulab.template.network.PayloadRegistry
import org.slf4j.LoggerFactory

object YukulabTemplate : ModInitializer {
    private val logger = LoggerFactory.getLogger(MOD_ID)
    private val job = SupervisorJob()
    val modDispatcher = Dispatchers.Default + job
    lateinit var serverDispatcher: CoroutineDispatcher
      private set

    override fun onInitialize() {
        AutoConfig.register(ModConfig::class.java, ::Toml4jConfigSerializer)
        PayloadRegistry.registerAll()
        initCoroutines()
        logger.info("Hello Fabric world!")
    }

    private fun initCoroutines() {
        ServerLifecycleEvents.SERVER_STARTED.register { server ->
            serverDispatcher = server.asCoroutineDispatcher()
        }
        ServerLifecycleEvents.SERVER_STOPPED.register {
            runBlocking {
                job.cancelAndJoin()
            }
        }
    }
}