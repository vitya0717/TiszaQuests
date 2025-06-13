package org.vitya0717.tiszaQuests.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.vitya0717.tiszaQuests.commands.QuestCommands;
import org.vitya0717.tiszaQuests.configuration.CustomConfig;
import org.vitya0717.tiszaQuests.configuration.player.PlayerConfig;
import org.vitya0717.tiszaQuests.configuration.player.PlayerConfigManager;
import org.vitya0717.tiszaQuests.listeners.inventory.ButtonClickListener;
import org.vitya0717.tiszaQuests.listeners.inventory.InventoryListener;
import org.vitya0717.tiszaQuests.listeners.PlayerJoinListener;
import org.vitya0717.tiszaQuests.listeners.objective.BlockPlaceListener;
import org.vitya0717.tiszaQuests.quest.QuestManager;
import org.vitya0717.tiszaQuests.quest.exceptions.InvalidQuestConfigurationException;
import org.vitya0717.tiszaQuests.quest.exceptions.InvalidQuestObjectiveConfigurationException;
import org.vitya0717.tiszaQuests.quest.inventory.QuestsPageManager;
import org.vitya0717.tiszaQuests.quest.playerProfile.PlayerProfileManager;

import java.util.ArrayList;
import java.util.List;

public final class Main extends JavaPlugin {

    public static QuestManager questManager;
    public static QuestsPageManager questsPageManager;
    public static PlayerProfileManager profileManager;
    public static CustomConfig questConfig;
    public static PlayerConfigManager playerConfigManager;

    public static Main instance;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        instance = this;

        questConfig = new CustomConfig();
        questConfig.saveDefaultConfig();

        questManager = new QuestManager(instance);
        questsPageManager = new QuestsPageManager(instance);
        profileManager = new PlayerProfileManager(instance);
        playerConfigManager = new PlayerConfigManager();

        registerCommands();
        registerEvents(instance);

        getLogger().info("Loading quests...");
            try {
                questManager.loadQuests();
                getLogger().info("Successfully loaded quests.");
            } catch (InvalidQuestConfigurationException | InvalidQuestObjectiveConfigurationException e) {
                getLogger().severe("Failed to load a quests");
                getLogger().severe(e.getMessage());
            }

        getLogger().info("Loading online players profile...");
        for(Player online : Bukkit.getOnlinePlayers()) {
            profileManager.loadProfile(online.getUniqueId());

        }
        getLogger().info("Successfully loaded "+profileManager.allLoadedProfile.size()+" online profile.");

        getLogger().info("The quest plugin enabled on this server!");

    }

    private void registerCommands() {
        getCommand("quests").setExecutor(new QuestCommands());
    }

    private void registerEvents(Main instance) {
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), instance);
        Bukkit.getPluginManager().registerEvents(new ButtonClickListener(), instance);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), instance);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), instance);
    }

    @Override
    public void onDisable() {
        System.out.println("A kuldetes plugin leallt");
    }
}
