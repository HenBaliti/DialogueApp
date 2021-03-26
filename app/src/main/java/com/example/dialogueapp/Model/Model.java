package com.example.dialogueapp.Model;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;

import java.util.List;

public class Model {
    public final static Model instance = new Model();
    ModelFireBase modelFireBase = new ModelFireBase();
    ModelSql modelSql = new ModelSql();

    private Model(){

    }

    public interface GetAllLessonsListener{
        void onComplete(List<Lesson> data);
    }

    LiveData<List<Lesson>> lessonList;
    public LiveData<List<Lesson>> getAllLessons(){
        if(lessonList==null){
            lessonList = modelSql.getAllLessons();
            refreshAllLessons(null);
        }
        return lessonList;
    }

    public LiveData<List<Lesson>> getLessonsByDate(String date){
        if(lessonList==null){
            lessonList = modelSql.getLessonsByDate(date);
            refreshAllLessons(null);
        }
        return lessonList;
    }




    ///////// REFRESH ALL LESSONS //////////
    public void refreshAllLessons(GetAllLessonsListener listener){

        //1. Get local last update date
        final SharedPreferences sp = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        Long lastUpdated = sp.getLong("lastUpdated",0);

        //2. Get all updated record from firebase from the last update date
        modelFireBase.getAllLessons(lastUpdated,new GetAllLessonsListener() {
            @Override
            public void onComplete(List<Lesson> data) {

                //3. Insert the new updates to the local db
                long lastUp = 0;
                for (Lesson less: data) {
                    modelSql.addLesson(less,null);
                    if(less.getLastUpdated()>lastUp){
                        lastUp = less.getLastUpdated();
                    }
                }

                //4. Update the local last update date
                SharedPreferences.Editor editor = sp.edit();
                editor.putLong("lastUpdated",lastUp);
                editor.commit();


                //5. Return the updates data to the listeners
                if(listener!=null){
                    listener.onComplete(data);
                }

            }
        });


//        modelFireBase.getAllLessons(new GetAllLessonsListener() {
//            @Override
//            public void onComplete(List<Lesson> data) {
//                lessonList.setValue(data);
//                listener.onComplete(null);
//            }
//        });
    }


    public interface GetLessonListener{
        void onComplete(Lesson lesson);
    }
    public void getLesson(int id,GetLessonListener listener){
        modelFireBase.getLesson(id,listener);
    }


    public interface AddLessonListener{
        void onComplete();
    }
    public void addLesson(Lesson lesson,AddLessonListener listener){
        modelFireBase.addLesson(lesson, new AddLessonListener() {
            @Override
            public void onComplete() {
                refreshAllLessons(new GetAllLessonsListener() {
                    @Override
                    public void onComplete(List<Lesson> data) {
                        listener.onComplete();
                    }
                });
            }
        });
    }

    public interface UpdateLessonListener extends AddLessonListener{
    }
    public void updateLesson(Lesson lesson,AddLessonListener listener){
        modelFireBase.updateLesson(lesson,listener);
    }


    //User
    public interface GetUserListener{
        void onComplete(User user);
    }
    public interface GetAllUsersListener{
        void onComplete(List<User> data);
    }
    LiveData<List<User>> userList;
    public LiveData<List<User>> getAllUsers(){
        if(userList==null){
            userList = modelSql.getAllUsers();
            refreshAllUsers(null);
        }
        return userList;
    }

    public void refreshAllUsers(GetAllUsersListener listener){

        //1. Get local last update date
        final SharedPreferences sp = MyApplication.context.getSharedPreferences("TAG", Context.MODE_PRIVATE);
        Long lastUpdated = sp.getLong("lastUpdated",0);

        //2. Get all updated record from firebase from the last update date
        modelFireBase.getAllUsers(lastUpdated,new GetAllUsersListener() {
            @Override
            public void onComplete(List<User> data) {

                //3. Insert the new updates to the local db
                long lastUp = 0;
                for (User us : data) {
                    modelSql.addUser(us, null);
                    if (us.getLastUpdated() > lastUp) {
                        lastUp = us.getLastUpdated();
                    }
                }

                //4. Update the local last update date
                SharedPreferences.Editor editor = sp.edit();
                editor.putLong("lastUpdated", lastUp);
                editor.commit();


                //5. Return the updates data to the listeners
                if (listener != null) {
                    listener.onComplete(data);
                }

            }
        });
        }

    public interface AddUserListener{
        void onComplete();
    }
    public void addUser(final User user, final AddUserListener listener) {
        modelFireBase.addUser(user,listener);
    }
}
