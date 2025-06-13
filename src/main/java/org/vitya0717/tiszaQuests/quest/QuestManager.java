package org.vitya0717.tiszaQuests.quest;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quest.exceptions.InvalidQuestConfigurationException;
import org.vitya0717.tiszaQuests.quest.exceptions.InvalidQuestObjectiveConfigurationException;
import org.vitya0717.tiszaQuests.quest.inventory.QuestInventory;
import org.vitya0717.tiszaQuests.quest.objectives.parent.Objective;
import org.vitya0717.tiszaQuests.quest.objectives.enums.ObjectiveType;
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

    public void saveQuest(Quest quest) {
        Main.questConfig.getConfig().set("quests." + quest.getId(), null);
        Main.questConfig.getConfig().set("quests." + quest.getId() + ".name", quest.getDisplayName());
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
            HashMap<String, Objective> objectiveList = loadQuestObjectives(id);

            if(itemName == null || itemName.isEmpty()) {
                throw new InvalidQuestConfigurationException("Invalid Material in this quest: "+id);
            }

            Material display = Material.getMaterial(itemName.toUpperCase());
            if(display == null) {
                throw new InvalidQuestConfigurationException("Invalid Material in this quest: "+id);
            }

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
}
