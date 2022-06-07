package com.example.zapateria;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class FragmentUno extends Fragment {
    private  View fragmento;

    private ImageView formales, deportivos, casuales, trabajos;

    public FragmentUno() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      fragmento=inflater.inflate(R.layout.fragment_uno, container, false);

      formales = (ImageView) fragmento.findViewById(R.id.formale);
      deportivos = (ImageView) fragmento.findViewById(R.id.deportivo);
      casuales = (ImageView) fragmento.findViewById(R.id.casuale);
      trabajos = (ImageView) fragmento.findViewById(R.id.trabajo);

      formales.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              Intent intent = new Intent(getContext(),AgregarproductoActivity.class);
              intent.putExtra("Categoria", "Zapatos formales");
              startActivity(intent);
          }
      });

        deportivos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AgregarproductoActivity.class);
                intent.putExtra("Categoria", "Zapatos deportivos");
                startActivity(intent);
            }
        });

        casuales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AgregarproductoActivity.class);
                intent.putExtra("Categoria", "Zapatos casuales");
                startActivity(intent);
            }
        });

        trabajos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),AgregarproductoActivity.class);
                intent.putExtra("Categoria", "Zapatos trabajo");
                startActivity(intent);
            }
        });

      return  fragmento;
    }
}