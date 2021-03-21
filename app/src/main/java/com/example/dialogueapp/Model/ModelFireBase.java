package com.example.dialogueapp.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ModelFireBase {
    public void getAllLessons(Model.GetAllLessonsListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Lesson").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<Lesson> data = new LinkedList<Lesson>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult())
                    {
                        Lesson lesson = doc.toObject(Lesson.class);
                        data.add(lesson);
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
                .set(lesson).addOnSuccessListener(new OnSuccessListener<Void>() {
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
