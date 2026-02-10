package net.yukulab.template.network.payload

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.Identifier
import net.yukulab.template.YukulabTemplate

data class ExampleC2SPayload(
    val action: String,
) : CustomPacketPayload {
    override fun type() = TYPE

    companion object {
        val TYPE: CustomPacketPayload.Type<ExampleC2SPayload> = CustomPacketPayload.Type(
            Identifier.fromNamespaceAndPath(YukulabTemplate.MOD_ID, "example_c2s"),
        )
        val STREAM_CODEC: StreamCodec<FriendlyByteBuf, ExampleC2SPayload> = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, ExampleC2SPayload::action,
            ::ExampleC2SPayload,
        )
    }
}
