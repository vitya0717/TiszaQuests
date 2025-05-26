package org.vitya0717.tiszaQuests.main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.vitya0717.tiszaQuests.commands.QuestCommands;
import org.vitya0717.tiszaQuests.configuration.CustomConfig;
import org.vitya0717.tiszaQuests.listeners.InventoryListener;
import org.vitya0717.tiszaQuests.listeners.PlayerJoinListener;
import org.vitya0717.tiszaQuests.listeners.objective.BlockPlaceListener;
import org.vitya0717.tiszaQuests.quests.QuestManager;
import org.vitya0717.tiszaQuests.quests.playerProfile.PlayerProfileManager;

public final class Main extends JavaPlugin {

    public static QuestManager questManager;
    public static PlayerProfileManager profileManager;
    public static CustomConfig questConfig;

    public static Main instance;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        instance = this;

        questConfig = new CustomConfig();
        questConfig.saveDefaultConfig();

        questManager = new QuestManager(instance);
        profileManager = new PlayerProfileManager(instance);

        registerCommands(instance);
        registerEvents(instance);

        questManager.loadQuests();
        for(Player online : Bukkit.getOnlinePlayers()) {
            profileManager.loadProfile(online.getUniqueId());
        }

        System.out.println("A kuldetes plugin elindult!");

    }

    private void registerCommands(Main instance) {
        getCommand("quests").setExecutor(new QuestCommands(instance));
    }

    private void registerEvents(Main instance) {
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), instance);
        Bukkit.getPluginManager().registerEvents(new BlockPlaceListener(), instance);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), instance);
    }

    @Override
    public void onDisable() {
        System.out.println("A kuldetes plugin leallt");
    }
}
