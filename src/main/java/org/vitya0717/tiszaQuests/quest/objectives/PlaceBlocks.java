package org.vitya0717.tiszaQuests.quest.objectives;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.vitya0717.tiszaQuests.configuration.player.PlayerConfig;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quest.Quest;
import org.vitya0717.tiszaQuests.quest.objectives.enums.ObjectiveType;
import org.vitya0717.tiszaQuests.quest.objectives.parent.Objective;
import org.vitya0717.tiszaQuests.quest.playerProfile.QuestPlayerProfile;
import org.vitya0717.tiszaQuests.utils.Utils;
import org.vitya0717.tiszaQuests.utils.tasks.QuestDelay;

public class PlaceBlocks extends Objective implements Cloneable {

    private int placedBlocksCount;
    private final Material blockType;
    private int requiredBlocksCount;
    private QuestDelay questDelay = null;
    private QuestPlayerProfile profile;
    private PlayerConfig playerConfig;


    public PlaceBlocks(String objectiveId, String questId, String displayName, Material blockType, ObjectiveType type, int requiredBlocksCount, int placedBlocksCount) {
        super(objectiveId, questId, displayName, type);

        this.requiredBlocksCount = requiredBlocksCount;
        this.placedBlocksCount = placedBlocksCount;
        this.blockType = blockType;
    }

    @Override
    public String toString() {
        return "PlaceBlocks{" +
                "placedBlocksCount=" + placedBlocksCount +
                ", blockType=" + blockType +
                ", requiredBlocksCount=" + requiredBlocksCount +
                '}';
    }

    @Override
    public void progress(String objectiveId, String questId, Player player) {
        if(profile == null) {
            profile = Main.profileManager.allLoadedProfile.get(player.getUniqueId());
        }
        if(playerConfig == null) {
            playerConfig = Main.playerConfigManager.findPlayerConfig(profile.getPlayerUUID());
        }

        Quest quest = profile.findActiveQuestByQuestId(questId);
        PlaceBlocks placeObjective = (PlaceBlocks) quest.getObjective(objectiveId);
        placeObjective.increasePlacedBlocksCount(1);

        if(placeObjective.getRequiredBlocksCount() == placeObjective.getPlacedBlocksCount()) {
            finishObjective(objectiveId,quest.getId(), player);
            return;
        }

        player.sendMessage(Utils.Placeholders(quest, "%prefix% | &6"+"%quest_objective_display_name_"+objectiveId+"%"+" &8| &a%quest_placed_blocks_"+objectiveId+"%&7/&a%quest_required_placed_blocks_"+objectiveId+"%"));

        quest.setUpdateRequired(true);

        playerConfig.setChanged(true);

    }

    public void increasePlacedBlocksCount(int placedBlocksCount) {
        this.placedBlocksCount += placedBlocksCount;
    }

    @Override
    public void finishObjective(String objectiveId, String questId, Player player) {

        QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(player.getUniqueId());

        Quest quest = profile.findActiveQuestByQuestId(questId);

        quest.getObjective(objectiveId).finishObjective(true);

        player.sendMessage(Utils.Placeholders(quest, "%prefix% &aSikeresen teljesítetted a "+"%quest_objective_display_name_"+objectiveId+"%"+" objectivet !"));

        if(quest.getObjectives().size() != quest.finishedObjectives().size()) {
            return;
        }
        finishQuest(quest, player);
    }

    @Override
    public void finishQuest(Quest quest, Player player) {
        QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(player.getUniqueId());
        PlayerConfig playerConfig = Main.playerConfigManager.findPlayerConfig(profile.getPlayerUUID());

        profile.getActiveQuests().remove(quest.getId());
        profile.getActiveQuestIds().remove(quest.getId());

        profile.getCompletedQuests().put(quest.getId(), quest);
        profile.getCompletedQuestsIds().add(quest.getId());

        player.sendMessage( Utils.Placeholders(quest, "&aSikeresen teljesitetted a kuldetest: "+quest.getDisplayName()));

        if(questDelay == null) {
            questDelay = new QuestDelay(Main.instance, quest, player.getUniqueId());
        }
        BukkitTask questDelayTask = questDelay.runTaskTimer(Main.instance, 0, 20);
        profile.getQuestDelays().put(quest.getId(), questDelayTask);
        playerConfig.setChanged(true);
    }

    public int getPlacedBlocksCount() {
        return placedBlocksCount;
    }

    public Material getBlockType() {
        return blockType;
    }

    public int getRequiredBlocksCount() {
        return requiredBlocksCount;
    }

    public void setRequiredBlocksCount(int requiredBlocksCount) {
        this.requiredBlocksCount = requiredBlocksCount;
    }

    @Override
    public PlaceBlocks clone() {
        return (PlaceBlocks) super.clone();
    }

    public QuestDelay getQuestDelay() {
        return questDelay;
    }

    public void setQuestDelay(QuestDelay questDelay) {
        this.questDelay = questDelay;
    }
}
