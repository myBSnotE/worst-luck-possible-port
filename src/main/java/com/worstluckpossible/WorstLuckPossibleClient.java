package com.worstluckpossible;

import com.worstluckpossible.client.ClientConfigState;
import com.worstluckpossible.client.WorstLuckConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class WorstLuckPossibleClient implements ClientModInitializer {
	private static final KeyBinding.Category CATEGORY =
			KeyBinding.Category.create(Identifier.of(WorstLuckPossible.MOD_ID, "settings"));
	private static boolean globalKeyWasDown;

	@Override
	public void onInitializeClient() {
		ClientConfigState.registerReceiver();
		KeyBinding openSettings = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"key.worst-luck-possible.open_settings",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_L,
				CATEGORY));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			InputUtil.Key boundKey = KeyBindingHelper.getBoundKeyOf(openSettings);
			boolean globalKeyDown = boundKey.getCategory() == InputUtil.Type.KEYSYM
					&& InputUtil.isKeyPressed(client.getWindow(), boundKey.getCode());
			boolean pressedOutsideGameplay = client.currentScreen != null
					&& !(client.currentScreen instanceof WorstLuckConfigScreen)
					&& globalKeyDown
					&& !globalKeyWasDown;
			globalKeyWasDown = globalKeyDown;
			if (pressedOutsideGameplay) {
				ClientConfigState.open(client.currentScreen);
				return;
			}
			while (client.currentScreen == null && openSettings.wasPressed()) {
				ClientConfigState.open(null);
			}
		});
	}
}
