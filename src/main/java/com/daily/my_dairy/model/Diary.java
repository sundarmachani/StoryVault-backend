package com.daily.my_dairy.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Diary {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String day;

  private String description;

  private String entryDate;

  public Diary() {
  }

  public Diary(int id, String day, String description, String date) {
    this.id = id;
    this.day = day;
    this.description = description;
    this.entryDate = date;
  }

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

  @Override
  public String toString() {
    return "Diary{" +
        "id=" + id +
        ", day='" + day + '\'' +
        ", description='" + description + '\'' +
        ", entryDate='" + entryDate + '\'' +
        '}';
  }
}
