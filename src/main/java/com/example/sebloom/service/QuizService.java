package com.example.sebloom.service;

import com.example.sebloom.model.Progress;
import com.example.sebloom.model.Question;
import com.example.sebloom.repository.ProgressRepository;
import com.example.sebloom.repository.QuestionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class QuizService {
    private static final Logger logger = LoggerFactory.getLogger(QuizService.class);
    private final QuestionRepository questionRepo;
    private final ProgressRepository progressRepo;

    public QuizService(QuestionRepository questionRepo, ProgressRepository progressRepo) {
        this.questionRepo = questionRepo;
        this.progressRepo = progressRepo;
    }

    public List<Question> getQuestions(String topic, String bloomLevel) {
        logger.debug("Querying questions for topic: '{}' and level: '{}'", topic, bloomLevel);
        List<Question> questions = questionRepo.findByTopicAndBloomLevel(topic, bloomLevel);
        logger.info("Found {} questions for topic '{}' and level '{}'",
                questions.size(), topic, bloomLevel);
        return questions != null ? questions : Collections.emptyList();
    }

    public void saveProgress(String topic, String bloomLevel, int total, int correct) {
        logger.debug("Saving progress: Topic={}, Level={}, Total={}, Correct={}",
                topic, bloomLevel, total, correct);

        Progress p = new Progress();
        p.setTopic(topic);
        p.setBloomLevel(bloomLevel);
        p.setTotalQuestions(total);
        p.setCorrectAnswers(correct);

        progressRepo.save(p);
        logger.info("Progress saved successfully: ID={}", p.getId());
    }

    public List<Progress> getProgress(String topic) {
        logger.debug("Querying progress for topic: '{}'", topic);
        List<Progress> progress = progressRepo.findByTopicOrderByAttemptDateDesc(topic);
        logger.info("Found {} progress records for topic '{}'", progress.size(), topic);
        return progress != null ? progress : Collections.emptyList();
    }
}