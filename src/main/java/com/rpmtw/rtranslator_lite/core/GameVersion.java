package com.rpmtw.rtranslator_lite.core;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public final class GameVersion {
    private final int major;
    private final int minor;
    private final int patch;

    public GameVersion(int major, int minor, int patch) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getPatch() {
        return patch;
    }

    @Override
    public String toString() {
        return "GameVersion{" + "major=" + major + ", minor=" + minor + ", patch=" + patch + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameVersion that = (GameVersion) o;
        return major == that.major && minor == that.minor && patch == that.patch;
    }

    @Override
    public int hashCode() {
        return Objects.hash(major, minor, patch);
    }

    public boolean isOlderThan(GameVersion version) {
        if (major < version.getMajor()) {
            return true;
        } else if (major == version.getMajor()) {
            if (minor < version.getMinor()) {
                return true;
            } else if (minor == version.getMinor()) {
                return patch < version.getPatch();
            }
        }
        return false;
    }

    @Nullable
    public static GameVersion parse(String version) {
        String[] parts = version.split("\\.");
        if (parts.length < 2) {
            return null;
        }

        try {
            int major = Integer.parseInt(parts[0]);
            int minor = Integer.parseInt(parts[1]);
            int patch = Integer.parseInt(parts.length == 2 ? "0" : parts[2]);
            return new GameVersion(major, minor, patch);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
