package com.example.SMG.repository;

import com.example.SMG.repository.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    List<Task> findByLimitDateBetweenAndStatusAndContentContaining(Timestamp start,
                                                                   Timestamp end,
                                                                   int status,
                                                                   String keyword);
}
