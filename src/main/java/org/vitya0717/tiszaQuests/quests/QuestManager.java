package org.vitya0717.tiszaQuests.quests;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quests.objectives.Objective;
import org.vitya0717.tiszaQuests.quests.objectives.PlaceBlocks;
import org.vitya0717.tiszaQuests.utils.Text;
import org.vitya0717.tiszaQuests.utils.Utils;

import java.util.*;

public class QuestManager {

    private final Main instance;
    public final HashMap<String, Quest> allQuests = new HashMap<>();

    private final HashMap<UUID, Inventory> questInventories = new HashMap<>();
    //public final HashMap<String, Utils> questUtils = new HashMap<>();

    public QuestManager(Main instance) {
        this.instance = instance;
    }

    public void CreateQuest(Quest quest) {
        if (allQuests.keySet().stream().noneMatch(s -> Objects.equals(s, quest.getId()))) {
            allQuests.put(quest.getId(), quest);
        }
    }

    public void openQuestMenu(Player player) {

        int rowSize = 6;
        int columnSize = 9;

        if (player != null) {
            if (!questInventories.containsKey(player.getUniqueId())) {
                questInventories.put(player.getUniqueId(), Bukkit.createInventory(null, rowSize * columnSize, Utils.Colorize(Text.GUI_TITLE)));

                Inventory inventory = questInventories.get(player.getUniqueId());

                ItemStack fillItem = new ItemStack(Material.DIRT, 1);
                ItemMeta itemMeta = fillItem.getItemMeta();

                fillItem.addUnsafeEnchantment(Enchantment.LURE, 100);

                assert itemMeta != null;

                itemMeta.setDisplayName(Utils.Colorize(" "));
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                fillItem.setItemMeta(itemMeta);

                for (int i = 0; i < rowSize; i++) {
                    for (int j = 0; j < columnSize; j++) {
                        int itemIndex = i * columnSize + j;
                        if (i == 0 || i == rowSize - 1 || j == 0 || j == columnSize - 1) {
                            fillItem.setType((itemIndex % 2 == 0) ? Material.BLACK_STAINED_GLASS_PANE : Material.WHITE_STAINED_GLASS_PANE);
                            inventory.setItem(itemIndex, fillItem);
                        }
                    }
                }
                int startQuestsIndex = 10;

                for (Map.Entry<String, Quest> questEntry : allQuests.entrySet()) {
                    Quest quest = questEntry.getValue();
                    if (startQuestsIndex == 43) {
                        System.out.println("Elfogytak a helyek");
                        break;
                    }
                    if (inventory.getItem(startQuestsIndex) == null) {
                        inventory.setItem(startQuestsIndex, quest.getDisplayItem());
                        startQuestsIndex++;
                    } else {
                        startQuestsIndex += 2;
                    }
                }
            }
            player.openInventory(questInventories.get(player.getUniqueId()));
            player.sendMessage(Utils.Colorize(Text.OPEN_QUEST_MENU));
        }
    }


    public void saveQuest(Quest quest) {
        Main.questConfig.getConfig().set("quests." + quest.getId(), null);
        Main.questConfig.getConfig().set("quests." + quest.getId() + ".name", (String) quest.getName());
        Main.questConfig.getConfig().set("quests." + quest.getId() + ".description", quest.getDescription());
        Main.questConfig.getConfig().set("quests." + quest.getId() + ".displayItem", null);
        Main.questConfig.getConfig().set("quests." + quest.getId() + ".displayItem.type", quest.getDisplayItem().getType().name());
        Main.questConfig.getConfig().set("quests." + quest.getId() + ".displayItem.displayName", quest.getName());
        Main.questConfig.getConfig().set("quests." + quest.getId() + ".displayItem.lore", Arrays.asList("Ez egy alap lore", "változtasd meg."));
        Main.questConfig.saveConfig();
    }

    public void loadQuests() {
        ConfigurationSection configurationSection = Main.questConfig.getConfig().getConfigurationSection("quests");
        FileConfiguration config = Main.questConfig.getConfig();
        if (configurationSection == null) {
            System.out.println("Nem sikerült betölteni a küldetéseket!");
            return;
        }
        try {
            for (String id : configurationSection.getKeys(false)) {
                String name = config.getString("quests." + id + ".name");
                boolean enable = config.getBoolean("quests." + id + ".enable");
                boolean repeate = config.getBoolean("quests." + id + ".repeatable");
                QuestType type = QuestType.valueOf(config.getString("quests." + id + ".questType"));
                Objective obj = null;
                switch (type) {
                    case PLACE_BLOCKS:
                        Material block = Material.getMaterial(Objects.requireNonNull(config.getString("quests." + id + ".objective.block")));
                        if (block == null) {
                            System.out.println("Hibás blokk betöltés!");
                            return;
                        }
                        int count = config.getInt("quests." + id + ".objective.count");
                        obj = new PlaceBlocks(id, block, count);
                        break;
                }
                String description = config.getString("quests." + id + ".description");
                ItemStack displayItem = new ItemStack(Objects.requireNonNull(Material.getMaterial(Objects.requireNonNull(config.getString("quests." + id + ".displayItem.type")))));
                ItemMeta itemMeta = displayItem.getItemMeta();
                itemMeta.setDisplayName(Utils.Placeholders(config.getString("quests." + id + ".displayItem.displayName")));
                itemMeta.setLore((Utils.Placeholders(config.getStringList("quests." + id + ".displayItem.lore"))));
                displayItem.setItemMeta(itemMeta);

                Quest temp = new Quest(id, name, description, displayItem, type, obj, null, repeate, enable);

                CreateQuest(temp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        System.out.println("Sikeresen betöltve " + allQuests.size() + " küldetés");
    }
}
