package me.yukun.rankquests.commands;

import java.util.Map;
import me.yukun.rankquests.config.Messages;
import me.yukun.rankquests.config.Redeems;
import me.yukun.rankquests.quest.RankQuest;
import me.yukun.rankquests.voucher.Voucher;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RedeemCommand extends AbstractCommand {
  public RedeemCommand(CommandSender sender) {
    super(sender);
  }

  @Override
  public void execute() {
    if (!(super.sender instanceof Player)) {
      Messages.sendError(super.sender, Messages.ErrorType.SENDER);
      return;
    }
    Player player = (Player) sender;
    Map<String, Integer> rankQuestAmountMap = Redeems.getRankQuestAmountMap(player);
    for (String rank : rankQuestAmountMap.keySet()) {
      RankQuest.giveRedeemedQuest(player, rank, rankQuestAmountMap.get(rank));
    }
    Map<String, Integer> voucherAmountMap = Redeems.getVoucherAmountMap(player);
    for (String rank : voucherAmountMap.keySet()) {
      Voucher.giveRedeemedVoucher(player, rank, voucherAmountMap.get(rank));
    }
  }
}
