package com.poke.app;

import com.poke.app.client.Client;
import java.util.Scanner;

// Initializes client
public class Main {

  public static void main(String[] args) {
    Scanner scan = new Scanner(System.in); // Do not close this
    System.out.println("What is your name?");
    String name = scan.nextLine();
    Client c = new Client("127.0.0.1", 8080, name);
    // Start the listener thread
    new Thread(new Runnable() {
      @Override
      public void run() {
        c.test();  // Keep testing for messages from the server
      }
    }).start();


  }
}
