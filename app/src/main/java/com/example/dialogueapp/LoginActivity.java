package com.example.dialogueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dialogueapp.Model.Model;
import com.example.dialogueapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {

    private final static int CODE = 100;

    private FirebaseAuth mAuth;

    private CircularProgressButton loginBtn;
    private EditText emailET;
    private EditText passwordET;

    private Activity mActivity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        loginBtn.setOnClickListener(v -> {

            String email = emailET.getText().toString();
            String pass = passwordET.getText().toString();

            mAuth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(mActivity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "signInWithEmail:success");

                             //   Navigation.findNavController(view).navigate(R.id.action_fragment_login_to_fragment_home);
                                loginIntent(email);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("TAG", "signInWithEmail:failure", task.getException());
                                Toast.makeText(mActivity, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });



    }

    private void init (){

        //Authentication
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        mActivity = this;

        loginBtn = findViewById(R.id.cirLoginButton);
        emailET = findViewById(R.id.editTextEmail);
        passwordET = findViewById(R.id.editTextPassword);

    }

    private void loginIntent(String email){

        Model.instance.GetUserObjByID(email, new Model.GetUserObjByEmailListener() {
            @Override
            public void onComplete(User user) {
                if(user.getUser_type().equals("Student")){
                    Intent intent = new Intent(mActivity, MainActivity.class);
                    startActivityForResult(intent, CODE);
                }
                else{
                    Intent intent = new Intent(mActivity, MainActivityTeacher.class);
                    startActivityForResult(intent, CODE);
                }
            }
        });

    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,activity_register.class));
        overridePendingTransition(R.anim.slide_in_right, 0);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Toast.makeText(mActivity, "bfdbdfbdf", Toast.LENGTH_LONG).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {

                //Write your code if there's no result
            }
        }
    }//onActivityResult

}