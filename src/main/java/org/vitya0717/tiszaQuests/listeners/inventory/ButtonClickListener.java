package org.vitya0717.tiszaQuests.listeners.inventory;

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
import org.vitya0717.tiszaQuests.quest.Quest;
import org.vitya0717.tiszaQuests.quest.playerProfile.QuestPlayerProfile;
import org.vitya0717.tiszaQuests.utils.Text;
import org.vitya0717.tiszaQuests.utils.Utils;

import java.util.Objects;

public class ButtonClickListener implements Listener {


    @EventHandler
    public void onButtonClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        String clickedInvName = event.getView().getTitle();
        ClickType action = event.getClick();

        if (clickedInvName.equalsIgnoreCase(Main.questManager.questInventoryTitle)) {
            event.setCancelled(true);

            if (event.getCurrentItem() == null) return;

            ItemMeta meta = event.getCurrentItem().getItemMeta();

            if (meta == null) return;

            PersistentDataContainer data = meta.getPersistentDataContainer();
            NamespacedKey key = new NamespacedKey(Main.instance, "button-type");

            if (data.has(key, PersistentDataType.STRING)) {
                if (action.equals(ClickType.LEFT)) {
                    QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(player.getUniqueId());

                    if(profile == null) {
                        return;
                    }

                    String buttonType = data.get(key, PersistentDataType.STRING);

                    if (Objects.equals(buttonType, "nextButton")) {
                        if(Main.questsPageManager.getPages().get(profile.getCurrentPageOn() + 1) == null) {
                            return;
                        }
                        Main.questsPageManager.goToNextPage(profile);
                    } else if(Objects.equals(buttonType, "backButton")) {
                        if(Main.questsPageManager.getPages().get(profile.getCurrentPageOn() - 1) == null) {
                            return;
                        }
                        Main.questsPageManager.goToPreviousPage(profile);
                    }
                }
            }
        }

    }

}
