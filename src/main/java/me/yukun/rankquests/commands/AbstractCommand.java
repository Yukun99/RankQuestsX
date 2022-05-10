package me.yukun.rankquests.commands;

import org.bukkit.command.CommandSender;

public abstract class AbstractCommand {
  protected CommandSender sender;

  public AbstractCommand(CommandSender sender) {
    this.sender = sender;
  }

  protected static boolean isInt(String string) {
    try {
      Integer.parseInt(string);
    } catch (NumberFormatException nfe) {
      return false;
    }
    return true;
  }

  /**
   * Executes the command.
   */
  public abstract void execute();
}
