package org.vitya0717.tiszaQuests.utils.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quest.inventory.QuestInventory;

public class QuestInventoryTask extends BukkitRunnable {

    public final Main plugin;
    public QuestInventory questInventory;

    public QuestInventoryTask(Main plugin, QuestInventory questInventory){
        this.plugin = plugin;
        this.questInventory = questInventory;
    }

    @Override
    public void run() {
        Main.questsPageManager.updateQuestInventory(questInventory.getInventory(), questInventory.getOwner());
    }

}
