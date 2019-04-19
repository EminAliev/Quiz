package com.example.aliev.quiz.datebases;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.aliev.quiz.model.Category;
import com.example.aliev.quiz.model.Questions;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

public class DataBase extends SQLiteAssetHelper {
    private static final String NAME = "DataBase.db";
    private static final int VER = 1;

    private static DataBase inst;

    public static synchronized DataBase getInst(Context context) {
        if (inst == null) {
            inst = new DataBase(context);
        }
        return inst;
    }

    public DataBase(Context context) {
        super(context, NAME, null, VER);
    }

    public List<Category> getCategoriesFromDataBase() {
        SQLiteDatabase db = inst.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Category", null);
        List<Category> categories_ = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Category category = new Category(cursor.getInt(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("Name")),
                        cursor.getString(cursor.getColumnIndex("Image")));
                categories_.add(category);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return categories_;
    }

    public List<Questions> getQuestion(int category) {
        SQLiteDatabase db = inst.getWritableDatabase();
        Cursor cursor = db.rawQuery(String.format("SELECT * FROM Question WHERE CategoryID = %d ORDER BY RANDOM() LIMIT 30", category), null);
        List<Questions> questions = new ArrayList<>();
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Questions questions1 = new Questions(cursor.getInt(cursor.getColumnIndex("ID")),
                        cursor.getString(cursor.getColumnIndex("QuestionText")),
                        cursor.getString(cursor.getColumnIndex("QuestionImage")),
                        cursor.getString(cursor.getColumnIndex("AnswerA")),
                        cursor.getString(cursor.getColumnIndex("AnswerB")),
                        cursor.getString(cursor.getColumnIndex("AnswerC")),
                        cursor.getString(cursor.getColumnIndex("AnswerD")),
                        cursor.getString(cursor.getColumnIndex("CorrectAnswer")),
                        cursor.getInt(cursor.getColumnIndex("IsImageQuestion"))==0?Boolean.FALSE:Boolean.TRUE,
                        cursor.getInt(cursor.getColumnIndex("CategoryID")));
                questions.add(questions1);
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return questions;
    }

}
