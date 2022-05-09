package me.yukun.rankquests.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class PlayerInventoryHandler {
  public static ItemStack getItemInHand(PlayerInventory inventory) {
    return inventory.getItemInMainHand();
  }

  public static void setItemInHand(PlayerInventory inventory, ItemStack item) {
    inventory.setItemInMainHand(item);
  }

  /**
   * Gets amount of item that overflows when given to the player.
   * If inventory can receive all of the item, then 0 is returned.
   *
   * @param player Player to check given item for.
   * @param item   Item to be given to the player.
   * @return Amount of item that overflows.
   */
  public static int getInventoryOverflowAmount(Player player, ItemStack item) {
    int overflow = item.getAmount();
    if (player.getInventory().firstEmpty() != -1) {
      return 0;
    }
    for (ItemStack inventoryItem : player.getInventory().getContents()) {
      if (!inventoryItem.isSimilar(item)) {
        continue;
      }
      if (inventoryItem.getAmount() + item.getAmount() <= 64) {
        return 0;
      }
      overflow -= 64 - inventoryItem.getAmount();
      if (overflow <= 0) {
        return 0;
      }
    }
    return overflow;
  }
}
