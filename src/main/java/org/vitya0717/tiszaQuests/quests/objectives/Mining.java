package org.vitya0717.tiszaQuests.quests.objectives;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Mining extends Objective {

    private Material blockType;

    private int count;

    public Mining(String questId, Material blockType, int count) {
        super(questId, blockType, count);
    }

    @Override
    public void progress(Player player) {

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
