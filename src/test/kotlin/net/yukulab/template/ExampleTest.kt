package net.yukulab.template

import net.minecraft.server.Bootstrap
import net.minecraft.SharedConstants
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class ExampleTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            SharedConstants.tryDetectVersion()
            Bootstrap.bootStrap()
        }
    }

    @Test
    fun testDiamondItemStack() {
        val diamondStack = ItemStack(Items.DIAMOND, 64)

        assertTrue(diamondStack.`is`(Items.DIAMOND))
        assertEquals(64, diamondStack.count)
    }
}
