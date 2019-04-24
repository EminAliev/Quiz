package com.moremoregreen.quiz.common;

import com.moremoregreen.quiz.Model.Category;
import com.moremoregreen.quiz.Model.CurrentQuestion;
import com.moremoregreen.quiz.Model.Question;
import com.moremoregreen.quiz.QuestionFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Common {

    public static Category selectedCategory = new Category();
    public static List<Question> questionList = new ArrayList<>();
    public static List<CurrentQuestion> answerSheetList = new ArrayList<>();
    public static int right_answer_count = 0;
    public static int wrong_answer_count = 0;
    public static ArrayList<QuestionFragment> fragmentsList = new ArrayList<>();
    public static TreeSet<String> selected_values = new TreeSet<>();

    public enum ANSWER_TYPE {
        NO_ANSWER,
        WRONG_ANSWER,
        RIGHT_ANSWER
    }
}