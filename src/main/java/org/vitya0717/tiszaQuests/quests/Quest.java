package org.vitya0717.tiszaQuests.quests;

import org.bukkit.inventory.ItemStack;
import org.vitya0717.tiszaQuests.quests.objectives.Objective;

import java.util.List;

public class Quest implements Cloneable {

    private String id;
    private String name;
    private List<String> description;
    private ItemStack displayItem;
    private QuestType type;
    private Objective objective;
    private List<String> rewards;
    private boolean repeatable;
    private boolean active;
    private int questItemSlot;


    public Quest(String id, String name, List<String> description, ItemStack displayItem,int questItemSlot, QuestType type, Objective objective, List<String> rewards, boolean repeatable, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.displayItem = displayItem;
        this.type = type;
        this.objective = objective;
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

    public QuestType getType() {
        return type;
    }

    public void setType(QuestType type) {
        this.type = type;
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

    public Objective getObjective() {
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    @Override
    public String toString() {
        return "Quest{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", description=" + description +
                ", displayItem=" + displayItem.getType() +
                ", type=" + type +
                ", objective=" + objective +
                ", rewards=" + rewards +
                ", repeatable=" + repeatable +
                ", active=" + active +
                ", questItemSlot=" + questItemSlot +
                '}';
    }

    @Override
    public Quest clone() {
        try {
            return (Quest) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
