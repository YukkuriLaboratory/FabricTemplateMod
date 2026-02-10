@file:Suppress("UnstableApiUsage")

package net.yukulab.template

import net.fabricmc.fabric.api.client.gametest.v1.FabricClientGameTest
import net.fabricmc.fabric.api.client.gametest.v1.context.ClientGameTestContext

class YukulabTemplateClientGameTest : FabricClientGameTest {
    override fun runTest(context: ClientGameTestContext) {
        context.worldBuilder().create().use { singleplayer ->
            singleplayer.clientWorld.waitForChunksRender()
            context.takeScreenshot("yukulabtemplate-client-test")
        }
    }
}
