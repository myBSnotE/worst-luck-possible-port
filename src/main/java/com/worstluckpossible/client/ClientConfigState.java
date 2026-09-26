package com.worstluckpossible.client;

import com.worstluckpossible.config.WorstLuckConfig;
import com.worstluckpossible.config.WorstLuckConfigManager;
import com.worstluckpossible.network.ConfigRequestPayload;
import com.worstluckpossible.network.ConfigSyncPayload;
import com.worstluckpossible.network.ConfigUpdatePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public final class ClientConfigState {
	private static WorstLuckConfig worldConfig = new WorstLuckConfig();
	private static boolean editable;

	private ClientConfigState() {}

	public static void registerReceiver() {
		ClientPlayNetworking.registerGlobalReceiver(ConfigSyncPayload.ID, (payload, context) -> {
			worldConfig = WorstLuckConfigManager.fromJson(payload.json());
			editable = payload.editable();
		});
	}

	public static void open(Screen parent) {
		MinecraftClient client = MinecraftClient.getInstance();
		client.setScreen(createScreen(parent));
	}

	public static Screen createScreen(Screen parent) {
		MinecraftClient client = MinecraftClient.getInstance();
		boolean inWorld = client.getNetworkHandler() != null && client.world != null;
		if (inWorld) ClientPlayNetworking.send(ConfigRequestPayload.INSTANCE);
		WorstLuckConfig config = inWorld ? worldConfig.copy() : WorstLuckConfigManager.defaults().copy();
		return new WorstLuckConfigScreen(parent, config, inWorld, !inWorld || editable);
	}

	public static void save(WorstLuckConfig config, boolean world) {
		if (world) {
			ClientPlayNetworking.send(new ConfigUpdatePayload(WorstLuckConfigManager.toJson(config)));
		} else {
			WorstLuckConfigManager.saveDefaults(config);
		}
	}
}
