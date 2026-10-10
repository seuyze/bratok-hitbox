package com.bratok.hitbox.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class BratokHitboxClient implements ClientModInitializer {
	private static KeyMapping menuKeyBinding;

	@Override
	public void onInitializeClient() {
		BratokHitboxConfig.load();

		menuKeyBinding = KeyBindingHelper.registerKeyBinding(new KeyMapping(
				"key.bratok_hitbox.open_menu", 
				GLFW.GLFW_KEY_H, 
				"category.bratok_hitbox.menu"
		));

		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (menuKeyBinding.consumeClick()) {
				if (client.screen == null) {
					client.setScreen(new BratokHitboxMenuScreen());
				}
			}
		});
	}
}