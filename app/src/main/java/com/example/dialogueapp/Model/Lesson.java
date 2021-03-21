package com.example.dialogueapp.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Lesson {
    @PrimaryKey
    @NonNull
    private int lesson_id;
    private int teacher_id;
    private int student_id;
    private String schedule_date;
    private String lesson_time;
    private String lesson_title;
    private String lesson_summary;
    private int student_review_id;
    private boolean isCatch;
    private boolean isDone;


    public boolean getIsDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }


    public int getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(int lesson_id) {
        this.lesson_id = lesson_id;
    }

    public int getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        this.teacher_id = teacher_id;
    }

    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getSchedule_date() {
        return schedule_date;
    }

    public void setSchedule_date(String schedule_date) {
        this.schedule_date = schedule_date;
    }

    public String getLesson_time() {
        return lesson_time;
    }

    public void setLesson_time(String lesson_time) {
        this.lesson_time = lesson_time;
    }

    public String getLesson_title() {
        return lesson_title;
    }

    public void setLesson_title(String lesson_title) {
        this.lesson_title = lesson_title;
    }

    public String getLesson_summary() {
        return lesson_summary;
    }

    public void setLesson_summary(String lesson_summary) {
        this.lesson_summary = lesson_summary;
    }

    public int getStudent_review_id() {
        return student_review_id;
    }

    public void setStudent_review_id(int student_review_id) {
        this.student_review_id = student_review_id;
    }

    public boolean getIsCatch() {
        return isCatch;
    }

    public void setCatch(boolean aCatch) {
        isCatch = aCatch;
    }



}
