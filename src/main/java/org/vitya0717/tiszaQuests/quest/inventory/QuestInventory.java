package org.vitya0717.tiszaQuests.quest.inventory;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quest.playerProfile.QuestPlayerProfile;

public class QuestInventory {

    private final Main plugin;
    private String title;
    private Inventory inventory;
    private BukkitTask inventoryUpdateTask;
    private QuestPlayerProfile questPlayerProfile;


    public QuestInventory(Main plugin) {
        this.plugin = plugin;
    }

    public Main getPlugin() {
        return plugin;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void buildInventory(int  rowSize, int columnSize, String invTitle) {
        this.inventory = Bukkit.createInventory(null, rowSize * columnSize, invTitle);
    }

    public BukkitTask getInventoryUpdateTask() {
        return inventoryUpdateTask;
    }

    public void setInventoryUpdateTask(BukkitTask inventoryUpdateTask) {
        this.inventoryUpdateTask = inventoryUpdateTask;
    }

    public QuestPlayerProfile getOwner() {
        return questPlayerProfile;
    }

    public void setQuestPlayerProfile(QuestPlayerProfile questPlayerProfile) {
        this.questPlayerProfile = questPlayerProfile;
    }
}
