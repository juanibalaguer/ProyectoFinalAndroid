package com.example.terrasolecabaas.ui.productosyservicios;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class ServicioAdapter extends  RecyclerView.Adapter<ServicioAdapter.ViewHolder>{
    private Context context;
    private ArrayList<Producto_Servicio> servicios;
    private LayoutInflater inflater;
    private ProductosYServiciosViewModel productosYServiciosViewModel;
    public ServicioAdapter(Context context, ArrayList<Producto_Servicio> servicios, LayoutInflater inflater, ProductosYServiciosViewModel productosYServiciosViewModel) {
        this.context = context;
        this.servicios = servicios;
        this.inflater = inflater;
        this.productosYServiciosViewModel = productosYServiciosViewModel;
    }

    @NonNull
    @Override
    public ServicioAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_item_servicio, parent, false);
        return new ServicioAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicioAdapter.ViewHolder holder, int position) {
        holder.tvNombreProductoItem.setText(servicios.get(position).getNombre());
        Glide.with(holder.itemView)
                .asBitmap()
                .load(LoginActivity.RUTA + "Uploads/" + servicios.get(position).getFoto() + ".jpg")
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
        return servicios.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        protected ImageView ivFotoProducto;
        protected TextView tvNombreProductoItem;
        protected Button btAgregar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFotoProducto = itemView.findViewById(R.id.ivFotoProducto);
            tvNombreProductoItem = itemView.findViewById(R.id.tvNombreProductoItem);
            String rol = context.getSharedPreferences("cabaña", 0).getString("rol", "");
            if(rol.equals("inquilino")) {
                btAgregar = itemView.findViewById(R.id.btAgregar);
                btAgregar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("servicio", servicios.get(getAdapterPosition()));
                        Navigation.findNavController((Activity) context, R.id.nav_host_fragment).navigate(R.id.fragment_pedido, bundle);
                    }
                });
            } else {
                btAgregar.setVisibility(View.GONE);
            }
        }
    }
}
