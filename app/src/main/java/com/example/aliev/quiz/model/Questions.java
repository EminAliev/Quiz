package com.example.aliev.quiz.model;

public class Questions {
    private int id;
    private String qText, qImage, ansA, ansB, ansC, ansD, corretcly_answer;
    private int isImageQ;
    private int categoryId;

    public Questions() {
    }

    public Questions(int id, String qText, String qImage, String ansA, String ansB, String ansC, String ansD, String corretcly_answer, int isImageQ, int categoryId) {
        this.id = id;
        this.qText = qText;
        this.qImage = qImage;
        this.ansA = ansA;
        this.ansB = ansB;
        this.ansC = ansC;
        this.ansD = ansD;
        this.corretcly_answer = corretcly_answer;
        this.isImageQ = isImageQ;
        this.categoryId = categoryId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getqText() {
        return qText;
    }

    public void setqText(String qText) {
        this.qText = qText;
    }

    public String getqImage() {
        return qImage;
    }

    public void setqImage(String qImage) {
        this.qImage = qImage;
    }

    public String getAnsA() {
        return ansA;
    }

    public void setAnsA(String ansA) {
        this.ansA = ansA;
    }

    public String getAnsB() {
        return ansB;
    }

    public void setAnsB(String ansB) {
        this.ansB = ansB;
    }

    public String getAnsC() {
        return ansC;
    }

    public void setAnsC(String ansC) {
        this.ansC = ansC;
    }

    public String getAnsD() {
        return ansD;
    }

    public void setAnsD(String ansD) {
        this.ansD = ansD;
    }

    public String getCorretcly_answer() {
        return corretcly_answer;
    }

    public void setCorretcly_answer(String corretcly_answer) {
        this.corretcly_answer = corretcly_answer;
    }

    public int getIsImageQ() {
        return isImageQ;
    }

    public void setIsImageQ(int isImageQ) {
        this.isImageQ = isImageQ;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
}