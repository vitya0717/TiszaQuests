package org.vitya0717.tiszaQuests.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quests.playerProfile.PlayerProfileManager;
import org.vitya0717.tiszaQuests.quests.playerProfile.QuestPlayerProfile;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        PlayerProfileManager manager = Main.profileManager;

        if(!manager.allLoadedProfile.containsKey(player.getUniqueId())) {
            QuestPlayerProfile profile = new QuestPlayerProfile(player.getUniqueId(), 0,0,0);
            manager.registerPlayerProfile(profile);
            System.out.println("[DEBUG] játékos profil mentve, memóriába egyelőre");
        }
    }
}
