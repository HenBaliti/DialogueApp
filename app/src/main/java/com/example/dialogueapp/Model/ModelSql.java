package com.example.dialogueapp.Model;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.nio.channels.AsynchronousByteChannel;
import java.util.List;

public class ModelSql {


    ///// --- Async GetAll Lessons Function Room ---

//    public interface GetAllLessonsListener{
//        void onComplete(List<Lesson> data);
//    }

//    public void getAllLessons(GetAllLessonsListener listener){
//
//        /////////////////////////////////// ASYNC TASK /////////////////////////////////
//        class MyAsyncTask extends AsyncTask{
//            List<Lesson> dataLessons;
//
//            @Override
//            protected Object doInBackground(Object[] objects) {
//                dataLessons = AppLocalDb.db.lessonDao().getAllLessons();
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(Object o) {
//                super.onPostExecute(o);
//                listener.onComplete(dataLessons);
//            }
//        }
//        MyAsyncTask task = new MyAsyncTask();
//        task.execute();
//    }

    public LiveData<List<Lesson>> getAllLessons(){
        return AppLocalDb.db.lessonDao().getAllLessons();
    }


public LiveData<List<User>> getAllUsers() { return AppLocalDb.db.userDao().getAllUsers();}


    public LiveData<List<Lesson>> getLessonsByDate(String date){
        return AppLocalDb.db.lessonDao().findLessonByDate(date);
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
