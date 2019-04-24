package com.moremoregreen.quiz;

import com.moremoregreen.quiz.Model.CurrentQuestion;

public interface IQuestion {
    CurrentQuestion  getSelectedAnswer();
    void showCorrectAnswer();
    void disableAnswer();
    void resetQuestion();

}
