package com.daily.my_dairy.Repository;

import com.daily.my_dairy.model.Diary;
import com.daily.my_dairy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Integer> {
  List<Diary> findByUser(User user);
}