package me.yukun.rankquests.quest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.yukun.rankquests.Main;
import me.yukun.rankquests.config.Config;
import me.yukun.rankquests.config.Messages;
import me.yukun.rankquests.config.Quests;
import me.yukun.rankquests.exception.InvalidMaterialException;
import me.yukun.rankquests.hooks.Support;
import me.yukun.rankquests.inventory.PlayerInventoryHandler;
import me.yukun.rankquests.voucher.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RankQuest {
  public static final Map<String, RankQuest> nameRankQuestMap = new HashMap<>();
  private static final Map<Player, Integer> playerQuestSlotMap = new HashMap<>();
  private static final Map<Player, Integer> playerQuestTimeMap = new HashMap<>();
  private static final Map<Player, Integer> playerQuestTimerMap = new HashMap<>();
  private static final Map<Player, String> playerQuestRankMap = new HashMap<>();

  private final String name;
  private final int duration;

  private final List<Boolean> regionCheckToggleList;
  private final List<String> regionList;
  private final List<String> regionBlacklist;

  private Voucher voucher;

  private RankQuest(String name, int duration, List<Boolean> regionCheckToggleList,
                    List<String> regionList, List<String> regionBlacklist) {
    this.name = name;
    this.duration = duration;
    this.regionCheckToggleList = regionCheckToggleList;
    this.regionList = regionList;
    this.regionBlacklist = regionBlacklist;
    this.voucher = new Voucher(name);
  }

  public static void onEnable() {
    for (String rank : Quests.getAllRanks()) {
      int duration = Quests.getDuration(rank);
      List<Boolean> regionCheckToggleList = Quests.getRegionCheckToggleList(rank);
      List<String> regionList = Quests.getRegionList(rank);
      List<String> regionBlacklist = Quests.getRegionBlacklist(rank);
      RankQuest rankQuest =
          new RankQuest(rank, duration, regionCheckToggleList, regionList, regionBlacklist);
      nameRankQuestMap.put(rank, rankQuest);
    }
  }

  /**
   * Gets corresponding tier of RankQuest from item.
   *
   * @param item   Item to get corresponding tier of RankQuest from.
   * @param player Player to get corresponding tier of RankQuest from.
   * @return RankQuest instance that corresponds to specified item.
   */
  public static RankQuest getRankQuestFromItem(ItemStack item, Player player) {
    for (String rank : nameRankQuestMap.keySet()) {
      if (isQuest(item, rank, player)) {
        return nameRankQuestMap.get(rank);
      }
    }
    return null;
  }

  private static boolean isQuest(ItemStack item, String rank, Player player) {
    ItemStack questItem = new ItemStack(Material.AIR);
    try {
      questItem = Quests.getQuestItem(rank, 1, player);
    } catch (InvalidMaterialException e) {
      e.printStackTrace();
    }
    return item.isSimilar(questItem);
  }

  /**
   * Checks whether a player is doing any rank quest.
   *
   * @param player Player to be checked.
   * @return Whether specified player is doing any rank quest.
   */
  public static boolean isDoingRankQuest(Player player) {
    return playerQuestSlotMap.containsKey(player);
  }

  /**
   * Checks whether an inventory slot in a player's inventory is occupied by a rank quest.
   *
   * @param player Player to be checked.
   * @param slot   Inventory slot to be checked.
   * @return Whether specified inventory slot is occupied by a rank quest.
   */
  public static boolean isQuestSlot(Player player, int slot) {
    return slot == playerQuestSlotMap.get(player);
  }

  /**
   * Interrupts a rank quest when it does not complete successfully.
   *
   * @param player      Player to interrupt the rank quest for.
   * @param doDropQuest Whether to drop the quest item on the ground.
   */
  protected static void interruptQuest(Player player, boolean doDropQuest) {
    Bukkit.getScheduler().cancelTask(playerQuestTimerMap.get(player));
    playerQuestTimerMap.remove(player);
    playerQuestTimeMap.remove(player);
    int slot = playerQuestSlotMap.get(player);
    playerQuestSlotMap.remove(player);
    String rank = playerQuestRankMap.get(player);
    playerQuestRankMap.remove(player);
    ItemStack questItem = new ItemStack(Material.AIR);
    try {
      questItem = Quests.getQuestItem(rank, 1, player);
    } catch (InvalidMaterialException e) {
      e.printStackTrace();
    }
    if (doDropQuest) {
      player.getWorld().dropItemNaturally(player.getLocation(), questItem);
      player.getInventory().clear(slot);
      return;
    }
    player.getInventory().setItem(slot, questItem);
  }

  public static Voucher getVoucher(String rank) {
    return nameRankQuestMap.get(rank).voucher;
  }

  /**
   * Starts a rank quest for specified player.
   *
   * @param player Player to start rank quest for.
   * @param slot   Slot that rank quest item is in.
   */
  public void startQuest(Player player, int slot) {
    try {
      playerQuestTimeMap.put(player, duration);
      ItemStack startItem = Quests.getCdQuestItem(name, player, playerQuestTimeMap.get(player));
      playerQuestSlotMap.put(player, slot);
      player.getInventory().setItem(slot, startItem);
      playerQuestRankMap.put(player, name);
      int timer = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getPlugin(), new Runnable() {
        @Override
        public void run() {
          if (!Support.canStartRankQuest(player, regionCheckToggleList, regionList,
              regionBlacklist)) {
            interruptQuest(player, false);
            return;
          }
          if (playerQuestTimeMap.get(player) > 0) {
            updateQuest(player);
          } else {
            stopQuest(player);
          }
        }
      }, 20, 20);
      playerQuestTimerMap.put(player, timer);
      if (Config.disableFly()) {
        player.setFlying(false);
        player.setAllowFlight(false);
      }
      Messages.announceBegin(player, name);
    } catch (InvalidMaterialException e) {
      e.printStackTrace();
    }
  }

  /**
   * Checks whether a player is in the valid region to do the rank quest.
   *
   * @param player Player to be checked.
   * @return Whether specified player is in the valid region to do the rank quest.
   */
  public boolean isInValidRegion(Player player) {
    return Support.canStartRankQuest(player, regionCheckToggleList, regionList, regionBlacklist);
  }

  private void updateQuest(Player player) {
    playerQuestTimeMap.put(player, playerQuestTimeMap.get(player) - 1);
    try {
      ItemStack nextItem = Quests.getCdQuestItem(name, player, playerQuestTimeMap.get(player));
      player.getInventory().setItem(playerQuestSlotMap.get(player), nextItem);
    } catch (InvalidMaterialException e) {
      e.printStackTrace();
    }
  }

  private void stopQuest(Player player) {
    voucher.giveVoucher(player, name, 1, playerQuestSlotMap.get(player));
    Bukkit.getScheduler().cancelTask(playerQuestTimerMap.get(player));
    playerQuestTimerMap.remove(player);
    playerQuestTimeMap.remove(player);
    playerQuestSlotMap.remove(player);
    playerQuestRankMap.remove(player);
    Messages.announceComplete(player, name);
  }

  public static boolean isCdQuestItem(Player player, ItemStack item) {
    String rank = playerQuestRankMap.get(player);
    int time = playerQuestTimeMap.get(player);
    try {
      ItemStack cdItem = Quests.getCdQuestItem(rank, player, time);
      return item.isSimilar(cdItem);
    } catch (InvalidMaterialException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Gives a player a specified number of quest items. Input slot = null if you want items to simply
   * be inserted into first available slot.
   *
   * @param player Player to give specified quest item(s) to.
   * @param rank   Rank of quest item(s) to be given.
   * @param amount Amount of quest item(s) to be given.
   */
  public static void giveQuest(Player player, String rank, int amount, Integer slot) {
    try {
      ItemStack quest = Quests.getQuestItem(rank, amount, player);
      if (slot != null) {
        player.getInventory().setItem(slot, quest);
        Messages.sendQuestReceive(player, rank, amount);
        return;
      }
      if (PlayerInventoryHandler.getInventoryOverflowAmount(player, quest) == 0) {
        player.getInventory().addItem(quest);
        Messages.sendQuestReceive(player, rank, amount);
      } else {
        // TODO - REDEEMS STUFF
        Messages.sendInventoryFull(player);
      }
    } catch (InvalidMaterialException e) {
      e.printStackTrace();
    }
  }
}
