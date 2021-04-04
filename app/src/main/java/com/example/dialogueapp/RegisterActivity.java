package com.example.dialogueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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

public class RegisterActivity extends AppCompatActivity {

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
            BitmapDrawable drawable = (BitmapDrawable)profileImage.getDrawable();
            Bitmap bitmap =  drawable.getBitmap();
            Model.instance.uploadImage(bitmap, user.getUser_id(), new Model.UploadImageListener() {
                @Override
                public void onComplete(String url) {
                    if(url==null) {
                        displayFailedErrors();
                    }
                    else {
                        user.setImageUrl(url);
                        Model.instance.addUser(user, new Model.AddUserListener() {
                            @Override
                            public void onComplete() {
                                Log.d("TAG", "createUserFirebase:success");
                            }
                        });
                    }
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

        });




    }

    private void displayFailedErrors() {
        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Operation Failed");
        builder.setTitle("Saving Image failed, please try again later...");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
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
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle("Choose your profile picture");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);

                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        profileImage.setImageBitmap(selectedImage);
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                profileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }
}