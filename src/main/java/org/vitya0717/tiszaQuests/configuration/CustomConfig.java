package org.vitya0717.tiszaQuests.configuration;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.utils.Utils;

import java.io.File;
import java.io.IOException;

public class CustomConfig implements IConfiguration{

    private File file;
    private FileConfiguration fileConfiguration;

    @Override
    public FileConfiguration getConfig() {
        return fileConfiguration;
    }

    @Override
    public void saveDefaultConfig() {
        file = new File(Main.instance.getDataFolder(), "quests.yml");

        if(!file.exists()) {
            file.getParentFile().mkdirs();
            Main.instance.saveResource("quests.yml", false);
        }
        loadConfig();
    }

    @Override
    public void saveConfig() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            System.out.println(Utils.Colorize("&8[&fTalpra, Fiatalok&8] » &cCannot save the configuration!"));
        }
    }

    @Override
    public void loadConfig() {
        fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            System.out.println(Utils.Colorize("&8[&fTalpra, Fiatalok&8] » &cCannot load the configuration!"));
        }
    }
}
