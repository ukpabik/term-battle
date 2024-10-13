package com.pkg.app;

import com.pkg.app.client.Client;
import java.util.Scanner;
import java.util.NoSuchElementException;

// Initializes client
public class Main {

  public static void main(String[] args) {
    System.out.print("\033[H\033[2J");
    System.out.flush();
    Scanner scan = new Scanner(System.in); // Do not close this

    try{

      String name;
      while (true){
        System.out.println("Enter your username: ");
        name = scan.nextLine();
        if (name.length() >= 3){
          break;
        }
        else{
          System.out.println("Username must be at least 3 characters.");
        }
      }

      String password;
      while (true){
        System.out.println("Enter your password: ");
        password = scan.nextLine();
        if (password.length() >= 4){
          break;
        }
        else{
          System.out.println("Password must be at least 4 characters.");
        }
      }
      Client c = new Client("127.0.0.1", 8080, name, password);
      // Start the listener thread
      new Thread(new Runnable() {
        @Override
        public void run() {
          c.start();
        }
      }).start();
    }
    catch(NoSuchElementException e){
      System.out.println("User disconnected suddenly. Closing all resources...");
    }
  }
}
