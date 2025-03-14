package org.vitya0717.tiszaQuests.main;

import org.bukkit.plugin.java.JavaPlugin;
import org.vitya0717.tiszaQuests.commands.QuestCommands;
import org.vitya0717.tiszaQuests.configuration.CustomConfig;
import org.vitya0717.tiszaQuests.quests.QuestManager;

public final class Main extends JavaPlugin {

    public static QuestManager questManager;
    public static CustomConfig questConfig;

    public static Main instance;

    @Override
    public void onEnable() {

        saveDefaultConfig();

        instance = this;

        questConfig = new CustomConfig();
        questConfig.saveDefaultConfig();

        questManager = new QuestManager(instance);

        registerCommands(instance);

        questManager.loadQuests();

        System.out.println("A kuldetes plugin elindult!");

    }

    private void registerCommands(Main instance) {
        getCommand("quests").setExecutor(new QuestCommands(instance));
    }

    @Override
    public void onDisable() {
        System.out.println("A keldetes plugin leallt");
    }
}
