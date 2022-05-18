package com.example.zapateria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {
    private EditText txtcorreo;
    private EditText txtpassword;
    private Button loginbtn;
    private Button registrobtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        txtcorreo=findViewById(R.id.txtcorreo);
        txtpassword=findViewById(R.id.txtpassword);
        loginbtn=findViewById(R.id.loginbtn);
        registrobtn=findViewById(R.id.registrobtn);

        mAuth= FirebaseAuth.getInstance();


        //llammos las clases para los diferentes botones

        //clase de boton de login
        loginbtn.setOnClickListener(view ->{
            userLogin();
        });

        //clase de boton de registro
        registrobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    //metodo de botn de login
    private void userLogin() {
        String mail = txtcorreo.getText().toString();
        String password = txtpassword.getText().toString();

        //validamos que los campos del login no esten vacios
        if(TextUtils.isEmpty(mail)){
            txtcorreo.setError("ingrese su correo");
            txtcorreo.requestFocus();
        }else if (TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this, "ingrese su contraseña", Toast.LENGTH_SHORT).show();
            txtpassword.requestFocus();
        }else{
            mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(LoginActivity.this, "Bienvenid@", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, categoriasActivity.class));
                    }else{
                        Log.w("TAG","Error", task.getException());
                    }

                }
            });
        }

    }
}