package com.example.zapateria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zapateria.Modal.Productos;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductoDetallesActivity extends AppCompatActivity {
    private Button agregarCarrito;
    TextView productoPrecio, productoDescripcion, productoNombre;
    private  String productoID = "", estado= "Normal", CurrentUserId;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_producto_detalles);

        productoID = getIntent().getStringExtra("pid");

        agregarCarrito=(Button) findViewById(R.id.boton_siguiente_detalles);
        productoPrecio=(TextView) findViewById(R.id.producto_precio_detalles);
        productoDescripcion=(TextView) findViewById(R.id.producto_descripcion_detalles);
        productoNombre=(TextView) findViewById(R.id.producto_nombre_detalles);
        ObtenerDatosProducto(productoID);
        auth=FirebaseAuth.getInstance();
        CurrentUserId = auth.getCurrentUser().getUid();
        agregarCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (estado.equals("Pedido") || estado.equals("Enviado")){
                    Toast.makeText(ProductoDetallesActivity.this, "Esperando a que el primer pedido finalice.....", Toast.LENGTH_SHORT).show();
                }else {
                    agregarAlaLista();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        VerificarestadoOrden();
    }

    private void agregarAlaLista() {

        String CurrentTime, CurrentDate;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat data = new SimpleDateFormat("MM-dd-yyyy");
        CurrentDate =data.format(calendar.getTime());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm:ss");
        CurrentTime=time.format(calendar.getTime());

        final DatabaseReference CartListRef = FirebaseDatabase.getInstance().getReference().child("Carrito");

        final HashMap<String, Object>map = new HashMap<>();
        map.put("pid",productoID);
        map.put("nombre",productoNombre.getText().toString());
        map.put("precio",productoPrecio.getText().toString());
        map.put("fecha",CurrentDate);
        map.put("hora",CurrentTime);
        map.put("descuento","");

        CartListRef.child("Usuario Compra").child(CurrentUserId).child("Productos").child(productoID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    CartListRef.child("Administracion").child(CurrentUserId).child("Productos").child(productoID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(ProductoDetallesActivity.this, "Agregado....", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ProductoDetallesActivity.this,CarritoActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }

    private void ObtenerDatosProducto(String productoID) {
        DatabaseReference ProductoRef = FirebaseDatabase.getInstance().getReference().child("Productos");
        ProductoRef.child(productoID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Productos productos = snapshot.getValue(Productos.class);
                    productoNombre.setText(productos.getNombre());
                    productoDescripcion.setText(productos.getDescripcion());
                    productoPrecio.setText(productos.getPrecioven());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void VerificarestadoOrden() {
        DatabaseReference OrdenRef;

        OrdenRef = FirebaseDatabase.getInstance().getReference().child("Ordenes").child(CurrentUserId);
        OrdenRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String envioStado = snapshot.child("estado").getValue().toString();
                    if (envioStado.equals("Enviado")){
                        estado="Enviado";
                    }else if (envioStado.equals("No enviado")){
                        estado="Pedido";
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}



