package org.vitya0717.tiszaQuests.quests.playerProfile;

import org.vitya0717.tiszaQuests.main.Main;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class PlayerProfileManager {

    private final Main instance;
    public final HashMap<UUID, QuestPlayerProfile> allLoadedProfile = new HashMap<>();

    public PlayerProfileManager(Main instance) {
        this.instance = instance;
    }

    public void registerPlayerProfile(QuestPlayerProfile profile) {
        if (allLoadedProfile.keySet().stream().noneMatch(s -> Objects.equals(s, profile.getPlayerUUID()))) {
            allLoadedProfile.put(profile.getPlayerUUID(), profile);
        }
    }

    public void loadProfile(UUID uniqueId) {

    }

    public void saveProfile(UUID uniqueId) {

    }

}
