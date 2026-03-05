package net.yukulab.template

import net.minecraft.Bootstrap
import net.minecraft.SharedConstants
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class ExampleTest {
    companion object {
        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            SharedConstants.createGameVersion()
            Bootstrap.initialize()
        }
    }

    @Test
    fun testDiamondItemStack() {
        val diamondStack = ItemStack(Items.DIAMOND, 64)

        assertTrue(diamondStack.isOf(Items.DIAMOND))
        assertEquals(64, diamondStack.count)
    }
}
