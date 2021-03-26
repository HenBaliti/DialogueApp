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
    private String first_name;
    private String last_name;
    private String email;
    private String user_type;
    private Long lastUpdated;


    public Map<String,Object> toMap(){
        HashMap<String,Object> result = new HashMap<>();
        result.put("user_id",user_id);
        result.put("first_name",first_name);
        result.put("last_name",last_name);
        result.put("email",email);
        result.put("user_type",user_type);
        result.put("lastUpdated", FieldValue.serverTimestamp());
        return result;

    }

    public void fromMap(Map<String,Object> map){
        user_id = ((Long)map.get("user_id")).intValue();
        first_name = (String)map.get("first_name");
        last_name = (String)map.get("last_name");
        email = (String)map.get("email");
        user_type = (String)map.get("user_type");
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

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
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
}
