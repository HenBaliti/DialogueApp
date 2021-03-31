package com.example.dialogueapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    NavController navController;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle mToggle;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        Intent intent = getIntent();
        if (intent != null) {
            //Authentication
            // Initialize Firebase Auth
            mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
        }
        Toast.makeText(this, user.getEmail() + " work", Toast.LENGTH_LONG ).show();


        navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Bottom Navigation Controller
        NavigationUI.setupWithNavController(bottomNav, navController);

//        //Drawer Navigation Controller
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);


//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Intent intent;
//                if(item.getItemId()==R.id.fragment_home)
//                    Log.d("MsgAlert","This is home clicking");
//                switch (item.getItemId()) {
//                    case R.id.fragment_home :
//                        Log.d("Checking","--------------------------------------------------------");
//                        intent = new Intent(MainActivity.this,fragment_home.class);
//                        break;
//                    case R.id.fragment_schedule_student :
//                        intent = new Intent(MainActivity.this,fragment_home.class);
//                        break;
//                    case R.id.fragment_history :
//                        intent = new Intent(MainActivity.this,fragment_home.class);
//                        break;
//                }
//                return true;
//            }
//        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }


}