package com.example.zapateria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListatoZapatosActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
        DrawerLayout drawerLayout= findViewById(R.id.list_layout);
        androidx.appcompat.app.ActionBarDrawerToggle toggle = new androidx.appcompat.app.ActionBarDrawerToggle(
                this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView= navigationView.getHeaderView(0);
        TextView nombreHeader = (TextView) headerView.findViewById(R.id.encabezado_menu);
        CircleImageView imgHeader = (CircleImageView) headerView.findViewById(R.id.img_usuario);

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.list_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
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

    //metodo para opciones de menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_carrito){
            activityCarrito();
        }
        else if(id == R.id.menu_categoria){
            activityCategoria();
        }
        else if(id == R.id.menu_Buscar_pedido){
            activityBuscar();
        }
        else if(id == R.id.menu_usuario){
            activityUsuario();
        }
        else if(id == R.id.menu_salir){

            Intent intent = new Intent(ListatoZapatosActivity.this, HomeActivity.class);

            startActivity(intent);
        }
        DrawerLayout drawerLayout= (DrawerLayout) findViewById(R.id.list_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void activityUsuario() {
        Toast.makeText(ListatoZapatosActivity.this, "usuario", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ListatoZapatosActivity.this, usuarioActivity.class);
        startActivity(intent);
    }
    private void activityBuscar() {
        Toast.makeText(ListatoZapatosActivity.this, "pedido", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(ListatoZapatosActivity.this, buscar_Activity.class);
        startActivity(intent);
    }
    private void activityCategoria() {
        Toast.makeText(ListatoZapatosActivity.this, "categoria", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ListatoZapatosActivity.this, categoriasActivity.class);
        startActivity(intent);
    }
    private void activityCarrito() {
        Toast.makeText(ListatoZapatosActivity.this, "carrito", Toast.LENGTH_SHORT).show();
        Intent intent= new Intent(ListatoZapatosActivity.this, CarritoActivity.class);
        startActivity(intent);
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