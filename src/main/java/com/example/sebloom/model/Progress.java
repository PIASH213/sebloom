package com.example.sebloom.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "progress")
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String topic;

    @Column(name = "bloom_level")
    private String bloomLevel;

    private int totalQuestions;
    private int correctAnswers;

    @Column(name = "attempt_date", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime attemptDate = LocalDateTime.now();

    public Progress() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getBloomLevel() { return bloomLevel; }
    public void setBloomLevel(String bloomLevel) { this.bloomLevel = bloomLevel; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }

    public int getCorrectAnswers() { return correctAnswers; }
    public void setCorrectAnswers(int correctAnswers) { this.correctAnswers = correctAnswers; }

    public LocalDateTime getAttemptDate() { return attemptDate; }
    public void setAttemptDate(LocalDateTime attemptDate) { this.attemptDate = attemptDate; }
}