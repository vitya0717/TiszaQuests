package org.vitya0717.tiszaQuests.configuration.player;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.vitya0717.tiszaQuests.configuration.exceptions.PlayerConfigurationExistsException;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Logger;

public class PlayerConfig implements IPlayerConfiguration{

    private File file;
    private FileConfiguration fileConfiguration;
    private final String userDataPath = Main.instance.getDataFolder()+"/";
    private final UUID playerUuid;
    private final Logger logger = Bukkit.getLogger();
    private boolean isChanged = false;

    public PlayerConfig(UUID uuid) {
        this.playerUuid = uuid;
    }

    @Override
    public void saveDefaultConfig(UUID playerUuid) {
        try {
            Main.playerConfigManager.registerPlayer(playerUuid);

            file = new File(userDataPath+"userdata", playerUuid.toString()+".yml");

            if(!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            loadConfig();

        } catch (PlayerConfigurationExistsException e) {
            logger.severe(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveConfig() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            System.out.println(Utils.Colorize("Cannot save the player configuration!"));
        }
    }

    @Override
    public void loadConfig() {
        fileConfiguration = new YamlConfiguration();
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            System.out.println(Utils.Colorize("Cannot load the player configuration!"));
        }
    }

    @Override
    public FileConfiguration getConfig() {
        return fileConfiguration;
    }

    @Override
    public boolean changed() {
        return false;
    }

    public UUID getPlayerUuid() {
        return playerUuid;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }
}
