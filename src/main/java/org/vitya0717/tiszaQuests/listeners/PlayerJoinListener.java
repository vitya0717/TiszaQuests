package org.vitya0717.tiszaQuests.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.vitya0717.tiszaQuests.configuration.player.PlayerConfig;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quest.playerProfile.PlayerProfileManager;
import org.vitya0717.tiszaQuests.quest.playerProfile.QuestPlayerProfile;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerProfileManager manager = Main.profileManager;

        PlayerConfig config = Main.playerConfigManager.findPlayerConfig(player.getUniqueId());

        if(config == null || !manager.allLoadedProfile.containsKey(player.getUniqueId())) {

            QuestPlayerProfile profile = new QuestPlayerProfile(player.getUniqueId());
            manager.registerPlayerProfile(profile);

            config = new PlayerConfig(player.getUniqueId());
            config.saveDefaultConfig(player.getUniqueId());
            config.getConfig().set(player.getUniqueId().toString(),profile.serialize());
            config.saveConfig();
            return;
        }
        config.loadConfig();
    }
}
