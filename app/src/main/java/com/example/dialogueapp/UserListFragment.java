package com.example.dialogueapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.dialogueapp.Model.Model;
import com.example.dialogueapp.Model.UserListViewModel;
import com.example.dialogueapp.Model.User;

import java.util.List;

public class UserListFragment extends Fragment {
    UserListViewModel viewModelList;
    Button btn_add;
    ProgressBar pb;
    UserListFragment.MyAdapter adapter;
    SwipeRefreshLayout sref;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search_users, container, false);

        viewModelList = new ViewModelProvider(this).get(UserListViewModel.class);

        ListView list = view.findViewById(R.id.teacher_list);
        pb = view.findViewById(R.id.progressBar_teacher_list);
        btn_add = view.findViewById(R.id.btn_add_teacher);
        pb.setVisibility(View.INVISIBLE);
        sref = view.findViewById(R.id.teacherList_swipe);

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
                addNewUser();
            }
        });


        //--ViewModel--
        viewModelList.getListTeachers().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.notifyDataSetChanged();
            }
        });


        reloadData();

        return view;
    }

    static int id = 0;
    private void addNewUser() {
        btn_add.setEnabled((false));
        int id = viewModelList.getListTeachers().getValue().size();
        User user = new User();
        user.setUser_id(id);
        //user.setLesson_title("LESSON "+id);
        pb.setVisibility(View.VISIBLE);
        Model.instance.addUser(user, new Model.AddUserListener() {
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
        Model.instance.refreshAllUsers(new Model.GetAllUsersListener() {
            @Override
            public void onComplete(List<User> data) {
                pb.setVisibility(View.INVISIBLE);
                btn_add.setEnabled((true));
                sref.setRefreshing(false);
            }
        });
    }
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            if(viewModelList.getListTeachers().getValue()==null)
                return 0;
            return viewModelList.getListTeachers().getValue().size();
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
            User user = viewModelList.getListTeachers().getValue().get(position);
            //txtLessonId.setText(""+lesson.getLesson_id());

            return convertView;
        }
    }
}
