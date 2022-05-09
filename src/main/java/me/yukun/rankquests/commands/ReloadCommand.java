package me.yukun.rankquests.commands;

import me.yukun.rankquests.config.Config;
import me.yukun.rankquests.config.Messages;
import me.yukun.rankquests.config.Quests;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends AbstractCommand {
  public ReloadCommand(CommandSender sender) {
    super(sender);
  }

  @Override
  public void execute() {
    Config.reload();
    Messages.reload();
    Quests.reload();
    // TODO - REDEEMS AND LOGGERS STUFF
    Messages.sendReloaded(super.sender);
  }
}
