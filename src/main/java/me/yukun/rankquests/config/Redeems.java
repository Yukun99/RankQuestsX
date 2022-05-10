package me.yukun.rankquests.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Redeems {
  private static FileConfiguration redeemsFile = FileManager.getInstance().redeems;

  public static void reload() {
    redeemsFile = FileManager.getInstance().reloadRedeems();
  }

  /**
   * Gets the int at the specified path. Due to this plugin not expecting negative values, default
   * value for values that do not exist will default to -1.
   *
   * @param path Path to fetch the int from.
   * @return Integer at the specified path.
   */
  private static int getInt(String path) {
    return redeemsFile.getInt(path, -1);
  }

  @SuppressWarnings("ConstantConditions")
  private static List<String> getConfigurationSection(String path) {
    if (!redeemsFile.isConfigurationSection(path)) {
      return new ArrayList<>();
    }
    return new ArrayList<>(redeemsFile.getConfigurationSection(path).getKeys(false));
  }

  private static void setInt(String path, int value) {
    redeemsFile.set(path, value);
    FileManager.getInstance().saveRedeems(redeemsFile);
  }

  public static Map<String, Integer> getRankQuestAmountMap(Player player) {
    String uuid = player.getUniqueId().toString();
    Map<String, Integer> rankAmountMap = new HashMap<>();
    for (String rank : getConfigurationSection(uuid + ".Quests")) {
      int amount = getInt(uuid + ".Quests." + rank);
      rankAmountMap.put(rank, amount);
    }
    return rankAmountMap;
  }

  public static Map<String, Integer> getVoucherAmountMap(Player player) {
    String uuid = player.getUniqueId().toString();
    Map<String, Integer> rankAmountMap = new HashMap<>();
    for (String rank : getConfigurationSection(uuid + ".Vouchers")) {
      int amount = getInt(uuid + ".Vouchers." + rank);
      rankAmountMap.put(rank, amount);
    }
    return rankAmountMap;
  }

  public static void addQuest(Player player, String rank, int amount) {
    String path = player.getUniqueId() + ".Quests." + rank;
    if (getInt(path) != -1) {
      int current = getInt(path);
      amount += current;
    }
    setInt(path, amount);
  }

  public static void addVoucher(Player player, String rank, int amount) {
    String path = player.getUniqueId() + ".Vouchers." + rank;
    if (getInt(path) != -1) {
      int current = getInt(path);
      amount += current;
    }
    setInt(path, amount);
  }

  public static void setQuest(Player player, String rank, int amount) {
    String path = player.getUniqueId() + ".Quests." + rank;
    setInt(path, amount);
  }

  public static void setVoucher(Player player, String rank, int amount) {
    String path = player.getUniqueId() + ".Vouchers." + rank;
    setInt(path, amount);
  }
}
