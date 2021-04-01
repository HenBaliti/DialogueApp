package com.example.dialogueapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dialogueapp.Model.Model;
import com.example.dialogueapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.UUID;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class activity_register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String usertype;
    private CircleImageView profileImage;
    private RadioGroup radioGroup;
    private CircularProgressButton registerBtn;
    private TextView NameET;
    private TextView UserNameET;
    private TextView EmailET;
    private TextView PasswordET;
    private Activity mActivity;
    static final int REQUEST_IMAGE_CAPTURE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        init();
        changeStatusBarColor();
        profileImage.setOnClickListener(v -> {
            EditImage();
        });


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                if (null != rb) {
                    usertype = rb.getText().toString().toLowerCase().intern().equals("student") ? "Student" : "Teacher";
                }
            }
        });

        registerBtn.setOnClickListener(v -> {
            User user = new User();
            user.setUser_id(String.valueOf(UUID.randomUUID()));
            user.setEmail(EmailET.getText().toString());
            user.setFull_name(NameET.getText().toString());
            user.setUser_name(UserNameET.getText().toString());
            user.setUser_type(usertype);
            String email = EmailET.getText().toString();
            String password = PasswordET.getText().toString();


            Model.instance.addUser(user, new Model.AddUserListener() {
                @Override
                public void onComplete() {
                    Log.d("TAG", "createUserFirebase:success");
                }
            });

            //When The user clicked the login and the pass and email was correct
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("TAG", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Intent returnIntent = new Intent();
                                setResult(Activity.RESULT_OK,returnIntent);
                                finish();


//                                Navigation.findNavController(view).navigate(R.id.action_fragment_register_to_fragment_home); //need to send the user through the navigation
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.d("TAG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(mActivity, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });

            //Add Image to firebase
            BitmapDrawable drawable = (BitmapDrawable)profileImage.getDrawable();
            Bitmap bitmap =  drawable.getBitmap();
            Model.instance.uploadImage(bitmap, user.getUser_id(), new Model.UploadImageListener() {
                @Override
                public void onComplete(String url) {
                    if(url==null) {

                    }
                    else {
                        user.setImageUrl(url);

                    }
                }
            });
        });




    }

    private void init() {

        //Authentication
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mActivity = this;

        profileImage = findViewById(R.id.profile_image);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        NameET = findViewById(R.id.editTextName);
        UserNameET = findViewById(R.id.editTextUserName);
        EmailET = findViewById(R.id.editTextEmail);
        PasswordET = findViewById(R.id.editTextPassword);

        registerBtn = findViewById(R.id.cirRegisterButton);



    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View View){
        startActivity(new Intent(this,LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left, 0);

    }



    private void EditImage() {
        Intent takePictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE &&
                resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profileImage.setImageBitmap(imageBitmap);
        }
    }
}