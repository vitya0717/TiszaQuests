package org.vitya0717.tiszaQuests.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.vitya0717.tiszaQuests.configuration.player.PlayerConfig;
import org.vitya0717.tiszaQuests.main.Main;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PlayerConfig config = Main.playerConfigManager.findPlayerConfig(player.getUniqueId());

        if(config.changed()) {
            config.saveConfig();
            config.setChanged(false);
        }

        Main.playerConfigManager.unregisterPlayerConfig(player.getUniqueId());
        Main.profileManager.unregisterPlayer(player.getUniqueId());
    }
}
