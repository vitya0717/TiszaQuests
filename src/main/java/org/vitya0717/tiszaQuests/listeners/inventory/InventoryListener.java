package org.vitya0717.tiszaQuests.listeners.inventory;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quest.Quest;
import org.vitya0717.tiszaQuests.quest.inventory.QuestInventory;
import org.vitya0717.tiszaQuests.quest.playerProfile.QuestPlayerProfile;
import org.vitya0717.tiszaQuests.utils.Text;
import org.vitya0717.tiszaQuests.utils.Utils;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        String clickedInvName = event.getView().getTitle();
        ClickType action = event.getClick();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedInvName.equalsIgnoreCase(Main.questManager.questInventoryTitle)) {

            event.setCancelled(true);

            ItemMeta meta = clickedItem != null ? clickedItem.getItemMeta() : null;
            if (meta == null) return;

            PersistentDataContainer data = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(Main.instance, "questId");

            if (data.has(key, PersistentDataType.STRING)) {

                Quest clickedQuest = Main.questManager.findQuestById(data.get(key, PersistentDataType.STRING));
                QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(player.getUniqueId());

                if (clickedQuest == null || profile == null) {
                    return;
                }

                Quest playerQuest = profile.findActiveQuestByQuestId(data.get(key, PersistentDataType.STRING));

                if (action.equals(ClickType.LEFT) && playerQuest == null) {

                    profile.getActiveQuests().put(clickedQuest.getId(), clickedQuest.clone());
                    profile.getActiveQuestIds().add(clickedQuest.getId());

                    clickedQuest = Main.questManager.findQuestInPlayerProfile(player.getUniqueId(), data.get(key, PersistentDataType.STRING));
                    player.sendMessage(Utils.Placeholders(clickedQuest, Text.QUEST_ACCEPT));
                } else {
                    player.sendMessage(Utils.Placeholders(clickedQuest, Text.ALREADY_ACCEPTED_QUEST));
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        QuestInventory inv = Main.questsPageManager.questInventories.get(player.getUniqueId());

        if (inv != null && event.getView().getTitle().equals(inv.getTitle()) && inv.getInventoryUpdateTask() != null && !inv.getInventoryUpdateTask().isCancelled()) {
            Bukkit.getLogger().info("Quest inventory update paused");
            inv.getInventoryUpdateTask().cancel();
            inv.setInventoryUpdateTask(null);
        }
    }
}
