package com.example.dialogueapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dialogueapp.Model.Lesson;
import com.example.dialogueapp.Model.Model;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class fragment_history extends Fragment {

    private FirebaseAuth mAuth;
    RecyclerView list;
    LessonListViewModel viewModelList;
    MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        //--ViewModel--
        viewModelList = new ViewModelProvider(this).get(LessonListViewModel.class);
        viewModelList.getStLesson().observe(getViewLifecycleOwner(), new Observer<List<Lesson>>() {
            @Override
            public void onChanged(List<Lesson> lessons) {
                adapter.notifyDataSetChanged();
            }
        });

        list = view.findViewById(R.id.recycler_history);
        list.hasFixedSize();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);


        adapter = new MyAdapter();
        list.setAdapter(adapter);

        adapter.setOnClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("TAG","row was clicked " + position);
            }
        });


        ////////////////////////////////
        ////////////User Auth///////////
        ////////////////////////////////
        ImageButton logOutBtn = view.findViewById(R.id.btn_logout_history);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            // User is signed in
            logOutBtn.setVisibility(view.VISIBLE);

            //BUTTON LOGOUT -- Press
            logOutBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogOutFunction();
                    Navigation.findNavController(view).navigate(R.id.action_fragment_history_to_fragment_home);
                }

                private void LogOutFunction() {
                    FirebaseAuth.getInstance().signOut();
                }
            });
        } else {
            // No user is signed in
            logOutBtn.setVisibility(view.GONE);
        }


        return view;
    }



    //////////////////////////////////////
    ///////////// ViewHolder ////////////////
    //////////////////////////////////////
    interface OnItemClickListener{
        void onItemClick(int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView txtLessonId;
        TextView txtLessonTitle;
        TextView txtLessonDate;
        TextView txtLessonTime;
        TextView txtLessonLengthTime;
        TextView txtImageTeacherName;
        public OnItemClickListener listener;
        int position;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtLessonId = itemView.findViewById(R.id.txt_lesson_row_id);
            txtLessonTitle = itemView.findViewById(R.id.txt_lesson_row_title);
            txtLessonDate = itemView.findViewById(R.id.txt_lesson_row_date);
            txtLessonTime = itemView.findViewById(R.id.txt_lesson_row_time);
            txtLessonLengthTime = itemView.findViewById(R.id.txt_lesson_row_length_time);
            //Todo -> Need to put the imageUrl of the teacher on the list_history

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position);
                }
            });
        }

        public void bindData(Lesson lesson, int position) {
            txtLessonId.setText(""+lesson.getLesson_id());
            txtLessonTitle.setText(lesson.getLesson_title());
            txtLessonDate.setText(""+lesson.getSchedule_date());
            txtLessonTime.setText(""+lesson.getLesson_time());
            txtLessonLengthTime.setText(""+lesson.getNumOfMinutesPerLesson());
            this.position = position;
        }
    }


    //////////////////////////////////////
    ///////////// Adapter ////////////////
    //////////////////////////////////////
    class MyAdapter extends RecyclerView.Adapter<MyViewHolder>{
        private OnItemClickListener listener;

        void setOnClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.lesson_list_row,parent,false);
            MyViewHolder holder = new MyViewHolder(view);
            holder.listener = listener;
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
            Lesson lesson = viewModelList.getStLesson().getValue().get(position);
            holder.bindData(lesson,position);
        }

        @Override
        public int getItemCount() {
            if(viewModelList.getStLesson().getValue()==null)
                return 0;
            return viewModelList.getStLesson().getValue().size();
        }
    }
}