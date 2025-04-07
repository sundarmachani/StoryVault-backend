package com.daily.my_dairy.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class DiaryVM {
  @Id
  @GeneratedValue
  private int id;

  private String day;

  private String description;

  private String entryDate;

  public DiaryVM() {
  }

  public DiaryVM(int id, String day, String description, String date) {
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
    return "DiaryVM{" +
        "id=" + id +
        ", day='" + day + '\'' +
        ", description='" + description + '\'' +
        ", entryDate='" + entryDate + '\'' +
        '}';
  }
}
