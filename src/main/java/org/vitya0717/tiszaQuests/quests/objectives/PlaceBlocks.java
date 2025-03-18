package org.vitya0717.tiszaQuests.quests.objectives;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quests.Quest;
import org.vitya0717.tiszaQuests.quests.playerProfile.QuestPlayerProfile;
import org.vitya0717.tiszaQuests.utils.Utils;

public class PlaceBlocks extends Objective {


    public PlaceBlocks(String questId,Material blockType, int count) {
       super(questId, blockType, count);
    }

    @Override
    public void progress(Quest quest, Player player) {
        //player.sendMessage(Utils.Placeholders(quest, "%prefix% &aElőrelépés a "+quest.getName() +" &aküldetésben"));
        quest.getObjective().increasePlacedBlocksCount(1);
        if(quest.getObjective().getRequiredBlocksCount() == quest.getObjective().getPlacedBlocksCount()) {
            finish(quest, player);
            return;
        }
        player.sendMessage(Utils.Placeholders(quest, "%prefix% &a%quest_placed_blocks%&7/&a%quest_required_placed_blocks%"));

    }

    @Override
    public void finish(Quest quest, Player player) {
        QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(player.getUniqueId());

        quest.getObjective().setPlacedBlocksCount(0);
        profile.getActiveQuests().remove(quest);
        System.out.println("[DEBUG] PlaceBlocks: "+quest.getName()+", törölve");
        profile.getCompletedQuests().add(quest);
        System.out.println("[DEBUG] PlaceBlocks: "+quest.getName()+", hozzáadva a kész küldetésekhez");


        player.sendMessage(Utils.Placeholders(quest, "%prefix% &aSikeresen teljesítetted a küldetést!"));
    }

}
