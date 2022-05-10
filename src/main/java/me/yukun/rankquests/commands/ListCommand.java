package me.yukun.rankquests.commands;

import me.yukun.rankquests.config.Messages;
import org.bukkit.command.CommandSender;

public class ListCommand extends AbstractCommand {
  public ListCommand(CommandSender sender) {
    super(sender);
  }

  @Override
  public void execute() {
    if (!sender.hasPermission("rankquest.list") && !sender.hasPermission("rankquest.admin")
        && !sender.hasPermission("rankquest.*") && !sender.isOp()) {
      return;
    }
    Messages.sendQuestList(sender);
  }
}
