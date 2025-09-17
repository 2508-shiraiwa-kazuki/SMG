package com.example.SMG.repository;

import com.example.SMG.repository.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findTop1000ByLimitDateBetweenAndContentAndStatusOrderByLimitDateAsc(Timestamp start,
                                                                                   Timestamp end,
                                                                                   String keyword,
                                                                                   int status);

    List<Task> findTop1000ByLimitDateBetweenAndStatusOrderByLimitDateAsc(Timestamp start,
                                                                         Timestamp end,
                                                                         int status);

    List<Task> findTop1000ByLimitDateBetweenAndContentOrderByLimitDateAsc(Timestamp start,
                                                                          Timestamp end,
                                                                          String keyword);

    List<Task> findTop1000ByLimitDateBetweenOrderByLimitDateAsc(Timestamp start, Timestamp end);
}
