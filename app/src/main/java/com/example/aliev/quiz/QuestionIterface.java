package com.example.aliev.quiz;

import com.example.aliev.quiz.model.Questions_curr;

public interface QuestionIterface {
    Questions_curr getSelectAns();
    void show_correct();
    void disable_anw();
    void reset_question();
}
