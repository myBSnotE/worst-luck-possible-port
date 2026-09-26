package com.worstluckpossible.network;

import com.worstluckpossible.WorstLuckPossible;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ConfigSyncPayload(String json, boolean editable) implements CustomPayload {
	public static final Id<ConfigSyncPayload> ID =
			new Id<>(Identifier.of(WorstLuckPossible.MOD_ID, "config_sync"));
	public static final PacketCodec<RegistryByteBuf, ConfigSyncPayload> CODEC = PacketCodec.tuple(
			PacketCodecs.string(32_768), ConfigSyncPayload::json,
			PacketCodecs.BOOLEAN, ConfigSyncPayload::editable,
			ConfigSyncPayload::new);
	@Override public Id<? extends CustomPayload> getId() { return ID; }
}
