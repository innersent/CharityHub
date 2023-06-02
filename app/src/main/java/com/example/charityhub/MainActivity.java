package com.example.charityhub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.charityhub.Announcement;
import com.example.charityhub.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        NavController navController = Navigation.findNavController(this, R.id.navHostFragment);
        NavigationUI.setupWithNavController(navigationView, navController);

        Announcement announcment = new Announcement("https://mail.kz/images/912f0e1537799e5a9e6b697c11424631-slider.jpg", "девятка", "", "https://www.meme-arsenal.com/memes/df2292c6c154dbedf5b22ab0fc54c755.jpg", "Просьба анон пж", 0, 0, false);
//        mDatabase.push().setValue(announcment);
//        mAuth.signOut();
    }

    public void init(){
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Announcements");
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
    }

    public void logOut(View view){
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, GreetingActivity.class));
    }

    public void openHome(View view){
        Navigation.findNavController(this, R.id.navHostFragment).navigate(R.id.homePage);
    }
    public void openCreate(View view){
        Navigation.findNavController(this, R.id.navHostFragment).navigate(R.id.createPage);
    }

    public void openMenu(View view){
        drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            startActivity(new Intent(MainActivity.this, GreetingActivity.class));
        }
    }
}