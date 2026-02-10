package net.yukulab.template.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.yukulab.template.network.payload.ExampleS2CPayload
import org.slf4j.LoggerFactory

object ClientPayloadHandler {
    private val logger = LoggerFactory.getLogger(ClientPayloadHandler::class.java)

    fun registerAll() {
        ClientPlayNetworking.registerGlobalReceiver(ExampleS2CPayload.TYPE) { payload, context ->
            logger.info("Received S2C message: '{}' with value {}", payload.message, payload.value)
        }
    }
}
