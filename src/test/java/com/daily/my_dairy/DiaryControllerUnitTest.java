package com.daily.my_dairy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CREATED;

import com.daily.my_dairy.controller.DiaryController;
import com.daily.my_dairy.model.Diary;
import com.daily.my_dairy.model.DiaryVM;
import com.daily.my_dairy.service.DiaryService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

//@SpringBootTest
public class DiaryControllerUnitTest {

  @InjectMocks
  private DiaryController diaryController;

  @Mock
  private DiaryService diaryService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createNewEntry_ShouldReturnCreatedResponse_WhenValidDiaryProvided() {
    Diary inputDiary = new Diary(0, "Monday", "Monday's story", "2025-01-27");
    DiaryVM expectedDiaryVM = new DiaryVM(1, "Monday", "Monday's story", "2025-01-27");
    when(diaryService.newDairyEntry(inputDiary)).thenReturn(expectedDiaryVM);
    ResponseEntity<DiaryVM> response = diaryController.createNewEntry(inputDiary);
    assertEquals(CREATED, response.getStatusCode());
    assertEquals("/get-entry/1", response.getHeaders().getLocation().toString());
    assertEquals(expectedDiaryVM, response.getBody());
  }

  @Test
  void getEntryById_ShouldReturnCreatedResponse_WhenValidIdProvided() {
    Diary inputDiary = new Diary(1, "Monday", "Monday's story", "2025-01-27");
    DiaryVM expectedDiaryVM = new DiaryVM(1, "Monday", "Monday's story", "2025-01-27");
    when(diaryService.getById(inputDiary.getId())).thenReturn(expectedDiaryVM);
    DiaryVM response = diaryController.getEntryById(inputDiary.getId());
    assertEquals(1, response.getId());
    assertEquals("Monday", response.getDay());
  }

  @Test
  void getAll_ShouldReturnDiaryList() {
    List<DiaryVM> diaryList = new ArrayList<>();
    DiaryVM diary1 = new DiaryVM(1, "Monday", "Monday's story", "2025-01-27");
    DiaryVM diary2 = new DiaryVM(2, "Tuesday", "Tuesday's story", "2025-01-28");
    DiaryVM diary3 = new DiaryVM(3, "Wednesday", "Wednesday's story", "2025-01-29");
    diaryList.add(diary1);
    diaryList.add(diary2);
    diaryList.add(diary3);
    when(diaryService.getAll()).thenReturn(diaryList);
    List<DiaryVM> diaryVMList = diaryController.getAllEntries();
    assertEquals(3, diaryVMList.size());
    assertEquals(1, diaryVMList.get(0).getId());
  }

  @Test
  void updateEntry_ShouldReturnCreatedResponse_WhenValidIdProvided() {
    Diary inputDiary = new Diary(1, "Monday", "Monday's story", "2025-01-27");
    DiaryVM expectedDiaryVM = new DiaryVM(1, "Monday", "Monday's story", "2025-01-27");
    when(diaryService.updateEntry(inputDiary)).thenReturn(expectedDiaryVM);
    DiaryVM response = diaryController.updateEntry(inputDiary);
    assertEquals(1, response.getId());
    assertEquals("Monday", response.getDay());
  }

  @Test
  void deleteEntry_ShouldReturnCreatedResponse_WhenValidIdProvided() {
    Diary inputDiary = new Diary(1, "Monday", "Monday's story", "2025-01-27");
    ResponseEntity<Object> response = diaryController.deleteEntryById(inputDiary.getId());
    assertEquals("Success", response.getBody());
  }

  @Test
  void updateEntry_ShouldThrowException_WhenInvalidDateProvided() {
    Diary invalidDiary = new Diary(1, "Monday", "Invalid update", "invalid-date");
    when(diaryService.updateEntry(invalidDiary)).thenThrow(new RuntimeException("Invalid date"));
    assertThrows(RuntimeException.class, () -> diaryController.updateEntry(invalidDiary));
  }

  @Test
  void getAll_ShouldReturnEmptyList_WhenNoEntriesExist() {
    when(diaryService.getAll()).thenReturn(new ArrayList<>());
    List<DiaryVM> diaryVMList = diaryController.getAllEntries();
    assertEquals(0, diaryVMList.size());
  }

}