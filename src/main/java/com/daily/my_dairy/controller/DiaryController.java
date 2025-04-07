package com.daily.my_dairy.controller;

import com.daily.my_dairy.model.Diary;
import com.daily.my_dairy.model.DiaryVM;
import com.daily.my_dairy.service.DiaryService;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiaryController {

  DiaryService dairyService;

  public DiaryController(DiaryService dairyService) {
    this.dairyService = dairyService;
  }

  @PostMapping("/new-entry")
  public ResponseEntity<DiaryVM> createNewEntry(@RequestBody Diary dairy) {

    DiaryVM dairyVM = dairyService.newDairyEntry(dairy);
    URI location = URI.create("/get-entry/" + dairyVM.getId());
    return ResponseEntity.created(location).body(dairyVM);
  }

  @GetMapping("/get-entry/{id}")
  public DiaryVM getEntryById(@PathVariable int id) {
    return dairyService.getById(id);
  }

  @GetMapping("/get-all")
  public List<DiaryVM> getAllEntries() {
    return dairyService.getAll();
  }

  @PutMapping("/update-entry")
  public DiaryVM updateEntry(@RequestBody Diary dairy) {
    return dairyService.updateEntry(dairy);
  }

  @DeleteMapping("/delete-entry/{id}")
  public ResponseEntity<Object> deleteEntryById(@PathVariable int id) {
    dairyService.deleteEntryById(id);
    return ResponseEntity.ok().body("Success");
  }

}
