package me.yukun.rankquests.voucher;

import java.util.List;
import me.yukun.rankquests.config.Messages;
import me.yukun.rankquests.config.Quests;
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
  public void giveVoucher(Player player, String rank, int amount, Integer slot) {
    try {
      ItemStack voucher = Quests.getVoucherItem(rank, amount);
      if (slot != null) {
        player.getInventory().setItem(slot, voucher);
        Messages.sendVoucherReceive(player, rank, amount);
        return;
      }
      if (PlayerInventoryHandler.getInventoryOverflowAmount(player, voucher) == 0) {
        player.getInventory().addItem(voucher);
        Messages.sendVoucherReceive(player, rank, amount);
      } else {
        // TODO - REDEEMS STUFF
        Messages.sendInventoryFull(player);
      }
    } catch (InvalidMaterialException e) {
      e.printStackTrace();
    }
  }

  /**
   * Run commands from the voucher.
   */
  public void runCommands() {
    for (String command : voucherCommandList) {
      Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
    }
  }
}
