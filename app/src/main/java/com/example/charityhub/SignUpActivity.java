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

public class SignUpActivity extends AppCompatActivity {
    FirebaseAuth mAuth;

    EditText emailSignUp;
    EditText passwordSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }
    public void init(){
        mAuth = FirebaseAuth.getInstance();
        emailSignUp = findViewById(R.id.emailSignUpEditText);
        passwordSignUp = findViewById(R.id.passwordSignUpEditText);
    }

    public void goBackPageFromSignUp(View view){
        startActivity(new Intent(SignUpActivity.this, GreetingActivity.class));
    }

    public void signUp(View view){
        String email = emailSignUp.getText().toString();
        String password = passwordSignUp.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    Toast.makeText(SignUpActivity.this, "Авторизация прошла успешно", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(SignUpActivity.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}