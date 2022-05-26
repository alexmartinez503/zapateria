package com.example.zapateria;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class categoriasActivity extends AppCompatActivity {

    private Button btndeportivo;
    private Button btncasual;
    private Button btnformal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        btndeportivo=findViewById(R.id.btndeportivo);
        btncasual=findViewById(R.id.btncasual);
        btnformal=findViewById(R.id.btnformal);


        btndeportivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(categoriasActivity.this, deporteActivity2.class);
                startActivity(intent);
            }
        });
        btncasual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(categoriasActivity.this, casualesActivity.class);
                startActivity(intent);
            }
        });

        btnformal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(categoriasActivity.this, formalesActivity2.class);
                startActivity(intent);
            }
        });

    }
}