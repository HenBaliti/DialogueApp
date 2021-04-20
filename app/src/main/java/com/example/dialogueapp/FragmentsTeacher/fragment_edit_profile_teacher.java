package com.example.dialogueapp.FragmentsTeacher;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.dialogueapp.FragmentsStudent.fragment_edit_profileArgs;
import com.example.dialogueapp.Model.Model;
import com.example.dialogueapp.Model.User;
import com.example.dialogueapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_edit_profile_teacher extends Fragment {
    private static final int RESULT_OK = -1;
    private EditText txtEditUserName, txtEditFullName;
    private Button saveBtn;
    private CircleImageView EditimageProfile;
    private FirebaseAuth mAuth;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int RESULT_CANCELED = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile_student, container, false);

        //get args
        String userName = fragment_edit_profile_teacherArgs.fromBundle(getArguments()).getUsername();
        String fullName = fragment_edit_profile_teacherArgs.fromBundle(getArguments()).getFullname();
        //   String email = fragment_edit_profileArgs.fromBundle(getArguments()).getEmail();
        String imageUrl = fragment_edit_profile_teacherArgs.fromBundle(getArguments()).getImageUrl();
        txtEditUserName = view.findViewById(R.id.Edit_username_student);
        txtEditFullName = view.findViewById(R.id.edit_fullname_student);
        //txtEditEmail = view.findViewById(R.id.edit_email_student);
        EditimageProfile = view.findViewById(R.id.edit_image_profile_student);
        saveBtn = view.findViewById(R.id.save_changes_student);
        txtEditUserName.setText(userName);
        txtEditFullName.setText(fullName);
        // txtEditEmail.setText(email);;
        if (imageUrl != null) {
            Picasso.get().load(imageUrl).into(EditimageProfile);

        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        TextView moveToProfile = view.findViewById(R.id.back_profile_student);
        moveToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_fragment_edit_profile_teacher_to_fragment_profile_teacher);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Model.instance.GetUserObjByID(user.getEmail(), new Model.GetUserObjByEmailListener() {
                    @Override
                    public void onComplete(User currentUser) {

                        User updatedUser = new User();
                        updatedUser.setUser_id(currentUser.getUser_id());
                        updatedUser.setUser_type(currentUser.getUser_type());
                        updatedUser.setEmail(currentUser.getEmail());
                        updatedUser.setUser_name(txtEditUserName.getText().toString());
                        updatedUser.setFull_name(txtEditFullName.getText().toString());
                        updatedUser.setImageUrl(imageUrl);
                        Model.instance.UpdateU(updatedUser, new Model.UpdateUser() {
                            @Override
                            public void onComplete() {
                                Toast.makeText(getActivity(),"Profile Details Changed Successfully", Toast.LENGTH_SHORT ).show();
                            }
                        });
                        // Navigation.findNavController(view).navigate(R.id.action_fragment_edit_profile_to_fragment_profile);


                    }
                });
            }
        });




        EditimageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click","image url");
                EditImage();
            }
        });




        return view;
    }

    private void EditImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                        //profileImage.setImageBitmap(selectedImage);
                        EditimageProfile.setImageURI(data.getData());
                    }

                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();

                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                EditimageProfile.setImageURI(data.getData());
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

