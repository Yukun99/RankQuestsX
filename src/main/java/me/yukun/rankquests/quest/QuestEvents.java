package me.yukun.rankquests.quest;

import me.yukun.rankquests.config.Config;
import me.yukun.rankquests.config.Messages;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class QuestEvents implements Listener {
  @EventHandler
  public void questStartListener(PlayerInteractEvent e) {
    Player player = e.getPlayer();
    ItemStack questItem = player.getInventory().getItemInMainHand();
    RankQuest rankQuest = RankQuest.getRankQuestFromItem(questItem, player);
    int slot = player.getInventory().getHeldItemSlot();
    if (rankQuest == null) {
      questItem = player.getInventory().getItemInOffHand();
      rankQuest = RankQuest.getRankQuestFromItem(questItem, player);
      if (rankQuest == null) {
        return;
      }
      slot = 45;
    }
    if (RankQuest.isDoingRankQuest(player)) {
      Messages.sendQuestWarningsStart(player);
      return;
    }
    if (questItem.getAmount() > 1) {
      Messages.sendQuestWarningsStack(player);
      return;
    }
    if (!rankQuest.isInValidRegion(player)) {
      Messages.sendQuestWarningsRegion(player);
      return;
    }
    rankQuest.startQuest(player, slot);
  }

  @EventHandler
  public void questInventoryMoveQuestListener(InventoryClickEvent e) {
    Player player = (Player) e.getWhoClicked();
    if (!RankQuest.isDoingRankQuest(player)) {
      return;
    }
    // Check if player is trying to use number keys to move items around.
    if (e.getClick().equals(ClickType.NUMBER_KEY)) {
      // Check if player is clicking number that is quest item or moving item to other hotbar slot
      if (!RankQuest.isQuestSlot(player, e.getHotbarButton())
          && !RankQuest.isQuestSlot(player, e.getSlot())) {
        return;
      }
      // Check if player is trying to click on quest slot otherwise.
      // Make sure clicked inventory is player's own.
    } else if (!(e.getClickedInventory() instanceof PlayerInventory)
        || !RankQuest.isQuestSlot(player, e.getSlot())) {
      return;
    }
    e.setCancelled(true);
    Messages.sendQuestWarningsMove(player);
  }

  @EventHandler
  public void questPlayerSwapHandsListener(PlayerSwapHandItemsEvent e) {
    Player player = e.getPlayer();
    if (!RankQuest.isDoingRankQuest(player)) {
      return;
    }
    if (!RankQuest.isQuestSlot(player, 45) && !RankQuest.isQuestSlot(player,
        player.getInventory().getHeldItemSlot())) {
      return;
    }
    e.setCancelled(true);
    Messages.sendQuestWarningsMove(player);
  }

  @EventHandler
  public void questPlayerDropQuestListener(PlayerDropItemEvent e) {
    Player player = e.getPlayer();
    if (!RankQuest.isDoingRankQuest(player)) {
      return;
    }
    if (!RankQuest.isQuestSlot(player, player.getInventory().getHeldItemSlot())) {
      return;
    }
    // Solving a problem that really does not NEED to be this hard.
    // Why the fuck does Minecraft treat the player as always opening their own inventory even when
    // they aren't?
    // Save me from this hell holy fuck.
    ItemStack droppedItem = e.getItemDrop().getItemStack();
    if (!RankQuest.isCdQuestItem(player, droppedItem)) {
      return;
    }
    e.setCancelled(true);
    Messages.sendQuestWarningsDrop(player);
  }

  @EventHandler
  public void questPlayerFlyListener(PlayerToggleFlightEvent e) {
    Player player = e.getPlayer();
    if (!RankQuest.isDoingRankQuest(player)) {
      return;
    }
    if (!Config.disableFly()) {
      return;
    }
    if (!player.isFlying()) {
      return;
    }
    player.setFlying(false);
    player.setAllowFlight(false);
  }

  @EventHandler
  public void questPlayerDisconnectListener(PlayerQuitEvent e) {
    Player player = e.getPlayer();
    if (!Config.dropOnDC()) {
      return;
    }
    if (!RankQuest.isDoingRankQuest(player)) {
      return;
    }
    RankQuest.interruptQuest(player, true);
  }

  @EventHandler
  public void questPlayerDeathListener(PlayerDeathEvent e) {
    Player player = e.getEntity();
    if (!RankQuest.isDoingRankQuest(player)) {
      return;
    }

    RankQuest.interruptQuest(player, true);
  }
}
