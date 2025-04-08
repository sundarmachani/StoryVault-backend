package com.daily.my_dairy.model;

import jakarta.persistence.*;

@Entity
public class Diary {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String day;

  private String description;

  private String entryDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public Diary() {
  }

  public Diary(int id, String day, String description, String entryDate, User user) {
    this.id = id;
    this.day = day;
    this.description = description;
    this.entryDate = entryDate;
    this.user = user;
  }

  // Getters and setters

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getDay() {
    return day;
  }

  public void setDay(String day) {
    this.day = day;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getEntryDate() {
    return entryDate;
  }

  public void setEntryDate(String entryDate) {
    this.entryDate = entryDate;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "Diary{" +
        "id=" + id +
        ", day='" + day + '\'' +
        ", description='" + description + '\'' +
        ", entryDate='" + entryDate + '\'' +
        ", user=" + (user != null ? user.getUsername() : "null") +
        '}';
  }
}