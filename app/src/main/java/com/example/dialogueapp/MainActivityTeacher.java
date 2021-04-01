package com.example.dialogueapp;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityTeacher extends AppCompatActivity {
    private FirebaseAuth mAuth;

    NavController navController;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle mToggle;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_teacher);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_teacher);


        Intent intent = getIntent();
        if (intent != null) {
            //Authentication
            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
        }
        Toast.makeText(this, user.getEmail() + " work", Toast.LENGTH_LONG ).show();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_teacher);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Bottom Navigation Controller
        NavigationUI.setupWithNavController(bottomNav, navController);
    }
}