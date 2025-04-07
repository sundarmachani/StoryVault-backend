//package com.daily.my_dairy;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import com.daily.my_dairy.Repository.DiaryRepository;
//import com.daily.my_dairy.controller.DiaryController;
//import com.daily.my_dairy.model.Diary;
//import com.daily.my_dairy.model.DiaryVM;
//import jakarta.transaction.Transactional;
//import java.util.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.ResponseEntity;
//
//@SpringBootTest
//@Transactional
//class DiaryControllerIntegrationTest {
//
//  @Autowired
//  private DiaryController diaryController;
//
//  @Autowired
//  private DiaryRepository diaryRepository;
//
//  private Diary testDiary;
//
//  @BeforeEach
//  void setUp() {
//    testDiary = new Diary(0, "Monday", "Monday's story", "2025-01-27");
//    diaryRepository.save(testDiary);
//  }
//
//  @Test
//  void createNewEntry_FullFlowWithDatabase_ShouldReturnCreatedResponse_WhenValidDiaryProvided() {
//    Diary inputDiary = new Diary(0, null, "Monday's story", "2025-01-27");
//    ResponseEntity<DiaryVM> response = diaryController.createNewEntry(inputDiary);
//    assertEquals(201, response.getStatusCode().value());
//    assertNotNull(response.getBody());
//    assertEquals("Monday", response.getBody().getDay());
//  }
//
//  @Test
//  void getEntryById_FullFlowWithDatabase_ShouldReturn_Diary() {
//    DiaryVM diaryVM = diaryController.getEntryById(testDiary.getId());
//    assertNotNull(diaryVM);
//    assertEquals(testDiary.getId(), diaryVM.getId());
//    assertEquals("Monday", diaryVM.getDay());
//  }
//
//  @Test
//  void getEntryById_ShouldThrowException_WhenEntryDoesNotExist() {
//    assertThrows(RuntimeException.class, () -> diaryController.getEntryById(9999));
//  }
//
//  @Test
//  void createNewEntry_ShouldFail_WhenInvalidDateProvided() {
//    Diary invalidDiary = new Diary(0, null, "Invalid date story", "invalid-date");
//    assertThrows(RuntimeException.class, () -> diaryController.createNewEntry(invalidDiary));
//  }
//
//  @Test
//  void getAll_FullFlowWithDatabase_ShouldReturn_AllEntries() {
//    List<DiaryVM> diaryList = diaryController.getAllEntries();
//    System.out.println(diaryList);
//    assertNotNull(diaryList);
//    assertFalse(diaryList.isEmpty());
//  }
//
//  @Test
//  void updateEntry_FullFlowWithDatabase_ShouldReturn_UpdatedEntry() {
//    testDiary.setEntryDate("2025-01-28");
//    DiaryVM diaryVM = diaryController.updateEntry(testDiary);
//    assertNotNull(diaryVM);
//    assertEquals("Tuesday",diaryVM.getDay());
//  }
//
//  @Test
//  void updateEntry_FullFlowWithDatabase_ShouldReturn_Error() {
//    testDiary.setId(999);
//    assertThrows(RuntimeException.class, () -> diaryController.updateEntry(testDiary));
//  }
//
//  @Test
//  void deleteEntry_FullFlowWithDatabase_ShouldReturn_success() {
//    ResponseEntity<Object> response = diaryController.deleteEntryById(testDiary.getId());
//    assertNotNull(response);
//    assertEquals(200,response.getStatusCode().value());
//    assertEquals("Success", response.getBody());
//  }
//
//  @Test
//  void deleteEntry_FullFlowWithDatabase_ShouldReturn_Failure() {
//    testDiary.setId(999);
//    assertThrows(RuntimeException.class, () -> diaryController.deleteEntryById(testDiary.getId()));
//  }
//
//  @Test
//  void updateEntry_ShouldFail_WhenInvalidDateProvided() {
//    testDiary.setEntryDate("invalid-date");
//    assertThrows(RuntimeException.class, () -> diaryController.updateEntry(testDiary));
//  }
//
//  @Test
//  void getAll_ShouldReturnEmptyList_WhenNoEntriesExist() {
//    diaryRepository.deleteAll();
//    List<DiaryVM> diaryList = diaryController.getAllEntries();
//    assertEquals(0, diaryList.size());
//  }
//}
