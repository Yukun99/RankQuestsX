package me.yukun.rankquests.exception;

public class InvalidMaterialException extends Exception {
  private final String filename;
  private final String path;

  public InvalidMaterialException(String filename, String path) {
    super();
    this.filename = filename;
    this.path = path;
  }

  public String toString() {
    return "You have used an invalid item type in your config files!"
        + "\nFile: " + filename
        + "\nPath: " + path;
  }
}
