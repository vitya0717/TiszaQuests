package org.vitya0717.tiszaQuests.quests.objectives;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.vitya0717.tiszaQuests.quests.Quest;

public abstract class Objective {

    private final String questId;
    private String objectiveId;
    private Material blockType;
    private ObjectiveType type;
    private int requiredBlocksCount;
    private int placedBlocksCount;
    private boolean finishedObjective = false;

    public Objective(String objectiveId, String questId, Material blockType, ObjectiveType type, int requiredBlocksCount) {
        this.objectiveId = objectiveId;
        this.questId = questId;
        this.blockType = blockType;
        this.type = type;
        this.requiredBlocksCount = requiredBlocksCount;
    }

    @Override
    public String toString() {
        return "Objective{" +
                "questId='" + questId + '\'' +
                ", blockType=" + blockType +
                ", count=" + requiredBlocksCount +
                '}';
    }

    public abstract void progress(String objectiveId, Quest value, Player player);

    public abstract void finishObjective(String objectiveId, Quest value, Player player);

    public abstract void finishQuest(String objectiveId, Quest value, Player player);

    public Material getBlockType() {
        return blockType;
    }

    public void setBlockType(Material blockType) {
        this.blockType = blockType;
    }

    public int getRequiredBlocksCount() {
        return requiredBlocksCount;
    }

    public void setRequiredBlocksCount(int count) {
        this.requiredBlocksCount = count;
    }

    public String getQuestId() {
        return questId;
    }

    public int getPlacedBlocksCount() {
        return placedBlocksCount;
    }

    public void increasePlacedBlocksCount(int placedBlocksCount) {
        this.placedBlocksCount += placedBlocksCount;
    }

    public void setPlacedBlocksCount(int placedBlocksCount) {
        this.placedBlocksCount = placedBlocksCount;
    }

    public String getObjectiveId() {
        return objectiveId;
    }

    public void setObjectiveId(String objectiveId) {
        this.objectiveId = objectiveId;
    }

    public ObjectiveType getType() {
        return type;
    }

    public void setType(ObjectiveType type) {
        this.type = type;
    }

    public boolean isFinishedObjective() {
        return finishedObjective;
    }

    public void setFinishedObjective(boolean finishedObjective) {
        this.finishedObjective = finishedObjective;
    }
}
