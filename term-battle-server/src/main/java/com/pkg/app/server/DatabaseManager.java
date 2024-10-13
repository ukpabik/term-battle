package com.pkg.app.server;

import io.github.cdimascio.dotenv.Dotenv;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.pkg.app.party.monster.Monster;
import com.pkg.app.party.monster.Type;
import com.pkg.app.server.auth.PasswordManager;
import com.pkg.app.server.text.AnsiText;


public abstract class DatabaseManager {
  private static final ThreadLocal<Connection> threadConnection = new ThreadLocal<>();
  private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
  private static String url = dotenv.get("DB_URL");


  // Connects to the database
  public static Connection getConnection() throws SQLException {
    Connection con = threadConnection.get();
    if (con == null || con.isClosed()){
      con = DriverManager.getConnection(url);
      threadConnection.set(con);
    }

    return con;
  }


  // Closes the database connection
  public void closeConnection(Connection connection) throws SQLException {
    Connection con = threadConnection.get();
    if (con != null && !con.isClosed()){
      con.close();
      threadConnection.remove();
    }
  }


  public static void createMonsterTable() throws SQLException {
    String query = """
      CREATE TABLE IF NOT EXISTS monsters (
          monster_name VARCHAR(60) PRIMARY KEY,
          health INTEGER NOT NULL,
          attack INTEGER NOT NULL,
          speed INTEGER NOT NULL,
          type VARCHAR(50) NOT NULL
          );
    """;

    try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
      stmt.execute();
    }

  }

  public static void createUsersTable() throws SQLException {
    String query = """
      CREATE TABLE IF NOT EXISTS users (
          username VARCHAR(60) PRIMARY KEY,
          password VARCHAR(60) NOT NULL,
          wins INTEGER NOT NULL,
          losses INTEGER NOT NULL,
          games_played INTEGER NOT NULL,
          joined TIMESTAMP NOT NULL
          );
    """;

    try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
      stmt.execute();
    }

  }

  public static void dropMonstersTable() throws SQLException {
    String query = "DROP TABLE IF EXISTS monsters";

    try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
      stmt.execute();
    }
  }

  public static void dropUsersTable() throws SQLException {
    String query = "DROP TABLE IF EXISTS users";

    try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
      stmt.execute();
    }
  }

  // Inserts a new monster into the database
  public static void insertMonster(String name, int health, int attack, int speed, String type) throws SQLException {
    String query = "INSERT INTO monsters (monster_name, health, attack, speed, type) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
      pstmt.setString(1, name);
      pstmt.setInt(2, health);
      pstmt.setInt(3, attack);
      pstmt.setInt(4, speed);
      pstmt.setString(5, type);
      pstmt.executeUpdate();
    } 
    catch (SQLException e) {
      System.err.println("Error inserting monster: " + name);
      e.printStackTrace();
    }
  }

  // Inserts a list of monsters into the database
  public static void insertMonsters(List<Monster> monsters) throws SQLException {
    String query = "INSERT INTO monsters (monster_name, health, attack, speed, type) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement pstmt = getConnection().prepareStatement(query)) {
      for (Monster monster : monsters) {
        pstmt.setString(1, monster.getName());
        pstmt.setInt(2, monster.getHealth());
        pstmt.setInt(3, monster.getAttack());
        pstmt.setInt(4, monster.getSpeed());
        pstmt.setString(5, monster.getType().getName());
        pstmt.addBatch();
      }
      pstmt.executeBatch();
    } 
    catch (SQLException e) {
      System.err.println("Error inserting monsters");
      e.printStackTrace();
    }  
  }

  // Checks if a user exists in the database, if not creates a new user
  public static boolean validateUser(String name, String password) {
    String checkUserQuery = "SELECT password FROM users WHERE username = ?";
    String insertUserQuery = """
      INSERT INTO users (username, password, wins, losses, games_played, joined)
      VALUES (?, ?, 0, 0, 0, ?)
      """;
    try (PreparedStatement stmt = getConnection().prepareStatement(checkUserQuery)) {

      stmt.setString(1, name);
      try (ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
          // If user exists, validate password
          String storedPassword = rs.getString("password");
          boolean passed = PasswordManager.compare(storedPassword, password);
          if (passed) {
            System.out.println(AnsiText.color("Successfully authenticated: " + name, AnsiText.GREEN));
          } 
          return passed;
        } 
        else {
          // If user doesn't exist, create a new user
          try (PreparedStatement insertStmt = getConnection().prepareStatement(insertUserQuery)) {
            insertStmt.setString(1, name);
            insertStmt.setString(2, PasswordManager.hash(password));
            insertStmt.setTimestamp(3, Timestamp.from(Instant.now()));
            insertStmt.executeUpdate();
            System.out.println(AnsiText.color("New user created: " + name, AnsiText.GREEN));
            return true;
          }
        }
      }
    } 
    catch (SQLException e) {
      System.err.println("Error validating user: " + name);
      e.printStackTrace();
      return false;
    }
  }

  // Returns a list of 5 random monsters
  public static List<Monster> getRandomMonsters() {
    List<Monster> monsters = new ArrayList<>();
    String query = "SELECT * FROM monsters ORDER BY RANDOM() LIMIT 4";
    try (PreparedStatement stmt = getConnection().prepareStatement(query);
        ResultSet rs = stmt.executeQuery()) {
      while (rs.next()) {
        String name = rs.getString("monster_name");
        int health = rs.getInt("health");
        int attack = rs.getInt("attack");
        int speed = rs.getInt("speed");
        String type = rs.getString("type");
        monsters.add(new Monster(name, health, attack, speed, new Type(type)));
      }
    } 
    catch (SQLException e) {
      System.err.println("Error fetching random monsters");
      e.printStackTrace();
    }
    return monsters;
  }


  public static void addWinner(String name) {
    String query = "UPDATE users SET wins = wins + 1, games_played = games_played + 1 WHERE username = ?";
    try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
      stmt.setString(1, name);
      stmt.executeUpdate();
      Logger.info(name + " won their game!");
    } 
    catch (SQLException e) {
      System.err.println("Error updating winner stats for user: " + name);
      e.printStackTrace();
    }
  }
  public static void addLoser(String name) {
    String query = "UPDATE users SET losses = losses + 1, games_played = games_played + 1 WHERE username = ?";
    try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
      stmt.setString(1, name);
      stmt.executeUpdate();
      Logger.info(name + " lost their game!");
    } 
    catch (SQLException e) {
      System.err.println("Error updating loser stats for user: " + name);
      e.printStackTrace();
    }
  }
}
