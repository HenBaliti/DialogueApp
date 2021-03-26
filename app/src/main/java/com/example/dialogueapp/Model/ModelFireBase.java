package com.example.dialogueapp.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFireBase {
    public void getAllLessons(Long lastUpdated, Model.GetAllLessonsListener listener) {

        //////////////////TODO - fix filter records according to lastUpdated
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(lastUpdated,0);
        db.collection("Lesson").whereGreaterThanOrEqualTo("lastUpdated",ts).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Lesson> data = new LinkedList<Lesson>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult())
                    {
                        Lesson less1 = new Lesson();
                        less1.fromMap(doc.getData());
//                        Lesson lesson = doc.toObject(Lesson.class);
                        data.add(less1);
                    }
                }
                listener.onComplete(data);
            }
        });


    }

    ////////////////////////////////////////////////
    ///////// Get Lessons By Specific Date /////////
    ////////////////////////////////////////////////

    public void getAllLessonsByDate(String date,Long lastUpdated, Model.GetAllLessonsListener listener) {

        //////////////////TODO - fix filter records according to lastUpdated
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(lastUpdated,0);
        db.collection("Lesson").whereGreaterThanOrEqualTo("lastUpdated",ts).whereEqualTo("schedule_date",date).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Lesson> data = new LinkedList<Lesson>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult())
                    {
                        Lesson less1 = new Lesson();
                        less1.fromMap(doc.getData());
//                        Lesson lesson = doc.toObject(Lesson.class);
                        data.add(less1);
                    }
                }
                listener.onComplete(data);
            }
        });


    }



    public void addLesson(Lesson lesson, Model.AddLessonListener listener) {
        // Access a Cloud Firestore instance from your Activity
        FirebaseFirestore db = FirebaseFirestore.getInstance();


// Add a new document with a generated ID
        db.collection("Lesson").document(""+lesson.getLesson_id())
                .set(lesson.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("FireBaseAddingLesson","Lesson added succecfully");
                listener.onComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FireBaseAddingLesson","Failing by adding lesson");
                listener.onComplete();
            }
        });
    }

    public void updateLesson(Lesson lesson, Model.AddLessonListener listener) {
        addLesson(lesson,listener);
    }

    public void getLesson(int id, Model.GetLessonListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Lesson").document(""+id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Lesson lesson = null;
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc!=null){
                        lesson = task.getResult().toObject(Lesson.class);
                    }
                listener.onComplete(lesson);
                }
            }
        });
    }
}
