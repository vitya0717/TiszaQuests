package org.vitya0717.tiszaQuests.quest.playerProfile;

import org.bukkit.scheduler.BukkitTask;
import org.vitya0717.tiszaQuests.quest.Quest;

import java.util.*;

public class QuestPlayerProfile {

    private UUID playerUUID;
    private int questPoints;
    private final int completedQuestsCount;
    private final int activeQuestCount;
    private int currentPageOn = 1;
    //private boolean invNeedUpdate = false;

    private final HashMap<String, Quest> completedQuests = new HashMap<>();
    private final Set<String> completedQuestsIds = new HashSet<>();

    private final HashMap<String, Quest> activeQuests = new HashMap<>();
    private final Set<String> activeQuestIds = new HashSet<>();

    private final HashMap<String, BukkitTask> delayedQuests = new HashMap<>();

    public QuestPlayerProfile(UUID playerUUID, int questPoints, int completedQuestsCount, int activeQuestCount) {
        this.playerUUID = playerUUID;
        this.questPoints = questPoints;
        this.completedQuestsCount = completedQuestsCount;
        this.activeQuestCount = activeQuestCount;
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

    public int getCompletedQuestsCount() {
        return completedQuestsCount;
    }

    public int getActiveQuestCount() {
        return activeQuestCount;
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
                ", completedQuestsCount=" + completedQuestsCount +
                ", activeQuestCount=" + activeQuestCount +
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

    public HashMap<String, BukkitTask> getDelayedQuests() {
        return delayedQuests;
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
}
