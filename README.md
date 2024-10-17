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

To set up the project, follow these steps:

1. **Clone the Repository**:

    ```sh
    git clone https://github.com/ukpabik/termbattle.git
    cd termbattle
    ```

2. **Configure Environment Variables:**

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

3. **Downloading Dependencies**

   Navigate to the `docker-base` directory:

    ```bash
    cd docker-base
    docker build -t maven:3.9.9-jdk-21 .
    ```


## Running the Application

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


    **Note:** Once the client starts, type `/help` to see a list of all available commands.
## About This Project

I'm having a lot of fun developing **TermBattle**, and it's giving me a way to learn more about things I am interested in, including: 
- **Server/Client Connections:** Building an efficient communication system between a server and multiple clients to ensure smooth real-time interactions.
- **TCP Protocol:** Implementing reliable data transmission using TCP to handle the complexities of real-time multiplayer gaming.
- **Game Logic:** Designing and implementing the core mechanics that make battles strategic and engaging for players.
- **Databases:** Utilizing SQL systems, specifically PostgreSQL, to manage and store persistent server data efficiently.
