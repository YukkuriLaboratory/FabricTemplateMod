package net.yukulab.template

import net.fabricmc.fabric.api.gametest.v1.CustomTestMethodInvoker
import net.fabricmc.fabric.api.gametest.v1.GameTest
import net.minecraft.gametest.framework.GameTestHelper
import net.minecraft.world.level.block.Blocks
import java.lang.reflect.Method

class YukulabTemplateGameTest : CustomTestMethodInvoker {
    @GameTest
    fun testAirBlockPresent(context: GameTestHelper) {
        context.assertBlockPresent(Blocks.AIR, 0, 0, 0)
        context.succeed()
    }

    override fun invokeTestMethod(context: GameTestHelper, method: Method) {
        method.invoke(this, context)
    }
}
