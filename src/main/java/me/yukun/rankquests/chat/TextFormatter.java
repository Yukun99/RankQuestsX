package me.yukun.rankquests.chat;

import org.bukkit.ChatColor;

public class TextFormatter {

  public static String color(String msg) {
    return ChatColor.translateAlternateColorCodes('&', msg);
  }
}
