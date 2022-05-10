package me.yukun.rankquests.config;

import org.bukkit.configuration.file.FileConfiguration;

public class Config {
  private static FileConfiguration configFile = FileManager.getInstance().config;

  public static void reload() {
    configFile = FileManager.getInstance().reloadConfig();
  }

  private static boolean getBoolean(String path) {
    return configFile.getBoolean(path);
  }

  /**
   * Checks whether to drop rank quest on player disconnect.
   *
   * @return Whether to drop rank quest on player disconnect.
   */
  public static boolean dropOnDC() {
    return getBoolean("DropOnDC");
  }

  /**
   * Checks whether to disable flying on rank quest start.
   *
   * @return Whether to disable flying on rank quest start.
   */
  public static boolean disableFly() {
    return getBoolean("DisableFly");
  }
}
