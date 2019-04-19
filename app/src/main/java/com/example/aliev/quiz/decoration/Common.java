package com.example.aliev.quiz.decoration;

import com.example.aliev.quiz.BlankFragment;
import com.example.aliev.quiz.model.Category;
import com.example.aliev.quiz.model.Questions;
import com.example.aliev.quiz.model.Questions_curr;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class Common {

    public static Category select = new Category();
    public static List<Questions> questionList = new ArrayList<>();
    public static List<Questions_curr> answerList = new ArrayList<>();
    public static int rightCount = 0;
    public static int wrongCount = 0;
    public static ArrayList<BlankFragment> fragList = new ArrayList<>();
    public static TreeSet<String> select_val = new TreeSet<>();

    public enum ANSWER_TYPE {
        WRONG_ANSWER,
        NO_ANSWER,
        RIGHT_ANSWER
    }
}