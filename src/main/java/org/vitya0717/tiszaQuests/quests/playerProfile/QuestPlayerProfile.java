package org.vitya0717.tiszaQuests.quests.playerProfile;

import org.vitya0717.tiszaQuests.quests.Quest;

import java.util.*;

public class QuestPlayerProfile {

    private UUID playerUUID;
    private int questPoints;
    private int completedQuestsCount;
    private int activeQuestCount;
    private boolean invNeedUpdate = false;
    private final List<Quest> completedQuests = new ArrayList<>();

    private final HashMap<String,Quest> activeQuests = new HashMap<>();
    private final Set<String> activeQuestIds = new HashSet<>();

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

    public void setCompletedQuestsCount(int completedQuestsCount) {
        this.completedQuestsCount = completedQuestsCount;
    }

    public int getActiveQuestCount() {
        return activeQuestCount;
    }

    public HashMap<String, Quest> getActiveQuests() {
        return activeQuests;
    }

    public void setActiveQuestCount(int activeQuestCount) {
        this.activeQuestCount = activeQuestCount;
    }

    public List<Quest> getCompletedQuests() {
        return completedQuests;
    }

    public QuestPlayerProfile(UUID playerUUID, int questPoints, int completedQuestsCount, int activeQuestCount) {
        this.playerUUID = playerUUID;
        this.questPoints = questPoints;
        this.completedQuestsCount = completedQuestsCount;
        this.activeQuestCount = activeQuestCount;
    }

    public boolean isInvNeedUpdate() {
        return invNeedUpdate;
    }

    public void setInvNeedUpdate(boolean invNeedUpdate) {
        this.invNeedUpdate = invNeedUpdate;
    }

    public Quest findActiveQuestByQuestId(String questId) {
        for (String qId : this.getActiveQuestIds()) {
            if (Objects.equals(qId, questId)) {
                return this.getActiveQuests().get(qId);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "QuestPlayerProfile{" +
                "playerUUID=" + playerUUID +
                ", questPoints=" + questPoints +
                ", completedQuestsCount=" + completedQuestsCount +
                ", activeQuestCount=" + activeQuestCount +
                ", invNeedUpdate=" + invNeedUpdate +
                ", completedQuests=" + completedQuests +
                ", activeQuests=" + activeQuests +
                '}';
    }

    public Set<String> getActiveQuestIds() {
        return activeQuestIds;
    }
}
