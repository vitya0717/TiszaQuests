package org.vitya0717.tiszaQuests.configuration.player;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.UUID;

public interface IPlayerConfiguration {

    void saveDefaultConfig(UUID playerUuid);
    void saveConfig();
    void loadConfig();
    FileConfiguration getConfig();

    boolean changed();

    void setFile(File file);

    void setYaml(FileConfiguration yaml);

    void set(String path, Object value);
}
