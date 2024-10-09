package com.pkg.app.server.auth;

import at.favre.lib.crypto.bcrypt.BCrypt;

// Util class for handling password safety and authentication
public class PasswordManager {

  public static String hash(String password){
    if (password.length() > 0){
      String hashedString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
      return hashedString;
    }
    return "";
  }

  public static boolean compare(String hashedPassword, String unhashedPassword){
    if (unhashedPassword.length() > 0){
      BCrypt.Result result = BCrypt.verifyer().verify(unhashedPassword.toCharArray(), hashedPassword);
      return result.verified;
    }
    return false;
  }
}
