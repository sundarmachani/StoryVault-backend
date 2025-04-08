package com.daily.my_dairy.service;

import com.daily.my_dairy.Repository.DiaryRepository;
import com.daily.my_dairy.Repository.UserRepository;
import com.daily.my_dairy.custom.ResourceNotFoundException;
import com.daily.my_dairy.model.Diary;
import com.daily.my_dairy.model.DiaryVM;
import com.daily.my_dairy.model.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiaryService {

  private final DiaryRepository diaryRepository;
  private final UserRepository userRepository;

  public DiaryService(DiaryRepository diaryRepository, UserRepository userRepository) {
    this.diaryRepository = diaryRepository;
    this.userRepository = userRepository;
  }

  public DiaryVM newDiaryEntry(Diary diary) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new ResourceNotFoundException("User not found");
    }

    LocalDate date = LocalDate.parse(diary.getEntryDate());
    String dayOfWeek = date.format(DateTimeFormatter.ofPattern("EEEE"));
    diary.setDay(dayOfWeek);
    diary.setUser(user);
    Diary diaryDb = diaryRepository.save(diary);

    return new DiaryVM(diaryDb.getId(), diaryDb.getDay(), diaryDb.getDescription(),
        diaryDb.getEntryDate());
  }

  public DiaryVM getById(int id) {
    Diary diaryDb = diaryRepository.findById(id).orElseThrow(() ->
        new ResourceNotFoundException("Entry with id " + id + " not found!"));
    return new DiaryVM(diaryDb.getId(), diaryDb.getDay(), diaryDb.getDescription(),
        diaryDb.getEntryDate());
  }

  public List<DiaryVM> getAllByCurrentUser() {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findByUsername(username);
    if (user == null) {
      throw new RuntimeException("User not found");
    }

    List<Diary> diaryList = diaryRepository.findByUser(user);
    return diaryList.stream()
        .map(d -> new DiaryVM(d.getId(), d.getDay(), d.getDescription(), d.getEntryDate()))
        .sorted(Comparator.comparingInt(DiaryVM::getId).reversed())
        .toList();
  }

  public DiaryVM updateEntry(Diary diary) {
    diaryRepository.findById(diary.getId()).orElseThrow(() ->
        new ResourceNotFoundException("Entry with id " + diary.getId() + " not found!"));
    LocalDate date = LocalDate.parse(diary.getEntryDate());
    String dayOfWeek = date.format(DateTimeFormatter.ofPattern("EEEE"));
    diary.setDay(dayOfWeek);
    Diary diaryDb = diaryRepository.save(diary);
    return new DiaryVM(diaryDb.getId(), diaryDb.getDay(), diaryDb.getDescription(),
        diaryDb.getEntryDate());
  }

  public void deleteEntryById(int id) {
    diaryRepository.findById(id).orElseThrow(() ->
        new ResourceNotFoundException("Entry with id " + id + " not found!"));
    diaryRepository.deleteById(id);
  }
}