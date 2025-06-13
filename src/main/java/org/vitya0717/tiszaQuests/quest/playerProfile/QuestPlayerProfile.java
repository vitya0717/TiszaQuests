package org.vitya0717.tiszaQuests.quest.playerProfile;

import org.bukkit.scheduler.BukkitTask;
import org.vitya0717.tiszaQuests.quest.Quest;

import java.io.Serializable;
import java.util.*;

public class QuestPlayerProfile implements Serializable {

    private UUID playerUUID;
    private int questPoints;
    private int currentPageOn = 1;

    private final HashMap<String, Quest> completedQuests = new HashMap<>();
    private final Set<String> completedQuestsIds = new HashSet<>();

    private final HashMap<String, Quest> activeQuests = new HashMap<>();
    private final Set<String> activeQuestIds = new HashSet<>();

    private final HashMap<String, BukkitTask> questDelays = new HashMap<>();

    private boolean isFirstQuestMenuOpen = true;
    private boolean questInventoryNeedsUpdate = false;

    public QuestPlayerProfile(UUID playerUUID) {
        this.playerUUID = playerUUID;
        this.questPoints = 0;
    }

    public int getQuestPoints() {
        return questPoints;
    }

    public void setQuestPoints(int questPoints) {
        this.questPoints = questPoints;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public HashMap<String, Quest> getActiveQuests() {
        return activeQuests;
    }

    public HashMap<String, Quest> getCompletedQuests() {
        return completedQuests;
    }

    public Set<String> getActiveQuestIds() {
        return activeQuestIds;
    }

    @Override
    public String toString() {
        return "QuestPlayerProfile{" +
                "playerUUID=" + playerUUID +
                ", questPoints=" + questPoints +
                ", completedQuests=" + completedQuests +
                ", activeQuests=" + activeQuests +
                '}';
    }

    public Quest findActiveQuestByQuestId(String questId) {
        for (String qId : this.getActiveQuestIds()) {
            if (Objects.equals(qId, questId)) {
                return this.getActiveQuests().get(qId);
            }
        }
        return null;
    }

    public HashMap<String, BukkitTask> getQuestDelays() {
        return questDelays;
    }

    public Set<String> getCompletedQuestsIds() {
        return completedQuestsIds;
    }

    public int getCurrentPageOn() {
        return currentPageOn;
    }

    public void setCurrentPageOn(int currentPage) {
        this.currentPageOn = currentPage;
    }

    public boolean isFirstQuestMenuOpen() {
        return isFirstQuestMenuOpen;
    }

    public void setFirstQuestMenuOpen(boolean firstQuestMenuOpen) {
        isFirstQuestMenuOpen = firstQuestMenuOpen;
    }

    public boolean questInventoryNeedsUpdate() {
        return questInventoryNeedsUpdate;
    }

    public void setQuestInventoryNeedsUpdate(boolean questInventoryNeedsUpdate) {
        this.questInventoryNeedsUpdate = questInventoryNeedsUpdate;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<>();

        data.put("playerUUID",this.playerUUID);
        data.put("questPoints",this.questPoints);
        data.put("currentPageOn",this.currentPageOn);
        data.put("completedQuests", serializeQuests(completedQuests));
        data.put("completedQuestsIds",this.completedQuestsIds);
        data.put("activeQuests",serializeQuests(activeQuests));
        data.put("activeQuestIds",this.activeQuestIds);
        data.put("delayedQuests", serializeDelays());
        data.put("isFirstQuestMenuOpen",this.isFirstQuestMenuOpen);
        data.put("questInventoryNeedsUpdate",this.questInventoryNeedsUpdate);

        return data;
    }

    private Map<String, Object> serializeDelays() {
        Map<String, Object> data = new HashMap<>();
        if(questDelays != null || !questDelays.isEmpty()) {
            data.putAll(this.questDelays);
        }
        return data;
    }

    private Map<String, Object> serializeQuests(HashMap<String, Quest> quests) {
        Map<String, Object> data = new HashMap<>();
        if (quests != null || !quests.isEmpty()) {
            for (Map.Entry<String, Quest> questEntry : quests.entrySet()) {
                data.put(questEntry.getKey(), questEntry.getValue().serialize());
            }
        }
        return data;
    }
}
