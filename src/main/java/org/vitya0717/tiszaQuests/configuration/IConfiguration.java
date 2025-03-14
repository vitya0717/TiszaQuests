package org.vitya0717.tiszaQuests.configuration;

import org.bukkit.configuration.file.FileConfiguration;

public interface IConfiguration {

    void saveDefaultConfig();
    void saveConfig();
    void loadConfig();
    FileConfiguration getConfig();

}
