package net.yukulab.template

import net.fabricmc.api.ClientModInitializer
import net.yukulab.template.network.ClientPayloadHandler

object YukulabTemplateClient : ClientModInitializer {
    override fun onInitializeClient() {
        ClientPayloadHandler.registerAll()
    }
}
