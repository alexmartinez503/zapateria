package com.example.zapateria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //opciones de menu .....


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_app,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.menu_carrito){
            Toast.makeText(ListatoZapatosActivity.this, "carrito", Toast.LENGTH_SHORT).show();
            activityCarrito();
        }
        if(item.getItemId() == R.id.menu_categoria){
            Toast.makeText(ListatoZapatosActivity.this, "categoria", Toast.LENGTH_SHORT).show();
            activityCategoria();
        }
        if(item.getItemId() == R.id.menu_Buscar_pedido){
            Toast.makeText(ListatoZapatosActivity.this, "pedido", Toast.LENGTH_SHORT).show();
            activityBuscar();
        }
        if(item.getItemId() == R.id.menu_usuario){
            Toast.makeText(ListatoZapatosActivity.this, "usuario", Toast.LENGTH_SHORT).show();
            activityUsuario();
        }
        if(item.getItemId() == R.id.menu_salir){
            Toast.makeText(ListatoZapatosActivity.this, "salir", Toast.LENGTH_SHORT).show();
            auth.signOut();
        }


        return true;
    }


    private void activityUsuario() {


        Intent intent = new Intent(ListatoZapatosActivity.this, usuarioActivity.class);
        startActivity(intent);
    }

    private void activityBuscar() {
    }

    private void activityCategoria() {
    }

    private void activityCarrito() {

    }

    //fin de las opciones de menu
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