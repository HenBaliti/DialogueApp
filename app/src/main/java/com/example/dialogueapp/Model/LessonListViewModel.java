package com.example.dialogueapp.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dialogueapp.Model.Lesson;
import com.example.dialogueapp.Model.Model;

import java.util.LinkedList;
import java.util.List;

public class LessonListViewModel extends ViewModel {
    private LiveData<List<Lesson>> stLesson = Model.instance.getAllLessons();

    public LiveData<List<Lesson>> getStLesson() {
        return stLesson;
    }

    public void setStLessonFilteredDate(String date) {
        this.stLesson = Model.instance.getLessonsByDate(date);
    }

    public void setStLessonHistoryForUser(String currentUserId) {
        this.stLesson = Model.instance.getLessonsHistoryForUser(currentUserId);
    }

    public void setStLessonHistoryOfTeacher(String currentUserId) {
        this.stLesson = Model.instance.findLessonHistoryOfTeacher(currentUserId);
    }

    public void setMyLessonsTeacher(String currentUserId) {
        this.stLesson = Model.instance.getMyLessonsTeacher(currentUserId);
    }

    public void setMyLessons(String currentUserId) {
        this.stLesson = Model.instance.getMyLessons(currentUserId);
    }



}
