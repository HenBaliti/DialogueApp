package com.example.dialogueapp.Model;

import android.os.AsyncTask;

import java.nio.channels.AsynchronousByteChannel;
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


    public void getAllLessons(GetAllLessonsListener listener){
        modelFireBase.getAllLessons(listener);
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
        modelFireBase.addLesson(lesson,listener);
    }

    public interface UpdateLessonListener extends AddLessonListener{
    }
    public void updateLesson(Lesson lesson,AddLessonListener listener){
        modelFireBase.updateLesson(lesson,listener);
    }
}
