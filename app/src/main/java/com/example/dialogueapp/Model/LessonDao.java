package com.example.dialogueapp.Model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LessonDao {
    @Query("select * from Lesson")
    LiveData<List<Lesson>> getAllLessons();

//    //Get all the lesson by date
//    @Query("SELECT * FROM Lesson WHERE schedule_date LIKE :date")
//    LiveData<List<Lesson>> findLessonByDate(String date);

    //Get all the lesson by date
    @Query("SELECT * FROM Lesson WHERE Lesson.schedule_date= :date")
    LiveData<List<Lesson>> findLessonByDate(String date);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Lesson... lessons);

    @Update
    void update(Lesson lesson);

    @Delete
    void delete(Lesson lesson);
}
