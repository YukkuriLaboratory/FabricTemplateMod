package net.yukulab.template

import net.fabricmc.fabric.api.gametest.v1.FabricGameTest
import net.minecraft.block.Blocks
import net.minecraft.test.GameTest
import net.minecraft.test.TestContext
import java.lang.reflect.Method

class YukulabTemplateGameTest : FabricGameTest {
    @GameTest(templateName = FabricGameTest.EMPTY_STRUCTURE)
    fun testAirBlockPresent(context: TestContext) {
        context.expectBlock(Blocks.AIR, 0, 1, 0)
        context.complete()
    }

    override fun invokeTestMethod(context: TestContext, method: Method) {
        method.invoke(this, context)
    }
}
