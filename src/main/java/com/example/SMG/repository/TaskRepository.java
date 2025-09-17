package com.example.SMG.repository;

import com.example.SMG.repository.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByLimitDateBetweenAndContentContainingAndStatus(Timestamp start,
                                                                   Timestamp end,
                                                                   String keyword,
                                                                   int status);

    List<Task> findByLimitDateBetweenAndContentContaining(Timestamp start,
                                                          Timestamp end,
                                                          String keyword);
}
