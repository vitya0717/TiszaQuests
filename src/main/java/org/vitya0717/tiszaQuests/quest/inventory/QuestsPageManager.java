package org.vitya0717.tiszaQuests.quest.inventory;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quest.Quest;
import org.vitya0717.tiszaQuests.quest.inventory.questspage.QuestsPage;
import org.vitya0717.tiszaQuests.quest.playerProfile.QuestPlayerProfile;
import org.vitya0717.tiszaQuests.utils.Text;
import org.vitya0717.tiszaQuests.utils.Utils;
import org.vitya0717.tiszaQuests.utils.tasks.QuestInventoryTask;

import java.util.*;

public class QuestsPageManager {

    private final Main instance;
    private final Queue<Integer> pageBuffer = new ArrayDeque<>();
    public QuestInventoryTask itemUpdate = null;
    private final HashMap<Integer, QuestsPage> pages = new HashMap<>();

    private ItemStack nextPageButton = null;
    private ItemStack backPageButton = null;
    private final NamespacedKey nextKey = new NamespacedKey(Main.instance, "button-type");

    public QuestsPageManager(Main instance) {
        this.instance = instance;
    }

    public void openQuestMenu(Player player) {

        int rowSize = 6;
        int columnSize = 9;

        if (player != null) {

            QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(player.getUniqueId());
            QuestInventory inventory = Main.questManager.questInventories.get(player.getUniqueId());

            //create quest inventory without pagination
            if (inventory == null) {

                inventory = new QuestInventory(instance);
                inventory.buildInventory(rowSize, columnSize, Main.questManager.questInventoryTitle);
                inventory.setQuestPlayerProfile(profile);

            }
            Main.questManager.questInventories.put(player.getUniqueId(), inventory);

            if (itemUpdate == null || inventory.getInventoryUpdateTask() == null) {
                itemUpdate = new QuestInventoryTask(instance, inventory);
                inventory.setInventoryUpdateTask(itemUpdate.runTaskTimer(instance, 0, 20));
            }

            if(profile.isFirstQuestMenuOpen()) {
                buildQuestInventory(inventory.getInventory(), profile);
            } else {
                fillUpQuestInventory(inventory.getInventory(), profile);
            }
            player.openInventory(inventory.getInventory());
            player.sendMessage(Utils.Colorize(Text.OPEN_QUEST_MENU));
        }
    }

    private void fillUpQuestInventory(Inventory inventory, QuestPlayerProfile profile) {

        QuestsPage page = getPage(profile.getCurrentPageOn());

        for (Quest quest : page.getPageContents()) {

            Quest playerQuest = Main.questManager.findQuestInPlayerProfile(profile.getPlayerUUID(), quest.getId());
            Quest completedQuest = Main.questManager.findQuestInCompletedPlayersQuests(profile.getPlayerUUID(), quest.getId());

            if (playerQuest != null) {
                quest = playerQuest;
            }

            if (completedQuest != null) {
                quest = completedQuest;
            }

            if (quest.getDisplayItem() == null) {
                Main.instance.getLogger().warning("Quest" + quest.getDisplayName() + " display item was null");
            }

            if(!quest.isUpdateRequired() && !profile.questInventoryNeedsUpdate()) {
                continue;
            }

            ItemMeta meta = quest.getDisplayItem().getItemMeta();

            if (meta == null) continue;
            List<String> lore = Utils.Placeholders(quest, quest.getDescription());
            meta.setLore(lore);

            quest.getDisplayItem().setItemMeta(meta);
            inventory.setItem(quest.getItemSlot(), quest.getDisplayItem());
            quest.setUpdateRequired(false);
        }
        fillPaginationButtons(inventory);
        profile.setQuestInventoryNeedsUpdate(false);
    }

    private void buildQuestInventory(Inventory inventory, QuestPlayerProfile profile) {
        QuestsPage page = getPage(profile.getCurrentPageOn());
        for (Quest quest : page.getPageContents()) {
            ItemMeta meta = quest.getDisplayItem().getItemMeta();

            if (meta == null) continue;
            List<String> lore = Utils.Placeholders(quest, quest.getDescription());
            meta.setLore(lore);

            quest.getDisplayItem().setItemMeta(meta);
            inventory.setItem(quest.getItemSlot(), quest.getDisplayItem());
        }
    }

    private void fillPaginationButtons(Inventory inventory) {
        if(nextPageButton == null) {
            nextPageButton = new ItemStack(Material.PAPER);
            ItemMeta nextPageButtonMeta = nextPageButton.getItemMeta();
            nextPageButtonMeta.setDisplayName("next page ");

            PersistentDataContainer nextButtonData = nextPageButtonMeta.getPersistentDataContainer();
            nextButtonData.set(nextKey, PersistentDataType.STRING, "nextButton");

            nextPageButton.setItemMeta(nextPageButtonMeta);
        }
        inventory.setItem(53, nextPageButton);

        if(backPageButton == null) {
            backPageButton = new ItemStack(Material.PAPER);
            ItemMeta backPageButtonMeta = nextPageButton.getItemMeta();
            backPageButtonMeta.setDisplayName("back");

            PersistentDataContainer backButtonData = backPageButtonMeta.getPersistentDataContainer();
            backButtonData.set(nextKey, PersistentDataType.STRING, "backButton");

            backPageButton.setItemMeta(backPageButtonMeta);
        }
        inventory.setItem(45, backPageButton);
    }

    public void generatePagesOfQuests(HashMap<String, Quest> allQuests) {
        //create pagination
        int questCount = 0;
        int currentQuestCount = allQuests.size();
        int pageLen = 1;
        int maxContentPerPage = 18;

        QuestsPage questsPage = new QuestsPage(instance);
        questsPage.setMaxPageContents(maxContentPerPage);

        for (Map.Entry<String, Quest> entry : allQuests.entrySet()) {
            Quest quest = entry.getValue();

            questsPage.getPageContents().add(quest);
            questCount++;
            currentQuestCount--;

            if (questsPage.getMaxPageContents() == questCount || currentQuestCount == 0) {

                questsPage.setPageNumber(pageLen);
                addPage(questsPage);
                pageLen++;
                questCount = 0;

                if (currentQuestCount != 0) {

                    questsPage = new QuestsPage(instance);
                    questsPage.setMaxPageContents(maxContentPerPage);
                }
            }
        }
    }

    private QuestsPage getPage(int currentPageOn) {
        return pages.get(currentPageOn);
    }

    public void goToNextPage(QuestPlayerProfile profile) {
        profile.setCurrentPageOn(profile.getCurrentPageOn() + 1);
        profile.setQuestInventoryNeedsUpdate(true);
    }

    public void goToPreviousPage(QuestPlayerProfile profile) {
        profile.setCurrentPageOn(profile.getCurrentPageOn() - 1);
        profile.setQuestInventoryNeedsUpdate(true);
    }

    public void updateQuestInventory(Inventory inventory, QuestPlayerProfile owner) {
        if(owner.questInventoryNeedsUpdate()) {
            inventory.clear();
        }
        fillUpQuestInventory(inventory, owner);
    }

    public Main getInstance() {
        return instance;
    }

    public HashMap<Integer, QuestsPage> getPages() {
        return pages;
    }

    public void addPage(QuestsPage questsPage) {
        pages.put(pages.size()+1, questsPage);
    }
}
