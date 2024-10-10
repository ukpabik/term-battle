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
    String query = "INSERT INTO monsters (monster_name, health, attack, speed, type) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement pstmt = con.prepareStatement(query)) {
      pstmt.setString(1, name);
      pstmt.setInt(2, health);
      pstmt.setInt(3, attack);
      pstmt.setInt(4, speed);
      pstmt.setString(5, type);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.err.println("Error inserting monster: " + name);
      e.printStackTrace();
    }
  }

  // Inserts a list of monsters into the database
  public static void insertMonsters(List<Monster> monsters) throws SQLException {
    con = getConnection();
    for (Monster monster : monsters) {
      String query = "INSERT INTO monsters (monster_name, health, attack, speed, type) VALUES (?, ?, ?, ?, ?)";
      try (PreparedStatement pstmt = con.prepareStatement(query)) {
        pstmt.setString(1, monster.getName());
        pstmt.setInt(2, monster.getHealth());
        pstmt.setInt(3, monster.getAttack());
        pstmt.setInt(4, monster.getSpeed());
        pstmt.setString(5, monster.getType().getName());
        pstmt.executeUpdate();
      } catch (SQLException e) {
        System.err.println("Error inserting monster: " + monster.getName());
        e.printStackTrace();
      }
    }
  }

  // Checks if a user exists in the database, if not creates a new user
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
        // Here we need to unhash and check the password
        // Check if the stored password unhashed == the user inputted password
        boolean passed = PasswordManager.compare(storedPassword, password);
        if (passed){
          System.out.println(AnsiText.color("Successfully authenticated: " + name, AnsiText.GREEN));
        }
        else{
          System.out.println(AnsiText.color("Authentication failed: " + name, AnsiText.RED));
        }
        return passed;
      } else {
        // If user doesn't exist, create a new user
        String insertUserQuery = """
          INSERT INTO users (username, password, wins, losses, games_played, joined)
          VALUES (?, ?, 0, 0, 0, ?)
          """;
        PreparedStatement insertStmt = con.prepareStatement(insertUserQuery);
        insertStmt.setString(1, name);
        insertStmt.setString(2, PasswordManager.hash(password)); 
        insertStmt.setTimestamp(3, Timestamp.from(Instant.now()));
        insertStmt.executeUpdate();
        System.out.println(AnsiText.color("New user created: " + name, AnsiText.GREEN));
        return true;  
      }
    } finally {
      if (rs != null) rs.close();
      if (stmt != null) stmt.close();
      if (con != null) con.close();
    }
  }

  // Returns a list of 5 random monsters
  public static List<Monster> getRandomMonsters() {
    List<Monster> monsters = new ArrayList<>();
    try {
      con = getConnection();
      PreparedStatement stmt = con.prepareStatement("SELECT * FROM monsters ORDER BY RANDOM() LIMIT 5");
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        String name = rs.getString("monster_name");
        int health = rs.getInt("health");
        int attack = rs.getInt("attack");
        int speed = rs.getInt("speed");
        String type = rs.getString("type");
        monsters.add(new Monster(name, health, attack, speed, new Type(type)));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return monsters;
  }
}
