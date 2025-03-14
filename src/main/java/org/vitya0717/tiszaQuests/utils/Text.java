package org.vitya0717.tiszaQuests.utils;

import org.bukkit.ChatColor;

public class Text {

    public static final String PREFIX = ChatColor.translateAlternateColorCodes('&',"&8[&fTalpra, Fiatalok&8] »");
    public static final String GUI_TITLE = "              &6&lKüldetések";
    public static final String OPEN_QUEST_MENU = "%prefix% &6Megnyitottad a küldetés választót.";

    public static final String QUEST_ID_EMPTY = "%prefix% &cAdj meg egy egyedi küldetés ID-t(pl: quest1)";
    public static final String QUEST_NAME_EMPTY = "%prefix% &cAdj meg egy küldetés nevet(pl: miner)";
    public static final String EMPTY_ID = "%prefix% &cÜres nem lehet az ID!";
    public static final String CREATE_QUEST = "%prefix% &6Sikeresen létrehoztad a &f'%current_quest%' &6küldetést.";
    public static final String QUEST_DISPLAY_ITEM_EMPTY = "%prefix% &cAdd meg a tárgyat, ami megjelenik a menüben!";
    public static final String QUEST_DISPLAY_ITEM_WRONG = "%prefix% &cÉrvényes tárgyat adj meg!";
}
