package net.yukulab.template.network.payload

import net.minecraft.network.PacketByteBuf
import net.yukulab.template.id

data class ExampleC2SPayload(val action: String) {
    fun write(buf: PacketByteBuf) {
        buf.writeString(action)
    }

    companion object {
        val ID = id("example_c2s")

        fun read(buf: PacketByteBuf): ExampleC2SPayload {
            val action = buf.readString()
            return ExampleC2SPayload(action)
        }
    }
}
