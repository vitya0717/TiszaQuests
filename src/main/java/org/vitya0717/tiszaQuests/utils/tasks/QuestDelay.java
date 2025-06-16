package org.vitya0717.tiszaQuests.utils.tasks;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quest.Quest;
import org.vitya0717.tiszaQuests.quest.playerProfile.QuestPlayerProfile;
import org.vitya0717.tiszaQuests.utils.Utils;

import java.util.UUID;

public class QuestDelay extends BukkitRunnable {

    public final Main plugin;
    public Quest quest;
    private final UUID playerUUID;
    private final QuestPlayerProfile playerProfile;

    public QuestDelay(Main plugin, Quest quest, UUID playerUUID) {
        this.plugin = plugin;
        this.quest = quest;
        this.playerUUID = playerUUID;
        this.playerProfile =  Main.profileManager.allLoadedProfile.get(playerUUID);
    }

    @Override
    public void run() {
        int delay = Utils.calculateTimeLeft(quest.getQuestDelay());
        Player player = Bukkit.getPlayer(playerUUID);

        if(player == null || !player.isOnline()) {
            playerProfile.getQuestDelays().remove(quest.getId());
            this.cancel();
            return;
        }

        if(delay == 0) {
            playerProfile.getCompletedQuestsIds().remove(quest.getId());
            playerProfile.getCompletedQuests().remove(quest.getId());
            playerProfile.getQuestDelays().remove(quest.getId());
            playerProfile.setQuestInventoryNeedsUpdate(true);
            this.cancel();
        }
        Bukkit.getLogger().info(String.valueOf(delay));
        quest.setUpdateRequired(true);
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }
}
