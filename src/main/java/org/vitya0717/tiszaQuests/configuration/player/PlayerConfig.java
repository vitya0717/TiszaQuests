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
    private final String confPath;

    public PlayerConfig(UUID uuid) {
        this.playerUuid = uuid;
        this.confPath = playerUuid.toString()+".";
    }

    @Override
    public void saveDefaultConfig(UUID playerUuid) {
        try {

           setFile(new File(userDataPath+"userdata", playerUuid.toString()+".yml"));

            if(!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();

                loadConfig();

                fileConfiguration.set(confPath + "QuestPoints", 0);
                fileConfiguration.set(confPath + "Quests", "");


                return;
            }
            loadConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveConfig() {
        try {
            fileConfiguration.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Cannot save the player configuration!");
        }
    }

    @Override
    public void loadConfig() {
        setYaml(new YamlConfiguration());
        try {
            fileConfiguration.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getLogger().severe("Cannot load the player configuration!");
        }
    }

    @Override
    public FileConfiguration getConfig() {
        return fileConfiguration;
    }

    @Override
    public boolean changed() {
        return isChanged;
    }

    @Override
    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void setYaml(FileConfiguration yaml) {
        this.fileConfiguration = yaml;
    }

    @Override
    public void set(String path, Object value) {
        this.getConfig().set(path, value);
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

    @Override
    public String toString() {
        return "PlayerConfig{" +
                "file=" + file +
                ", fileConfiguration=" + fileConfiguration +
                ", userDataPath='" + userDataPath + '\'' +
                ", playerUuid=" + playerUuid +
                ", isChanged=" + isChanged +
                ", confPath='" + confPath + '\'' +
                '}';
    }
}
