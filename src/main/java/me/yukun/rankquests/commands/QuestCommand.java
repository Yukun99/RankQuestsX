package me.yukun.rankquests.commands;

import me.yukun.rankquests.config.Messages;
import me.yukun.rankquests.config.Quests;
import me.yukun.rankquests.quest.RankQuest;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuestCommand extends AbstractCommand {
  private final Player player;
  private final String rank;
  private final int amount;
  private Messages.ErrorType errorType = null;

  public QuestCommand(CommandSender sender, Player player, String rank, int amount) {
    super(sender);
    this.player = player;
    this.rank = rank;
    this.amount = amount;
  }

  private static QuestCommand getErrorCommand(CommandSender sender, Messages.ErrorType errorType) {
    QuestCommand errorCommand = new QuestCommand(sender, null, null, -1);
    errorCommand.errorType = errorType;
    return errorCommand;
  }

  public static AbstractCommand parseCommand(CommandSender sender, String[] args) {
    switch (args.length) {
      case 1:
        return null;
      case 2:
        if (Quests.getAllRanks().contains(args[1])) {
          if (!(sender instanceof Player)) {
            return getErrorCommand(sender, Messages.ErrorType.SENDER);
          }
          Player player = (Player) sender;
          String rank = args[1];
          int amount = 1;
          return new QuestCommand(sender, player, rank, amount);
        }
      case 3:
        if (Quests.getAllRanks().contains(args[1])) {
          if (!(sender instanceof Player)) {
            return getErrorCommand(sender, Messages.ErrorType.SENDER);
          }
          if (!isValidAmount(args[2])) {
            return getErrorCommand(sender, Messages.ErrorType.AMOUNT);
          }
          Player player = (Player) sender;
          String rank = args[1];
          int amount = Integer.parseInt(args[2]);
          return new QuestCommand(sender, player, rank, amount);
        }
        if (Quests.getAllRanks().contains(args[2])) {
          if (Bukkit.getPlayer(args[1]) == null) {
            return getErrorCommand(sender, Messages.ErrorType.PLAYER);
          }
          Player player = Bukkit.getPlayer(args[1]);
          String rank = args[2];
          int amount = 1;
          return new QuestCommand(sender, player, rank, amount);
        }
      default:
        // /rq quest xu_yukun VIP 65
        if (!isValidAmount(args[3])) {
          return getErrorCommand(sender, Messages.ErrorType.AMOUNT);
        }
        if (Bukkit.getPlayer(args[1]) == null) {
          return getErrorCommand(sender, Messages.ErrorType.PLAYER);
        }
        Player player = Bukkit.getPlayer(args[1]);
        String rank = args[2];
        int amount = Integer.parseInt(args[3]);
        return new QuestCommand(sender, player, rank, amount);
    }
  }

  private static boolean isValidAmount(String argument) {
    if (!isInt(argument)) {
      return false;
    }
    int amount = Integer.parseInt(argument);
    return amount > 0 && amount <= 64;
  }

  @Override
  public void execute() {
    if (!sender.hasPermission("rankquest.quest") && !sender.hasPermission("rankquest.admin")
        && !sender.hasPermission("rankquest.*") && !sender.isOp()) {
      return;
    }
    if (errorType != null) {
      Messages.sendError(super.sender, errorType);
      return;
    }
    RankQuest.giveQuest(player, rank, amount, null);
  }
}
