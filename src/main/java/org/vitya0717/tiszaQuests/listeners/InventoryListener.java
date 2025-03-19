package org.vitya0717.tiszaQuests.listeners;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quests.Quest;
import org.vitya0717.tiszaQuests.quests.objectives.Objective;
import org.vitya0717.tiszaQuests.quests.objectives.PlaceBlocks;
import org.vitya0717.tiszaQuests.quests.playerProfile.PlayerProfileManager;
import org.vitya0717.tiszaQuests.quests.playerProfile.QuestPlayerProfile;
import org.vitya0717.tiszaQuests.utils.Text;
import org.vitya0717.tiszaQuests.utils.Utils;

public class InventoryListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        Player player = (Player) event.getWhoClicked();
        String clickedInvName = event.getView().getTitle();
        ClickType action = event.getClick();

        if(clickedInvName.equalsIgnoreCase(Main.questManager.questInventoryTitle)) {

            event.setCancelled(true);

            if(event.getCurrentItem() == null) return;

            ItemMeta meta = event.getCurrentItem().getItemMeta();

            if(meta == null) return;

            PersistentDataContainer data = meta.getPersistentDataContainer();

            NamespacedKey key = new NamespacedKey(Main.instance, "questId");

            if(data.has(key, PersistentDataType.STRING)) {
                Quest clickedQuest = Main.questManager.allQuests.get(data.get(key, PersistentDataType.STRING)).clone();
                QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(player.getUniqueId());

                if (profile != null && action.equals(ClickType.LEFT) && profile.getActiveQuests().stream().noneMatch(q -> q.getId().equalsIgnoreCase(clickedQuest.getId()))) {
                    /*switch (clickedQuest.getType()) {
                        case PLACE_BLOCKS:
                            clickedQuest.setObjective(new PlaceBlocks(clickedQuest.getId(), clickedQuest.getObjective().getBlockType(),clickedQuest.getObjective().getRequiredBlocksCount()));
                            break;
                    }*/
                    profile.getActiveQuests().add(clickedQuest);
                    player.sendMessage(Utils.Placeholders(clickedQuest, Text.QUEST_ACCEPT));
                } else {
                    player.sendMessage(Utils.Placeholders(clickedQuest, Text.ALREADY_ACCEPTED_QUEST));
                }
            }
        }
    }

}
