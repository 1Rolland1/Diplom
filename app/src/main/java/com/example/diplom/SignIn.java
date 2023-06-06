package com.example.diplom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText ETemail;
    private EditText ETpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in

                } else {
                    // User is signed out

                }

            }
        };

        ETemail = (EditText) findViewById(R.id.etEmail);
        ETpassword = (EditText) findViewById(R.id.etPassword);

    }

    public void input(View view){
        signin(ETemail.getText().toString(),ETpassword.getText().toString());
    }

    public void register(View view){
        registration(ETemail.getText().toString(),ETpassword.getText().toString());
    }

    public void signin(String email , String password)
    {
        Intent intent = new Intent(this, Documents.class);
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    int atIndex = email.indexOf("@");
                    String name = "";

                    if (atIndex != -1) {
                        name = email.substring(0, atIndex);
                    }

                    Toast.makeText(SignIn.this, "Приветсвуем Вас," + name, Toast.LENGTH_SHORT).show();



                    startActivity(intent);

                }else
                    Toast.makeText(SignIn.this, "Данные пользователя введены не верно", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void registration (String email , String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(SignIn.this, "Регистрация прошла успешна", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(SignIn.this, "Данный пользователь уже зарегистрирован или данные введены неверно", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
