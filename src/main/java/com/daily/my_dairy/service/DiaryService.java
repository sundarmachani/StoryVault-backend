package com.daily.my_dairy.service;

import com.daily.my_dairy.Repository.DiaryRepository;
import com.daily.my_dairy.custom.ResourceNotFoundException;
import com.daily.my_dairy.model.Diary;
import com.daily.my_dairy.model.DiaryVM;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class DiaryService {

  DiaryRepository dairyRepository;

  public DiaryService(DiaryRepository dairyRepository) {
    this.dairyRepository = dairyRepository;
  }

  public DiaryVM newDairyEntry(Diary dairy) {
    LocalDate date = LocalDate.parse(dairy.getEntryDate());
    String dayOfWeek = date.format(DateTimeFormatter.ofPattern("EEEE"));
    dairy.setDay(dayOfWeek);
    Diary dairyDb = dairyRepository.save(dairy);
    return new DiaryVM(dairyDb.getId(), dairyDb.getDay(), dairyDb.getDescription(),
        dairyDb.getEntryDate());
  }

  public DiaryVM getById(int id) {
    Diary dairyDb = dairyRepository.findById(id).orElseThrow(() ->
        new ResourceNotFoundException("Entry with id " + id + " not found!"));
    return new DiaryVM(dairyDb.getId(), dairyDb.getDay(), dairyDb.getDescription(),
        dairyDb.getEntryDate());
  }

  public List<DiaryVM> getAll() {
    List<Diary> diaryList= dairyRepository.findAll();
    List<DiaryVM> diaryListVM = new ArrayList<>(diaryList.stream()
        .map(d -> new DiaryVM(d.getId(), d.getDay(), d.getDescription(), d.getEntryDate()))
        .toList());
    diaryListVM.sort(Comparator.comparingInt(DiaryVM::getId).reversed());
    return diaryListVM;
  }

  public DiaryVM updateEntry(Diary dairy) {
    dairyRepository.findById(dairy.getId()).orElseThrow(() ->
        new ResourceNotFoundException("Entry with id " + dairy.getId() + " not found!"));
    LocalDate date = LocalDate.parse(dairy.getEntryDate());
    String dayOfWeek = date.format(DateTimeFormatter.ofPattern("EEEE"));
    dairy.setDay(dayOfWeek);
    Diary dairyDb = dairyRepository.save(dairy);
    return new DiaryVM(dairyDb.getId(), dairyDb.getDay(), dairyDb.getDescription(),
        dairyDb.getEntryDate());
  }

  public void deleteEntryById(int id) {
    dairyRepository.findById(id).orElseThrow(() ->
        new ResourceNotFoundException("Entry with id " + id + " not found!"));
    dairyRepository.deleteById(id);
  }
}
