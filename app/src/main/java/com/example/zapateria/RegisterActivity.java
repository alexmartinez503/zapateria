package com.example.zapateria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private  String correo= "", password1="";
    private ProgressDialog dialog;

    private String userID;
    private FirebaseAuth mAuth;
   // private FirebaseFirestore db;
    private DatabaseReference userRef;


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
        //db=FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        userRef = FirebaseDatabase.getInstance().getReference().child("users1");
        dialog= new ProgressDialog(this);

        btnregistro.setOnClickListener(view ->{
            createUser();
        });

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            correo = bundle.getString("correo");
            password1= bundle.getString("password1");
        }

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
            dialog.setTitle("Guardando");
            dialog.setMessage("Por favor espere..");
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);

            HashMap map= new HashMap();
            map.put("nombre", name);
            map.put("telefono", phone);
            map.put("correo",mail);
            map.put("contraseña", password);
            map.put("id",userID);

            userRef.child(userID).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Intent intent= new Intent(RegisterActivity.this, ListatoZapatosActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("correo", mail);
                        intent.putExtra("password1", password);
                        startActivity(intent);
                        finish();
                        dialog.dismiss();
                    }else {
                        String mensaje = task.getException().toString();
                        Toast.makeText(RegisterActivity.this, "error"+mensaje, Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

}