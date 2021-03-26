package com.example.dialogueapp.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LessonDao {
    @Query("select * from Lesson")
    LiveData<List<Lesson>> getAllLessons();

    @Query("SELECT * FROM Lesson WHERE schedule_date LIKE :date")
    LiveData<List<Lesson>> findLessonByDate(String date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Lesson... lessons);

    @Delete
    void delete(Lesson lesson);
}