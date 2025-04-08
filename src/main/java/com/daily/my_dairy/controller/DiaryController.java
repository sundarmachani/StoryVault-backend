package com.daily.my_dairy.controller;

import com.daily.my_dairy.model.Diary;
import com.daily.my_dairy.model.DiaryVM;
import com.daily.my_dairy.service.DiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class DiaryController {

  private final DiaryService diaryService;

  public DiaryController(DiaryService diaryService) {
    this.diaryService = diaryService;
  }

  @PostMapping("/new-entry")
  public ResponseEntity<DiaryVM> createNewEntry(@RequestBody Diary diary) {
    DiaryVM diaryVM = diaryService.newDiaryEntry(diary);
    URI location = URI.create("/get-entry/" + diaryVM.getId());
    return ResponseEntity.created(location).body(diaryVM);
  }

  @GetMapping("/get-entry/{id}")
  public DiaryVM getEntryById(@PathVariable int id) {
    return diaryService.getById(id);
  }

  @GetMapping("/get-all")
  public List<DiaryVM> getAllEntries() {
    return diaryService.getAllByCurrentUser();
  }

  @PutMapping("/update-entry")
  public DiaryVM updateEntry(@RequestBody Diary diary) {
    return diaryService.updateEntry(diary);
  }

  @DeleteMapping("/delete-entry/{id}")
  public ResponseEntity<Object> deleteEntryById(@PathVariable int id) {
    diaryService.deleteEntryById(id);
    return ResponseEntity.ok().body("Success");
  }
}