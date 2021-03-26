package com.example.dialogueapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.dialogueapp.Model.Lesson;
import com.example.dialogueapp.Model.Model;

import java.util.List;

public class fragment_LessonList extends Fragment {
//    private FirebaseAuth mAuth;

    LessonListViewModel viewModelList;
    Button btn_add;
    ProgressBar pb;
    MyAdapter adapter;
    SwipeRefreshLayout sref;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment__lesson_list, container, false);

        viewModelList = new ViewModelProvider(this).get(LessonListViewModel.class);

        String datePassed = fragment_LessonListArgs.fromBundle(getArguments()).getDateFilter();
        Log.d("TAG",datePassed);
//        ImageButton logOutBtn = view.findViewById(R.id.btnLesson);
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user != null) {
//            // User is signed in
//            logOutBtn.setVisibility(view.VISIBLE);
//
//            //BUTTON LOGOUT -- Press
//            logOutBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    LogOutFunction();
//                    Navigation.findNavController(view).navigate(R.id.action_fragment_LessonList_to_fragment_home);
//                }
//
//                private void LogOutFunction() {
//                    FirebaseAuth.getInstance().signOut();
//                }
//            });
//        } else {
//            // No user is signed in
//            logOutBtn.setVisibility(view.GONE);
//        }
//


        //--LIST--
        ListView list = view.findViewById(R.id.lesson_list);
        pb = view.findViewById(R.id.progressBar_lesson_list);
        btn_add = view.findViewById(R.id.btn_add_lesson);
        pb.setVisibility(View.INVISIBLE);
        sref = view.findViewById(R.id.lessonList_swipe);

        sref.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sref.setRefreshing(true);
                reloadData();
            }
        });


        adapter = new MyAdapter();
        list.setAdapter(adapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewLesson();
            }
        });


        //--ViewModel--
        viewModelList.getStLesson().observe(getViewLifecycleOwner(), new Observer<List<Lesson>>() {
            @Override
            public void onChanged(List<Lesson> lessons) {
                adapter.notifyDataSetChanged();
            }
        });


        reloadData();

        return view;
    }

    static int id = 0;
    private void addNewLesson() {
        btn_add.setEnabled((false));
        int id = viewModelList.getStLesson().getValue().size();
        Lesson lesson = new Lesson();
        lesson.setLesson_id(id);
        lesson.setLesson_title("LESSON "+id);
        pb.setVisibility(View.VISIBLE);
        Model.instance.addLesson(lesson, new Model.AddLessonListener() {
            @Override
            public void onComplete() {
                reloadData();
            }
        });
        id++;
    }



    //Add New Lesson -> Student

    void reloadData(){
        pb.setVisibility(View.VISIBLE);
        btn_add.setEnabled((false));
        Model.instance.refreshAllLessons(new Model.GetAllLessonsListener() {
            @Override
            public void onComplete(List<Lesson> data) {
                pb.setVisibility(View.INVISIBLE);
                btn_add.setEnabled((true));
                sref.setRefreshing(false);
            }
        });
    }
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {

            if(viewModelList.getStLesson().getValue()==null)
                return 0;
            return viewModelList.getStLesson().getValue().size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = getLayoutInflater().inflate(R.layout.lesson_list_row,null);
            }

            TextView txtLessonId = convertView.findViewById(R.id.txt_lesson_row_id);
            TextView txtLessonTitle = convertView.findViewById(R.id.txt_lesson_row_title);
            TextView txtLessonDate = convertView.findViewById(R.id.txt_lesson_row_date);
            TextView txtLessonTime = convertView.findViewById(R.id.txt_lesson_row_time);
            TextView txtLessonLengthTime = convertView.findViewById(R.id.txt_lesson_row_length_time);

            Lesson lesson = viewModelList.getStLesson().getValue().get(position);

            txtLessonId.setText(""+lesson.getLesson_id());
            txtLessonTitle.setText(lesson.getLesson_title());
            txtLessonDate.setText(""+lesson.getSchedule_date());
            txtLessonTime.setText(""+lesson.getLesson_time());
            txtLessonLengthTime.setText(""+lesson.getNumOfMinutesPerLesson());


            return convertView;
        }
    }
}