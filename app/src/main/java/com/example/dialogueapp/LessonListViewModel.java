package com.example.dialogueapp;

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

}
