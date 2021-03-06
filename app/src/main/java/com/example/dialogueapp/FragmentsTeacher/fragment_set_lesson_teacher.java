package com.example.dialogueapp.FragmentsTeacher;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dialogueapp.DatePickerFragment;
import com.example.dialogueapp.Model.LessonListViewModel;
import com.example.dialogueapp.Model.Lesson;
import com.example.dialogueapp.Model.Model;
import com.example.dialogueapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.UUID;

public class fragment_set_lesson_teacher extends Fragment {
    Button createLesson_btn;
    LessonListViewModel viewModelList;
    TextView txt_Title;
    TextView timePickerValueTextView;
    TextView tvDate;
    FirebaseUser user;
    Button btn_15;
    Button btn_30;
    Button btn_45;
    Lesson lesson;
    boolean isValidate = false;
    int numOfMinutes;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_set_lesson_teacher, container, false);

        viewModelList = new ViewModelProvider(this).get(LessonListViewModel.class);

        txt_Title = view.findViewById(R.id.txt_picked_title_teacher);
        timePickerValueTextView = view.findViewById(R.id.txt_picked_time_teacher);
        tvDate = view.findViewById(R.id.txt_picked_date_teacher);
        txt_Title.setText("");
        tvDate.setText("");
        timePickerValueTextView.setText("");

        ImageButton logOutBtn = view.findViewById(R.id.btn_logout_schedule_teacher);
        user = FirebaseAuth.getInstance().getCurrentUser();
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




        //TimePicker -> Shcedule time -> 15 | 30 | 45
        btn_15 = view.findViewById(R.id.btn_15);
        btn_15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //FF747277
                isValidate = true;
                numOfMinutes = 15;
                btn_15.setBackgroundColor(Color.parseColor("#11CFC5"));
                btn_30.setBackgroundColor(Color.parseColor("#FF747277"));
                btn_45.setBackgroundColor(Color.parseColor("#FF747277"));
//                btn_45.setTextColor(Color.WHITE);
            }
        });
        btn_30 = view.findViewById(R.id.btn_30);
        btn_30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //FF747277
                isValidate = true;
                numOfMinutes = 30;
                btn_15.setBackgroundColor(Color.parseColor("#FF747277"));
                btn_30.setBackgroundColor(Color.parseColor("#11CFC5"));
                btn_45.setBackgroundColor(Color.parseColor("#FF747277"));
//                btn_45.setTextColor(Color.WHITE);
            }
        });
        btn_45 = view.findViewById(R.id.btn_45);
        btn_45.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //FF747277
                isValidate = true;
                numOfMinutes = 45;
                btn_15.setBackgroundColor(Color.parseColor("#FF747277"));
                btn_30.setBackgroundColor(Color.parseColor("#FF747277"));
                btn_45.setBackgroundColor(Color.parseColor("#11CFC5"));
//                btn_45.setTextColor(Color.WHITE);
            }
        });

        //SCHEDULE PICKER
        ImageButton clickScheduleBtn = (ImageButton)view.findViewById(R.id.btn_schedule_picker_teacher);

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
        // Get open TimePickerDialog button.
        ImageButton timePickerDialogButton = (ImageButton)view.findViewById(R.id.btn_time_picker_teacher);
        timePickerDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a new OnTimeSetListener instance. This listener will be invoked when user click ok button in TimePickerDialog.
                TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        StringBuffer strBuf = new StringBuffer();
//                        strBuf.append("You select time is ");
                        strBuf.append(String.valueOf(hour));
                        strBuf.append(":");
                        strBuf.append(String.valueOf(minute));
//                        Log.d("Tagggg",strBuf.toString());

                        timePickerValueTextView = (TextView)getActivity().findViewById(R.id.txt_picked_time_teacher);
                        timePickerValueTextView.setText(strBuf.toString());
                    }
                };

                Calendar now = Calendar.getInstance();
                int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
                int minute = now.get(java.util.Calendar.MINUTE);

                // Whether show time in 24 hour format or not.
                boolean is24Hour = true;

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), onTimeSetListener, hour, minute, is24Hour);

                timePickerDialog.setIcon(R.drawable.ic_history);
                timePickerDialog.setTitle("Please select time.");

                timePickerDialog.show();
            }
        });


        //--------------------------
        //Create Lesson - Button
        //--------------------------

        createLesson_btn = view.findViewById(R.id.btn_create_lesson_teacher);
        createLesson_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(isValidate){
                    Log.d("TAG",timePickerValueTextView.getText().toString());
                    if(timePickerValueTextView.getText().toString().length()>=3){
                        if(tvDate.getText().toString().length()>=4){
                            if(txt_Title.getText().toString().length()>0){
                                addNewLessonTeacher();
                            }
                            else{
                                Toast.makeText(getActivity(), "You Need To Choose A Lesson Title.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }else{
                            Toast.makeText(getActivity(), "You Need To Choose A Lesson Schedule Date.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(getActivity(), "You Need To Choose A Lesson Schedule Time.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getActivity(), "You Need To Choose A Lesson Length Time.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


        return view;
    }


    //Add New Lesson -> Teacher

    private void addNewLessonTeacher() {
//        String teacherId = user.getUid();

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Creating New Lesson...");
        progressDialog.show();
        createLesson_btn.setEnabled((false));
//        id = viewModelList.getStLesson().getValue().size();
        lesson = new Lesson();

        Model.instance.getStudentByEmail(user.getEmail(), new Model.GetUserByEmailListener() {
            @Override
            public void onComplete(String userId) {
                Log.d("Teacher ID IS: ",""+userId);
                lesson.setLesson_id(String.valueOf(UUID.randomUUID()));
                lesson.setLesson_title(txt_Title.getText().toString());
                lesson.setSchedule_date(tvDate.getText().toString());
                lesson.setLesson_time(timePickerValueTextView.getText().toString());
                lesson.setTeacher_id(userId);
                lesson.setNumOfMinutesPerLesson(numOfMinutes);
                lesson.setCatch(false);
                lesson.setDone(false);

                Model.instance.addLesson(lesson, new Model.AddLessonListener() {
                    @Override
                    public void onComplete() {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), "Added a new Lesson Succeeded",
                                Toast.LENGTH_SHORT).show();
                        createLesson_btn.setEnabled((true));
                        txt_Title.setText("");
                        timePickerValueTextView.setText("");
                        tvDate.setText("");
                        numOfMinutes = 0;
                        btn_15.setBackgroundColor(Color.parseColor("#FF747277"));
                        btn_30.setBackgroundColor(Color.parseColor("#FF747277"));
                        btn_45.setBackgroundColor(Color.parseColor("#FF747277"));

                        Model.instance.refreshAllLessons(null);
                    }
                });

            }
        });

    }



    //--------------------------
    //Date Picker
    //--------------------------

    DatePickerDialog.OnDateSetListener onDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            tvDate.setText(String.valueOf(year) + "-" + String.valueOf(monthOfYear+1)
                    + "-" + String.valueOf(dayOfMonth));
        }
    };

}