package com.example.dialogueapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class fragment_register extends Fragment {
    private FirebaseAuth mAuth;

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
        TextView txt_firstName = view.findViewById(R.id.signup_edit_firstname);
        TextView txt_lastName = view.findViewById(R.id.signup_edit_lastname);


        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = txt_Email.getText().toString();
                String password = txt_Password.getText().toString();
                String firstName = txt_firstName.getText().toString();
                String lastName = txt_lastName.getText().toString();

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