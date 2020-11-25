package com.example.terrasolecabaas.ui.productosyservicios;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.terrasolecabaas.R;
import com.example.terrasolecabaas.modelo.Producto_Servicio;
import com.example.terrasolecabaas.ui.login.LoginActivity;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.recyclerview.widget.RecyclerView;

public class ProductoAdapter extends RecyclerView.Adapter<ProductoAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Producto_Servicio> productos;
    private LayoutInflater inflater;
    private ProductosYServiciosViewModel productosYServiciosViewModel;

    public ProductoAdapter(Context context, ArrayList<Producto_Servicio> productos, LayoutInflater inflater, ProductosYServiciosViewModel productosYServiciosViewModel) {
        this.context = context;
        this.productos = productos;
        this.inflater = inflater;
        this.productosYServiciosViewModel = productosYServiciosViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_item_producto, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNombreProductoItem.setText(productos.get(position).getNombre());
        holder.tvPrecio.setText("$" + String.format("%.2f", productos.get(position).getPrecio()));
        holder.tvCantidad.setText(0 + "");

        Glide.with(holder.itemView)
                .asBitmap()
                .load(LoginActivity.RUTA + "Uploads/" + productos.get(position).getFoto() + ".jpg")
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap foto, @Nullable Transition<? super Bitmap> transition) {
                        Resources res = context.getResources();
                        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(res, foto);
                        dr.setCornerRadius(100.0f);
                        holder.ivFotoProducto.setMaxHeight(100);
                        holder.ivFotoProducto.setImageDrawable(dr);
                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        holder.ivFotoProducto.setImageDrawable(placeholder);
                    }
                });

    }

    @Override
    public int getItemCount() {
        return productos.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView ivFotoProducto;
        protected TextView tvNombreProductoItem, tvPrecio;
        protected TextView tvCantidad;
        protected ImageButton btSumar, btRestar;
        protected Button btAgregar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFotoProducto = itemView.findViewById(R.id.ivFotoProducto);
            tvNombreProductoItem = itemView.findViewById(R.id.tvNombreProductoItem);
            tvPrecio = itemView.findViewById(R.id.tvPrecio);
            tvCantidad = itemView.findViewById(R.id.etDescripcion);
            btSumar = itemView.findViewById(R.id.btSumar);
            btRestar = itemView.findViewById(R.id.btRestar);
            btAgregar = itemView.findViewById(R.id.btAgregar);
            String rol = context.getSharedPreferences("cabaña", 0).getString("rol", "");
            if(rol.equals("inquilino")) {
                btRestar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int cantidad = Integer.valueOf(tvCantidad.getText().toString());
                        if (cantidad > 0) {
                            cantidad --;
                        }
                        tvCantidad.setText(cantidad + "");
                    }
                });
                btSumar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int cantidad = Integer.valueOf(tvCantidad.getText().toString());
                        cantidad ++;
                        tvCantidad.setText(cantidad + "");
                    }
                });
                btAgregar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        productosYServiciosViewModel.agregarProducto(productos.get(getAdapterPosition()),
                                Integer.valueOf(tvCantidad.getText().toString()));
                    }
                });
            } else {
                btAgregar.setVisibility(View.GONE);
                btSumar.setVisibility(View.GONE);
                btRestar.setVisibility(View.GONE);
            }

        }
    }
}
