package com.example.dialogueapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class fragment_home extends Fragment {
//    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        Button Btn = view.findViewById(R.id.button);
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_home_to_fragment_set_lesson_teacher);
            }
        });

        Button Btn3 = view.findViewById(R.id.button3);
        Btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_home_to_fragment_my_lessons_student);
            }
        });


        Button loginBtn = view.findViewById(R.id.button_login);
       // Button SearchBtn = view.findViewById(R.id.btn_searchusers);
        Button signUpBtn = view.findViewById(R.id.button_signup);
        ImageButton logOutBtn = view.findViewById(R.id.btn_logout);
        TextView txt_user_firstName = view.findViewById(R.id.txt_user_firstName_home);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            // User is signed in
            String email = user.getEmail().toString();
            loginBtn.setVisibility(view.GONE);
            logOutBtn.setVisibility(view.VISIBLE);
            signUpBtn.setVisibility(view.GONE);
            txt_user_firstName.setVisibility(view.VISIBLE);
            txt_user_firstName.setText(email);

            //BUTTON LOGOUT -- Press
            logOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogOutFunction();
                    Navigation.findNavController(view).navigate(R.id.action_fragment_home_self);
                }

                private void LogOutFunction() {
                    FirebaseAuth.getInstance().signOut();
                }
            });
        } else {
            // No user is signed in
            loginBtn.setVisibility(view.VISIBLE);
            signUpBtn.setVisibility(view.VISIBLE);
            txt_user_firstName.setVisibility(view.GONE);
        }


        /// ---- onClick ->SignUp
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_home_to_fragment_register);
            }
        });
//        SearchBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(view).navigate(R.id.action_fragment_home_to_fragment_SearchUsers2);
//            }
//        });

        /// ---- OnClick -> Login
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_home_to_fragment_login);
            }
        });
        return view;
    }
}