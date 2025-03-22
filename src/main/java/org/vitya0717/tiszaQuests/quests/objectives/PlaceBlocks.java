package org.vitya0717.tiszaQuests.quests.objectives;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quests.Quest;
import org.vitya0717.tiszaQuests.quests.playerProfile.QuestPlayerProfile;
import org.vitya0717.tiszaQuests.utils.Utils;

public class PlaceBlocks extends Objective {


    public PlaceBlocks(String objectiveId, String questId,Material blockType, ObjectiveType type, int count) {
       super(objectiveId, questId, blockType,type, count);
    }

    @Override
    public void progress(String objectiveId, Quest quest, Player player) {
        //player.sendMessage(Utils.Placeholders(quest, "%prefix% &aElőrelépés a "+quest.getName() +" &aküldetésben"));
        quest.getObjective(objectiveId).increasePlacedBlocksCount(1);
        if(quest.getObjective(objectiveId).getRequiredBlocksCount() == quest.getObjective(objectiveId).getPlacedBlocksCount()) {
            finishObjective(objectiveId,quest, player);
            return;
        }

        QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(player.getUniqueId());

        //send update request for quest gui if needed
        if(!profile.isInvNeedUpdate()) {
            profile.setInvNeedUpdate(true);
        }

        player.sendMessage(Utils.Placeholders(quest, "%prefix% | &6"+quest.getName()+" &8| &a%quest_placed_blocks_"+objectiveId+"%&7/&a%quest_required_placed_blocks_"+objectiveId+"%"));

    }

    @Override
    public void finishObjective(String objectiveId, Quest quest, Player player) {
        QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(player.getUniqueId());
        //quest.getObjective(objectiveId).setPlacedBlocksCount(0);
        quest.getObjective(objectiveId).setFinishedObjective(true);
        player.sendMessage(Utils.Placeholders(quest, "%prefix% &aSikeresen teljesítetted a küldetést!"));
    }

    @Override
    public void finishQuest(String objectiveId, Quest quest, Player player) {
        QuestPlayerProfile profile = Main.profileManager.allLoadedProfile.get(player.getUniqueId());
        profile.getActiveQuests().remove(quest);
        profile.getCompletedQuests().add(quest);
    }

}
