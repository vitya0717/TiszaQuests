package org.vitya0717.tiszaQuests.quest;

import org.bukkit.inventory.ItemStack;
import org.vitya0717.tiszaQuests.quest.objectives.Objective;
import org.vitya0717.tiszaQuests.quest.objectives.ObjectiveType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Quest implements Cloneable {

    private final String id;

    private String displayName;
    private List<String> description;
    private ItemStack displayItem;
    private ObjectiveType type;
    private HashMap<String,Objective> objectives;
    private List<String> rewards;
    private boolean repeatable;
    private boolean active;
    private int questItemSlotNumber;
    private int questDelay;


    public Quest(String id, String displayName, ItemStack displayItem, int questItemSlotNumber, int questDelay) {
        this.id = id;
        this.displayName = displayName;
        this.displayItem = displayItem;
        this.rewards = null;
        this.repeatable = true;
        this.active = true;
        this.questItemSlotNumber = questItemSlotNumber;
        this.questDelay = questDelay;
    }

    public String getId() {
        return id;
    }


    public String getName() {
        return displayName;
    }
    public void setName(String name) {
        this.displayName = name;
    }


    public List<String> getDescription() {
        return description;
    }
    public void setDescription(List<String> description) {
        this.description = description;
    }


    public List<String> getRewards() {
        return rewards;
    }
    public void setRewards(List<String> rewards) {
        this.rewards = rewards;
    }


    public boolean isRepeatable() {
        return repeatable;
    }
    public void setRepeatable(boolean repeatable) {
        this.repeatable = repeatable;
    }


    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public ItemStack getDisplayItem() {
        return displayItem;
    }
    public void setDisplayItem(ItemStack displayItem) {
        this.displayItem = displayItem;
    }


    public HashMap<String, Objective> getObjectives() {
        return objectives;
    }
    public void setObjective(HashMap<String, Objective> objectives) {
        this.objectives = objectives;
    }


    public int getQuestDelay() {
        return questDelay;
    }
    public void setQuestDelay(int questDelay) {
        this.questDelay = questDelay;
    }

    public int getItemSlot() {
        return questItemSlotNumber;
    }
    public void setQuestItemSlotNumber(int questItemSlotNumber) {this.questItemSlotNumber=questItemSlotNumber;}


    public Objective getObjective(String objectiveId) {
        if(objectives.containsKey(objectiveId)) {
            return objectives.get(objectiveId);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Quest{" +
                "id='" + id + '\'' +
                ", name='" + displayName + '\'' +
                ", name='" + displayName + '\'' +
                ", displayItem=" + displayItem.getType() +
                ", type=" + type +
                ", objectives=" + objectives +
                ", rewards=" + rewards +
                ", repeatable=" + repeatable +
                ", active=" + active +
                '}';
    }

    @Override
    public Quest clone() {
        try {
            Quest clone = (Quest) super.clone();

            clone.setObjective(null);
            HashMap<String, Objective> oldHash = new HashMap<>(this.objectives);
            HashMap<String, Objective> newHash = new HashMap<>(this.objectives);
            for (Map.Entry<String, Objective> obj : oldHash.entrySet()) {
                newHash.put(obj.getKey(), obj.getValue().clone());
            }
            clone.setObjective(newHash);
            clone.description = new ArrayList<>(this.description);
            //clone.rewards = new ArrayList<>(this.rewards);

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
