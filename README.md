# TermBattle

## Table of Contents

- [Overview](#overview)
- [Installation](#installation)
- [Running the Application](#running-the-application)

## Overview
TermBattle is a real-time multiplayer game that runs entirely in the terminal. Players connect to a server and engage in battles using text-based commands. The game emphasizes strategy and quick thinking, offering a unique experience compared to traditional graphical games.

---



[Demo](https://github.com/user-attachments/assets/d5691d77-90fb-43cb-82ab-2c44e57e1ba2)



## Installation
Prerequisites:
- Java: JDK 21 or higher. Download JDK [here](https://www.oracle.com/java/technologies/downloads/).
- Apache Maven: For build and dependency management. Download Maven [here](https://maven.apache.org/download.cgi).
- PostgreSQL: For the server-side database. Download PostgreSQL [here](https://www.postgresql.org/download/) or sign up on Supabase for a free cloud database [here](https://supabase.com/)! 

To set up the project, follow these steps:

1. **Clone the Repository**:

    ```sh
    git clone https://github.com/ukpabik/termbattle.git
    cd termbattle
    ```

2. **Install Server Dependencies**:

    Navigate to the server directory and install the required Maven packages:
    ```sh
    cd term-battle-server
    mvn clean install
    ```


4. **Install Client Dependencies**:

    Navigate to the client directory and install the required Maven packages:
    ```sh
    cd ../term-battle-client
    mvn clean install
    ```


6. **Configure Environment Variables**:

    Create a `.env` file in the `term-battle-server` directory with the following variables:

    ```plaintext
    DB_URL = YOUR_DB_URL
    ```
    
## Running the Application

1. **Start the Server**:

    In the `term-battle-server` directory, start the server:

    ```sh
    mvn exec:java
    ```

2. **Start the Client**:

    In the `term-battle-client` directory, start the server:

    ```sh
    mvn exec:java
    ```
    
Once the client starts, you can type /help to see a list of all available commands.

## About This Project

I'm having a lot of fun developing **TermBattle**, and it's giving me a way to learn more about things I am interested in, including: 
- **Server/Client Connections:** Building an efficient communication system between a server and multiple clients to ensure smooth real-time interactions.
- **TCP Protocol:** Implementing reliable data transmission using TCP to handle the complexities of real-time multiplayer gaming.
- **Game Logic:** Designing and implementing the core mechanics that make battles strategic and engaging for players.
- **Databases:** Utilizing SQL systems, specifically PostgreSQL, to manage and store persistent server data efficiently.
