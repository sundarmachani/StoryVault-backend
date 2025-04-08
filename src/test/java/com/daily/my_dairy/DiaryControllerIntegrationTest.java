package com.daily.my_dairy;

import static org.junit.jupiter.api.Assertions.*;

import com.daily.my_dairy.Repository.DiaryRepository;
import com.daily.my_dairy.Repository.UserRepository;
import com.daily.my_dairy.controller.DiaryController;
import com.daily.my_dairy.model.Diary;
import com.daily.my_dairy.model.DiaryVM;
import com.daily.my_dairy.model.User;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootTest
@Transactional
class DiaryControllerIntegrationTest {

  @Autowired
  private DiaryController diaryController;

  @Autowired
  private DiaryRepository diaryRepository;

  @Autowired
  private UserRepository userRepository;

  private User user;
  private Diary testDiary;

  private void mockAuthenticatedUser(String username) {
    var auth = new UsernamePasswordAuthenticationToken(username, null, List.of(() -> "ROLE_USER"));
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  @BeforeEach
  void setUp() {
    // Ensure user exists
    user = userRepository.findByUsername("test@example.com");
    if (user == null) {
      user = new User("test@example.com", "$2a$10$wH3tXPo4cK0DFz/VB9vdFOyHF0DbiEBjpl0WtvMNZj1/Yfr/ZybXG"); // hashed "Test@1234"
      user = userRepository.save(user);
    }

    // Mock authentication
    mockAuthenticatedUser(user.getUsername());

    // Create a diary for the user
    testDiary = new Diary(0, "Monday", "Monday's story", "2025-01-27", user);
    diaryRepository.save(testDiary);
  }

  @Test
  void createNewEntry_ShouldReturnCreated_WhenValidDiaryProvided() {
    Diary inputDiary = new Diary(0, null, "New story", "2025-01-27", null);
    ResponseEntity<DiaryVM> response = diaryController.createNewEntry(inputDiary);
    assertEquals(201, response.getStatusCode().value());
    assertNotNull(response.getBody());
    assertEquals("Monday", response.getBody().getDay());
  }

  @Test
  void getEntryById_ShouldReturnDiaryVM_WhenValidIdProvided() {
    DiaryVM diaryVM = diaryController.getEntryById(testDiary.getId());
    assertNotNull(diaryVM);
    assertEquals(testDiary.getId(), diaryVM.getId());
    assertEquals("Monday", diaryVM.getDay());
  }

  @Test
  void getEntryById_ShouldThrow_WhenInvalidIdProvided() {
    assertThrows(RuntimeException.class, () -> diaryController.getEntryById(9999));
  }

  @Test
  void createNewEntry_ShouldThrow_WhenInvalidDate() {
    Diary invalidDiary = new Diary(0, null, "Invalid date", "invalid-date", null);
    assertThrows(RuntimeException.class, () -> diaryController.createNewEntry(invalidDiary));
  }

  @Test
  void getAll_ShouldReturnListOfEntries() {
    List<DiaryVM> diaryList = diaryController.getAllEntries();
    assertNotNull(diaryList);
    assertFalse(diaryList.isEmpty());
  }

  @Test
  void updateEntry_ShouldReturnUpdatedDiary() {
    testDiary.setEntryDate("2025-01-28");
    DiaryVM diaryVM = diaryController.updateEntry(testDiary);
    assertNotNull(diaryVM);
    assertEquals("Tuesday", diaryVM.getDay());
  }

  @Test
  void updateEntry_ShouldThrow_WhenIdNotFound() {
    testDiary.setId(999);
    assertThrows(RuntimeException.class, () -> diaryController.updateEntry(testDiary));
  }

  @Test
  void deleteEntry_ShouldReturnSuccess_WhenValidId() {
    ResponseEntity<Object> response = diaryController.deleteEntryById(testDiary.getId());
    assertNotNull(response);
    assertEquals(200, response.getStatusCode().value());
    assertEquals("Success", response.getBody());
  }

  @Test
  void deleteEntry_ShouldThrow_WhenInvalidId() {
    assertThrows(RuntimeException.class, () -> diaryController.deleteEntryById(9999));
  }

  @Test
  void updateEntry_ShouldThrow_WhenInvalidDate() {
    testDiary.setEntryDate("not-a-date");
    assertThrows(RuntimeException.class, () -> diaryController.updateEntry(testDiary));
  }

  @Test
  void getAll_ShouldReturnEmptyList_WhenNoEntries() {
    diaryRepository.deleteAll();
    List<DiaryVM> diaryList = diaryController.getAllEntries();
    assertEquals(0, diaryList.size());
  }
}