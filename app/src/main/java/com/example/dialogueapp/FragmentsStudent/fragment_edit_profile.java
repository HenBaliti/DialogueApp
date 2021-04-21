package com.example.dialogueapp.FragmentsStudent;

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

import com.example.dialogueapp.Model.Model;
import com.example.dialogueapp.Model.User;
import com.example.dialogueapp.R;
import com.example.dialogueapp.activity_register;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_edit_profile extends Fragment {
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
        String userName = fragment_edit_profileArgs.fromBundle(getArguments()).getUsername();
        String fullName = fragment_edit_profileArgs.fromBundle(getArguments()).getFullname();
     //   String email = fragment_edit_profileArgs.fromBundle(getArguments()).getEmail();
        String imageUrl = fragment_edit_profileArgs.fromBundle(getArguments()).getImageprofile();
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
                Navigation.findNavController(view).navigate(R.id.action_fragment_edit_profile_to_fragment_profile);
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






        return view;
    }

}
