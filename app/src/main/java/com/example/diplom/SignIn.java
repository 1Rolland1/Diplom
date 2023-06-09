package com.example.diplom;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.diplom.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class SignIn extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText ETemail;
    private EditText ETpassword;
    DBHelper2 dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
        dbHelper = new DBHelper2(this);

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
        Intent intent = new Intent(SignIn.this, Menu.class);


        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    String name = getName(email);

                    if (dbHelper.CheckName(name)){
                        Toast.makeText(SignIn.this, "Приветсвуем Вас," + name, Toast.LENGTH_SHORT).show();
                        // Добавляем значение переменной name в Intent
                        intent.putExtra("name", name);
                        startActivity(intent);
                    } else {
                        Toast.makeText(SignIn.this, "Пользователя с таким логином нет в базе данных", Toast.LENGTH_SHORT).show();
                    }




                }else {
                    Toast.makeText(SignIn.this, "Данные пользователя введены не верно", Toast.LENGTH_SHORT).show();
                // Добавляем значение переменной name в Intent
                    String name = getName(email);//удалить
                    intent.putExtra("name", name);//удалить
                    startActivity(intent);//удалить
                }
                }
        });
    }

    public String getName(String email) {
        int atIndex = email.indexOf("@");
        String name = "";

        if (atIndex != -1) {
            name = email.substring(0, atIndex);
        }
        return name;
    }

    public void registration (String email , String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    String name = getName(email);
                    Toast.makeText(SignIn.this, "Регистрация прошла успешна", Toast.LENGTH_SHORT).show();
                    try {
                        KeyPair keyPair = getKeyPair();
                        User user = new User (name, keyPair);
                        dbHelper.AddOne(user);

                    } catch (NoSuchAlgorithmException | IOException e) {
                        throw new RuntimeException(e);
                    }


                }
                else{
                    try {//удалить
                        String name = getName(email);//удалить
                        KeyPair keyPair = getKeyPair();//удалить
                        User user = new User (name, keyPair);//удалить
                        dbHelper.AddOne(user);//удалить

                    } catch (NoSuchAlgorithmException | IOException e) {//удалить
                        throw new RuntimeException(e);//удалить
                    }//удалить
                    Toast.makeText(SignIn.this, "Данный пользователь уже зарегистрирован или данные введены неверно", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private static KeyPair getKeyPair() throws NoSuchAlgorithmException
    {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");

        kpg.initialize(1024);

        return kpg.genKeyPair();
    }


}
