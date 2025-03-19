package org.vitya0717.tiszaQuests.quests.objectives;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.vitya0717.tiszaQuests.quests.Quest;

public class Mining extends Objective {

    private Material blockType;

    private int count;

    public Mining(String objectiveId ,String questId, Material blockType, ObjectiveType type, int count) {
        super(objectiveId, questId, blockType, type, count);
    }

    @Override
    public void progress(String objectiveId, Quest value, Player player) {

    }

    @Override
    public void finish(String objectiveId, Quest value, Player player) {

    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Material getBlockType() {
        return blockType;
    }

    public void setBlockType(Material blockType) {
        this.blockType = blockType;
    }


}
