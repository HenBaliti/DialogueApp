package com.example.dialogueapp.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class TeacherListViewModel extends ViewModel {
    private LiveData<List<User>> listTeachers = Model.instance.getAllUsers();

    public LiveData<List<User>> getListTeachers() {
        return listTeachers;
    }
}
