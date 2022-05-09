package me.yukun.rankquests.commands;

import me.yukun.rankquests.config.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) {
      Messages.sendCommandList(sender);
      return false;
    }
    AbstractCommand abstractCommand = null;
    switch (args[0]) {
      case "list":
        abstractCommand = new ListCommand(sender);
        break;
      case "redeem":
        abstractCommand = new RedeemCommand(sender);
        break;
      case "reload":
        abstractCommand = new ReloadCommand(sender);
        break;
      case "quest":
        abstractCommand = QuestCommand.parseCommand(sender, args);
        break;
      case "voucher":
        abstractCommand = VoucherCommand.parseCommand(sender, args);
    }
    if (abstractCommand != null) {
      if (abstractCommand.isSilentErrorCommand()) {
        return false;
      }
      abstractCommand.execute();
      return true;
    }
    Messages.sendCommandList(sender);
    return false;
  }
}
