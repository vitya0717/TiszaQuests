package org.vitya0717.tiszaQuests.configuration.player;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.UUID;

public interface IPlayerConfiguration {

    void saveDefaultConfig(UUID playerUuid);
    void saveConfig();
    void loadConfig();
    FileConfiguration getConfig();

    boolean changed();
}
