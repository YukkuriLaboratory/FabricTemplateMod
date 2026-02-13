package net.yukulab.template.network.payload

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import net.yukulab.template.MOD_ID
import net.yukulab.template.id

data class ExampleS2CPayload(
    val message: String,
    val value: Int,
) : CustomPacketPayload {
    override fun type() = TYPE

    companion object {
        val TYPE: CustomPacketPayload.Type<ExampleS2CPayload> = CustomPacketPayload.Type(
            id("example_s2c"),
        )
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, ExampleS2CPayload> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ExampleS2CPayload::message,
            ByteBufCodecs.VAR_INT, ExampleS2CPayload::value,
            ::ExampleS2CPayload,
        )
    }
}
