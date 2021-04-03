package com.example.dialogueapp.FragmentsStudent;

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
import android.widget.Toast;

import com.example.dialogueapp.Model.LessonListViewModel;
import com.example.dialogueapp.Model.Lesson;
import com.example.dialogueapp.Model.Model;
import com.example.dialogueapp.Model.User;
import com.example.dialogueapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_my_lessons_student extends Fragment {

    private FirebaseAuth mAuth;
    RecyclerView list;
    LessonListViewModel viewModelList;
    MyAdapterMyLessons adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_lessons_student, container, false);


        list = view.findViewById(R.id.recycler_my_lessons_student);
        list.hasFixedSize();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        list.setLayoutManager(layoutManager);


        adapter = new MyAdapterMyLessons();
        list.setAdapter(adapter);

        adapter.setOnClickListener(new fragment_history.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("TAG","row was clicked " + position);
            }
        });


        ////////////////////////////////
        ////////////User Auth///////////
        ////////////////////////////////
        ImageButton logOutBtn = view.findViewById(R.id.btn_logout_my_lessons_student);
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
                viewModelList.setMyLessons(id);
                viewModelList.getStLesson().observe(getViewLifecycleOwner(), new Observer<List<Lesson>>() {
                    @Override
                    public void onChanged(List<Lesson> lessons) {
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

    class MyViewHolderMyLessons extends RecyclerView.ViewHolder{

        TextView txtLessonTitle;
        TextView txtLessonDate;
        TextView txtLessonTime;
        TextView txtLessonLengthTime;
        TextView txtImageTeacherName;
        CircularProgressButton isDone;
        ImageView img_done;
        CircleImageView imageTeacher;
        public fragment_history.OnItemClickListener listener;
        int position;

        public MyViewHolderMyLessons(@NonNull View itemView) {
            super(itemView);

            txtLessonTitle = itemView.findViewById(R.id.txt_lesson_row_title);
            txtLessonDate = itemView.findViewById(R.id.txt_lesson_row_date);
            txtLessonTime = itemView.findViewById(R.id.txt_lesson_row_time);
            txtLessonLengthTime = itemView.findViewById(R.id.txt_lesson_row_length_time);
            isDone = itemView.findViewById(R.id.btn_order_now);
            img_done = itemView.findViewById(R.id.imageViewDone);
            imageTeacher = itemView.findViewById(R.id.image_teacher_row_lesson);
            //Todo -> Need to put the imageUrl of the teacher on the list_history

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(position);
                }
            });
            Lesson lesson = viewModelList.getStLesson().getValue().get(position);
        }

        public void bindData(Lesson lesson, int position) {

            txtLessonTitle.setText(lesson.getLesson_title());
            txtLessonDate.setText(""+lesson.getSchedule_date());
            txtLessonTime.setText(""+lesson.getLesson_time());
            txtLessonLengthTime.setText(""+lesson.getNumOfMinutesPerLesson());
            if(lesson.getIsDone()){
                isDone.setVisibility(View.GONE);
                img_done.setVisibility(View.VISIBLE);
                Log.d("isDone??",lesson.getIsDone()+"");
            }else{
                isDone.setVisibility(View.VISIBLE);
                img_done.setVisibility(View.GONE);
                Log.d("isDone??",lesson.getIsDone()+"");

            }
            isDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lesson.setDone(true);
                    Model.instance.addLesson(lesson, new Model.AddLessonListener() {
                        @Override
                        public void onComplete() {

                            Toast.makeText(getActivity(), "You Have Done This Lesson Successfully",
                                    Toast.LENGTH_SHORT).show();

                            isDone.setEnabled(true);
                            Model.instance.refreshAllLessons(null);
                        }
                    });
                }
            });
            Model.instance.GetUserByID(lesson.getTeacher_id(), new Model.GetUserByIDListener() {
                @Override
                public void onComplete(User user) {
                    if(user.getImageUrl()!=null){
                        Picasso.get().load(user.getImageUrl()).into(imageTeacher);
                    }
                }
            });
            this.position = position;
        }
    }


    //////////////////////////////////////
    ///////////// Adapter ////////////////
    //////////////////////////////////////
    class MyAdapterMyLessons extends RecyclerView.Adapter<MyViewHolderMyLessons>{
        private fragment_history.OnItemClickListener listener;

        void setOnClickListener(fragment_history.OnItemClickListener listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public MyViewHolderMyLessons onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.lst_item_my_lessons_student,parent,false);
            MyViewHolderMyLessons holder = new MyViewHolderMyLessons(view);
            holder.listener = listener;
            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolderMyLessons holder, int position) {
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