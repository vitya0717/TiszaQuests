package org.vitya0717.tiszaQuests.utils.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quest.Quest;
import org.vitya0717.tiszaQuests.quest.playerProfile.QuestPlayerProfile;

import java.util.UUID;

public class QuestDelay extends BukkitRunnable {

    public final Main plugin;
    public Quest quest;
    private int delay;
    private final UUID playerUUID;

    public QuestDelay(Main plugin, Quest quest, UUID playerUUID) {
        this.plugin = plugin;
        this.quest = quest;
        this.delay = quest.getQuestDelay();
        this.playerUUID = playerUUID;
    }


    @Override
    public void run() {
        quest.setQuestDelay(delay--);
        if (quest.getQuestDelay() == 0) {
            QuestPlayerProfile playerProfile = Main.profileManager.allLoadedProfile.get(playerUUID);
            playerProfile.getCompletedQuestsIds().remove(quest.getId());
            playerProfile.getCompletedQuests().remove(quest.getId());
            this.cancel();
        }
    }
}
