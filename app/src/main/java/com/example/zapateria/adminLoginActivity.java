package com.example.zapateria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class adminLoginActivity extends AppCompatActivity {
    private EditText numeroadmin, codigoadmin;
    private Button enviarnumeroadmin, enviarcodigoadmin;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String VerificacionID;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private FirebaseAuth auth;
    private ProgressDialog dialog;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        numeroadmin=(EditText)findViewById(R.id.numeroadmin);
        codigoadmin=(EditText)findViewById(R.id.codigoadmin);
        enviarnumeroadmin=(Button) findViewById(R.id.enviarnumeroadmin);
        enviarcodigoadmin=(Button)findViewById(R.id.enviarcodigoadmin);

        auth=FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);
        enviarnumeroadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = numeroadmin.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(adminLoginActivity.this, "Ingresa tu numero primero", Toast.LENGTH_SHORT).show();
                }else{
                    dialog.setTitle("Validando numero");
                    dialog.setMessage("Por favor espere mientras validamos su numero");
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(true);

                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(adminLoginActivity.this)
                            .setCallbacks(callbacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);//Envia el numero...
                }

            }
        });
        enviarcodigoadmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                numeroadmin.setVisibility(View.GONE);
                enviarnumeroadmin.setVisibility(View.GONE);
                String verificacionCode = codigoadmin.getText().toString();
                if (TextUtils.isEmpty(verificacionCode)){
                    Toast.makeText(adminLoginActivity.this, "Ingresa el codigoc recibido", Toast.LENGTH_SHORT).show();

                }else{
                    dialog.setTitle("Verificando");
                    dialog.setMessage("Espere por favor...");
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(true);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificacionID, verificacionCode);
                    IngresadoConExito(credential);
                }
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                IngresadoConExito(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                dialog.dismiss();
                Toast.makeText(adminLoginActivity.this, "Fallo el inicio Causas: \n1. Numero Invalido\n2.Sin conexion a internet\n3.Sin codigo de region", Toast.LENGTH_SHORT).show();
                numeroadmin.setVisibility(View.VISIBLE);
                enviarnumeroadmin.setVisibility(View.VISIBLE);
                codigoadmin.setVisibility(View.GONE);
                enviarcodigoadmin.setVisibility(View.GONE);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                VerificacionID = s;
                resendingToken =token;
                dialog.dismiss();
                Toast.makeText(adminLoginActivity.this, "Codigo enviado satisfactoriamente, Revisa tu bandeja de entrada", Toast.LENGTH_SHORT).show();
                numeroadmin.setVisibility(View.GONE);
                enviarnumeroadmin.setVisibility(View.GONE);
                codigoadmin.setVisibility(View.VISIBLE);
                enviarcodigoadmin.setVisibility(View.VISIBLE);
            }
        };
    }

    private void IngresadoConExito(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                    Toast.makeText(adminLoginActivity.this, "Ingresado con exito", Toast.LENGTH_SHORT).show();
                    EnviaralaPrincipal();
                }else{
                    String err = task.getException().toString();
                    Toast.makeText(adminLoginActivity.this, "Error :"+err, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null){
           EnviaralaPrincipal();
        }
    }*/

    private void EnviaralaPrincipal() {
        Intent intent = new Intent(adminLoginActivity.this,AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("phone",phoneNumber);
        startActivity(intent);
        finish();
    }



}