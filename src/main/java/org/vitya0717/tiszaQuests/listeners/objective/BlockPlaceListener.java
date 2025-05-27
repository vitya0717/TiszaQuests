package org.vitya0717.tiszaQuests.listeners.objective;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quests.Quest;
import org.vitya0717.tiszaQuests.quests.objectives.Objective;
import org.vitya0717.tiszaQuests.quests.objectives.ObjectiveType;
import org.vitya0717.tiszaQuests.quests.objectives.PlaceBlocks;
import org.vitya0717.tiszaQuests.quests.playerProfile.PlayerProfileManager;
import org.vitya0717.tiszaQuests.quests.playerProfile.QuestPlayerProfile;

import java.util.*;

public class BlockPlaceListener implements Listener {


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Material material = event.getBlock().getType();
        PlayerProfileManager manager = Main.profileManager;

        if (!manager.allLoadedProfile.containsKey(player.getUniqueId())) return;

        QuestPlayerProfile profile = manager.allLoadedProfile.get(player.getUniqueId());

        HashMap<String, Quest> playerQuests = new HashMap<>(profile.getActiveQuests());

        if (playerQuests.isEmpty()) {
            return;
        }

        if (Main.questManager.hasObjectiveType(playerQuests, ObjectiveType.PLACE_BLOCKS) && !ObjectivesContainsBlock(playerQuests, material)) {
            player.sendMessage("Nincs benne");
            return;
        }

        for (Map.Entry<String, Quest> entry :  playerQuests.entrySet()) {
            Quest quest = entry.getValue();
            for (String key : quest.getObjectives().keySet()) {

                PlaceBlocks placeObjective = (PlaceBlocks) quest.getObjective(key);

                if (placeObjective.isFinishedObjective()) continue;

                if (placeObjective.getBlockType().equals(material)) {
                    placeObjective.progress(key, quest.getId(), player);
                }
            }
        }
    }

    private boolean ObjectivesContainsBlock(HashMap<String, Quest> activeQuests, Material material) {
        for (Map.Entry<String, Quest> questEntry : activeQuests.entrySet()) {
            Quest quest = questEntry.getValue();
            for (Map.Entry<String, Objective> entry : quest.getObjectives().entrySet()) {
                Objective obj = entry.getValue();
                PlaceBlocks placeObj = (PlaceBlocks) obj;
                if (placeObj.getBlockType().equals(material)) {
                    return true;
                }
            }
        }

        return false;
    }
}
