package com.example.dialogueapp.FragmentsStudent;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dialogueapp.Model.LessonListViewModel;
import com.example.dialogueapp.Model.Lesson;
import com.example.dialogueapp.Model.Model;
import com.example.dialogueapp.Model.User;
import com.example.dialogueapp.R;
import com.example.dialogueapp.FragmentsStudent.fragment_LessonListArgs;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import de.hdodenhof.circleimageview.CircleImageView;

public class fragment_LessonList extends Fragment {
//    private FirebaseAuth mAuth;
    FirebaseUser user;
    ProgressBar pb;
    MyAdapter adapter;
    SwipeRefreshLayout sref;
    RecyclerView list;
    LessonListViewModel viewModelList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment__lesson_list, container, false);

        String datePassed = fragment_LessonListArgs.fromBundle(getArguments()).getDateFilter();
        Log.d("TAG",datePassed);

        //--ViewModel--
        viewModelList = new ViewModelProvider(this).get(LessonListViewModel.class);
        if(datePassed!=null){ ////////////////---------Need to add a else statement
            viewModelList.setStLessonFilteredDate(datePassed);
        }
        viewModelList.getStLesson().observe(getViewLifecycleOwner(), new Observer<List<Lesson>>() {
            @Override
            public void onChanged(List<Lesson> lessons) {
                adapter.notifyDataSetChanged();
            }
        });
        list = view.findViewById(R.id.recycler_list_lesson);
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
        ImageButton logOutBtn = view.findViewById(R.id.btn_logout_lesson_list);
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
//


        //--LIST--

        pb = view.findViewById(R.id.progressBar_lesson_list);
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



        reloadData();

        return view;
    }


    //////////////////////////////////////
    ///////////// ViewHolder ////////////////
    //////////////////////////////////////
    interface OnItemClickListener{
        void onItemClick(int position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView txtLessonTitle;
        TextView txtLessonDate;
        TextView txtLessonTime;
        TextView txtLessonLengthTime;
        CircularProgressButton btn_order_Now;
        CircleImageView imageTeacher;
        public OnItemClickListener listener;
        int position;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtLessonTitle = itemView.findViewById(R.id.txt_lesson_row_title);
            txtLessonDate = itemView.findViewById(R.id.txt_lesson_row_date);
            txtLessonTime = itemView.findViewById(R.id.txt_lesson_row_time);
            txtLessonLengthTime = itemView.findViewById(R.id.txt_lesson_row_length_time);
            btn_order_Now = itemView.findViewById(R.id.btn_order_now);
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

            Model.instance.GetUserByID(lesson.getTeacher_id(), new Model.GetUserByIDListener() {
                @Override
                public void onComplete(User teacherData) {

                    txtLessonTitle.setText(lesson.getLesson_title());
                    txtLessonDate.setText(""+lesson.getSchedule_date());
                    txtLessonTime.setText(""+lesson.getLesson_time());
                    txtLessonLengthTime.setText(""+lesson.getNumOfMinutesPerLesson());


                    Model.instance.GetUserObjByID(user.getEmail(), new Model.GetUserObjByEmailListener() {
                        @Override
                        public void onComplete(User user) {
//
//                            imageTeacher.setImageURI();
                        }
                    });


                    btn_order_Now.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //UPDATE -> ROOM AND FIREBASE

                            //Get the user(Student) id
                            Model.instance.getStudentByEmail(user.getEmail(), new Model.GetUserByEmailListener() {
                                @Override
                                public void onComplete(String userId) {
                                    Log.d("User ID IS: ",""+userId);

                                    //Get the Lesson on click Id + set the student id for the lesson
                                    lesson.setStudent_id(userId);
                                    lesson.setCatch(true);

                                    Model.instance.addLesson(lesson, new Model.AddLessonListener() {
                                        @Override
                                        public void onComplete() {
//                                    Toast.makeText(getActivity(), "Update Lesson Succeeded",
//                                            Toast.LENGTH_SHORT).show();
                                            Toast.makeText(getActivity(), "You Have Set A Lesson to the "+lesson.getSchedule_date()+"\n"+"Time: "+lesson.getLesson_time()+" Successfully.",
                                                    Toast.LENGTH_SHORT).show();

                                            Model.instance.refreshAllLessons(null);
                                        }
                                    });

                                }
                            });



                        }
                    });
                }
            });

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
            View view = getLayoutInflater().inflate(R.layout.lst_item_find_free_lessons,parent,false);
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



    //Add New Lesson -> Student

    void reloadData(){
        pb.setVisibility(View.VISIBLE);

        Model.instance.refreshAllLessons(new Model.GetAllLessonsListener() {
            @Override
            public void onComplete(List<Lesson> data) {
                pb.setVisibility(View.INVISIBLE);

                sref.setRefreshing(false);
            }
        });
    }

}