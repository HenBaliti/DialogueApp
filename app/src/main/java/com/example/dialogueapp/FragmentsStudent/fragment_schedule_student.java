package com.example.dialogueapp.FragmentsStudent;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.dialogueapp.DatePickerFragment;
import com.example.dialogueapp.R;
import com.example.dialogueapp.FragmentsStudent.fragment_schedule_studentDirections;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;


public class fragment_schedule_student extends Fragment {
//    private FirebaseAuth mAuth;

    TextView tvDate;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_schedule_student, container, false);



        ImageButton logOutBtn = view.findViewById(R.id.btn_logout_schedule);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
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

            String errorMsg = "You are not Login yet.";
//            fragment_schedule_studentDirections.ActionFragmentScheduleStudentToFragmentLogin action = fragment_schedule_studentDirections.actionFragmentScheduleStudentToFragmentLogin(errorMsg);
//            Navigation.findNavController(view).navigate(action);

        }




        //SCHEDULE PICKER
        ImageButton clickScheduleBtn = (ImageButton)view.findViewById(R.id.btn_schedule_picker);

        // Using an onclick listener on the editText to show the datePicker
        clickScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dpf = new DatePickerFragment().newInstance();
                dpf.setCallBack(onDate);
                dpf.show(getFragmentManager().beginTransaction(), "DatePickerFragment");
            }
        });


        //----------------------------------------------------



        Button findFreeTeachers_btn = view.findViewById(R.id.btn_find_free_teachers);
        findFreeTeachers_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_schedule_studentDirections.ActionFragmentScheduleStudentToFragmentLessonList action = fragment_schedule_studentDirections.actionFragmentScheduleStudentToFragmentLessonList(tvDate.getText().toString());
                Navigation.findNavController(view).navigate(action);
            }
        });

        return view;
    }

    DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            tvDate = (TextView) getActivity().findViewById(R.id.txt_picked_date);
            tvDate.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(dayOfMonth));
        }
    };





}