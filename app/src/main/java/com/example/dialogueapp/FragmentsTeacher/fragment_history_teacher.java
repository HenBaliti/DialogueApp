package com.example.dialogueapp.FragmentsTeacher;

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
import android.widget.TextView;

import com.example.dialogueapp.FragmentsStudent.fragment_history;
import com.example.dialogueapp.Model.Lesson;
import com.example.dialogueapp.Model.LessonListViewModel;
import com.example.dialogueapp.Model.Model;
import com.example.dialogueapp.Model.User;
import com.example.dialogueapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class fragment_history_teacher extends Fragment {

    private FirebaseAuth mAuth;
    RecyclerView list;
    LessonListViewModel viewModelList;
    MyAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_teacher, container, false);



        list = view.findViewById(R.id.recycler_history_teacher);
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
        ImageButton logOutBtn = view.findViewById(R.id.btn_logout_history_teacher);
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
        }



        //--ViewModel--
        viewModelList = new ViewModelProvider(this).get(LessonListViewModel.class);
        Model.instance.getStudentByEmail(user.getEmail(), new Model.GetUserByEmailListener() {
            @Override
            public void onComplete(String id) {
                Log.d("CheckLess2",""+id);
                viewModelList.setStLessonHistoryOfTeacher(id);
                viewModelList.getStLesson().observe(getViewLifecycleOwner(), new Observer<List<Lesson>>() {
                    @Override
                    public void onChanged(List<Lesson> lessons) {
                        Log.d("CheckLess",lessons.size()+"");
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });


        return view;
    }



    //////////////////////////////////////
    ///////////// ViewHolder ////////////////
    //////////////////////////////////////
    interface OnItemClickListener{
        void onItemClick(int position);
    }

    class MyViewHolderHistoryTeacher extends RecyclerView.ViewHolder{

        TextView txtLessonTitle;
        TextView txtLessonDate;
        TextView txtLessonTime;
        TextView txtLessonLengthTime;
        CircleImageView imageStudent;
        public OnItemClickListener listener;
        int position;

        public MyViewHolderHistoryTeacher(@NonNull View itemView) {
            super(itemView);

            txtLessonTitle = itemView.findViewById(R.id.txt_lesson_row_title);
            txtLessonDate = itemView.findViewById(R.id.txt_lesson_row_date);
            txtLessonTime = itemView.findViewById(R.id.txt_lesson_row_time);
            txtLessonLengthTime = itemView.findViewById(R.id.txt_lesson_row_length_time);
            imageStudent = itemView.findViewById(R.id.image_teacher_row_lesson);
            //Todo -> Need to put the imageUrl of the teacher on the list_history

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position);
                }
            });
        }

        public void bindData(Lesson lesson, int position) {
            txtLessonTitle.setText(lesson.getLesson_title());
            txtLessonDate.setText(""+lesson.getSchedule_date());
            txtLessonTime.setText(""+lesson.getLesson_time());
            txtLessonLengthTime.setText(""+lesson.getNumOfMinutesPerLesson());
            Model.instance.GetUserByID(lesson.getTeacher_id(), new Model.GetUserByIDListener() {
                @Override
                public void onComplete(User user) {
                    if(user.getImageUrl()!=null){
                        Picasso.get().load(user.getImageUrl()).into(imageStudent);
                    }
                }
            });
            this.position = position;
        }
    }


    //////////////////////////////////////
    ///////////// Adapter ////////////////
    //////////////////////////////////////
    class MyAdapter extends RecyclerView.Adapter<MyViewHolderHistoryTeacher>{
        private OnItemClickListener listener;

        void setOnClickListener(OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolderHistoryTeacher onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.lst_item_history,parent,false);
            MyViewHolderHistoryTeacher holder = new MyViewHolderHistoryTeacher(view);
            holder.listener = listener;
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolderHistoryTeacher holder, int position) {
//            Lesson lesson = viewModelList.getStLesson().getValue().get(position);
//            holder.bindData(lesson,position);
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