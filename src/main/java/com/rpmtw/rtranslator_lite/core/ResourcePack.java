package com.rpmtw.rtranslator_lite.core;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class ResourcePack {
    /// Minimal version required to load the resource pack
    private final GameVersion version;

    private final String downloadUrl;

    public ResourcePack(GameVersion version, String downloadUrl) {
        this.version = version;
        this.downloadUrl = downloadUrl;
    }

    public boolean isCompatible(GameVersion version) {
        return this.version.isOlderThan(version) || Objects.equals(this.version, version);
    }

    public String getFileName() {
        return String.format("rtranslator-translation-pack-%d.%d.%d.zip", version.getMajor(), version.getMinor(), version.getPatch());
    }

    public String download(Path directory) throws IOException {
        String fileName = getFileName();
        Path tmpFile = Paths.get(System.getProperty("java.io.tmpdir")).resolve(fileName);

        URL url = new URL(downloadUrl);
        Files.copy(url.openStream(), tmpFile, StandardCopyOption.REPLACE_EXISTING);
        Files.copy(tmpFile, directory.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);

        return fileName;
    }
}
