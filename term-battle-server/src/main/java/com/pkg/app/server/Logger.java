package com.pkg.app.server;

import com.pkg.app.server.text.AnsiText;

public class Logger {

  public static void info(String message) {
    System.out.println(AnsiText.color("[INFO] ", AnsiText.GREEN) + message);
  }

  public static void warning(String message) {
    System.out.println(AnsiText.color("[WARNING] ", AnsiText.YELLOW) + message);
  }

  public static void error(String message) {
    System.out.println(AnsiText.color("[ERROR] ", AnsiText.RED) + message);
  }

  public static void debug(String message) {
    System.out.println(AnsiText.color("[DEBUG] ", AnsiText.BLUE) + message);
  }
}

