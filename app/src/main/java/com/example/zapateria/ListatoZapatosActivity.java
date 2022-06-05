package com.example.zapateria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class ListatoZapatosActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private String userID;
    private DatabaseReference userRef;
    private String correo= "", password1="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listato_zapatos);

        auth= FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users1");

        Bundle bundle = getIntent().getExtras();
        if (bundle !=null){
            correo = bundle.getString("correo");
            password1 = bundle.getString("password1");
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser== null){
            enviarLogin();
        }else{
            VerificarUsuario();
        }
    }


    private void VerificarUsuario() {
        final String userID = auth.getCurrentUser().getUid();
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChild(userID)){
                    enviarRegistro();
                }else {
                    enviarLogin();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void enviarRegistro() {
        Intent intent = new Intent(ListatoZapatosActivity.this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("correo", correo);
        intent.putExtra("password1", password1);
        startActivity(intent);
        finish();
    }
    private void enviarLogin() {
        Intent intent = new Intent(ListatoZapatosActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}