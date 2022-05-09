package me.yukun.rankquests.commands;

import org.bukkit.command.CommandSender;

public abstract class AbstractCommand {
  protected static final AbstractCommand SILENT_ERROR_COMMAND = new AbstractCommand(null) {
    @Override
    public void execute() {}
  };
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

  public boolean isSilentErrorCommand() {
    return this == SILENT_ERROR_COMMAND;
  }
}
