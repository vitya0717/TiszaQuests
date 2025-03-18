package org.vitya0717.tiszaQuests.utils;

import org.bukkit.ChatColor;
import org.vitya0717.tiszaQuests.quests.Quest;

import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static String id;

    public static String Placeholders(Quest quest, String s) {
        s = ChatColor.translateAlternateColorCodes('&', s);
        s = s.replace("%prefix%", Text.PREFIX);
        
        if(quest != null) {
            s = s.replace("%quest_name%", quest.getName());
            s = s.replace("%quest_required_placed_blocks%", quest.getObjective().getRequiredBlocksCount()+"");
            s = s.replace("%quest_placed_blocks%", quest.getObjective().getPlacedBlocksCount()+"");
        }
        return s;
    }

    public static String Colorize(String s) {
        s = ChatColor.translateAlternateColorCodes('&', s);
        s = s.replace("%prefix%", Text.PREFIX);
        return s;
    }

    public static List<String> Placeholders(List<String> s) {
        s = s.stream().map(c -> ChatColor.translateAlternateColorCodes('&', c)).collect(Collectors.toList());
        s = s.stream().map(c -> c.replace("%prefix%", Text.PREFIX)).collect(Collectors.toList());

        return s;
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Utils.id = id;
    }
}
