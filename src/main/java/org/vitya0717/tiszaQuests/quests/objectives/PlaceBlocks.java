package org.vitya0717.tiszaQuests.quests.objectives;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quests.Quest;
import org.vitya0717.tiszaQuests.quests.playerProfile.QuestPlayerProfile;
import org.vitya0717.tiszaQuests.utils.Utils;

public class PlaceBlocks extends Objective implements Cloneable {

    private int placedBlocksCount;
    private final Material blockType;
    private int requiredBlocksCount;

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
        QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(player.getUniqueId());

        Quest quest = Main.questManager.findQuestInProfileByQuestId(profile, questId);
        PlaceBlocks placeObjective = (PlaceBlocks) quest.getObjective(objectiveId);
        placeObjective.increasePlacedBlocksCount(1);

        if(placeObjective.getRequiredBlocksCount() == placeObjective.getPlacedBlocksCount()) {
            finishObjective(objectiveId,quest.getId(), player);
            return;
        }

        //send update request for quest gui if needed
        if(!profile.isInvNeedUpdate()) {
            profile.setInvNeedUpdate(true);
        }

        player.sendMessage(Utils.Placeholders(quest, "%prefix% | &6"+"%quest_objective_display_name_"+objectiveId+"%"+" &8| &a%quest_placed_blocks_"+objectiveId+"%&7/&a%quest_required_placed_blocks_"+objectiveId+"%"));

    }

    public void increasePlacedBlocksCount(int placedBlocksCount) {
        this.placedBlocksCount += placedBlocksCount;
    }

    @Override
    public void finishObjective(String objectiveId, String questId, Player player) {

        QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(player.getUniqueId());

        Quest quest = Main.questManager.findQuestInProfileByQuestId(profile, questId);

        quest.getObjective(objectiveId).finishObjective(true);

        player.sendMessage(Utils.Placeholders(quest, "%prefix% &aSikeresen teljesítetted a "+"%quest_objective_display_name_"+objectiveId+"%"+" objectivet !"));

        if(quest.getObjectives().size() != Main.questManager.finishedObjectivesCount(quest)) {
            return;
        }

        finishQuest(quest, player);
    }

    @Override
    public void finishQuest(Quest quest, Player player) {
        QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(player.getUniqueId());
        profile.getActiveQuests().remove(quest);
        profile.getCompletedQuests().add(quest);
        player.sendMessage("Sikeresen teljesitetted a kuldetest: "+quest.getName());
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
}
