package me.yukun.rankquests.voucher;

import me.yukun.rankquests.config.Messages;
import me.yukun.rankquests.quest.RankQuest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class VoucherEvents implements Listener {
  @EventHandler
  public void voucherUseEvent(PlayerInteractEvent e) {
    if (!e.getAction().equals(Action.RIGHT_CLICK_AIR)
        && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
      return;
    }
    Player player = e.getPlayer();
    ItemStack handItem = player.getInventory().getItemInMainHand();
    String rank = Voucher.getRank(handItem);
    if (rank == null) {
      return;
    }
    e.setCancelled(true);
    handItem.setAmount(handItem.getAmount() - 1);
    RankQuest.getVoucher(rank).runCommands(player);
    Messages.sendVoucherUse(player, rank);
  }
}
