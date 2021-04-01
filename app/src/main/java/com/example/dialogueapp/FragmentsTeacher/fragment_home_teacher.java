package com.example.dialogueapp.FragmentsTeacher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dialogueapp.Model.Model;
import com.example.dialogueapp.Model.User;
import com.example.dialogueapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class fragment_home_teacher extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home_teacher, container, false);


        ImageButton logOutBtn = view.findViewById(R.id.btn_logout_home_teacher);
        TextView txt_user_firstName = view.findViewById(R.id.txt_user_firstName_home_teacher);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            // User is signed in
            String email = user.getEmail().toString();
            logOutBtn.setVisibility(view.VISIBLE);
            txt_user_firstName.setVisibility(view.VISIBLE);

            //Finding the user
            Model.instance.GetUserObjByID(email, new Model.GetUserObjByEmailListener() {
                @Override
                public void onComplete(User user) {
                    txt_user_firstName.setText(user.getFull_name());
                }
            });

            //BUTTON LOGOUT -- Press
            logOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogOutFunction();
                    getActivity().getSupportFragmentManager().popBackStack();//Need to backstack the stack of activities !
                }

                private void LogOutFunction() {
                    FirebaseAuth.getInstance().signOut();
                }
            });
        } else {
            // No user is signed in

            txt_user_firstName.setVisibility(view.GONE);
        }


        return view;
    }
}