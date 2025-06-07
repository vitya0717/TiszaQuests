package org.vitya0717.tiszaQuests.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.vitya0717.tiszaQuests.main.Main;
import org.vitya0717.tiszaQuests.quest.Quest;
import org.vitya0717.tiszaQuests.utils.Text;
import org.vitya0717.tiszaQuests.utils.Utils;

import java.util.Arrays;

public class QuestCommands implements CommandExecutor {

    private Main instance;

    public QuestCommands(Main instance) {
        this.instance = instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if(args.length == 0) {
            try {
                if(sender.hasPermission("tiszaquest.default") || sender.hasPermission("tiszaquest.admin")) {
                    Main.questsPageManager.openQuestMenu((Player) sender);
                    return true;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        if(args.length >= 1) {
            if(args[0].equalsIgnoreCase("create")) {
                if(sender.hasPermission("tiszaquest.create") || sender.hasPermission("tiszaquest.admin")) {

                    // /quests create first Miner [nagyon fullos kis küldetés] STONE_PICKAXE
                    if(args.length == 1) {
                        sender.sendMessage(Utils.Colorize(Text.QUEST_ID_EMPTY));
                        return true;
                    }
                    String questId = args[1];

                    if(questId.isEmpty()) {
                        sender.sendMessage(Utils.Colorize(Text.EMPTY_ID));
                    }

                    //=================================================

                    if(args.length == 2) {
                        sender.sendMessage(Utils.Colorize(Text.QUEST_NAME_EMPTY));
                        return true;
                    }
                    String questName = args[2];
                    if(args.length == 3) {
                        sender.sendMessage(Utils.Colorize(Text.QUEST_DISPLAY_ITEM_EMPTY));
                        return true;
                    }
                    //=================================================

                    Material displayItem = Material.getMaterial(args[3].toUpperCase());
                    if(displayItem == null) {
                        sender.sendMessage(Utils.Colorize(Text.QUEST_DISPLAY_ITEM_WRONG));
                        return true;
                    }

                    //=================================================

                    Quest tempQuest = new Quest(questId, questName, new ItemStack(displayItem), -1, 0);

                    Main.questManager.registerQuest(tempQuest);
                    Main.questManager.saveQuest(tempQuest);

                    sender.sendMessage(Utils.Colorize(Text.CREATE_QUEST));
                    return true;
                }
            }
        }

        return true;
    }
}
