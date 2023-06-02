package com.example.charityhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    EditText emailSignIn;
    EditText passwordSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        init();
    }
    public void init(){
        mAuth = FirebaseAuth.getInstance();
        emailSignIn = findViewById(R.id.emailSignInEditText);
        passwordSignIn = findViewById(R.id.passwordSignInEditText);
    }

    public void goBackPage(View view){
        startActivity(new Intent(SignInActivity.this, GreetingActivity.class));
    }

    public void signIn(View view){
        String email = emailSignIn.getText().toString();
        String password = passwordSignIn.getText().toString();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                    Toast.makeText(SignInActivity.this, "Авторизация прошла успешно", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignInActivity.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}