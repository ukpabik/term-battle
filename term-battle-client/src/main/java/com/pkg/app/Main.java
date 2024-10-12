package com.pkg.app;

import com.pkg.app.client.Client;
import java.util.Scanner;

// Initializes client
public class Main {

  public static void main(String[] args) {
    System.out.print("\033[H\033[2J");
    System.out.flush();
    Scanner scan = new Scanner(System.in); // Do not close this
    System.out.println("Enter your username: ");
    String name = scan.nextLine();
    System.out.println("Enter your password: ");
    String password = scan.nextLine();
    Client c = new Client("127.0.0.1", 8080, name, password);
    // Start the listener thread
    new Thread(new Runnable() {
      @Override
      public void run() {
        c.start();
      }
    }).start();

  }
}
