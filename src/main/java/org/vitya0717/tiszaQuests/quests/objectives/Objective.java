package org.vitya0717.tiszaQuests.quests.objectives;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class Objective {

    private String questId;
    private Material blockType;
    private int count;

    public Objective(String questId, Material blockType, int count) {
        this.questId = questId;
        this.blockType = blockType;
        this.count = count;
    }

    public abstract void progress(Player player);
}
