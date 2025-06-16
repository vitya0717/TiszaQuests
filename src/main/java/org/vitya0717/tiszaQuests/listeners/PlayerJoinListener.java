package org.vitya0717.tiszaQuests.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;
import org.vitya0717.tiszaQuests.configuration.exceptions.PlayerConfigurationExistsException;
import org.vitya0717.tiszaQuests.configuration.player.PlayerConfig;
import org.vitya0717.tiszaQuests.configuration.player.PlayerConfigManager;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quest.playerProfile.PlayerProfileManager;
import org.vitya0717.tiszaQuests.quest.playerProfile.QuestPlayerProfile;

import java.util.Map;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerProfileManager manager = Main.profileManager;
        PlayerConfigManager configManager = Main.playerConfigManager;

        PlayerConfig config = Main.playerConfigManager.findPlayerConfig(player.getUniqueId());

        if(!manager.allLoadedProfile.containsKey(player.getUniqueId())) {
            manager.loadProfile(player.getUniqueId());
        }
    }
}
