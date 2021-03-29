package com.example.dialogueapp;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dialogueapp.Model.Model;
import com.example.dialogueapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class fragment_register extends Fragment {
    private FirebaseAuth mAuth;
    String usertype;
    static int id=0;
    @Override
    public void onStart() {
        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        if(currentUser != null){
//            reload();
//        }
        //Authentication
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Button confirmBtn = view.findViewById(R.id.signup_button_confirm);
        TextView txt_Email = view.findViewById(R.id.signup_edit_email);
        TextView txt_Password = view.findViewById(R.id.signup_edit_password);
        TextView txt_username = view.findViewById(R.id.signup_edit_username);
        TextView txt_fullname = view.findViewById(R.id.signup_edit_fullname);
        Button signStudent = view.findViewById(R.id.btn_registerstudent);
        Button signTeacher = view.findViewById(R.id.btn_registerteacher);
        TextView signIn = view.findViewById(R.id.text_signin);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_register_to_fragment_login); //need to send the user through the navigation

            }
        });

        signStudent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signStudent.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                signTeacher.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                usertype="Student";
            }
        });
        signTeacher.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                signStudent.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                signTeacher.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                usertype="Teacher";
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = txt_Email.getText().toString();
                String password = txt_Password.getText().toString();
                String fullname = txt_fullname.getText().toString();
                String username = txt_username.getText().toString();
                User user = new User();
                user.setUser_id(id++);
                user.setEmail(email);
                user.setFull_name(fullname);
                user.setUser_name(username);
                user.setUser_type(usertype);
                Model.instance.addUser(user, new Model.AddUserListener() {
                    @Override
                    public void onComplete() {
                        Log.d("TAG", "createUserFirebase:success");
                    }
                });

                //When The user clicked the login and the pass and wmail was correct
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("TAG", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Navigation.findNavController(view).navigate(R.id.action_fragment_register_to_fragment_home); //need to send the user through the navigation
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.d("TAG", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(getActivity(), "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

            }
        });
        return view;
    }
}