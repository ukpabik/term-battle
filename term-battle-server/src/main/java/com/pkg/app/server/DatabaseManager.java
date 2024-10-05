package com.pkg.app.server;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public abstract class DatabaseManager {
  private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
  private static String url = dotenv.get("DB_URL");
  private static Connection con;


  // Connects to the database
  public static Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url);
  }


  // Closes the database connection
  public void closeConnection(Connection connection) throws SQLException {
    connection.close();
  }

  public static void createMonsterTable() throws SQLException {
    con = getConnection();
    con.createStatement().execute("""
        CREATE TABLE IF NOT EXISTS monsters (
            monster_name VARCHAR(60) PRIMARY KEY,
            health INTEGER NOT NULL,
            attack INTEGER NOT NULL,
            speed INTEGER NOT NULL,
            type VARCHAR(50) NOT NULL
          );
        """);
  }

  public static void createUsersTable() throws SQLException {
    con = getConnection();
    con.createStatement().execute("""
        CREATE TABLE IF NOT EXISTS users (
            username VARCHAR(60) PRIMARY KEY,
            password VARCHAR(60) NOT NULL,
            wins INTEGER NOT NULL,
            losses INTEGER NOT NULL,
            games_played INTEGER NOT NULL,
            joined TIMESTAMP NOT NULL
          );
        """);
  }

  public static void dropMonstersTable() throws SQLException {
    con = getConnection();
    con.createStatement().execute("DROP TABLE IF EXISTS monsters");
  }

  public static void dropUsersTable() throws SQLException {
    con = getConnection();
    con.createStatement().execute("DROP TABLE IF EXISTS users");
  }

  // Inserts a new monster into the database
  public static void insertMonster(String name, int health, int attack, int speed, String type) throws SQLException {
    con = getConnection();
    con.createStatement().execute("INSERT INTO monsters VALUES ('" + name + "', " + health + ", " + attack + ", " + speed
        + ", '" + type + "')");
  }

  public static boolean validateUser(String name, String password) throws SQLException {
    con = getConnection(); 
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        // Check if the user already exists
        String checkUserQuery = "SELECT * FROM users WHERE username = ?";
        stmt = con.prepareStatement(checkUserQuery);
        stmt.setString(1, name);
        rs = stmt.executeQuery();

        if (rs.next()) {
            // If user exists, validate password
            String storedPassword = rs.getString("password");
            return storedPassword.equals(password);  // Plain text password check (but see note on hashing)
        } else {
            // If user doesn't exist, create a new user
            String insertUserQuery = """
                INSERT INTO users (username, password, wins, losses, games_played, joined)
                VALUES (?, ?, 0, 0, 0, ?)
            """;
            PreparedStatement insertStmt = con.prepareStatement(insertUserQuery);
            insertStmt.setString(1, name);
            insertStmt.setString(2, password); // Consider hashing before storing
            insertStmt.setTimestamp(3, Timestamp.from(Instant.now())); // Set the joined time
            insertStmt.executeUpdate();
            System.out.println("New user created: " + name);
            return true;  
        }
    } finally {
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();
        if (con != null) con.close();
    }
}}
