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

        HashMap<String, Quest> placeQuests = new HashMap<>();

        for (Quest q : profile.getActiveQuests()) {
            if (!placeQuests.containsKey(q.getId())) {
                for (Map.Entry<String, Objective> obj : q.getObjectives().entrySet()) {
                    Objective objective = obj.getValue();
                    if (objective.getType().equals(ObjectiveType.PLACE_BLOCKS)) {
                        placeQuests.put(q.getId(), q);
                        System.out.println("[DEBUG] Place quest hozzáadva");
                    }
                }
            }
        }

        for (Map.Entry<String, Quest> q : placeQuests.entrySet()) {
            Quest quest = q.getValue();
            for (String key : quest.getObjectives().keySet()) {
                if (quest.getObjective(key).getBlockType().equals(material)) {
                    quest.getObjective(key).progress(key, q.getValue(), player);
                }
            }
        }
    }
}
