package me.yukun.rankquests.hooks;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Support {
  private static WorldGuardSupport worldGuardSupport = null;
  private static FactionsUUIDSupport factionsUUIDSupport = null;

  public static boolean canStartRankQuest(Player player, List<Boolean> regionCheckToggleList,
                                          List<String> regionList, List<String> regionBlackList) {
    if (regionCheckToggleList.get(0)) {
      if (!hasFactionsUUID()) {
        return true;
      }
      if (!factionsUUIDSupport.isInWarzone(player)) {
        return false;
      }
    }
    if (!hasWorldGuard()) {
      return true;
    }
    if (regionCheckToggleList.get(1)) {
      if (!worldGuardSupport.isInRegion(player, regionList)) {
        return false;
      }
    }
    if (regionCheckToggleList.get(2)) {
      if (!worldGuardSupport.notInRegion(player, regionBlackList)) {
        return false;
      }
    }
    if (regionCheckToggleList.get(3)) {
      return worldGuardSupport.isInPvPRegion(player);
    }
    return true;
  }

  private static boolean hasWorldGuard() {
    return Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard");
  }

  private static boolean hasFactionsUUID() {
    if (!Bukkit.getServer().getPluginManager().isPluginEnabled("Factions")) {
      return false;
    }
    Plugin factions = Bukkit.getServer().getPluginManager().getPlugin("Factions");
    assert factions != null;
    return factions.getDescription().getAuthors().contains("drtshock");
  }

  public void onEnable() {
    if (hasWorldGuard()) {
      worldGuardSupport = WorldGuardSupport.getInstance();
    }
    if (hasFactionsUUID()) {
      factionsUUIDSupport = FactionsUUIDSupport.getInstance();
    }
  }
}
