package org.vitya0717.tiszaQuests.listeners.objective;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quest.Quest;
import org.vitya0717.tiszaQuests.quest.objectives.parent.Objective;
import org.vitya0717.tiszaQuests.quest.objectives.enums.ObjectiveType;
import org.vitya0717.tiszaQuests.quest.objectives.PlaceBlocks;
import org.vitya0717.tiszaQuests.quest.playerProfile.PlayerProfileManager;
import org.vitya0717.tiszaQuests.quest.playerProfile.QuestPlayerProfile;

import java.util.*;
import java.util.stream.Collectors;

public class BlockPlaceListener implements Listener {


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Material material = event.getBlock().getType();
        PlayerProfileManager profileManager = Main.profileManager;

        if (!profileManager.allLoadedProfile.containsKey(player.getUniqueId())) return;
        QuestPlayerProfile profile = profileManager.allLoadedProfile.get(player.getUniqueId());

        HashMap<String, Quest> playerQuests = new HashMap<>(profile.getActiveQuests());

        if (playerQuests.isEmpty()) {
            return;
        }

        HashMap<String, Quest> filteredQuests = (HashMap<String, Quest>) playerQuests.entrySet().stream()
                .filter(entry -> entry.getValue().hasObjectiveType(ObjectiveType.PLACE_BLOCKS)
                        && ObjectivesContainsBlock(entry.getValue().getObjectives(), material))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        for (Map.Entry<String, Quest> entry :  filteredQuests.entrySet()) {
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

    private boolean ObjectivesContainsBlock(HashMap<String, Objective> objectives, Material material) {
            for (Map.Entry<String, Objective> entry : objectives.entrySet()) {
                Objective obj = entry.getValue();
                PlaceBlocks placeObj = (PlaceBlocks) obj;
                if (placeObj.getBlockType().equals(material)) {
                    return true;
                }
            }
        return false;
    }
}
