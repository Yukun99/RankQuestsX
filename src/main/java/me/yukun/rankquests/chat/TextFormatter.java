package me.yukun.rankquests.chat;

import org.bukkit.ChatColor;

public class TextFormatter {
  public static String color(String msg) {
    return ChatColor.translateAlternateColorCodes('&', msg);
  }

  public static String removeColor(String msg) {
    msg = ChatColor.stripColor(msg);
    return msg;
  }
}
