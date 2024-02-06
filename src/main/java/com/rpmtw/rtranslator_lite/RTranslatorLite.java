package com.rpmtw.rtranslator_lite;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rpmtw.rtranslator_lite.core.GameOptions;
import com.rpmtw.rtranslator_lite.core.GameVersion;
import com.rpmtw.rtranslator_lite.core.RTLiteLogger;
import com.rpmtw.rtranslator_lite.core.ResourcePack;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.*;

public class RTranslatorLite {
    static List<ResourcePack> packs = Arrays.asList(new ResourcePack(new GameVersion(1, 19, 0), "https://github.com/RPMTW/Translate-Resource-Pack/releases/latest/download/RPMTW-Translate-Resource-Pack-1.19.zip"), new ResourcePack(new GameVersion(1, 18, 0), "https://github.com/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated-1.18/RPMTW-1.18.zip"), new ResourcePack(new GameVersion(1, 17, 0), "https://github.com/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated-1.17/RPMTW-1.17.zip"), new ResourcePack(new GameVersion(1, 13, 0), "https://github.com/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated/RPMTW-1.16.zip"), new ResourcePack(new GameVersion(1, 6, 0), "https://github.com/RPMTW/ResourcePack-Mod-zh_tw/raw/Translated-1.12/RPMTW-1.12.zip"));

    static public void init(Path gameDir, String rawVersion, boolean hasFabricApi) {
        GameVersion version = GameVersion.parse(rawVersion);
        if (version == null) {
            RTLiteLogger.error("Failed to parse game version");
            return;
        }

        for (ResourcePack pack : packs) {
            if (pack.isCompatible(version)) {
                RTLiteLogger.info("Start to download resource pack for translation");
                String fileName = downloadPack(gameDir, pack);
                if (fileName == null) return;

                RTLiteLogger.info("Start to load resource pack for translation");
                loadPack(fileName, gameDir, version, hasFabricApi);
                break;
            }
        }
    }

    @Nullable
    static private String downloadPack(Path gameDir, ResourcePack pack) {
        Path resourcePackDir = gameDir.resolve("resourcepacks");
        if (!resourcePackDir.toFile().exists()) {
            boolean success = resourcePackDir.toFile().mkdirs();
            if (!success) {
                RTLiteLogger.warn("Failed to create resource pack directory");
                return null;
            }
        }

        String fileName;
        try {
            fileName = pack.download(resourcePackDir);
        } catch (IOException e) {
            RTLiteLogger.error("Failed to download resource pack for translation", e);
            return null;
        }

        return fileName;
    }

    static private void loadPack(String fileName, Path gameDir, GameVersion version, boolean hasFabricApi) {
        try {
            Path optionsPath = gameDir.resolve("options.txt");
            if (!optionsPath.toFile().exists()) {
                boolean success = optionsPath.toFile().createNewFile();
                if (!success) {
                    RTLiteLogger.warn("Failed to create game options file");
                    return;
                }
            }

            GameOptions options = GameOptions.parse(optionsPath);
            boolean isLegacyVersion = version.isOlderThan(new GameVersion(1, 13, 0));
            String packName = isLegacyVersion ? fileName : "file/" + fileName;
            String raw = options.get("resourcePacks") == null ? "[]" : options.get("resourcePacks");

            Gson gson = new Gson();
            Type typeToken = new TypeToken<ArrayList<String>>() {
            }.getType();
            ArrayList<String> resourcePacks = gson.fromJson(raw, typeToken);

            // Add "vanilla" resource pack for 1.13+ versions.
            if (!isLegacyVersion && !resourcePacks.contains("vanilla")) {
                resourcePacks.add("vanilla");
            }

            if (hasFabricApi) {
                /// Fabric API 1.19.3+ uses "fabric" instead of "Fabric Mods" as resource pack name.
                if (version.isOlderThan(new GameVersion(1, 19, 3))) {
                    if (!resourcePacks.contains("Fabric Mods")) resourcePacks.add("Fabric Mods");
                } else {
                    if (!resourcePacks.contains("fabric")) resourcePacks.add("fabric");
                }
            }


            if (!resourcePacks.contains(packName)) {
                resourcePacks.add(packName);
                options.set("resourcePacks", gson.toJson(resourcePacks));

                if (isIncompatible(version)) {
                    ArrayList<String> incompatiblePacks = gson.fromJson(options.get("incompatibleResourcePacks") == null ? "[]" : options.get("incompatibleResourcePacks"), typeToken);
                    incompatiblePacks.add(packName);
                    options.set("incompatibleResourcePacks", gson.toJson(incompatiblePacks));
                }
            }

            if (version.isOlderThan(new GameVersion(1, 11, 0))) {
                options.set("lang", "zh_TW");
            } else {
                options.set("lang", "zh_tw");
            }

            options.save(optionsPath);
            RTLiteLogger.info("Successfully loaded resource pack for translation");
        } catch (IOException e) {
            RTLiteLogger.error("Failed to load/save game options", e);
        }
    }

    /// Source: https://minecraft.wiki/w/Pack_format#Resources
    private static boolean isIncompatible(GameVersion version) {
        List<GameVersion> incompatibleVersions = Arrays.asList(new GameVersion(1, 20, 1), new GameVersion(1, 20, 0), new GameVersion(1, 19, 3), new GameVersion(1, 19, 2), new GameVersion(1, 19, 1), new GameVersion(1, 19, 0), new GameVersion(1, 16, 1), new GameVersion(1, 16, 0));

        return incompatibleVersions.contains(version);
    }
}
