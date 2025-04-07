package com.daily.my_dairy.controller;

import com.daily.my_dairy.model.User;
import com.daily.my_dairy.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@RequestBody User user) {
    try {
      User newUser = userService.registerUser(user);
      return ResponseEntity.ok(newUser);
    } catch (RuntimeException ex) {
      return ResponseEntity.badRequest().body(ex.getMessage());
    }
  }
}