package me.yukun.rankquests.hooks;

import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import java.util.List;
import org.bukkit.Location;

public class WorldGuardSupport {

  protected static boolean isInRegion(Location location, List<String> regions) {
    RegionManager manager = getRegionManager(location);
    for (ProtectedRegion set : getApplicableRegions(location, manager)) {
      if (set == null) {
        continue;
      }
      if (regions.contains(set.getId())) {
        return true;
      }
    }
    return false;
  }

  protected static boolean hasPvPFlag(Location location) {
    RegionManager manager = getRegionManager(location);
    if (manager == null) {
      return true;
    }
    ApplicableRegionSet set = getApplicableRegions(location, manager);
    State state = set.queryState(null, Flags.PVP);
    if (state == null) {
      return true;
    }
    return state.equals(State.ALLOW);
  }

  private static RegionManager getRegionManager(Location location) {
    WorldGuard wg = WorldGuard.getInstance();
    RegionContainer container = wg.getPlatform().getRegionContainer();
    BukkitWorld world = new BukkitWorld(location.getWorld());
    return container.get(world);
  }

  private static ApplicableRegionSet getApplicableRegions(Location location,
      RegionManager manager) {
    BlockVector3 v = BlockVector3.at(location.getX(), location.getY(), location.getZ());
    return manager.getApplicableRegions(v);
  }
}
