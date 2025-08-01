package com.example.sebloom.repository;

import com.example.sebloom.model.Progress;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgressRepository extends JpaRepository<Progress, Long> {
    List<Progress> findByTopicOrderByAttemptDateDesc(String topic);
}