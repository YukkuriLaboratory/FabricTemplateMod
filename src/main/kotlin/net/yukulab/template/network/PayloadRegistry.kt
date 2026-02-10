package net.yukulab.template.network

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.yukulab.template.network.payload.ExampleC2SPayload
import net.yukulab.template.network.payload.ExampleS2CPayload
import org.slf4j.LoggerFactory

object PayloadRegistry {
    private val logger = LoggerFactory.getLogger(PayloadRegistry::class.java)

    fun registerAll() {
        // S2C payload types
        PayloadTypeRegistry.playS2C().register(ExampleS2CPayload.TYPE, ExampleS2CPayload.STREAM_CODEC)
        // C2S payload types
        PayloadTypeRegistry.playC2S().register(ExampleC2SPayload.TYPE, ExampleC2SPayload.STREAM_CODEC)
        // Server-side C2S handlers
        registerServerHandlers()
        logger.info("Registered network payloads")
    }

    private fun registerServerHandlers() {
        ServerPlayNetworking.registerGlobalReceiver(ExampleC2SPayload.TYPE) { payload, context ->
            logger.info("Received C2S action '{}' from {}", payload.action, context.player().name.string)
        }
    }
}
