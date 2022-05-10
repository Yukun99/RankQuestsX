package me.yukun.rankquests.hooks;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import java.util.List;
import java.util.Objects;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WorldGuardSupport {
  private static final WorldGuardSupport worldGuardSupport = new WorldGuardSupport();
  private final WorldGuard wg = WorldGuard.getInstance();

  private WorldGuardSupport() {}

  protected static WorldGuardSupport getInstance() {
    return worldGuardSupport;
  }

  private ApplicableRegionSet getRegionSet(Player player) {
    Location loc = player.getLocation();
    BukkitWorld world = new BukkitWorld(loc.getWorld());
    BlockVector3 v = BlockVector3.at(loc.getX(), loc.getY(), loc.getZ());
    try {
      RegionManager regionManager = wg.getPlatform().getRegionContainer().get(world);
      assert regionManager != null;
      return regionManager.getApplicableRegions(v);
    } catch (NullPointerException e) {
      e.printStackTrace();
      return null;
    }
  }

  public boolean isInPvPRegion(Player player) {
    ApplicableRegionSet regionSet = getRegionSet(player);
    assert regionSet != null;
    return regionSet.queryState(null, Flags.PVP) != StateFlag.State.DENY;
  }

  public boolean isInRegion(Player player, List<String> regionList) {
    for (ProtectedRegion region : Objects.requireNonNull(getRegionSet(player))) {
      for (String configRegion : regionList) {
        if (region.getId().equals(configRegion)) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean notInRegion(Player player, List<String> regionBlacklist) {
    for (ProtectedRegion region : Objects.requireNonNull(getRegionSet(player))) {
      if (region != null) {
        for (String configBlacklistRegion : regionBlacklist) {
          if (region.getId().equalsIgnoreCase(configBlacklistRegion)) {
            return false;
          }
        }
      }
    }
    return true;
  }
}
