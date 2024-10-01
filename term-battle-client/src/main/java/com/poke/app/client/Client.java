package com.poke.app.client;

import java.net.*;
import java.io.*;
import java.util.Scanner;
public class Client {
  private Socket socket;
  private String name;


  //Initialize the client
  public Client(String address, int port, String name){
    try{
      this.socket = new Socket(address, port);
      this.name = name;
      System.out.println("Yay! Client created");
    }
    catch(Exception e){
      System.out.println("Unable to create client");
    }
  }

  public Socket getSocket(){
    return socket;
  }

  public String getName(){
    return name;
  }


  // Listens for connections across the server
  public void test(){
    Scanner scan = new Scanner(System.in);
    while (true){
      System.out.println("Type 'exit' to close the client: ");
      String input = scan.nextLine();
      if (input.equalsIgnoreCase("exit")){
        break;
      }
    }

    try{
      this.socket.close();
    }
    catch (Exception e){
      System.out.println("Error closing the client socket");
    }
    finally{
      scan.close();
      System.out.println("All resources closed!");
    }
  }
}
