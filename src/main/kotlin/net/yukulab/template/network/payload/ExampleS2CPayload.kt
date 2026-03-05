package net.yukulab.template.network.payload

import net.minecraft.network.PacketByteBuf
import net.yukulab.template.id

data class ExampleS2CPayload(val message: String, val value: Int) {
    fun write(buf: PacketByteBuf) {
        buf.writeString(message)
        buf.writeVarInt(value)
    }

    companion object {
        val ID = id("example_s2c")

        fun read(buf: PacketByteBuf): ExampleS2CPayload {
            val message = buf.readString()
            val value = buf.readVarInt()
            return ExampleS2CPayload(message, value)
        }
    }
}
