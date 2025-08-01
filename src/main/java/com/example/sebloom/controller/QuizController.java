package com.example.sebloom.controller;

import com.example.sebloom.model.Progress;
import com.example.sebloom.model.Question;
import com.example.sebloom.service.QuizService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/quiz")
public class QuizController {
    private static final Logger logger = LoggerFactory.getLogger(QuizController.class);
    private static final String DEFAULT_TOPIC = "Software Engineering";
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping
    public String showQuizForm(Model model) {
        String[] bloomLevels = {"REMEMBER", "UNDERSTAND", "APPLY", "ANALYZE", "EVALUATE", "CREATE"};
        model.addAttribute("bloomLevels", bloomLevels);
        logger.info("Displaying quiz form with levels: {}", (Object) bloomLevels);
        return "quiz";
    }

    @PostMapping("/questions")
    public String getQuestions(@RequestParam String level, Model model) {
        try {
            logger.info("Fetching questions for level: {}", level);
            List<Question> questions = quizService.getQuestions(DEFAULT_TOPIC, level);
            logger.debug("Found {} questions for level: {}", questions.size(), level);

            if (questions.isEmpty()) {
                logger.warn("No questions found for level: {}", level);
                model.addAttribute("error", "No questions available for this level");
                String[] bloomLevels = {"REMEMBER", "UNDERSTAND", "APPLY", "ANALYZE", "EVALUATE", "CREATE"};
                model.addAttribute("bloomLevels", bloomLevels);
                return "quiz";
            }

            model.addAttribute("questions", questions);
            model.addAttribute("level", level);
            logger.info("Displaying quiz form with {} questions", questions.size());
            return "quiz-form";
        } catch (Exception e) {
            logger.error("Error fetching questions for level: {}", level, e);
            model.addAttribute("error", "Error loading questions");
            String[] bloomLevels = {"REMEMBER", "UNDERSTAND", "APPLY", "ANALYZE", "EVALUATE", "CREATE"};
            model.addAttribute("bloomLevels", bloomLevels);
            return "quiz";
        }
    }

    @PostMapping("/submit")
    public String submitQuiz(
            @RequestParam String level,
            @RequestParam Map<String, String> allParams,
            Model model) {

        try {
            logger.info("Submitting quiz for level: {}", level);
            logger.debug("All params: {}", allParams);

            List<Question> questions = quizService.getQuestions(DEFAULT_TOPIC, level);
            logger.debug("Loaded {} questions for evaluation", questions.size());

            if (questions.isEmpty()) {
                logger.error("No questions found during submission for level: {}", level);
                model.addAttribute("error", "Quiz submission failed - no questions found");
                String[] bloomLevels = {"REMEMBER", "UNDERSTAND", "APPLY", "ANALYZE", "EVALUATE", "CREATE"};
                model.addAttribute("bloomLevels", bloomLevels);
                return "quiz";
            }

            int correctCount = 0;
            Map<Long, String> userAnswers = new HashMap<>();
            Map<Long, String> correctAnswers = new HashMap<>();

            for (Question q : questions) {
                String paramName = "q" + q.getId();
                String userAnswer = allParams.get(paramName);
                String correct = q.getCorrectAnswer();

                if (userAnswer == null) {
                    userAnswer = "Not answered";
                }

                userAnswers.put(q.getId(), userAnswer);
                correctAnswers.put(q.getId(), correct);

                if (userAnswer.equalsIgnoreCase(correct)) {
                    correctCount++;
                    logger.debug("Question {} correct: User={}, Correct={}", q.getId(), userAnswer, correct);
                } else {
                    logger.debug("Question {} incorrect: User={}, Correct={}", q.getId(), userAnswer, correct);
                }
            }

            // Save progress
            quizService.saveProgress(DEFAULT_TOPIC, level, questions.size(), correctCount);
            logger.info("Progress saved: Level={}, Total={}, Correct={}", level, questions.size(), correctCount);

            // Add attributes to model
            model.addAttribute("total", questions.size());
            model.addAttribute("correct", correctCount);
            model.addAttribute("level", level);
            model.addAttribute("userAnswers", userAnswers);
            model.addAttribute("correctAnswers", correctAnswers);
            model.addAttribute("questions", questions);

            logger.info("Quiz submitted successfully. Score: {}/{}", correctCount, questions.size());
            logger.debug("Model attributes for result page: total={}, correct={}, level={}, questions={}, userAnswers={}",
                    questions.size(), correctCount, level, questions.size(), userAnswers.size());

            return "result";

        } catch (Exception e) {
            logger.error("Error submitting quiz for level: {}", level, e);
            model.addAttribute("error", "Error processing your quiz submission");
            String[] bloomLevels = {"REMEMBER", "UNDERSTAND", "APPLY", "ANALYZE", "EVALUATE", "CREATE"};
            model.addAttribute("bloomLevels", bloomLevels);
            return "quiz";
        }
    }

    @GetMapping("/progress")
    public String viewProgress(Model model) {
        try {
            logger.info("Fetching progress history");
            List<Progress> progressList = quizService.getProgress(DEFAULT_TOPIC);
            logger.debug("Found {} progress records", progressList.size());

            if (progressList == null) {
                progressList = Collections.emptyList();
            }

            model.addAttribute("progressList", progressList);
            logger.info("Displaying progress page with {} records", progressList.size());
            return "progress";
        } catch (Exception e) {
            logger.error("Error fetching progress history", e);
            model.addAttribute("error", "Error loading progress history");
            String[] bloomLevels = {"REMEMBER", "UNDERSTAND", "APPLY", "ANALYZE", "EVALUATE", "CREATE"};
            model.addAttribute("bloomLevels", bloomLevels);
            return "quiz";
        }
    }
}