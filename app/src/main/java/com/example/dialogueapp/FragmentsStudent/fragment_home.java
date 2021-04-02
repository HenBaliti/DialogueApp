package com.example.dialogueapp.FragmentsStudent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.dialogueapp.Model.Model;
import com.example.dialogueapp.Model.User;
import com.example.dialogueapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class fragment_home extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);


        CircularProgressButton btn_UpComingLessons = view.findViewById(R.id.lastLessonsStudent);
        ImageButton logOutBtn = view.findViewById(R.id.btn_logout_home_student);
        TextView txt_user_firstName = view.findViewById(R.id.txt_user_firstName_home);
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
                    getActivity().finish();//Need to backstack the stack of activities !
                }

                private void LogOutFunction() {
                    FirebaseAuth.getInstance().signOut();
                }
            });

        } else {
            // No user is signed in

            txt_user_firstName.setVisibility(view.GONE);
        }

        btn_UpComingLessons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_home_to_fragment_my_lessons_student);
            }
        });
        return view;
    }
}