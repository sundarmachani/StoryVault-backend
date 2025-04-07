package com.daily.my_dairy.service;

import com.daily.my_dairy.Repository.UserRepository;
import com.daily.my_dairy.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  // Define a regular expression for password strength:
  // At least 8 characters, one uppercase, one lowercase, one digit, and one special character.
  private static final String PASSWORD_PATTERN =
      "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";

  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public User registerUser(User user) {
    // Check if username already exists
    if (userRepository.findByUsername(user.getUsername()) != null) {
      throw new RuntimeException("Username already exists");
    }

    // Check password strength
    if (!isPasswordStrong(user.getPassword())) {
      throw new RuntimeException("Password is too weak. It must contain at least 8 characters, " +
          "including one uppercase letter, one lowercase letter, one digit, and one special character.");
    }

    // TODO: Encrypt the password before saving in production environments.
    return userRepository.save(user);
  }

  private boolean isPasswordStrong(String password) {
    return password.matches(PASSWORD_PATTERN);
  }
}