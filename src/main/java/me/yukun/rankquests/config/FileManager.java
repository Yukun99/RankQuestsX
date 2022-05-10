package me.yukun.rankquests.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import me.yukun.rankquests.chat.TextFormatter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Class that manages configuration file I/O.
 */
public class FileManager {
  // Prefix to be used before config files are loaded.
  private static final String DEFAULT_PREFIX = "[RankQuests] ";
  // Configuration related debug messages.
  private static final String FOLDER_NOT_CREATED = "Config folder not created, creating now...";
  private static final String FILE_NOT_CREATED = " not created, creating now...";
  private static FileManager fileManager;
  private final File dataFolder;
  // Files and FileConfigurations
  protected FileConfiguration config;
  protected File cfile;
  protected FileConfiguration messages;
  protected File mfile;
  protected FileConfiguration redeems;
  protected File rfile;
  protected FileConfiguration quests;
  protected File qfile;

  private FileManager(Plugin plugin) {
    if (plugin.getDataFolder().mkdir()) {
      printFolderNotCreated();
    }
    dataFolder = plugin.getDataFolder();
    cfile = createFile("Config.yml");
    config = YamlConfiguration.loadConfiguration(cfile);
    mfile = createFile("Messages.yml");
    messages = YamlConfiguration.loadConfiguration(mfile);
    rfile = createFile("Redeems.yml");
    redeems = YamlConfiguration.loadConfiguration(rfile);
    qfile = createFile("Quests.yml");
    quests = YamlConfiguration.loadConfiguration(qfile);
  }

  /**
   * Instantiates the FileManager instance to be used by the plugin.
   *
   * @param p Plugin to be instantiated from.
   */
  public static void createInstance(Plugin p) {
    fileManager = new FileManager(p);
  }

  /**
   * Gets instance of FileManager. Call ONLY after createInstance() has been called.
   *
   * @return Instance of FileManager.
   */
  public static FileManager getInstance() {
    return fileManager;
  }

  private File createFile(String filename) {
    File file = new File(dataFolder, filename);
    if (!file.exists()) {
      printFileNotCreated(filename);
      try {
        File en = new File(dataFolder, "/" + filename);
        InputStream E = getClass().getResourceAsStream("/" + filename);
        copyFile(E, en);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return file;
  }

  private void printFolderNotCreated() {
    System.out.println(TextFormatter.color(DEFAULT_PREFIX + FOLDER_NOT_CREATED));
  }

  private void printFileNotCreated(String filename) {
    System.out.println(TextFormatter.color(DEFAULT_PREFIX + filename + FILE_NOT_CREATED));
  }

  protected void saveConfig(FileConfiguration config) {
    try {
      config.save(cfile);
    } catch (IOException e) {
      Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save Config.yml!");
    }
  }

  protected void saveMessages(FileConfiguration messages) {
    try {
      messages.save(mfile);
    } catch (IOException e) {
      Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save Messages.yml!");
    }
  }

  protected void saveRedeems(FileConfiguration redeems) {
    try {
      redeems.save(rfile);
    } catch (IOException e) {
      Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save Redeems.yml!");
    }
  }

  protected void saveQuests(FileConfiguration quests) {
    try {
      quests.save(qfile);
    } catch (IOException e) {
      Bukkit.getServer().getLogger().severe(ChatColor.RED + "Could not save Quests.yml!");
    }
  }

  protected FileConfiguration reloadConfig() {
    config = YamlConfiguration.loadConfiguration(cfile);
    return config;
  }

  protected FileConfiguration reloadMessages() {
    messages = YamlConfiguration.loadConfiguration(mfile);
    return messages;
  }

  protected FileConfiguration reloadRedeems() {
    redeems = YamlConfiguration.loadConfiguration(rfile);
    return redeems;
  }

  protected FileConfiguration reloadQuests() {
    quests = YamlConfiguration.loadConfiguration(qfile);
    return quests;
  }

  /**
   * Copies files from inside the jar to outside. Adapted from https://bukkit
   * .org/threads/extracting-file-from-jar.16962/
   *
   * @param in  Where to copy file from.
   * @param out Where to copy file to.
   * @throws Exception If file does not get copied successfully.
   */
  private void copyFile(InputStream in, File out) throws Exception {
    try (InputStream fis = in; FileOutputStream fos = new FileOutputStream(out)) {
      byte[] buf = new byte[1024];
      int i;
      while ((i = fis.read(buf)) != -1) {
        fos.write(buf, 0, i);
      }
    }
  }
}
