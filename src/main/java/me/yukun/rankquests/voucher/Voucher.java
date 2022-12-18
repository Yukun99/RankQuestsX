package me.yukun.rankquests.voucher;

import java.util.List;
import me.yukun.rankquests.config.Messages;
import me.yukun.rankquests.config.Quests;
import me.yukun.rankquests.config.Redeems;
import me.yukun.rankquests.exception.InvalidMaterialException;
import me.yukun.rankquests.inventory.PlayerInventoryHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Voucher {
  private final List<String> voucherCommandList;

  public Voucher(String rank) {
    this.voucherCommandList = Quests.getVoucherCommandList(rank);
  }

  /**
   * Gets corresponding tier of Voucher from item.
   *
   * @param item Item to get corresponding tier of Voucher from.
   * @return Tier that corresponds to specified item.
   */
  public static String getRank(ItemStack item) {
    try {
      for (String rank : Quests.getAllRanks()) {
        ItemStack voucherItem = Quests.getVoucherItem(rank, 1);
        if (item.isSimilar(voucherItem)) {
          return rank;
        }
      }
    } catch (InvalidMaterialException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Gives a player a specified number of voucher items. Input slot = null if you want items to
   * simply be inserted into first available slot.
   *
   * @param player Player to give specified voucher(s) to.
   * @param rank   Rank of voucher(s) to be given.
   * @param amount Amount of voucher(s) to be given.
   * @param slot   Slot to insert voucher(s) into.
   */
  public static void giveVoucher(Player player, String rank, int amount, Integer slot) {
    try {
      ItemStack voucher = Quests.getVoucherItem(rank, amount);
      if (slot != null) {
        player.getInventory().setItem(slot, voucher);
        Messages.sendVoucherReceive(player, rank, amount);
        return;
      }
      int overflow = PlayerInventoryHandler.getInventoryOverflowAmount(player, voucher);
      int remain = amount - overflow;
      Messages.sendVoucherReceive(player, rank, remain);
      if (overflow != 0) {
        Redeems.addVoucher(player, rank, overflow);
        voucher.setAmount(remain);
        Messages.sendInventoryFull(player);
      }
      player.getInventory().addItem(voucher);
    } catch (InvalidMaterialException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gives a player a specified number of voucher items from the player's possible redeemed items.
   *
   * @param player Player to give specified voucher(s) to.
   * @param rank   Rank of voucher(s) to be given.
   * @param amount Amount of voucher(s) to be given.
   */
  public static void giveRedeemedVoucher(Player player, String rank, int amount) {
    try {
      ItemStack voucher = Quests.getVoucherItem(rank, amount);
      int overflow = PlayerInventoryHandler.getInventoryOverflowAmount(player, voucher);
      int remain = amount - overflow;
      Messages.sendVoucherReceive(player, rank, remain);
      if (overflow != 0) {
        Redeems.setVoucher(player, rank, overflow);
        voucher.setAmount(remain);
        Messages.sendInventoryFull(player);
      }
      player.getInventory().addItem(voucher);
    } catch (InvalidMaterialException e) {
      e.printStackTrace();
    }
  }

  /**
   * Run commands from the voucher.
   */
  public void runCommands(Player player) {
    for (String command : voucherCommandList) {
      String parsed = command;
      parsed = parsed.replaceAll("%player%", player.getName());
      Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), parsed);
    }
  }
}
