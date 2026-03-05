package net.yukulab.template.network

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.yukulab.template.network.payload.ExampleC2SPayload
import org.slf4j.LoggerFactory

object PayloadRegistry {
    private val logger = LoggerFactory.getLogger(PayloadRegistry::class.java)

    fun registerAll() {
        registerServerHandlers()
        logger.info("Registered network packets")
    }

    private fun registerServerHandlers() {
        ServerPlayNetworking.registerGlobalReceiver(ExampleC2SPayload.ID) { server, player, handler, buf, responseSender ->
            val packet = ExampleC2SPayload.read(buf)
            logger.info("Received C2S action '{}' from {}", packet.action, player.name.string)
        }
    }
}
