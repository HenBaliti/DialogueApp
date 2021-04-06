package com.example.dialogueapp.FragmentsTeacher;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
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
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class fragment_profile_teacher extends Fragment {
    private FirebaseAuth mAuth;
    private TextView txtUserName, txtFullName, txtEmail, txtUserType, txtTopUserName, txtTopEmail;
    private CircleImageView imageProfile;
    private String UserID;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        TextView moveHome = view.findViewById(R.id.nav_profiletohome);
        moveHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_profile_to_fragment_home);
            }
        });
        txtUserName = view.findViewById(R.id.textUserName);
        txtFullName = view.findViewById(R.id.textFullName);
        txtEmail = view.findViewById(R.id.textEmail);
        txtUserType = view.findViewById(R.id.textUserType);
        imageProfile = view.findViewById(R.id.image_profile);
        txtTopUserName = view.findViewById(R.id.id_usernametop);
        txtTopEmail = view.findViewById(R.id.id_useremail);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        ImageButton logOutBtn = view.findViewById(R.id.btn_logout_profile);
        if(user != null) {
            // User is signed in
            logOutBtn.setVisibility(view.VISIBLE);

            //BUTTON LOGOUT -- Press
            logOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogOutFunction();
                    getActivity().finish();
                }

                private void LogOutFunction() {
                    FirebaseAuth.getInstance().signOut();
                }
            });
        } else {
            // No user is signed in
            logOutBtn.setVisibility(view.GONE);
        }

//
//

        Model.instance.GetUserObjByID(user.getEmail(), new Model.GetUserObjByEmailListener() {
            @Override
            public void onComplete(User currentUser) {
                txtUserName.setText(currentUser.getUser_name());
                txtFullName.setText(currentUser.getFull_name());
                txtEmail.setText(currentUser.getEmail());
                txtUserType.setText(currentUser.getUser_type());
                txtTopUserName.setText(currentUser.getUser_name());
                txtTopEmail.setText(currentUser.getEmail());
                if (currentUser.getImageUrl() != null) {
                    Log.d("check",currentUser.getImageUrl());
                    Picasso.get().load(currentUser.getImageUrl()).into(imageProfile);

                }
            }
        });
        return view;
    }

}