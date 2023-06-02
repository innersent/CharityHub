package com.example.charityhub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GreetingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);
    }
    public void goSignInPage(View view){
        startActivity(new Intent(GreetingActivity.this, SignInActivity.class));
    }
    public void goSignUpPage(View view){
        startActivity(new Intent(GreetingActivity.this, SignUpActivity.class));
    }
}