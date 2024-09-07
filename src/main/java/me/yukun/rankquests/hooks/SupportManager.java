package me.yukun.rankquests.hooks;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SupportManager {

  public static boolean canStartRankQuest(Player player, List<Boolean> regionCheckToggleList,
      List<String> regionList, List<String> regionBlackList) {
    Location location = player.getLocation();
    if (regionCheckToggleList.get(0)) {
      if (!hasFactions()) {
        return true;
      }
      if (!FactionsSupport.isInWarzone(location)) {
        return false;
      }
    }
    if (!hasWorldGuard()) {
      return true;
    }
    if (regionCheckToggleList.get(1)) {
      if (!WorldGuardSupport.isInRegion(location, regionList)) {
        return false;
      }
    }
    if (regionCheckToggleList.get(2)) {
      if (WorldGuardSupport.isInRegion(location, regionBlackList)) {
        return false;
      }
    }
    if (regionCheckToggleList.get(3)) {
      return WorldGuardSupport.hasPvPFlag(location);
    }
    return true;
  }

  private static boolean hasWorldGuard() {
    return Bukkit.getServer().getPluginManager().isPluginEnabled("WorldGuard");
  }

  private static boolean hasFactions() {
    return Bukkit.getServer().getPluginManager().isPluginEnabled("Factions");
  }
}
