package com.example.dialogueapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import java.util.LinkedList;
import java.util.List;

public class fragment_LessonList extends Fragment {
    Button btn_add;
    ProgressBar pb;
    List<Lesson> stLesson = new LinkedList<Lesson>();
    MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment__lesson_list, container, false);

        ListView list = view.findViewById(R.id.lesson_list);
        pb = view.findViewById(R.id.progressBar_lesson_list);
        btn_add = view.findViewById(R.id.btn_add_lesson);
        pb.setVisibility(View.INVISIBLE);


        adapter = new MyAdapter();
        list.setAdapter(adapter);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewLesson();
            }
        });

        reloadData();
        return view;
    }

    static int id = 0;
    private void addNewLesson() {
        btn_add.setEnabled((false));
        int id = stLesson.size();
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

    void reloadData(){
        pb.setVisibility(View.VISIBLE);
        btn_add.setEnabled((false));
        Model.instance.getAllLessons(new Model.GetAllLessonsListener() {
            @Override
            public void onComplete(List<Lesson> data) {
                stLesson = data;
                for (Lesson lesson: data) {
                    Log.d("TAG","Lesson id: "+lesson.getLesson_id());
                }
                pb.setVisibility(View.INVISIBLE);
                btn_add.setEnabled((true));
                adapter.notifyDataSetChanged();
            }
        });
    }
    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {

            if(stLesson==null)
                return 0;
            return stLesson.size();
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
            Lesson lesson = stLesson.get(position);
            txtLessonId.setText(""+lesson.getLesson_id());

            return convertView;
        }
    }
}