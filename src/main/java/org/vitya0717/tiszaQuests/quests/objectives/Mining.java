package org.vitya0717.tiszaQuests.quests.objectives;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.vitya0717.tiszaQuests.quests.Quest;

public class Mining extends Objective {

    private Material blockType;

    private int count;

    public Mining(String objectiveId ,String questId, String displayName, Material blockType, ObjectiveType type, int count) {
        super(objectiveId, questId, displayName, type);
    }

    @Override
    public void progress(String objectiveId, String questId, Player player) {

    }

    @Override
    public void finishObjective(String objectiveId, String questId, Player player) {

    }

    @Override
    public void finishQuest(Quest value, Player player) {

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
