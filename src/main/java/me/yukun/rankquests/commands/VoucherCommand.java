package me.yukun.rankquests.commands;

import me.yukun.rankquests.config.Messages;
import me.yukun.rankquests.config.Quests;
import me.yukun.rankquests.quest.RankQuest;
import me.yukun.rankquests.voucher.Voucher;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VoucherCommand extends AbstractCommand {
  private final Player player;
  private final String rank;
  private final int amount;

  public VoucherCommand(CommandSender sender, Player player, String rank, int amount) {
    super(sender);
    this.player = player;
    this.rank = rank;
    this.amount = amount;
  }

  public static AbstractCommand parseCommand(CommandSender sender, String[] args) {
    switch (args.length) {
      case 1:
        return null;
      case 2:
        if (Quests.getAllRanks().contains(args[1])) {
          if (!(sender instanceof Player)) {
            Messages.sendSenderNotPlayer(sender);
            return SILENT_ERROR_COMMAND;
          }
          Player player = (Player) sender;
          String rank = args[1];
          int amount = 1;
          return new VoucherCommand(sender, player, rank, amount);
        }
      case 3:
        if (Quests.getAllRanks().contains(args[1])) {
          if (!(sender instanceof Player)) {
            Messages.sendSenderNotPlayer(sender);
            return SILENT_ERROR_COMMAND;
          }
          if (!isInt(args[2])) {
            Messages.sendInvalidAmount(sender);
            return SILENT_ERROR_COMMAND;
          }
          Player player = (Player) sender;
          String rank = args[1];
          int amount = Integer.parseInt(args[2]);
          if (amount > 64) {
            Messages.sendInvalidAmount(sender);
            return SILENT_ERROR_COMMAND;
          }
          return new VoucherCommand(sender, player, rank, amount);
        }
        if (Quests.getAllRanks().contains(args[2])) {
          if (Bukkit.getPlayer(args[1]) == null) {
            Messages.sendInvalidPlayer(sender);
            return SILENT_ERROR_COMMAND;
          }
          Player player = Bukkit.getPlayer(args[1]);
          String rank = args[2];
          int amount = 1;
          return new VoucherCommand(sender, player, rank, amount);
        }
      default:
        if (!isInt(args[3])) {
          Messages.sendInvalidAmount(sender);
          return SILENT_ERROR_COMMAND;
        }
        if (Bukkit.getPlayer(args[1]) == null) {
          Messages.sendInvalidPlayer(sender);
          return SILENT_ERROR_COMMAND;
        }
        Player player = Bukkit.getPlayer(args[1]);
        String rank = args[2];
        int amount = Integer.parseInt(args[3]);
        if (amount > 64) {
          Messages.sendInvalidAmount(sender);
          return SILENT_ERROR_COMMAND;
        }
        return new VoucherCommand(sender, player, rank, amount);
    }
  }

  @Override
  public void execute() {
    Voucher voucher = RankQuest.getVoucher(rank);
    voucher.giveVoucher(player, rank, amount, null);
  }
}
