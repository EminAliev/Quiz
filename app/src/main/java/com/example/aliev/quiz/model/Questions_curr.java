package com.example.aliev.quiz.model;

import com.example.aliev.quiz.decoration.Common;

public class Questions_curr {
    private int questionID;
    private Common.ANSWER_TYPE type;

    public Questions_curr(int questionID, Common.ANSWER_TYPE type) {
        this.questionID = questionID;
        this.type = type;
    }

    public int getQuestionID() {
        return questionID;
    }

    public void setQuestionID(int questionID) {
        this.questionID = questionID;
    }

    public Common.ANSWER_TYPE getType() {
        return type;
    }

    public void setType(Common.ANSWER_TYPE type) {
        this.type = type;
    }
}
