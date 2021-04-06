package com.example.dialogueapp.Model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.nio.channels.AsynchronousByteChannel;
import java.util.List;

public class ModelSql {


    public LiveData<List<Lesson>> getAllLessons(){
        return AppLocalDb.db.lessonDao().getAllLessons();
    }


public LiveData<List<User>> getAllUsers() { return AppLocalDb.db.userDao().getAllUsers();}


    public LiveData<List<Lesson>> getLessonsByDate(String date){
        return AppLocalDb.db.lessonDao().findLessonByDate(date,false);
    }

    public LiveData<List<Lesson>> getLessonsHistoryForUser(String cureentUserId){
        return AppLocalDb.db.lessonDao().findLessonHistoryOfUser(cureentUserId,true);
    }

    public LiveData<List<Lesson>> findLessonHistoryOfTeacher(String cureentUserId){
        return AppLocalDb.db.lessonDao().findLessonHistoryOfTeacher(cureentUserId,true);
    }

    public LiveData<List<Lesson>> getMyLessons(String cureentUserId){
        return AppLocalDb.db.lessonDao().getMyLessons(cureentUserId);
    }

    public LiveData<List<Lesson>> getMyLessonsTeacher(String cureentUserId){
        return AppLocalDb.db.lessonDao().getMyLessonsTeacher(cureentUserId);
    }

    public interface AddLessonListener{
        void onComplete();
    }
    public void addLesson(Lesson lesson,AddLessonListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.lessonDao().insertAll(lesson);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(listener !=null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }
    public interface AddUserListener{
        void onComplete();
    }
    public void addUser(User user,AddUserListener listener){
        class MyAsyncTask extends AsyncTask {
            @Override
            protected Object doInBackground(Object[] objects) {
                AppLocalDb.db.userDao().insertAll(user);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                if(listener !=null){
                    listener.onComplete();
                }
            }
        };
        MyAsyncTask task = new MyAsyncTask();
        task.execute();
    }

}
