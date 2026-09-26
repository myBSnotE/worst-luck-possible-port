package com.worstluckpossible.network;

import com.worstluckpossible.WorstLuckPossible;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ConfigUpdatePayload(String json) implements CustomPayload {
	public static final Id<ConfigUpdatePayload> ID =
			new Id<>(Identifier.of(WorstLuckPossible.MOD_ID, "config_update"));
	public static final PacketCodec<RegistryByteBuf, ConfigUpdatePayload> CODEC = PacketCodec.tuple(
			PacketCodecs.string(32_768), ConfigUpdatePayload::json, ConfigUpdatePayload::new);
	@Override public Id<? extends CustomPayload> getId() { return ID; }
}
