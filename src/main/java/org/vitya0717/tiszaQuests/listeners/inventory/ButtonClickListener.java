package org.vitya0717.tiszaQuests.listeners.inventory;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quest.inventory.questspage.QuestsPage;
import org.vitya0717.tiszaQuests.quest.playerProfile.QuestPlayerProfile;

public class ButtonClickListener implements Listener {


    @EventHandler
    public void onButtonClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String clickedInvName = event.getView().getTitle();
        ClickType action = event.getClick();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedInvName.equalsIgnoreCase(Main.questManager.questInventoryTitle)) {

            event.setCancelled(true);

            ItemMeta meta = clickedItem != null ? clickedItem.getItemMeta() : null;
            if (meta == null) return;

            PersistentDataContainer data = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(Main.instance, "button-type");

            if (data.has(key, PersistentDataType.STRING) && action.equals(ClickType.LEFT)) {

                QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(player.getUniqueId());

                String buttonType = data.get(key, PersistentDataType.STRING);

                switch (buttonType) {
                    case "nextButton":
                        goNextPage(profile);
                        break;
                    case "backButton":
                        goBackPage(profile);
                        break;
                    case null:
                        return;
                    default:
                }
            }
        }
    }

    private void goBackPage(QuestPlayerProfile profile) {
        if (profile == null) {
            return;
        }
        QuestsPage page = Main.questsPageManager.getPages().get(profile.getCurrentPageOn() - 1);
        if (page == null) {
            return;
        }
        Main.questsPageManager.goToPreviousPage(profile);
    }

    private void goNextPage(QuestPlayerProfile profile) {

        if (profile == null) {
            Bukkit.getLogger().info(String.valueOf(profile));
            return;
        }
        QuestsPage page = Main.questsPageManager.getPages().get(profile.getCurrentPageOn() + 1);
        if (page == null) {
            return;
        }

        Main.questsPageManager.goToNextPage(profile);
    }

}
