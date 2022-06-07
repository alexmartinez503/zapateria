package com.example.zapateria;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView productoNom, productoDescrip, productoPrecio, productoCantidad;
    public ImageView productImagn;
    public ItemClickListener listener;
    public ProductoViewHolder(@NonNull View itemView) {
        super(itemView);
        productoNom = (TextView) itemView.findViewById(R.id.producto1_name);
        productoDescrip = (TextView) itemView.findViewById(R.id.description_product1);
        productoPrecio = (TextView) itemView.findViewById(R.id.precio_product1);
        productoCantidad= (TextView) itemView.findViewById(R.id.cantidad_product1);
        productImagn = (ImageView) itemView.findViewById(R.id.img_product1);

    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener=listener;
    }

    @Override
    public void onClick(View v) {
        listener.onClick(v, getAdapterPosition(), false);

    }
}
