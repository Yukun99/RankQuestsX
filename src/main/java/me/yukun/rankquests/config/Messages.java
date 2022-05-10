package me.yukun.rankquests.config;

import java.util.List;
import me.yukun.rankquests.chat.TextFormatter;
import me.yukun.rankquests.quest.RankQuest;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class Messages {
  private static FileConfiguration messageFile = FileManager.getInstance().messages;
  // Command Help Messages
  private static final String HELP_HEADER = "&b&l==========RankQuests Commands==========";
  private static final String HELP_HEADER2 =
      "&a[] denote optional arguments, () denote compulsory arguments.";
  private static final String USAGE_LIST = "/rankquest list";
  private static final String USAGE_REDEEM = "/rankquest redeem";
  private static final String USAGE_RELOAD = "/rankquest reload";
  private static final String USAGE_GIVE = "/rankquest quest [player] (rank) [amount]";
  private static final String USAGE_VOUCHER = "/rankquest voucher [player] (rank) [amount]";
  private static final String HELP_FOOTER = "&b&l=======================================";
  // Quest List Messages
  private static final String LIST_HEADER = "&b&l==========RankQuests==========";
  private static final String LIST_FOOTER = "&b&l==============================";
  private static final String QUEST_HEADER = "-------------------";
  private static final String QUEST_NAME = "Name: %rank%";
  private static final String QUEST_DURATION = "Duration: %time% sec";
  private static final String QUEST_FOOTER = "-------------------";
  // Command Reply Messages
  private static final String INVALID_SENDER = "&cCommand sender must be a player!";
  private static final String INVALID_AMOUNT = "&cSpecified amount must be number from 1 to 64!";
  private static final String INVALID_PLAYER = "&cTarget player does not exist or is not online!";
  private static final String RELOADED = "&aConfiguration files reloaded!";
  // General Messages
  private static String PREFIX = getString("Prefix");
  private static String INVENTORY_FULL = getString("Full");
  private static boolean USE_NICKS = getBoolean("UseNicks");
  // Quest Messages
  private static String QUEST_RECEIVE = getString("Quest.Receive");
  private static String QUEST_WARNINGS_START = getString("Quest.Warnings.Start");
  private static String QUEST_WARNINGS_STACK = getString("Quest.Warnings.Stack");
  private static String QUEST_WARNINGS_REGION = getString("Quest.Warnings.Region");
  private static String QUEST_WARNINGS_MOVE = getString("Quest.Warnings.Move");
  private static String QUEST_WARNINGS_DROP = getString("Quest.Warnings.Drop");
  // Announcement Messages
  private static List<String> ANNOUNCEMENT_BEGIN = getStringList("Announcement.Begin");
  private static List<String> ANNOUNCEMENT_BEGIN_ALL = getStringList("Announcement.BeginAll");
  private static List<String> ANNOUNCEMENT_COMPLETE = getStringList("Announcement.Complete");
  private static List<String> ANNOUNCEMENT_COMPLETE_ALL = getStringList("Announcement.CompleteAll");
  // Voucher Messages
  private static String VOUCHER_RECEIVE = getString("Voucher.Receive");
  private static String VOUCHER_USE = getString("Voucher.Use");

  public static void reload() {
    messageFile = FileManager.getInstance().reloadMessages();
    PREFIX = getString("Prefix");
    INVENTORY_FULL = getString("Full");
    USE_NICKS = getBoolean("UseNicks");
    QUEST_RECEIVE = getString("Quest.Receive");
    QUEST_WARNINGS_START = getString("Quest.Warnings.Start");
    QUEST_WARNINGS_STACK = getString("Quest.Warnings.Stack");
    QUEST_WARNINGS_REGION = getString("Quest.Warnings.Region");
    QUEST_WARNINGS_MOVE = getString("Quest.Warnings.Move");
    QUEST_WARNINGS_DROP = getString("Quest.Warnings.Drop");
    ANNOUNCEMENT_BEGIN = getStringList("Announcement.Begin");
    ANNOUNCEMENT_BEGIN_ALL = getStringList("Announcement.BeginAll");
    ANNOUNCEMENT_COMPLETE = getStringList("Announcement.Complete");
    ANNOUNCEMENT_COMPLETE_ALL = getStringList("Announcement.CompleteAll");
    VOUCHER_RECEIVE = getString("Voucher.Receive");
    VOUCHER_USE = getString("Voucher.Use");
  }

  private static String getString(String path) {
    return messageFile.getString(path);
  }

  private static List<String> getStringList(String path) {
    return messageFile.getStringList(path);
  }

  private static boolean getBoolean(String path) {
    return messageFile.getBoolean(path);
  }

  protected static String replacePlayerName(String message, Player player) {
    if (USE_NICKS) {
      return message.replaceAll("%player%", player.getDisplayName());
    } else {
      return message.replaceAll("%player%", player.getName());
    }
  }

  private static String replaceAmount(String message, int amount) {
    return message.replaceAll("%amount%", amount + "");
  }

  public static void sendInventoryFull(Player player) {
    player.sendMessage(TextFormatter.color(PREFIX + INVENTORY_FULL));
  }

  public static void sendQuestReceive(Player player, String rank, int amount) {
    String message = Quests.replaceRankName(QUEST_RECEIVE, rank);
    message = replaceAmount(message, amount);
    player.sendMessage(TextFormatter.color(PREFIX + message));
  }

  public static void sendQuestWarningsStart(Player player) {
    player.sendMessage(TextFormatter.color(PREFIX + QUEST_WARNINGS_START));
  }

  public static void sendQuestWarningsStack(Player player) {
    player.sendMessage(TextFormatter.color(PREFIX + QUEST_WARNINGS_STACK));
  }

  public static void sendQuestWarningsRegion(Player player) {
    player.sendMessage(TextFormatter.color(PREFIX + QUEST_WARNINGS_REGION));
  }

  public static void sendQuestWarningsMove(Player player) {
    player.sendMessage(TextFormatter.color(PREFIX + QUEST_WARNINGS_MOVE));
  }

  public static void sendQuestWarningsDrop(Player player) {
    player.sendMessage(TextFormatter.color(PREFIX + QUEST_WARNINGS_DROP));
  }

  public static void announceBegin(Player player, String rank) {
    for (String line : ANNOUNCEMENT_BEGIN) {
      line = Quests.replaceRankName(line, rank);
      line = replacePlayerName(line, player);
      player.sendMessage(TextFormatter.color(line));
    }
    for (String line : ANNOUNCEMENT_BEGIN_ALL) {
      for (Player online : player.getWorld().getPlayers()) {
        if (!online.equals(player)) {
          line = Quests.replaceRankName(line, rank);
          line = replacePlayerName(line, player);
          player.sendMessage(TextFormatter.color(line));
        }
      }
    }
  }

  public static void announceComplete(Player player, String rank) {
    for (String line : ANNOUNCEMENT_COMPLETE) {
      line = Quests.replaceRankName(line, rank);
      line = replacePlayerName(line, player);
      player.sendMessage(TextFormatter.color(line));
    }
    for (String line : ANNOUNCEMENT_COMPLETE_ALL) {
      for (Player online : player.getWorld().getPlayers()) {
        if (!online.equals(player)) {
          line = Quests.replaceRankName(line, rank);
          line = replacePlayerName(line, player);
          player.sendMessage(TextFormatter.color(line));
        }
      }
    }
  }

  public static void sendVoucherReceive(Player player, String rank, int amount) {
    String message = Quests.replaceRankName(VOUCHER_RECEIVE, rank);
    message = replacePlayerName(message, player);
    message = replaceAmount(message, amount);
    player.sendMessage(TextFormatter.color(PREFIX + message));
  }

  public static void sendVoucherUse(Player player, String rank) {
    String message = Quests.replaceRankName(VOUCHER_USE, rank);
    player.sendMessage(TextFormatter.color(PREFIX + message));
  }

  public static void sendCommandList(CommandSender sender) {
    sender.sendMessage(TextFormatter.color(HELP_HEADER));
    sender.sendMessage(TextFormatter.color(HELP_HEADER2));
    sender.sendMessage(TextFormatter.color(USAGE_LIST));
    sender.sendMessage(TextFormatter.color(USAGE_GIVE));
    sender.sendMessage(TextFormatter.color(USAGE_VOUCHER));
    sender.sendMessage(TextFormatter.color(USAGE_RELOAD));
    sender.sendMessage(TextFormatter.color(USAGE_REDEEM));
    sender.sendMessage(TextFormatter.color(HELP_FOOTER));
  }

  public static void sendQuestList(CommandSender sender) {
    sender.sendMessage(TextFormatter.color(LIST_HEADER));
    for (String rank : RankQuest.nameRankQuestMap.keySet()) {
      sendQuest(sender, rank);
    }
    sender.sendMessage(TextFormatter.color(LIST_FOOTER));
  }

  private static void sendQuest(CommandSender sender, String rank) {
    sender.sendMessage(TextFormatter.color(QUEST_HEADER));
    sender.sendMessage(TextFormatter.color(Quests.replaceRankName(QUEST_NAME, rank)));
    sender.sendMessage(TextFormatter.color(QUEST_DURATION.replaceAll("%time%",
        Quests.getDuration(rank) + "")));
    sender.sendMessage(TextFormatter.color(QUEST_FOOTER));
  }

  public static void sendReloaded(CommandSender sender) {
    sender.sendMessage(TextFormatter.color(PREFIX + RELOADED));
  }

  public static void sendError(CommandSender sender, ErrorType errorType) {
    sender.sendMessage(TextFormatter.color(PREFIX + errorType.message));
  }

  public enum ErrorType {
    SENDER(INVALID_SENDER),
    AMOUNT(INVALID_AMOUNT),
    PLAYER(INVALID_PLAYER);

    private final String message;

    ErrorType(String message) {
      this.message = message;
    }
  }
}
