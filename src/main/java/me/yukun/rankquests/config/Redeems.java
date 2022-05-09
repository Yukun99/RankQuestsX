package me.yukun.rankquests.config;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;

public class Redeems {
  private static final FileConfiguration redeemsFile = FileManager.getInstance().redeems;

  private static int getInt(String path) {
    return redeemsFile.getInt(path);
  }

  @SuppressWarnings("ConstantConditions")
  private static List<String> getConfigurationSection(String path) {
    if (!redeemsFile.isConfigurationSection(path)) {
      return new ArrayList<>();
    }
    return new ArrayList<>(redeemsFile.getConfigurationSection(path).getKeys(false));
  }
}
