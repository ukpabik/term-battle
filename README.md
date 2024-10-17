# TermBattle



## Overview
TermBattle is a real-time multiplayer game that runs entirely in the terminal. Players connect to a server and engage in battles using text-based commands. The game emphasizes strategy and quick thinking, offering a unique experience compared to traditional graphical games.

## Table of Contents
- [Demo](#demo)
- [Setup](#setup)
- [Prerequisites](#prerequisites)
- [Running the Application](#running-the-application)

---



## Demo
[Demo](https://github.com/user-attachments/assets/d5691d77-90fb-43cb-82ab-2c44e57e1ba2)

---


## Setup
### Prerequisites:
- **Docker & Docker Compose:** [Install Docker](https://www.docker.com/get-started)
- **PostgreSQL:** For the server-side database. Download PostgreSQL [here](https://www.postgresql.org/download/) or sign up on Supabase for a free cloud database [here](https://supabase.com/)!

Optional (Manual Installation)
- **Java:** JDK 21 or higher. Download JDK [here](https://www.oracle.com/java/technologies/downloads/).
- **Apache Maven:** For build and dependency management. Download Maven [here](https://maven.apache.org/download.cgi).

To set up the project, follow these steps:

1. **Clone the Repository**:

    ```sh
    git clone https://github.com/ukpabik/termbattle.git
    cd termbattle
    ```

2. **Choose your Setup Method**:

- [A. Docker-Based Setup](#a-docker-based-setup)
- [B. Manual Setup](#b-manual-setup)
  

#### A. Docker-Based Setup

1. **Configure Environment Variables:**

    Create a `.env` file in the `term-battle-server` directory:

    ```bash
    cd term-battle-server
    touch .env
    ```

    Add the following to `.env`:

    ```env
    DB_URL=YOUR_DB_URL
    ```

    Replace `YOUR_DB_URL` with your actual PostgreSQL connection string.

#### B. Manual Setup

1. **Install Server Dependencies:**

    ```bash
    cd term-battle-server
    mvn clean install
    ```

2. **Install Client Dependencies:**

    ```bash
    cd ../term-battle-client
    mvn clean install
    ```

3. **Configure Environment Variables:**

    Create a `.env` file in the `term-battle-server` directory:

    ```bash
    cd ../term-battle-server
    touch .env
    ```

    Add the following to `.env`:

    ```env
    DB_URL=YOUR_DB_URL
    ```

    Replace `YOUR_DB_URL` with your actual PostgreSQL connection string.

## Running the Application

### A. Using Docker Compose

1. **Start the Server in Detached Mode:**

    ```bash
    docker-compose up -d server
    ```

2. **Run the Client Interactively:**

    ```bash
    docker-compose run --rm client
    ```

    **Explanation:**
    - The server runs in the background.
    - The client runs interactively, allowing you to type commands.

3. **Stop the Application:**

    ```bash
    docker-compose down
    ```

### B. Manual Execution

1. **Start the Server:**

    ```bash
    cd term-battle-server
    mvn exec:java
    ```

2. **Start the Client:**

    Open a new terminal window/tab:

    ```bash
    cd term-battle-client
    mvn exec:java
    ```

    **Note:** Once the client starts, type `/help` to see a list of all available commands.
## About This Project

I'm having a lot of fun developing **TermBattle**, and it's giving me a way to learn more about things I am interested in, including: 
- **Server/Client Connections:** Building an efficient communication system between a server and multiple clients to ensure smooth real-time interactions.
- **TCP Protocol:** Implementing reliable data transmission using TCP to handle the complexities of real-time multiplayer gaming.
- **Game Logic:** Designing and implementing the core mechanics that make battles strategic and engaging for players.
- **Databases:** Utilizing SQL systems, specifically PostgreSQL, to manage and store persistent server data efficiently.
