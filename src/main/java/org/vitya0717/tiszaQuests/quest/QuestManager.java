package org.vitya0717.tiszaQuests.quest;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quest.exceptions.InvalidQuestConfigurationException;
import org.vitya0717.tiszaQuests.quest.exceptions.InvalidQuestObjectiveConfigurationException;
import org.vitya0717.tiszaQuests.quest.inventory.QuestInventory;
import org.vitya0717.tiszaQuests.quest.objectives.Objective;
import org.vitya0717.tiszaQuests.quest.objectives.ObjectiveType;
import org.vitya0717.tiszaQuests.quest.objectives.PlaceBlocks;
import org.vitya0717.tiszaQuests.quest.playerProfile.QuestPlayerProfile;
import org.vitya0717.tiszaQuests.utils.QuestValidator;
import org.vitya0717.tiszaQuests.utils.Text;
import org.vitya0717.tiszaQuests.utils.Utils;
import org.vitya0717.tiszaQuests.utils.tasks.QuestInventoryTask;

import java.util.*;

public class QuestManager {

    private final Main instance;
    //public final HashMap<String, Quest> allQuests = new HashMap<>();
    public final LinkedHashMap<String, Quest> allQuests = new LinkedHashMap<>();
    public QuestInventoryTask itemUpdate = null;

    public final HashMap<UUID, QuestInventory> questInventories = new HashMap<>();
    public String questInventoryTitle = Utils.Colorize(Text.GUI_TITLE);

    private final ConfigurationSection configurationSection = Main.questConfig.getConfig().getConfigurationSection("quests");
    private final FileConfiguration config = Main.questConfig.getConfig();

    public QuestManager(Main instance) {
        this.instance = instance;
    }

    public void registerQuest(Quest quest) {
        if (allQuests.keySet().stream().noneMatch(s -> Objects.equals(s, quest.getId()))) {
            allQuests.put(quest.getId(), quest);
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

    /*private void fillDummyItems(Inventory inventory, int rowSize, int columnSize) {
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
    }*/

    public void saveQuest(Quest quest) {
        Main.questConfig.getConfig().set("quests." + quest.getId(), null);
        Main.questConfig.getConfig().set("quests." + quest.getId() + ".name", quest.getName());
        Main.questConfig.getConfig().set("quests." + quest.getId() + ".displayItem", quest.getDisplayItem().getType().name());
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

    public void loadQuests() throws InvalidQuestConfigurationException, InvalidQuestObjectiveConfigurationException {

        if (configurationSection == null) {
            throw new InvalidQuestConfigurationException("'quest' section is missing, please provide it to load quests!");
        }

        for (String id : configurationSection.getKeys(false)) {

            QuestValidator validator = isValidQuestProperties(id);

            if (!validator.isValid()) {
                throw new InvalidQuestConfigurationException(validator.getErrorMessage());
            }

            String name = config.getString("quests." + id + ".name");
            boolean enable = config.getBoolean("quests." + id + ".enable");
            boolean repeat = config.getBoolean("quests." + id + ".repeatable");
            int slot = config.getInt("quests." + id + ".displaySlot");
            int delay = config.getInt("quests." + id + ".delay");
            List<String> description = config.getStringList("quests." + id + ".description");
            String itemName = config.getString("quests." + id + ".displayItem");

            assert itemName != null;

            Material display = Material.getMaterial(itemName.toUpperCase());
            HashMap<String, Objective> objectiveList = loadQuestObjectives(id);

            assert display != null;
            ItemStack displayItem = new ItemStack(display);

            //temp quest
            Quest temp = new Quest(id, name, displayItem, slot, delay);
            temp.setActive(enable);
            temp.setDescription(description);
            temp.setRepeatable(repeat);
            temp.setObjective(objectiveList);

            ItemMeta itemMeta = temp.getDisplayItem().getItemMeta();

            if (itemMeta == null) {
                throw new InvalidQuestConfigurationException(id + ": The metadata of the item was null. Please check the configuration.");
            }

            NamespacedKey questID = new NamespacedKey(instance, "questId");

            itemMeta.getPersistentDataContainer().set(questID, PersistentDataType.STRING, id);

            itemMeta.setDisplayName(Utils.Placeholders(null, name));
            itemMeta.setLore((Utils.Placeholders(temp, description)));
            temp.getDisplayItem().setItemMeta(itemMeta);

            registerQuest(temp);
        }

        //pages
        Main.questsPageManager.generatePagesOfQuests(allQuests);
    }

    private QuestValidator isValidQuestProperties(String id) {
        QuestValidator temp = new QuestValidator(instance);
        String basePath = "quests." + id;
        String[] requiredFields = {"name", "enable", "repeatable", "displaySlot", "delay", "description", "displayItem"};

        for (String field : requiredFields) {
            if (!config.contains(basePath + "." + field)) {
                temp.setErrorMessage(String.format("Quest %s: '%s' field is missing", id, field));
                return temp;
            }
        }
        temp.setValid(true);
        return temp;
    }

    private HashMap<String, Objective> loadQuestObjectives(String id) throws InvalidQuestObjectiveConfigurationException {
        ConfigurationSection objSection = config.getConfigurationSection("quests." + id + ".objectives");

        if (objSection == null) {
            throw new InvalidQuestObjectiveConfigurationException("Invalid objective configuration in that quest: " + id);
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
                        return null;
                    }
                    int count = config.getInt("quests." + id + ".objectives." + objKey + ".count");
                    obj = new PlaceBlocks(objKey, id, displayName, block, ObjectiveType.PLACE_BLOCKS, count, 0);
                    objectiveList.put(obj.getObjectiveId(), obj);
                    break;
            }
        }
        return objectiveList;
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

    public Quest findQuestInPlayerProfile(UUID playerUUID, String questId) {
        QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(playerUUID);
        if (profile == null) {
            return null;
        }
        for (String qId : profile.getActiveQuestIds()) {
            if (Objects.equals(qId, questId)) {
                return profile.getActiveQuests().get(qId);
            }
        }
        return null;
    }

    public Quest findQuestInCompletedPlayersQuests(UUID playerUUID, String questId) {
        QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(playerUUID);
        if (profile == null) {
            return null;
        }
        for (String qId : profile.getCompletedQuestsIds()) {
            if (Objects.equals(qId, questId)) {
                return profile.getCompletedQuests().get(qId);
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

    public boolean hasObjectiveType(HashMap<String, Quest> quests, ObjectiveType objectiveType) {
        for (Map.Entry<String, Quest> entry : quests.entrySet()) {
            Quest quest = entry.getValue();
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
