package com.pkg.app.client;

import java.net.*;
import java.io.*;
import java.util.Scanner;
public class Client {
  private Socket socket;
  private String name;
  private BufferedReader bf;
  private PrintWriter pw;


  //Initialize the client
  public Client(String address, int port, String name){
    try {
            this.socket = new Socket(address, port);
            this.name = name;

            // Initialize reader and writer
            this.bf = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.pw = new PrintWriter(socket.getOutputStream(), true);

            System.out.println("Yay! Client created");

            // Start a thread to listen for messages from the server
            new Thread(new ListenFromServer()).start();

            // Send the client's name to the server
            pw.println(name);

        } catch (Exception e) {
            System.out.println("Unable to create client: " + e.getMessage());
        }
  }

  // Listens for messages from the server 
  private class ListenFromServer implements Runnable {
        public void run() {
            String message;
            try {
                while ((message = bf.readLine()) != null) {
                    System.out.println(message);
                }
            } catch (IOException e) {
                System.out.println("Connection closed.");
            }
        }
    }




  public Socket getSocket(){
    return socket;
  }

  public String getName(){
    return name;
  }


  // Listens for connections across the server
  public void start() {
        Scanner scan = new Scanner(System.in);
        System.out.println("You can start typing messages...");

        while (true) {
            String input = scan.nextLine();
            if (input.equalsIgnoreCase("exit")) {
                break;
            }
            pw.println(input); 
        }

        try {
            socket.close();
        } catch (Exception e) {
            System.out.println("Error closing the client socket: " + e.getMessage());
        } finally {
            scan.close();
            System.out.println("All resources closed!");
        }
    }
}
