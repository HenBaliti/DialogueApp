package com.example.dialogueapp.Model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FieldValue;

import java.util.HashMap;
import java.util.Map;

@Entity
public class User {
    @PrimaryKey
    @NonNull
    private int user_id;
    private String user_name;
    private String full_name;
    private String email;
    private String user_type;
    private Long lastUpdated;

    private String imageUrl;


    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("user_id",user_id);
        result.put("user_name",user_name);
        result.put("full_name",full_name);
        result.put("email",email);
        result.put("user_type",user_type);
        result.put("imageUrl",imageUrl);
        result.put("lastUpdated", FieldValue.serverTimestamp());
        return result;

    }

    public void fromMap(Map<String,Object> map){
        user_id = ((Long)map.get("user_id")).intValue();
        user_name = (String)map.get("user_name");
        full_name = (String)map.get("full_name");
        email = (String)map.get("email");
        user_type = (String)map.get("user_type");
        imageUrl = (String)map.get("imageUrl");
        Timestamp ts = (Timestamp)map.get("lastUpdated");
        lastUpdated = (Long)ts.getSeconds();
    }



    //Getters and Setters
    public Long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
