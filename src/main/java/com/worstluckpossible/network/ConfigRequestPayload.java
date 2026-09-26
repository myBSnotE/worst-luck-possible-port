package com.worstluckpossible.network;

import com.worstluckpossible.WorstLuckPossible;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ConfigRequestPayload() implements CustomPayload {
	public static final ConfigRequestPayload INSTANCE = new ConfigRequestPayload();
	public static final Id<ConfigRequestPayload> ID =
			new Id<>(Identifier.of(WorstLuckPossible.MOD_ID, "config_request"));
	public static final PacketCodec<net.minecraft.network.RegistryByteBuf, ConfigRequestPayload> CODEC = PacketCodec.unit(INSTANCE);
	@Override public Id<? extends CustomPayload> getId() { return ID; }
}
