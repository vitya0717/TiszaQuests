package org.vitya0717.tiszaQuests.utils;

import org.bukkit.ChatColor;
import org.vitya0717.tiszaQuests.quest.Quest;
import org.vitya0717.tiszaQuests.quest.objectives.PlaceBlocks;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalField;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {

    private static String id;

    public static String Placeholders(Quest quest, String s) {
        s = ChatColor.translateAlternateColorCodes('&', s);
        s = s.replace("%prefix%", Text.PREFIX);

        if (quest != null) {
            s = s.replace("%quest_name%", quest.getDisplayName());
            s = s.replace("%quest_delay_time_left_" + quest.getId() + "%", Colorize(LocalDateTime.now().toString()));
            for (String key : quest.getObjectives().keySet()) {
                if (quest.getObjective(key) != null) {

                    s = s.replace("%quest_required_placed_blocks_" + key + "%", ((PlaceBlocks) quest.getObjective(key)).getRequiredBlocksCount() + "");
                    s = s.replace("%quest_placed_blocks_" + key + "%", ((PlaceBlocks) quest.getObjective(key)).getPlacedBlocksCount() + "");
                    s = s.replace("%quest_placed_block_type_" + key + "%", ((PlaceBlocks) quest.getObjective(key)).getPlacedBlocksCount() + "");
                    s = s.replace("%quest_objective_display_name_" + key + "%", Colorize(quest.getObjective(key).getDisplayName()));
                }
            }
        }
        return s;
    }

    public static String Colorize(String s) {
        s = ChatColor.translateAlternateColorCodes('&', s);
        s = s.replace("%prefix%", Text.PREFIX);
        return s;
    }

    public static List<String> Placeholders(Quest quest, List<String> s) {
        s = s.stream().map(c -> ChatColor.translateAlternateColorCodes('&', c)).collect(Collectors.toList());
        s = s.stream().map(c -> c.replace("%prefix%", Text.PREFIX)).collect(Collectors.toList());
        s = s.stream().map(c -> c.replace("%quest_delay_time_left_" + quest.getId() + "%", Colorize(timeFormatter(quest.getQuestBaseDelay())))).collect(Collectors.toList());
        for (String key : quest.getObjectives().keySet()) {
            if (quest.getObjective(key) != null) {
                s = s.stream().map(c -> c.replace("%quest_required_placed_blocks_" + key + "%", ((PlaceBlocks) quest.getObjective(key)).getRequiredBlocksCount() + "")).collect(Collectors.toList());
                s = s.stream().map(c -> c.replace("%quest_placed_blocks_" + key + "%", ((PlaceBlocks) quest.getObjective(key)).getPlacedBlocksCount() + "")).collect(Collectors.toList());
                s = s.stream().map(c -> c.replace("%quest_placed_block_type_" + key + "%", ((PlaceBlocks) quest.getObjective(key)).getBlockType().name())).collect(Collectors.toList());
                s = s.stream().map(c -> c.replace("%quest_objective_display_name_" + key + "%", Colorize(quest.getObjective(key).getDisplayName()))).collect(Collectors.toList());
            }
        }
        return s;
    }

    //dateTime = (current time in seconds + delay seconds) * 1000 to millisecond
    public static int calculateTimeLeft(LocalDateTime dateTime) {

        LocalDateTime localDateTime = LocalDateTime.now();
        long more = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        long less = localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

        if((more - less) <= 0) {
            return 0;
        }

        return Math.toIntExact((more - less) / 1000);
    }

    public static String timeFormatter(int s) {

        if(s <= 0) {
            return "No Delay";
        }
        int seconds = s % 60;
        int minutes = (s / 60) % 60;
        int hours = (s / 3600) % 24;
        int days = s / 86400;

        return String.format("%d days %d hours %d minutes %d seconds", days, hours, minutes, seconds);
    }



    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        Utils.id = id;
    }
}
