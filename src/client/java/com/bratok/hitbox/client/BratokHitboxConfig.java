package com.bratok.hitbox.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public class BratokHitboxConfig {
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("bratok_hitbox.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static BratokHitboxConfig INSTANCE = new BratokHitboxConfig();

    public HitboxMode currentMode = HitboxMode.DEFAULT;

    public int globalColor = 0xFFFFFF;
    public int globalAlpha = 255;
    public int globalLineWidth = 2;
    public boolean showEyeLine = true;
    public boolean showLookLine = true;

    public boolean showPlayers = true;
    public boolean showMobs = true;
    public boolean showOthers = true;

    public boolean enableDistanceFading = false;
    public int fadeMaxDistance = 30;

    public int fullColor = 0xFFFFFF;
    public int fullAlpha = 100;

    public int damageColor = 0xFF0000;
    public int damageAlpha = 200;
    public boolean damageShowDefault = true;

    public int sightColor = 0x00FF00;
    public int sightAlpha = 200;
    public boolean sightShowDefault = true;

    public static void load() {
        BratokHitboxConfig defaults = new BratokHitboxConfig();
        if (Files.exists(CONFIG_PATH)) {
            try (Reader reader = Files.newBufferedReader(CONFIG_PATH)) {

                com.google.gson.JsonObject json = com.google.gson.JsonParser.parseReader(reader).getAsJsonObject();
                BratokHitboxConfig config = GSON.fromJson(json, BratokHitboxConfig.class);

                if (!json.has("showPlayers")) config.showPlayers = defaults.showPlayers;
                if (!json.has("showMobs")) config.showMobs = defaults.showMobs;
                if (!json.has("showOthers")) config.showOthers = defaults.showOthers;
                if (!json.has("enableDistanceFading")) config.enableDistanceFading = defaults.enableDistanceFading;
                if (!json.has("fadeMaxDistance")) config.fadeMaxDistance = defaults.fadeMaxDistance;
                if (!json.has("damageShowDefault")) config.damageShowDefault = defaults.damageShowDefault;
                if (!json.has("sightShowDefault")) config.sightShowDefault = defaults.sightShowDefault;

                if (config != null) {
                    INSTANCE = config;
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        INSTANCE = defaults;
        save();
    }

    public static void save() {
        try {
            Files.createDirectories(CONFIG_PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(CONFIG_PATH)) {
                GSON.toJson(INSTANCE, writer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
