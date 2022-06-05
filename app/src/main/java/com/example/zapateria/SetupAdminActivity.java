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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupAdminActivity extends AppCompatActivity {

    private EditText nombre, ciudad, direccion, telefono;
    private Button guardar;
    private String phone = "";
    private CircleImageView imagen;
    private FirebaseAuth auth;
    private DatabaseReference UserRef;
    private ProgressDialog dialog;
    private String CurrentUserId;
    private static int Galery_Pick = 1;
    private StorageReference UserImagenPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_admin);

        auth = FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();
        UserRef = FirebaseDatabase.getInstance().getReference().child("Admin");
        dialog = new ProgressDialog(this);
        UserImagenPerfil = FirebaseStorage.getInstance().getReference().child("Perfil");
        nombre = (EditText) findViewById(R.id.asetup_nombre);
        ciudad = (EditText) findViewById(R.id.asetup_ciudad);
        direccion = (EditText) findViewById(R.id.asetup_direccion);
        telefono = (EditText) findViewById(R.id.asetup_telefono);
        guardar = (Button) findViewById(R.id.asetup_boton);
        imagen = (CircleImageView) findViewById(R.id.asetup_imagen);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phone = bundle.getString("phone");
        }

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuardarInformacion();
            }
        });



    }//ONCREATE****************************************

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==Galery_Pick && resultCode==RESULT_OK && data !=null){
            Uri imageUri = data.getData();
            Crop
        }
    }*/

    private void GuardarInformacion() {
        String nombres = nombre.getText().toString().toUpperCase();
        String direcciones = direccion.getText().toString();
        String ciudades = ciudad.getText().toString();
        String phones = telefono.getText().toString();

        if (TextUtils.isEmpty(nombres)) {
            Toast.makeText(this, "Ingrese el nombre", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(direcciones)) {
            Toast.makeText(this, "Ingrese su direccion", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ciudades)) {
            Toast.makeText(this, "Ingrese  la ciudad", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(phones)) {
            Toast.makeText(this, "Ingrese su numero", Toast.LENGTH_SHORT).show();
        }else{
            dialog.setTitle("Guardando");
            dialog.setMessage("Por favor espere");
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);

            HashMap map = new HashMap();
            map.put("nombre", nombres);
            map.put("direccion", direcciones);
            map.put("ciudad", ciudades);
            map.put("telefono", phones);
            map.put("uid", CurrentUserId);

            UserRef.child(CurrentUserId).updateChildren(map).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        EnviarAlInicio();
                        dialog.dismiss();
                    }else {
                        String mensaje = task.getException().toString();
                        Toast.makeText(SetupAdminActivity.this, "Error: "+mensaje, Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
        }
    }

    private void EnviarAlInicio() {
        Intent intent = new Intent(SetupAdminActivity.this, AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}