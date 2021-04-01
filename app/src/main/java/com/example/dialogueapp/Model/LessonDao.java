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

    //Get all the lesson by date
    @Query("SELECT * FROM Lesson WHERE Lesson.schedule_date LIKE :date AND Lesson.isCatch LIKE :isCatched")
    LiveData<List<Lesson>> findLessonByDate(String date,boolean isCatched);

    //Get all the lesson for current user which isDone = true
    @Query("SELECT * FROM Lesson WHERE Lesson.student_id LIKE :currentUserID AND Lesson.isDone LIKE :isDone")
    LiveData<List<Lesson>> findLessonHistoryOfUser(String currentUserID,boolean isDone);

    //Get all the lesson for current Teacher which isDone = true
    @Query("SELECT * FROM Lesson WHERE Lesson.teacher_id LIKE :currentUserID AND Lesson.isDone LIKE :isDone")
    LiveData<List<Lesson>> findLessonHistoryOfTeacher(String currentUserID,boolean isDone);

    //Get all the lesson for current user which isDone = true
    @Query("SELECT * FROM Lesson WHERE Lesson.student_id LIKE :currentUserID ")
    LiveData<List<Lesson>> getMyLessons(String currentUserID);

//    //Get all the lesson by date
//    @Query("SELECT * FROM Lesson WHERE Lesson.schedule_date= :date AND Lesson.isCatch= :isCatched")
//    LiveData<List<Lesson>> findLessonByDate(String date,boolean isCatched);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Lesson... lessons);

    @Update
    void update(Lesson lesson);

    @Delete
    void delete(Lesson lesson);
}
