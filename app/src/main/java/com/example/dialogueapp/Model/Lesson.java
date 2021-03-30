package com.example.dialogueapp.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class Lesson {
    @PrimaryKey
    @NonNull
    private String lesson_id;
    private String teacher_id;
    private String student_id;
    private String schedule_date;
    private String lesson_time;
    private String lesson_title;
    private String lesson_summary;
    private int student_review_id;
    private int numOfMinutesPerLesson;
    private boolean isCatch;
    private boolean isDone;
    private Long lastUpdated;


    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("lesson_id",lesson_id);
        result.put("teacher_id",teacher_id);
        result.put("student_id",student_id);
        result.put("schedule_date",schedule_date);
        result.put("lesson_time",lesson_time);
        result.put("lesson_title",lesson_title);
        result.put("lesson_summary",lesson_summary);
        result.put("student_review_id",student_review_id);
        result.put("numOfMinutesPerLesson",numOfMinutesPerLesson);
        result.put("isCatch",isCatch);
        result.put("isDone",isDone);
        result.put("lastUpdated", FieldValue.serverTimestamp());
        return result;

    }

    public void fromMap(Map<String,Object> map){
        lesson_id = (String)map.get("lesson_id");
        teacher_id = (String)map.get("teacher_id");
        student_id = (String)map.get("student_id");
        schedule_date = (String)map.get("schedule_date");
        lesson_time = (String)map.get("lesson_time");
        lesson_title = (String)map.get("lesson_title");
        lesson_summary = (String)map.get("lesson_summary");
        student_review_id = ((Long)map.get("student_review_id")).intValue();
        if(map.containsKey("numOfMinutesPerLesson"))
            numOfMinutesPerLesson = Integer.parseInt(map.get("numOfMinutesPerLesson").toString());
        isCatch = (boolean)map.get("isCatch");
        isDone = (boolean)map.get("isDone");
        Timestamp ts = (Timestamp)map.get("lastUpdated");
        lastUpdated = (Long)ts.getSeconds();
    }

    public int getNumOfMinutesPerLesson() {
        return numOfMinutesPerLesson;
    }

    public void setNumOfMinutesPerLesson(int numOfMinutesPerLesson) {
        this.numOfMinutesPerLesson = numOfMinutesPerLesson;
    }

    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }



    public boolean getIsDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }


    public String getLesson_id() {
        return lesson_id;
    }

    public void setLesson_id(String lesson_id) {
        this.lesson_id = lesson_id;
    }

    public String getTeacher_id() {
        return teacher_id;
    }

    public void setTeacher_id(String teacher_id) {
        this.teacher_id = teacher_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
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
