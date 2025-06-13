package org.vitya0717.tiszaQuests.configuration.player;

import org.vitya0717.tiszaQuests.configuration.exceptions.PlayerConfigurationExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerConfigManager {

    public static List<PlayerConfig> playerConfigList = new ArrayList<>();

    public void registerPlayer(UUID uuid) throws PlayerConfigurationExistsException {
        if(findPlayerConfig(uuid) != null) {
            throw new PlayerConfigurationExistsException("Player configuration already exists!", uuid.toString());
        }
    }

    public PlayerConfig findPlayerConfig(UUID playerUuid) {
        for (PlayerConfig config : playerConfigList){
            if(config.getPlayerUuid().compareTo(playerUuid) == 0) {
                return config;
            }
        }
        return null;
    }

}
