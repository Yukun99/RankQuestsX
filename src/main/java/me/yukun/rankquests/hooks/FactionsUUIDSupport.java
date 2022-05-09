package me.yukun.rankquests.hooks;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import org.bukkit.entity.Player;

public class FactionsUUIDSupport {
  private static final FactionsUUIDSupport factionsUUIDSupport = new FactionsUUIDSupport();

  private FactionsUUIDSupport() {}

  protected static FactionsUUIDSupport getInstance() {
    return factionsUUIDSupport;
  }

  public boolean isInWarzone(Player player) {
    FLocation fLocation = new FLocation(player.getLocation());
    return Board.getInstance().getFactionAt(fLocation).isWarZone();
  }
}
