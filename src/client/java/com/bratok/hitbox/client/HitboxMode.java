package com.bratok.hitbox.client;

import net.minecraft.network.chat.Component;

public enum HitboxMode {
    DEFAULT("bratok_hitbox.mode.default"),
    FULL("bratok_hitbox.mode.full"),
    DAMAGE("bratok_hitbox.mode.damage"),
    SIGHT("bratok_hitbox.mode.sight");

    private final String translationKey;

    HitboxMode(String translationKey) {
        this.translationKey = translationKey;
    }

    public Component getDisplayName() {
        return Component.translatable(translationKey);
    }

    public HitboxMode next() {
        HitboxMode[] values = values();
        return values[(this.ordinal() + 1) % values.length];
    }
}