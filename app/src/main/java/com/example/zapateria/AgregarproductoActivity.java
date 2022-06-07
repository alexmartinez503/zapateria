package com.example.zapateria;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AgregarproductoActivity extends AppCompatActivity {


    private EditText nombrepro, descripcionpro, preciocomprapro, precioventapro, cantidadpro;
    private TextView textox;
    private Button agregarpro ;
    private Uri imagenUrl;
    private  String productoRandomkey, downloadUri;
    private StorageReference ProductoImagenRef;
    private DatabaseReference ProductoRef;
    private ProgressDialog dialog;
    private  String Categoria, Nom, Desc,PrecioCom, PrecioVen,Cant,CurrentDate, CurrentTime;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregarproducto);

        Categoria = getIntent().getExtras().get("Categoria").toString();
        ProductoRef = FirebaseDatabase.getInstance().getReference().child("Productos");

        Toast.makeText(this, Categoria, Toast.LENGTH_SHORT).show();
        textox = (TextView) findViewById(R.id.textox);
        nombrepro = (EditText) findViewById(R.id.nombrepro);
        descripcionpro = (EditText) findViewById(R.id.descripcionpro);
        preciocomprapro = (EditText) findViewById(R.id.preciocomprapro);
        precioventapro = (EditText) findViewById(R.id.precioventapro);
        cantidadpro = (EditText) findViewById(R.id.cantidadpro);
        agregarpro = (Button) findViewById(R.id.agregarpro);

        dialog = new ProgressDialog(this);
        
        
        agregarpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarProducto();
            }
        });
        textox.setText(Categoria+"\nAgregar productos");
    }

    private void ValidarProducto() {
        Nom = nombrepro.getText().toString();
        Desc = descripcionpro.getText().toString();
        PrecioCom = preciocomprapro.getText().toString();
        PrecioVen = precioventapro.getText().toString();
        Cant = cantidadpro.getText().toString();
        if (TextUtils.isEmpty(Nom)){
            Toast.makeText(this, "Tienes que ingresar el nombre del producto", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Desc)){
            Toast.makeText(this, "Tienes que ingresar la descripcion del producto", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(PrecioCom)){
            Toast.makeText(this, "Tienes que ingresar el precio de compra del producto", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(PrecioVen)){
            Toast.makeText(this, "Tienes que ingresar el precio de venta del producto", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Cant)){
            Toast.makeText(this, "Tienes que ingresar la cantidad del producto", Toast.LENGTH_SHORT).show();
        }else{
            GuardarInformacionProduto();
        }


    }

    private void GuardarInformacionProduto() {

        dialog.setTitle("Guardando Producto");
        dialog.setMessage("Por favor espere mientras guardamos el producto");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat  curreDateFormat = new SimpleDateFormat("MM-dd-yyyy");
        CurrentDate = curreDateFormat.format(calendar.getTime());

        SimpleDateFormat CurrenTimeFormat = new SimpleDateFormat("HH:mm:ss");
        CurrentTime = CurrenTimeFormat.format(calendar.getTime());

        productoRandomkey = CurrentDate + CurrentTime;

        GuardarEnFireBase();

    }

    private void GuardarEnFireBase() {
        HashMap<String,Object> map = new HashMap<>();
        map.put("pid",productoRandomkey);
        map.put("fecha",CurrentDate);
        map.put("hora",CurrentTime);
        map.put("descripcion",Desc);
        map.put("nombre",Nom);
        map.put("preciocom",PrecioCom);
        map.put("preciovent",PrecioVen);
        map.put("cantidad",Cant);
        map.put("categoria",Categoria);

        ProductoRef.child(productoRandomkey).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent = new Intent(AgregarproductoActivity.this,FragmentUno.class);
                    startActivity(intent);
                    dialog.dismiss();
                    Toast.makeText(AgregarproductoActivity.this, "Solicitud Exitosa!", Toast.LENGTH_SHORT).show();

                }else {
                    dialog.dismiss();
                    String mensaje = task.getException().toString();
                    Toast.makeText(AgregarproductoActivity.this, "Error"+mensaje, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
