package me.yukun.rankquests;

import java.util.Objects;
import me.yukun.rankquests.commands.CommandHandler;
import me.yukun.rankquests.config.FileManager;
import me.yukun.rankquests.quest.QuestEvents;
import me.yukun.rankquests.quest.RankQuest;
import me.yukun.rankquests.voucher.VoucherEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
  private static Plugin rankQuests = null;

  public static Plugin getPlugin() {
    return rankQuests;
  }

  @Override
  public void onEnable() {
    // Setting up general stuff
    FileManager.createInstance(this);
    RankQuest.onEnable();
    rankQuests = Bukkit.getPluginManager().getPlugin("RankQuests");
    Objects.requireNonNull(getCommand("rankquest")).setExecutor(new CommandHandler());

    // Setting up listeners
    PluginManager pm = Bukkit.getServer().getPluginManager();
    pm.registerEvents(new QuestEvents(), this);
    pm.registerEvents(new VoucherEvents(), this);
  }
}
