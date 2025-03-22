package org.vitya0717.tiszaQuests.quests;

import org.bukkit.inventory.ItemStack;
import org.vitya0717.tiszaQuests.quests.objectives.Objective;
import org.vitya0717.tiszaQuests.quests.objectives.ObjectiveType;

import java.util.HashMap;
import java.util.List;

public class Quest  {

    private String id;
    private String name;
    private List<String> description;
    private ItemStack displayItem;
    private ObjectiveType type;
    private HashMap<String,Objective> objectives;
    private List<String> rewards;
    private boolean repeatable;
    private boolean active;
    private int questItemSlot;


    public Quest(String id, String name, List<String> description, ItemStack displayItem, int questItemSlot, HashMap<String,Objective> objectives, List<String> rewards, boolean repeatable, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.displayItem = displayItem;
        this.objectives = objectives;
        this.rewards = rewards;
        this.repeatable = repeatable;
        this.active = active;
        this.questItemSlot = questItemSlot;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getQuestItemSlot() {
        return questItemSlot;
    }

    public void setQuestItemSlot(int questItemSlot) {
        this.questItemSlot = questItemSlot;
    }

    public HashMap<String, Objective> getObjectives() {
        return objectives;
    }

    public void setObjective(HashMap<String, Objective> objectives) {
        this.objectives = objectives;
    }

    @Override
    public String toString() {
        return "Quest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description=" + description +
                ", displayItem=" + displayItem.getType() +
                ", type=" + type +
                ", objectives=" + objectives +
                ", rewards=" + rewards +
                ", repeatable=" + repeatable +
                ", active=" + active +
                ", questItemSlot=" + questItemSlot +
                '}';
    }

    public Objective getObjective(String objectiveId) {
        if(objectives.containsKey(objectiveId)) {
            return objectives.get(objectiveId);
        }
        return null;
    }
}
