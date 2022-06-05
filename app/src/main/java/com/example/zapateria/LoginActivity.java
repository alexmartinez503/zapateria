package com.example.zapateria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class LoginActivity extends AppCompatActivity {
    private EditText txtcorreo;
    private EditText txtpassword;
    private Button loginbtn;
    private Button registrobtn;
    private String correo="", password1="";
    private DatabaseReference userRef;
    private String userID;

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

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            correo = bundle.getString("correo");
            password1= bundle.getString("password1");
        }
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
            //validamos si los datos existen dentro de la base de datos y sino no nos deja iniciar sesion
            userRef.child("users1").child(userID).child("id").setValue(mail,password);
            userRef.child("users1").child(userID).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()) {
                        Intent intent= new Intent(LoginActivity.this, ListatoZapatosActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                }
            });
        }

    }
}