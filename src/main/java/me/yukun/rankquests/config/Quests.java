package me.yukun.rankquests.config;

import java.util.ArrayList;
import java.util.List;
import me.yukun.rankquests.chat.TextFormatter;
import me.yukun.rankquests.exception.InvalidMaterialException;
import me.yukun.rankquests.quest.RankQuest;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Quests {
  private static FileConfiguration questsFile = FileManager.getInstance().quests;

  public static void reload() {
    questsFile = FileManager.getInstance().reloadQuests();
    RankQuest.onEnable();
  }

  public static List<String> getAllRanks() {
    return getConfigurationSection("");
  }

  public static int getDuration(String rank) {
    return getInteger(rank + ".Time");
  }

  public static List<Boolean> getRegionCheckToggleList(String rank) {
    List<Boolean> regionCheckToggleList = new ArrayList<>();
    boolean warzone = getBoolean(rank + ".CheckWarzone");
    boolean worldguard = getBoolean(rank + ".CheckWorldGuard");
    boolean blacklist = getBoolean(rank + ".CheckBlacklist");
    boolean pvpFlag = getBoolean(rank + ".CheckPvPFlag");
    regionCheckToggleList.add(warzone);
    regionCheckToggleList.add(worldguard);
    regionCheckToggleList.add(blacklist);
    regionCheckToggleList.add(pvpFlag);
    return regionCheckToggleList;
  }

  public static List<String> getRegionList(String rank) {
    return getStringList(rank + ".Regions");
  }

  public static List<String> getRegionBlacklist(String rank) {
    return getStringList(rank + ".RegionBlacklist");
  }

  public static ItemStack getQuestItem(String rank, int amount, Player player)
      throws InvalidMaterialException {
    if (amount == 0) {
      return new ItemStack(Material.AIR);
    }
    Material material = getQuestItemType(rank);
    if (material == null) {
      throw new InvalidMaterialException("Quests.yml", rank + ".ItemType");
    }
    ItemStack questItem = new ItemStack(material, amount);
    setQuestItemMeta(questItem, rank, player);
    return questItem;
  }

  public static ItemStack getCdQuestItem(String rank, Player player, int time)
      throws InvalidMaterialException {
    Material material = getQuestItemType(rank);
    if (material == null) {
      throw new InvalidMaterialException("Quests.yml", rank + ".ItemType");
    }
    ItemStack cdQuestItem = new ItemStack(material, 1);
    setCdQuestItemMeta(cdQuestItem, rank, player, time);
    return cdQuestItem;
  }

  public static ItemStack getVoucherItem(String rank, int amount) throws InvalidMaterialException {
    Material material = getVoucherItemType(rank);
    if (material == null) {
      throw new InvalidMaterialException("Quests.yml", rank + ".Voucher.ItemType");
    }
    ItemStack voucherItem = new ItemStack(material, amount);
    setVoucherItemMeta(voucherItem, rank);
    return voucherItem;
  }

  private static void setQuestItemMeta(ItemStack item, String rank, Player player) {
    String name = ".Name";
    String lore = ".Lore";
    String configName = getString(rank + name);
    List<String> configLore = getStringList(rank + lore);
    String dname = getFormattedString(configName, rank, player, null);
    List<String> dlore = new ArrayList<>();
    for (String line : configLore) {
      dlore.add(getFormattedString(line, rank, player, null));
    }
    ItemMeta itemMeta = item.getItemMeta();
    assert itemMeta != null;
    itemMeta.setDisplayName(dname);
    itemMeta.setLore(dlore);
    item.setItemMeta(itemMeta);
  }

  private static void setCdQuestItemMeta(ItemStack item, String rank, Player player, int time) {
    String name = ".CdName";
    String lore = ".CdLore";
    String configName = getString(rank + name);
    List<String> configLore = getStringList(rank + lore);
    String dname = getFormattedString(configName, rank, player, time);
    List<String> dlore = new ArrayList<>();
    for (String line : configLore) {
      dlore.add(getFormattedString(line, rank, player, time));
    }
    ItemMeta itemMeta = item.getItemMeta();
    assert itemMeta != null;
    itemMeta.setDisplayName(dname);
    itemMeta.setLore(dlore);
    item.setItemMeta(itemMeta);
  }

  private static void setVoucherItemMeta(ItemStack item, String rank) {
    String name = ".Voucher.Name";
    String lore = ".Voucher.Lore";
    String configName = getString(rank + name);
    List<String> configLore = getStringList(rank + lore);
    String dname = getFormattedString(configName, rank, null, null);
    List<String> dlore = new ArrayList<>();
    for (String line : configLore) {
      dlore.add(getFormattedString(line, rank, null, null));
    }
    ItemMeta itemMeta = item.getItemMeta();
    assert itemMeta != null;
    itemMeta.setDisplayName(dname);
    itemMeta.setLore(dlore);
    item.setItemMeta(itemMeta);
  }

  public static List<String> getVoucherCommandList(String rank) {
    return getStringList(rank + ".Voucher.Commands");
  }

  public static String getFormattedString(String message, String rank, Player player,
                                          Integer time) {
    String result = message.replaceAll("%rank%", getFormattedRank(rank));
    if (player != null) {
      result = Messages.replacePlayerName(result, player);
    }
    if (time != null) {
      result = result.replaceAll("%time%", time + "");
    }
    return TextFormatter.color(result);
  }

  public static String replaceRankName(String message, String rank) {
    return message.replaceAll("%rank%", getFormattedRank(rank));
  }

  private static String getString(String path) {
    return questsFile.getString(path);
  }

  private static List<String> getStringList(String path) {
    return questsFile.getStringList(path);
  }

  private static boolean getBoolean(String path) {
    return questsFile.getBoolean(path);
  }

  private static int getInteger(String path) {
    return questsFile.getInt(path);
  }

  @SuppressWarnings("ConstantConditions")
  private static List<String> getConfigurationSection(String path) {
    if (!questsFile.isConfigurationSection(path)) {
      return new ArrayList<>();
    }
    return new ArrayList<>(questsFile.getConfigurationSection(path).getKeys(false));
  }

  private static Material getQuestItemType(String rank) {
    String itemtype = ".ItemType";
    String configMaterial = getString(rank + itemtype);
    return Material.getMaterial(configMaterial);
  }

  private static Material getVoucherItemType(String rank) {
    String itemtype = ".Voucher.ItemType";
    String configMaterial = getString(rank + itemtype);
    return Material.getMaterial(configMaterial);
  }

  private static String getFormattedRank(String rank) {
    String rankname = getString(rank + ".RankName");
    return TextFormatter.color(rankname);
  }
}
