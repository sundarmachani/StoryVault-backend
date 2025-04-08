package com.daily.my_dairy.service;

import com.daily.my_dairy.Repository.UserRepository;
import com.daily.my_dairy.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder passwordEncoder;

  private static final String PASSWORD_PATTERN =
      "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";

  public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User registerUser(User user) {
    if (userRepository.findByUsername(user.getUsername()) != null) {
      throw new RuntimeException("Username already exists");
    }

    if (!isPasswordStrong(user.getPassword())) {
      throw new RuntimeException("Password is too weak. It must contain at least 8 characters, " +
          "including one uppercase letter, one lowercase letter, one digit, and one special character.");
    }

    String encodedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);

    return userRepository.save(user);
  }

  private boolean isPasswordStrong(String password) {
    return password.matches(PASSWORD_PATTERN);
  }
}