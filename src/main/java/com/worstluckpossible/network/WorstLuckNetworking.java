package com.worstluckpossible.network;

import com.worstluckpossible.config.WorstLuckConfig;
import com.worstluckpossible.config.WorstLuckConfigManager;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

public final class WorstLuckNetworking {
	private WorstLuckNetworking() {}

	public static void registerPayloads() {
		PayloadTypeRegistry.playC2S().register(ConfigRequestPayload.ID, ConfigRequestPayload.CODEC);
		PayloadTypeRegistry.playC2S().register(ConfigUpdatePayload.ID, ConfigUpdatePayload.CODEC);
		PayloadTypeRegistry.playS2C().register(ConfigSyncPayload.ID, ConfigSyncPayload.CODEC);
	}

	public static void registerServerHandlers() {
		ServerPlayNetworking.registerGlobalReceiver(ConfigRequestPayload.ID,
				(payload, context) -> send(context.player()));
		ServerPlayNetworking.registerGlobalReceiver(ConfigUpdatePayload.ID, (payload, context) -> {
			ServerPlayerEntity player = context.player();
			if (!WorstLuckConfigManager.canEdit(player)) {
				send(player);
				return;
			}
			WorstLuckConfig updated = WorstLuckConfigManager.fromJson(payload.json());
			WorstLuckConfigManager.saveWorld(context.server(), updated);
			broadcast(context.server());
		});
		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> send(handler.player));
	}

	public static void send(ServerPlayerEntity player) {
		WorstLuckConfig config = WorstLuckConfigManager.get(player.getEntityWorld().getServer());
		ServerPlayNetworking.send(player, new ConfigSyncPayload(
				WorstLuckConfigManager.toJson(config), WorstLuckConfigManager.canEdit(player)));
	}

	public static void broadcast(MinecraftServer server) {
		for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) send(player);
	}
}
