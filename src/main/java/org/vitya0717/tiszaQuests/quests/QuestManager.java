package org.vitya0717.tiszaQuests.quests;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quests.objectives.Objective;
import org.vitya0717.tiszaQuests.quests.objectives.ObjectiveType;
import org.vitya0717.tiszaQuests.quests.objectives.PlaceBlocks;
import org.vitya0717.tiszaQuests.quests.playerProfile.QuestPlayerProfile;
import org.vitya0717.tiszaQuests.utils.Text;
import org.vitya0717.tiszaQuests.utils.Utils;

import java.util.*;
import java.util.logging.Level;

public class QuestManager {

    private final Main instance;
    public final HashMap<String, Quest> allQuests = new HashMap<>();

    public final HashMap<UUID, Inventory> questInventories = new HashMap<>();
    public String questInventoryTitle = Utils.Colorize(Text.GUI_TITLE);

    public QuestManager(Main instance) {
        this.instance = instance;
    }

    public void registerQuest(Quest quest) {
        if (allQuests.keySet().stream().noneMatch(s -> Objects.equals(s, quest.getId()))) {
            allQuests.put(quest.getId(), quest);
        }
    }

    public void openQuestMenu(Player player) {

        int rowSize = 6;
        int columnSize = 9;

        if (player != null) {
            Inventory inventory = questInventories.get(player.getUniqueId());

            if (inventory == null) {
                questInventories.put(player.getUniqueId(), Bukkit.createInventory(null, rowSize * columnSize, questInventoryTitle));
                inventory = questInventories.get(player.getUniqueId());

                fillDummyItems(inventory, rowSize, columnSize);

            }
            int size = (rowSize * 2) + (columnSize * 2);

            QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(player.getUniqueId());

            if (getItemCount(inventory) - size <= allQuests.size() || profile.isInvNeedUpdate()) {

                fillUpQuestInventory(inventory, rowSize, columnSize);

                if (profile != null && profile.isInvNeedUpdate()) {
                    profile.setInvNeedUpdate(false);
                }
            }
            player.openInventory(questInventories.get(player.getUniqueId()));
            player.sendMessage(Utils.Colorize(Text.OPEN_QUEST_MENU));
        }
    }

    private int getItemCount(Inventory inventory) {
        int output = 0;
        for (ItemStack item : inventory.getContents()) {
            if (item != null) {
                output++;
            }
        }
        return output;
    }

    private void fillDummyItems(Inventory inventory, int rowSize, int columnSize) {
        ItemStack fillItem = new ItemStack(Material.DIRT, 1);
        ItemMeta itemMeta = fillItem.getItemMeta();
        fillItem.addUnsafeEnchantment(Enchantment.LURE, 100);
        assert itemMeta != null;
        itemMeta.setDisplayName(Utils.Colorize(" "));
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        fillItem.setItemMeta(itemMeta);

        //dummy items
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < columnSize; j++) {
                int itemIndex = i * columnSize + j;
                if (i == 0 || i == rowSize - 1 || j == 0 || j == columnSize - 1) {
                    fillItem.setType((itemIndex % 2 == 0) ? Material.BLACK_STAINED_GLASS_PANE : Material.WHITE_STAINED_GLASS_PANE);
                    inventory.setItem(itemIndex, fillItem);
                }
            }
        }
    }

    private void fillUpQuestInventory(Inventory inventory, int rowSize, int columnSize) {
        for (Map.Entry<String, Quest> questEntry : allQuests.entrySet()) {
            Quest quest = questEntry.getValue();
            ItemMeta meta = quest.getDisplayItem().getItemMeta();

            if (meta == null) return;

            List<String> lore = Utils.Placeholders(quest, quest.getDescription());

            meta.setLore(lore);

            quest.getDisplayItem().setItemMeta(meta);
            inventory.setItem(quest.getQuestItemSlot(), quest.getDisplayItem());
        }
    }


    public void saveQuest(Quest quest) {
        Main.questConfig.getConfig().set("quests." + quest.getId(), null);
        Main.questConfig.getConfig().set("quests." + quest.getId() + ".name", (String) quest.getName());
        Main.questConfig.getConfig().set("quests." + quest.getId() + ".displayItem", quest.getDisplayItem().getType().name());
        Main.questConfig.getConfig().set("quests." + quest.getId() + ".displaySlot", quest.getQuestItemSlot());
        Main.questConfig.getConfig().set("quests." + quest.getId() + ".questType", "QUEST_TYPE");
        Main.questConfig.getConfig().set("quests." + quest.getId() + ".objective", null);

        //Objective part still need an update
        if (quest.getObjectives() != null) {
            for (Map.Entry<String, Objective> obj : quest.getObjectives().entrySet()) {
                Objective objective = obj.getValue();
                switch (objective.getType()) {
                    case PLACE_BLOCKS:
                        PlaceBlocks placeObjective = (PlaceBlocks) objective;
                        Main.questConfig.getConfig().set("quests." + quest.getId() + ".objective.block", placeObjective.getBlockType().name());
                        Main.questConfig.getConfig().set("quests." + quest.getId() + ".objective.count", placeObjective.getRequiredBlocksCount());
                        break;
                }
                Main.questConfig.getConfig().set("quests." + quest.getId() + ".objective.displayName", objective.getDisplayName());
            }
        }
        Main.questConfig.getConfig().set("quests." + quest.getId() + ".description", quest.getDescription());
        Main.questConfig.getConfig().set("quests." + quest.getId() + ".rewards", quest.getRewards());
        Main.questConfig.saveConfig();
    }

    public void loadQuests() {
        ConfigurationSection configurationSection = Main.questConfig.getConfig().getConfigurationSection("quests");
        FileConfiguration config = Main.questConfig.getConfig();
        if (configurationSection == null) {
            System.out.println("Nem sikerült betölteni a küldetéseket!");
            return;
        }
        try {
            for (String id : configurationSection.getKeys(false)) {
                String name = config.getString("quests." + id + ".name");
                boolean enable = config.getBoolean("quests." + id + ".enable");
                boolean repeate = config.getBoolean("quests." + id + ".repeatable");
                int slot = config.getInt("quests." + id + ".displaySlot");
                ConfigurationSection objSection = config.getConfigurationSection("quests." + id + ".objectives");
                if (objSection == null) {
                    Bukkit.getLogger().log(Level.CONFIG, "Hibás küldetés objektív beállítás");
                    return;
                }
                Objective obj = null;
                HashMap<String, Objective> objectiveList = new HashMap<>();

                for (String objKey : objSection.getKeys(false)) {

                    String displayName = config.getString("quests." + id + ".objectives." + objKey + ".displayName");

                    ObjectiveType type = ObjectiveType.valueOf(config.getString("quests." + id + ".objectives." + objKey + ".type"));
                    switch (type) {
                        case PLACE_BLOCKS:
                            Material block = Material.getMaterial(Objects.requireNonNull(config.getString("quests." + id + ".objectives." + objKey + ".block")));
                            if (block == null) {
                                System.out.println("Hibás blokk betöltés!");
                                return;
                            }
                            int count = config.getInt("quests." + id + ".objectives." + objKey + ".count");
                            obj = new PlaceBlocks(objKey, id, displayName, block, ObjectiveType.PLACE_BLOCKS, count, 0);
                            objectiveList.put(obj.getObjectiveId(), obj);
                            break;
                    }
                }

                List<String> description = config.getStringList("quests." + id + ".description");
                ItemStack displayItem = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(config.getString("quests." + id + ".displayItem")))));

                assert obj != null;

                Quest temp = new Quest(id, name, description, displayItem, slot, objectiveList, null, repeate, enable);

                ItemMeta itemMeta = temp.getDisplayItem().getItemMeta();
                NamespacedKey questID = new NamespacedKey(instance, "questId");
                assert itemMeta != null;
                itemMeta.getPersistentDataContainer().set(questID, PersistentDataType.STRING, id);
                itemMeta.setDisplayName(Utils.Placeholders(null, name));
                itemMeta.setLore((Utils.Placeholders(temp, description)));
                temp.getDisplayItem().setItemMeta(itemMeta);

                System.out.println(temp);

                registerQuest(temp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        System.out.println("Sikeresen betöltve " + allQuests.size() + " küldetés");
    }

    public Quest findQuestById(String questId) {
        for (Map.Entry<String, Quest> entry : allQuests.entrySet()) {
            Quest q = entry.getValue();
            if (Objects.equals(q.getId(), questId)) {
                return q;
            }
        }
        return null;
    }

    public Quest findQuestInProfileByQuestId(QuestPlayerProfile profile, String questId) {
        for (Quest q : profile.getActiveQuests()) {
            if (Objects.equals(q.getId(), questId)) {
                return q;
            }
        }
        return null;
    }

    public int finishedObjectivesCount(Quest quest) {
        int finishedObjectiveCount = 0;
        for (Map.Entry<String, Objective> entry : quest.getObjectives().entrySet()) {
            Objective obj = entry.getValue();
            if (obj.isFinishedObjective()) {
                finishedObjectiveCount++;
            }
        }
        return finishedObjectiveCount;
    }

    public boolean hasObjectiveType(List<Quest> quests, ObjectiveType objectiveType) {
        for (Quest quest : quests) {
            for (Map.Entry<String, Objective> objective : quest.getObjectives().entrySet()) {
                ObjectiveType type = objective.getValue().getType();
                if (type.equals(objectiveType)) {
                    return true;
                }
            }
        }
        return false;
    }
}
