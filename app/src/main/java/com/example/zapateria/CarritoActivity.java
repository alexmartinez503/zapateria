package com.example.zapateria;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CarritoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button siguiente;
    private TextView precioTotal, mensaje1;

    private double precioTotalID = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrito);
        recyclerView=(RecyclerView) findViewById(R.id.carrito_lista);
        recyclerView.setHasFixedSize(true);
        layoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        siguiente= (Button) findViewById(R.id.siguiente_paso);
        precioTotal= (TextView)findViewById(R.id.precio_total);
        mensaje1= (TextView) findViewById(R.id.mensaje_compra);


        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(CarritoActivity.this, confirmarPedidoActivity.class);
                intent.putExtra("total",String.valueOf(precioTotalID));
                startActivity(intent);
                finish();
            }
        });
    }
}