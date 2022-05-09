package me.yukun.rankquests;

import me.yukun.rankquests.commands.CommandHandler;
import me.yukun.rankquests.config.FileManager;
import me.yukun.rankquests.quest.QuestEvents;
import me.yukun.rankquests.quest.RankQuest;
import me.yukun.rankquests.voucher.VoucherEvents;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
  private static Plugin rankQuests = null;

  public static Plugin getPlugin() {
    return rankQuests;
  }

  //  @EventHandler
  //  public void devDebugEvent(PlayerInteractEvent e) {
  //    Player debugPlayer = e.getPlayer();
  //    try {
  //      if (e.getPlayer().getInventory().getItemInMainHand().isSimilar(Quests.getQuestItem
  //      ("VIP", 1,
  //          debugPlayer))) {
  //        RankQuest.nameRankQuestMap.get("VIP").startQuest(debugPlayer,
  //            debugPlayer.getInventory().getHeldItemSlot());
  //      }
  //    } catch (InvalidMaterialException ex) {
  //      ex.printStackTrace();
  //    }
  //  }

  @Override
  public void onEnable() {
    // Setting up general stuff
    FileManager.createInstance(this);
    RankQuest.onEnable();
    rankQuests = Bukkit.getPluginManager().getPlugin("RankQuests");
    getCommand("rankquest").setExecutor(new CommandHandler());

    // Setting up listeners
    PluginManager pm = Bukkit.getServer().getPluginManager();
    pm.registerEvents(this, this);
    pm.registerEvents(new QuestEvents(), this);
    pm.registerEvents(new VoucherEvents(), this);
  }
}
