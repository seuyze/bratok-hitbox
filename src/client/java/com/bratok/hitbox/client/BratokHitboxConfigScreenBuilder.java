package com.bratok.hitbox.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class BratokHitboxConfigScreenBuilder {
    public static Screen build(Screen parent) {
        BratokHitboxConfig config = BratokHitboxConfig.INSTANCE;
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("bratok_hitbox.config.title"))
                .setSavingRunnable(BratokHitboxConfig::save);

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory catGlobal = builder.getOrCreateCategory(Component.translatable("bratok_hitbox.config.category.global"));

        catGlobal.addEntry(entryBuilder.startTextDescription(Component.translatable("bratok_hitbox.config.sub.appearance")).build());
        catGlobal.addEntry(entryBuilder.startColorField(Component.translatable("bratok_hitbox.config.global.color"), config.globalColor)
                .setDefaultValue(0xFFFFFF)
                .setSaveConsumer(val -> config.globalColor = val)
                .build());
        catGlobal.addEntry(entryBuilder.startIntSlider(Component.translatable("bratok_hitbox.config.global.alpha"), config.globalAlpha, 0, 255)
                .setDefaultValue(255)
                .setSaveConsumer(val -> config.globalAlpha = val)
                .build());
        catGlobal.addEntry(entryBuilder.startIntSlider(Component.translatable("bratok_hitbox.config.global.line_width"), config.globalLineWidth, 1, 10)
                .setDefaultValue(2)
                .setSaveConsumer(val -> config.globalLineWidth = val)
                .build());

        catGlobal.addEntry(entryBuilder.startTextDescription(Component.translatable("bratok_hitbox.config.sub.lines")).build());
        catGlobal.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bratok_hitbox.config.global.eye_line"), config.showEyeLine)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.showEyeLine = val)
                .build());
        catGlobal.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bratok_hitbox.config.global.look_line"), config.showLookLine)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.showLookLine = val)
                .build());

        catGlobal.addEntry(entryBuilder.startTextDescription(Component.translatable("bratok_hitbox.config.sub.filters")).build());
        catGlobal.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bratok_hitbox.config.filter.players"), config.showPlayers)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.showPlayers = val)
                .build());
        catGlobal.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bratok_hitbox.config.filter.mobs"), config.showMobs)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.showMobs = val)
                .build());
        catGlobal.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bratok_hitbox.config.filter.others"), config.showOthers)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.showOthers = val)
                .build());

        catGlobal.addEntry(entryBuilder.startTextDescription(Component.translatable("bratok_hitbox.config.sub.fading")).build());
        catGlobal.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bratok_hitbox.config.fading.enable"), config.enableDistanceFading)
                .setDefaultValue(false)
                .setSaveConsumer(val -> config.enableDistanceFading = val)
                .build());
        catGlobal.addEntry(entryBuilder.startIntSlider(Component.translatable("bratok_hitbox.config.fading.max_distance"), config.fadeMaxDistance, 5, 100)
                .setDefaultValue(30)
                .setSaveConsumer(val -> config.fadeMaxDistance = val)
                .build());

        ConfigCategory catFull = builder.getOrCreateCategory(Component.translatable("bratok_hitbox.config.category.full"));
        catFull.addEntry(entryBuilder.startColorField(Component.translatable("bratok_hitbox.config.full.color"), config.fullColor)
                .setDefaultValue(0xFFFFFF)
                .setSaveConsumer(val -> config.fullColor = val)
                .build());
        catFull.addEntry(entryBuilder.startIntSlider(Component.translatable("bratok_hitbox.config.full.alpha"), config.fullAlpha, 0, 255)
                .setDefaultValue(100)
                .setSaveConsumer(val -> config.fullAlpha = val)
                .build());

        ConfigCategory catDam = builder.getOrCreateCategory(Component.translatable("bratok_hitbox.config.category.damage"));
        catDam.addEntry(entryBuilder.startColorField(Component.translatable("bratok_hitbox.config.damage.color"), config.damageColor)
                .setDefaultValue(0xFF0000)
                .setSaveConsumer(val -> config.damageColor = val)
                .build());
        catDam.addEntry(entryBuilder.startIntSlider(Component.translatable("bratok_hitbox.config.damage.alpha"), config.damageAlpha, 0, 255)
                .setDefaultValue(200)
                .setSaveConsumer(val -> config.damageAlpha = val)
                .build());
        catDam.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bratok_hitbox.config.damage.show_default"), config.damageShowDefault)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.damageShowDefault = val)
                .build());

        ConfigCategory catSight = builder.getOrCreateCategory(Component.translatable("bratok_hitbox.config.category.sight"));
        catSight.addEntry(entryBuilder.startColorField(Component.translatable("bratok_hitbox.config.sight.color"), config.sightColor)
                .setDefaultValue(0x00FF00)
                .setSaveConsumer(val -> config.sightColor = val)
                .build());
        catSight.addEntry(entryBuilder.startIntSlider(Component.translatable("bratok_hitbox.config.sight.alpha"), config.sightAlpha, 0, 255)
                .setDefaultValue(200)
                .setSaveConsumer(val -> config.sightAlpha = val)
                .build());
        catSight.addEntry(entryBuilder.startBooleanToggle(Component.translatable("bratok_hitbox.config.sight.show_default"), config.sightShowDefault)
                .setDefaultValue(true)
                .setSaveConsumer(val -> config.sightShowDefault = val)
                .build());

        return builder.build();
    }
}