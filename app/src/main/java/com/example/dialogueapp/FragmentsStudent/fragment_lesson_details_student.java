package com.example.dialogueapp.FragmentsStudent;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dialogueapp.Model.Lesson;
import com.example.dialogueapp.Model.Model;
import com.example.dialogueapp.Model.User;
import com.example.dialogueapp.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class fragment_lesson_details_student extends Fragment {

    TextView txt_isDone;
    TextView txt_length;
    TextView txt_schedule_date;
    TextView txt_title;
    TextView txt_schedule_time;
    TextView teacherName;
    TextView studentName;
    CircleImageView imageTeacher;
    CircleImageView imageStudent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.lesson_details, container, false);

        txt_isDone = view.findViewById(R.id.txt_isDone_Details);
        txt_length = view.findViewById(R.id.txt_length_time_details);
        txt_schedule_date = view.findViewById(R.id.txt_schedule_date_details);
        imageTeacher = view.findViewById(R.id.imageTeacherDetails);
        imageStudent = view.findViewById(R.id.imageStudentDetails);
        txt_title = view.findViewById(R.id.txt_title_lesson_details);
        txt_schedule_time = view.findViewById(R.id.txt_schedule_time_details);
        teacherName = view.findViewById(R.id.teacherName);
        studentName = view.findViewById(R.id.studentName);

        String lessonID = fragment_lesson_details_studentArgs.fromBundle(getArguments()).getLessonId();

        Model.instance.getLesson(lessonID, new Model.GetLessonListener() {
            @Override
            public void onComplete(Lesson lesson) {
                if(lesson.getIsDone()){
                    txt_isDone.setText("True");
                }
                else{
                    txt_isDone.setText("False");
                }

                txt_title.setText(lesson.getLesson_title());
                txt_length.setText(""+lesson.getNumOfMinutesPerLesson());
                txt_schedule_date.setText(""+lesson.getSchedule_date());
                txt_schedule_time.setText(""+lesson.getLesson_time());

                Model.instance.GetUserByID(lesson.getTeacher_id(), new Model.GetUserByIDListener() {
                    @Override
                    public void onComplete(User user) {
                        teacherName.setText(user.getFull_name());
                        if(user.getImageUrl()!=null){
                            Picasso.get().load(user.getImageUrl()).into(imageTeacher);
                        }
                    }
                });

                Model.instance.GetUserByID(lesson.getStudent_id(), new Model.GetUserByIDListener() {
                    @Override
                    public void onComplete(User user) {
                        studentName.setText(user.getFull_name());
                        if(user.getImageUrl()!=null){
                            Picasso.get().load(user.getImageUrl()).into(imageStudent);
                        }
                    }
                });

            }
        });

        return view;
    }
}