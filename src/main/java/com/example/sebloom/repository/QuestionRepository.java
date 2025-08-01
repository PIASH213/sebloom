package com.example.sebloom.repository;

import com.example.sebloom.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByTopicAndBloomLevel(String topic, String bloomLevel);
}