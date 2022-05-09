package me.yukun.rankquests.commands;

import me.yukun.rankquests.config.Messages;
import org.bukkit.command.CommandSender;

public class ListCommand extends AbstractCommand {
  public ListCommand(CommandSender sender) {
    super(sender);
  }

  @Override
  public void execute() {
    Messages.sendQuestList(sender);
  }
}
