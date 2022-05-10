package me.yukun.rankquests.commands;

import me.yukun.rankquests.config.Config;
import me.yukun.rankquests.config.Messages;
import me.yukun.rankquests.config.Quests;
import me.yukun.rankquests.config.Redeems;
import org.bukkit.command.CommandSender;

public class ReloadCommand extends AbstractCommand {
  public ReloadCommand(CommandSender sender) {
    super(sender);
  }

  @Override
  public void execute() {
    if (!sender.hasPermission("rankquest.reload") && !sender.hasPermission("rankquest.admin")
        && !sender.hasPermission("rankquest.*") && !sender.isOp()) {
      return;
    }
    Config.reload();
    Messages.reload();
    Quests.reload();
    Redeems.reload();
    Messages.sendReloaded(super.sender);
  }
}
