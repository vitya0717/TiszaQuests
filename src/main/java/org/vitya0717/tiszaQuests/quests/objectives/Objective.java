package org.vitya0717.tiszaQuests.quests.objectives;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.vitya0717.tiszaQuests.quests.Quest;

public abstract class Objective implements Cloneable {

    private final String questId;
    private Material blockType;
    private int requiredBlocksCount;
    private int placedBlocksCount;

    public Objective(String questId, Material blockType, int requiredBlocksCount) {
        this.questId = questId;
        this.blockType = blockType;
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

    public abstract void progress(Quest value, Player player);

    public abstract void finish(Quest value, Player player);

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

    @Override
    public Objective clone() {
        try {
            return (Objective) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
