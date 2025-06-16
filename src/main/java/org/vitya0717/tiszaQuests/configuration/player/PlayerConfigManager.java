package org.vitya0717.tiszaQuests.configuration.player;

import org.vitya0717.tiszaQuests.configuration.exceptions.PlayerConfigurationExistsException;
import org.vitya0717.tiszaQuests.main.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerConfigManager {

    public static List<PlayerConfig> playerConfigList = new ArrayList<>();
    private final String userDataPath = Main.instance.getDataFolder()+"/";

    public PlayerConfig registerPlayerConfiguration(UUID uuid) throws PlayerConfigurationExistsException {
        if(findPlayerConfig(uuid) != null) {
            throw new PlayerConfigurationExistsException("Player configuration already exists!", uuid.toString());
        }
        PlayerConfig temp = new PlayerConfig(uuid);
        temp.saveDefaultConfig(uuid);
        playerConfigList.add(temp);
        return temp;
    }

    public PlayerConfig findPlayerConfig(UUID playerUuid) {
        for (PlayerConfig config : playerConfigList){
            if(config.getPlayerUuid().compareTo(playerUuid) == 0) {
                return config;
            }
        }
        return null;
    }

    public void unregisterPlayerConfig(UUID uniqueId) {
        PlayerConfig config = findPlayerConfig(uniqueId);
        config.setFile(null);
        config.setYaml(null);
        playerConfigList.remove(config);
    }
}
