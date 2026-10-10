package com.bratok.hitbox.client;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BratokHitboxMenuScreen extends Screen {

    private boolean resetConfirmMode = false;

    public BratokHitboxMenuScreen() {
        super(Component.translatable("bratok_hitbox.menu.title"));
    }

    @Override
    protected void init() {
        super.init();

        int buttonWidth = 200;
        int buttonHeight = 20;
        int startY = this.height / 4 + 24;
        int spacing = 24;

        this.addRenderableWidget(Button.builder(Component.translatable("bratok_hitbox.menu.mode", BratokHitboxConfig.INSTANCE.currentMode.getDisplayName()), (btn) -> {
            BratokHitboxConfig.INSTANCE.currentMode = BratokHitboxConfig.INSTANCE.currentMode.next();
            BratokHitboxConfig.save();
            btn.setMessage(Component.translatable("bratok_hitbox.menu.mode", BratokHitboxConfig.INSTANCE.currentMode.getDisplayName()));
        }).bounds(this.width / 2 - buttonWidth / 2, startY, buttonWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(Component.translatable("bratok_hitbox.menu.settings"), (btn) -> {
            this.minecraft.setScreen(BratokHitboxConfigScreenBuilder.build(this));
        }).bounds(this.width / 2 - buttonWidth / 2, startY + spacing, buttonWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(Component.translatable("bratok_hitbox.menu.done"), (btn) -> {
            this.onClose();
        }).bounds(this.width / 2 - buttonWidth / 2, startY + spacing * 2 + 12, buttonWidth, buttonHeight).build());

        this.addRenderableWidget(Button.builder(Component.translatable("bratok_hitbox.menu.reset"), (btn) -> {
            if (!resetConfirmMode) {
                resetConfirmMode = true;
                btn.setMessage(Component.translatable("bratok_hitbox.menu.reset_confirm"));
            } else {
                BratokHitboxConfig.INSTANCE = new BratokHitboxConfig();
                BratokHitboxConfig.save();
                resetConfirmMode = false;
                btn.setMessage(Component.translatable("bratok_hitbox.menu.reset_done"));
                btn.active = false;
                this.minecraft.setScreen(new BratokHitboxMenuScreen());
            }
        }).bounds(10, this.height - 30, 100, 20).build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        guiGraphics.drawCenteredString(this.font, this.title, this.width / 2, this.height / 4 - 20, 0xFFFFFF);
    }
}