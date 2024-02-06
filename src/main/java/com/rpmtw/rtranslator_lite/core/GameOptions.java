package com.rpmtw.rtranslator_lite.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class GameOptions {
    private final Map<String, String> options = new LinkedHashMap<>();

    public void set(String key, String value) {
        options.put(key, value);
    }

    public String get(String key) {
        return options.get(key);
    }

    public void save(Path path) throws IOException {
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : options.entrySet()) {
            builder.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }

        Files.write(path, builder.toString().getBytes());
    }

    public static GameOptions parse(Path path) throws IOException {
        Scanner scanner = new Scanner(path);
        GameOptions gameOptions = new GameOptions();

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            if (line.startsWith("#")) {
                continue;
            }

            String[] parts = line.split(":", 2);
            if (parts.length == 2) {
                gameOptions.set(parts[0], parts[1]);
            }
        }

        scanner.close();
        return gameOptions;
    }
}
