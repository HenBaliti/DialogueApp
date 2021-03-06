package com.example.dialogueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
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
    private Uri imageurl;
    private boolean isImageOk = false;
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
                    Log.d(usertype, "usertype");
                }
            }
        });

        registerBtn.setOnClickListener(v -> {
            String namef = NameET.getText().toString();
            String nameu = UserNameET.getText().toString();
            String emailu = EmailET.getText().toString();
            String pass = PasswordET.getText().toString();
            //String imageu = profileImage.toString();
            boolean validate = false;
            if (namef.isEmpty()) {
                validate = true;
                Toast.makeText(getApplicationContext(), "Enter your fullname",
                        Toast.LENGTH_SHORT).show();
            }
            if (nameu.isEmpty()) {
                validate = true;
                Toast.makeText(getApplicationContext(), "Enter your username",
                        Toast.LENGTH_SHORT).show();
            }
            if (emailu.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(emailu).matches()) {
                Toast.makeText(getApplicationContext(), "unvalid email",
                        Toast.LENGTH_SHORT).show();
                validate = true;
            }

            if (pass.isEmpty() || pass.length() < 6 || pass.length() > 12) {
                validate = true;
                Toast.makeText(getApplicationContext(), "Password should be 6-12 digits",
                        Toast.LENGTH_SHORT).show();

            }
            if(usertype ==null) {
                validate = true;
                Toast.makeText(getApplicationContext(), "You need to chose user type",
                        Toast.LENGTH_SHORT).show();
            }
            if(isImageOk==false) {
                validate = true;
                Toast.makeText(getApplicationContext(), "You need to add image",
                        Toast.LENGTH_SHORT).show();
            }
            if(validate==false) {
                ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setMessage("Creating New User...");
                progressDialog.show();
                User user = new User();
                user.setUser_id(String.valueOf(UUID.randomUUID()));
                user.setEmail(EmailET.getText().toString());
                user.setFull_name(NameET.getText().toString());
                user.setUser_name(UserNameET.getText().toString());
                user.setUser_type(usertype);
                String email = EmailET.getText().toString();
                String password = PasswordET.getText().toString();

                //Add Image to firebase

                BitmapDrawable drawable = (BitmapDrawable) profileImage.getDrawable();
                Bitmap bitmap = drawable.getBitmap();
                Model.instance.uploadImage(bitmap, user.getUser_id(), new Model.UploadImageListener() {
                    @Override
                    public void onComplete(String url) {
                        if (url == null) {
                            displayFailedErrors();
                        } else {
                            user.setImageUrl(url);

                            Model.instance.addUser(user, new Model.AddUserListener() {
                                @Override
                                public void onComplete() {
                                    Log.d("TAG", "createUserFirebase:success");
                                    Toast.makeText(activity_register.this,"Registerd Successfully", Toast.LENGTH_SHORT ).show();

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
                                    setResult(Activity.RESULT_OK, returnIntent);
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
            }
        });




    }

    private void init() {

        //Authentication
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mActivity = this;

        profileImage = findViewById(R.id.profile_image_register);
        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.clearCheck();
        NameET = findViewById(R.id.editTextName);
        UserNameET = findViewById(R.id.editTextUserName);
        EmailET = findViewById(R.id.editTextEmail);
        PasswordET = findViewById(R.id.editTextPassword);

        registerBtn = findViewById(R.id.cirRegisterButton);



    }

    private void displayFailedErrors() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity_register.this);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(activity_register.this);
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
        imageurl = data.getData();
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        isImageOk = true;
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
                        profileImage.setImageBitmap(selectedImage);
//                        profileImage.setImageURI(data.getData());
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
                                isImageOk = true;
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                profileImage.setImageURI(data.getData());
                                //profileImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }

                    }
                    break;
            }
        }
    }
}