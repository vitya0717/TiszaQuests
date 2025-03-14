package org.vitya0717.tiszaQuests.quests;

import org.bukkit.inventory.ItemStack;
import org.vitya0717.tiszaQuests.quests.objectives.Objective;

import java.util.List;

public class Quest {

    private String id;
    private String name;
    private String description;
    private ItemStack displayItem;
    private QuestType type;
    private Objective objectives;
    private List<String> rewards;
    private boolean repeatable;
    private boolean active;


    public Quest(String id, String name, String description, ItemStack displayItem, QuestType type, Objective objectives, List<String> rewards, boolean repeatable, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.displayItem = displayItem;
        this.type = type;
        this.objectives = objectives;
        this.rewards = rewards;
        this.repeatable = repeatable;
        this.active = active;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
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
}
