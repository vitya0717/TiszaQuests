package org.vitya0717.tiszaQuests.quest.objectives;

import org.bukkit.entity.Player;
import org.vitya0717.tiszaQuests.quest.Quest;

public abstract class Objective implements Cloneable {

    private final String questId;
    private final String objectiveId;
    private final ObjectiveType type;
    private String displayName;
    private boolean finishedObjective = false;

    public Objective(String objectiveId, String questId, String displayName, ObjectiveType type) {
        this.objectiveId = objectiveId;
        this.questId = questId;
        this.displayName = displayName;
        this.type = type;
    }

    public abstract void progress(String objectiveId, String questId, Player player);

    public abstract void finishObjective(String objectiveId, String questId, Player player);

    public abstract void finishQuest(Quest quest, Player player);

    public String getQuestId() {
        return questId;
    }

    public String getObjectiveId() {
        return objectiveId;
    }

    public ObjectiveType getType() {
        return type;
    }

    public boolean isFinishedObjective() {
        return finishedObjective;
    }

    public void finishObjective(boolean finishedObjective) {
        this.finishedObjective = finishedObjective;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public Objective clone() {
        try {
            return (Objective) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
