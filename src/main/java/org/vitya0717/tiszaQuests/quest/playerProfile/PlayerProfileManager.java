package org.vitya0717.tiszaQuests.quest.playerProfile;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.vitya0717.tiszaQuests.configuration.exceptions.PlayerConfigurationExistsException;
import org.vitya0717.tiszaQuests.configuration.player.PlayerConfig;
import org.vitya0717.tiszaQuests.configuration.player.PlayerConfigManager;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quest.Quest;
import org.vitya0717.tiszaQuests.quest.inventory.QuestInventory;
import org.vitya0717.tiszaQuests.quest.objectives.PlaceBlocks;
import org.vitya0717.tiszaQuests.quest.objectives.enums.ObjectiveType;
import org.vitya0717.tiszaQuests.quest.objectives.parent.Objective;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PlayerProfileManager {

    private final Main instance;
    public final HashMap<UUID, QuestPlayerProfile> allLoadedProfile = new HashMap<>();

    public PlayerProfileManager(Main instance) {
        this.instance = instance;
    }

    public void registerPlayerProfile(QuestPlayerProfile profile) {
        if (allLoadedProfile.keySet().stream().noneMatch(s -> Objects.equals(s, profile.getPlayerUUID()))) {
            allLoadedProfile.put(profile.getPlayerUUID(), profile);
            QuestInventory inventory = Main.questsPageManager.registerQuestInventory(profile);
            Main.questsPageManager.questInventories.putIfAbsent(profile.getPlayerUUID(), inventory);
        }
    }

    public void loadProfile(UUID uniqueId) {
        PlayerConfig config = Main.playerConfigManager.findPlayerConfig(uniqueId);
        PlayerConfigManager configManager = Main.playerConfigManager;
        QuestPlayerProfile playerProfile = new QuestPlayerProfile(uniqueId);

        if (allLoadedProfile.containsKey(uniqueId)) {
            Bukkit.getLogger().severe("profile already exists");
            return;
        }

        if (config == null) {
            try {
                config = configManager.registerPlayerConfiguration(uniqueId);
                config.setChanged(true);
            } catch (PlayerConfigurationExistsException e) {
                Bukkit.getLogger().severe(e.getMessage());
            }
        }
        ConfigurationSection baseSection = config.getConfig().getConfigurationSection(uniqueId.toString());

        if (baseSection == null) {
            config.getConfig().set(uniqueId.toString(), "");
            config.getConfig().set(uniqueId.toString() + ".QuestPoints", "");
            config.getConfig().set(uniqueId.toString() + ".Quests", "");
            config.saveConfig();

            baseSection = config.getConfig().getConfigurationSection(uniqueId.toString());
        }

        for (String key : baseSection.getKeys(false)) {

            int questPoints = config.getConfig().getInt(key + ".QuestPoints");

            ConfigurationSection questSection = config.getConfig().getConfigurationSection(uniqueId.toString() + ".Quests");

            if (questSection != null) {
                for (String questKey : questSection.getKeys(false)) {

                    Quest baseQuest = Main.questManager.findQuestById(questKey);

                    if (baseQuest == null) {
                        continue;
                    }

                    Quest temp = new Quest(questKey, baseQuest.getDisplayName(), baseQuest.getDisplayItem(), baseQuest.getItemSlot(), baseQuest.getQuestBaseDelay());
                    temp.setDescription(baseQuest.getDescription());

                    ConfigurationSection objectivesSection = config.getConfig().getConfigurationSection(uniqueId.toString() + ".Quests." + questKey + ".ObjectivesProgress");

                    if (objectivesSection != null) {
                        for (String objectiveKey : objectivesSection.getKeys(false)) {
                            int placedBlocks = config.getConfig().getInt(uniqueId.toString() + ".Quests." + questKey + ".ObjectivesProgress." + objectiveKey + ".PlacedBlocks");

                            Objective objective = baseQuest.getObjective(objectiveKey);
                            temp.getObjectives().put(objectiveKey, buildObjective(objective, objectiveKey, temp, placedBlocks));
                        }
                    }
                    playerProfile.setQuestPoints(questPoints);
                    playerProfile.getActiveQuests().putIfAbsent(temp.getId(), temp);
                    playerProfile.getActiveQuestIds().add(temp.getId());
                }
            }
        }
        registerPlayerProfile(playerProfile);
    }

    private Objective buildObjective(Objective objective, String objectiveKey, Quest quest, int placedBlocks) {
        switch (objective.getType()) {
            case PLACE_BLOCKS:
                PlaceBlocks PLACE = (PlaceBlocks) objective;
                objective = new PlaceBlocks(objectiveKey, quest.getId(), quest.getDisplayName(), PLACE.getBlockType(), ObjectiveType.PLACE_BLOCKS, PLACE.getRequiredBlocksCount(), placedBlocks);
                return objective;
        }
        return objective;
    }

    public void saveProfile(UUID uniqueId) {

    }

    public Main getInstance() {
        return instance;
    }

    public void unregisterPlayer(UUID uniqueId) {
        allLoadedProfile.remove(uniqueId);
        Main.questsPageManager.questInventories.remove(uniqueId);
    }
}
