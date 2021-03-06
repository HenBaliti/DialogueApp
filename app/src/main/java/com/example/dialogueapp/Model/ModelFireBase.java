package com.example.dialogueapp.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;

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
        db.collection("Lesson").whereGreaterThanOrEqualTo("lastUpdated",ts).whereEqualTo("isCatch",false).whereEqualTo("schedule_date",date).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
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

    public void getLesson(String id, Model.GetLessonListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Lesson").document(""+id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                Lesson lesson = null;
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc!=null){
                        lesson = new Lesson();
                        lesson.fromMap(doc.getData());
                    }
                listener.onComplete(lesson);
                }
            }
        });
    }


    public void getAllUsers(Long lastUpdated, Model.GetAllUsersListener listener) {

        //////////////////TODO - fix filter records according to lastUpdated
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Timestamp ts = new Timestamp(lastUpdated,0);
        db.collection("User").whereGreaterThanOrEqualTo("lastUpdated",ts).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<User> data = new LinkedList<User>();
                if(task.isSuccessful()){
                    for(DocumentSnapshot doc:task.getResult())
                    {
                        User user1 = new User();
                        user1.fromMap(doc.getData());
                        data.add(user1);
                    }
                }
                listener.onComplete(data);
            }
        });


    }
    public void addUser(User user, Model.AddUserListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(""+user.getUser_id())
                .set(user.toMap()).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("FireBase Adding User","User added succecfully");
                listener.onComplete();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("FireBaseAddingLesson","Failing by adding User");
                listener.onComplete();
            }
        });
    }
    public void updateUser(User user, Model.AddUserListener listener) {
        addUser(user,listener);
    }

    public void getUser(int id, Model.GetUserListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(""+id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User user = null;
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc!=null){
                        user = task.getResult().toObject(User.class);
                    }
                    listener.onComplete(user);
                }
            }
        });
    }

    public void getStudentByEmail(String email, Model.GetUserByEmailListener listener) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("TAG", document.getId() + " => " + document.getData());
                        listener.onComplete(document.getId());
                    }
                } else {
                    Log.d("TAG", "Error getting documents: ", task.getException());
                }
            }
        });

    }

    public void GetUserByID(String userId, Model.GetUserByIDListener listener) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").document(String.valueOf(userId)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                User userById = null;
                if(task.isSuccessful()){
                    DocumentSnapshot doc = task.getResult();
                    if(doc!=null){
                        userById = new User();
                        userById.fromMap(doc.getData());
//                        userById = task.getResult().toObject(User.class);
//                        Log.d("MSG",task.getResult().toString());
                    }
                    listener.onComplete(userById);
                }
            }
        });
    }

    public void uploadImage(Bitmap imageBmp, String id, Model.UploadImageListener listener){
        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference imagesRef = storage.getReference().child("images").child(id);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.onComplete(null);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Uri downloadUrl = uri;
                        listener.onComplete(downloadUrl.toString());
                    }
                });
            }
        });
    }

    public void GetUserObjByEmail(String email, Model.GetUserObjByEmailListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("User").whereEqualTo("email",email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                User userObjByemail = null;
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult().getDocuments().get(0);
                    if (doc != null) {
                        userObjByemail = new User();
                        userObjByemail.fromMap(doc.getData());
//                        userById = task.getResult().toObject(User.class);
//                        Log.d("MSG",task.getResult().toString());
                    }
                    listener.onComplete(userObjByemail);
                }
            }
        });
    }

    public void DeleteLessonTeacher(Lesson lesson, Model.DeleteLessonListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Lesson").document(""+lesson.getLesson_id()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                listener.onComplete();
                Log.d("Delete:","Item Deleted");
            }
        });
    }
}
