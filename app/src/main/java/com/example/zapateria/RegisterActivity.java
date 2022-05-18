package com.example.zapateria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtnombre;
    private  EditText txtphone;
    private  EditText txtcorreo;
    private EditText txtpass;
    private Button btnregistro;

    private String userID;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtnombre= findViewById(R.id.txtnombre);
        txtcorreo= findViewById(R.id.txtcorreo);
        txtphone= findViewById(R.id.txtphone);
        txtpass = findViewById(R.id.txtpassword);
        btnregistro= findViewById(R.id.registerbtn);

        mAuth= FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        btnregistro.setOnClickListener(view ->{
            createUser();
        });

    }

    private void createUser() {
        String name = txtnombre.getText().toString();
        String phone= txtphone.getText().toString();
        String mail = txtcorreo.getText().toString();
        String password = txtpass.getText().toString();

        if(TextUtils.isEmpty(name)){
            txtnombre.setError("ingrese su nombre");
            txtnombre.requestFocus();
        }else if(TextUtils.isEmpty(phone)){
            txtphone.setError("ingrese su numero de telefono");
            txtphone.requestFocus();
        }else if (TextUtils.isEmpty(mail)){
            txtcorreo.setError("ingrese su correo");
            txtcorreo.requestFocus();
        }else if(TextUtils.isEmpty(password)){
            txtpass.setError("ingrese su contraseña");
            txtpass.requestFocus();
        }else {
            mAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        userID = mAuth.getCurrentUser().getUid();
                        DocumentReference documentReference = db.collection("users").document(userID);

                        Map<String,Object> user=new HashMap<>();
                        user.put("Nombre", name);
                        user.put("Telefono",phone);
                        user.put("Correo", mail);
                        user.put("Contraseña", password);

                        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("TAG", "onSuccess: Datos Registrados"+ userID);
                            }
                        });
                        Toast.makeText(RegisterActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }else {
                        Toast.makeText(RegisterActivity.this, "Usuario no Registrado"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}