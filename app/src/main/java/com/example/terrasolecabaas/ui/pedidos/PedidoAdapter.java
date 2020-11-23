package com.example.terrasolecabaas.ui.pedidos;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.terrasolecabaas.R;
import com.example.terrasolecabaas.modelo.Pedido;
import com.example.terrasolecabaas.modelo.PedidoLinea;
import com.example.terrasolecabaas.modelo.Producto_Servicio;
import com.example.terrasolecabaas.ui.login.LoginActivity;
import com.example.terrasolecabaas.ui.pedido.PedidoViewModel;
import com.example.terrasolecabaas.ui.productosyservicios.ProductosYServiciosViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class PedidoAdapter extends ArrayAdapter<Pedido> {
    private ArrayList<Pedido> pedidos;
    private Context context;
    private LayoutInflater inflater;
    private PedidosViewModel pedidosViewModel;
    public PedidoAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Pedido> pedidos, LayoutInflater inflater, PedidosViewModel pedidosViewModel) {
        super(context, resource, pedidos);
        this.context = context;
        this.pedidos = pedidos;
        this.inflater = inflater;
        this.pedidosViewModel = pedidosViewModel;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View viewPedido = convertView;
        if (viewPedido == null) {
            viewPedido = inflater.inflate(R.layout.fragment_item_pedido, parent, false);
        }
        TextView tvTitulo = viewPedido.findViewById(R.id.tvTitulo);
        TextView tvMontoPedido = viewPedido.findViewById(R.id.tvMontoPedido);
        TextView tvFechaPedido = viewPedido.findViewById(R.id.tvFechaPedido);
        TextView tvEstado = viewPedido.findViewById(R.id.tvEstado);
        ImageButton btCancelarPedido = viewPedido.findViewById(R.id.btCancelarPedido);
        ImageButton btEdit = viewPedido.findViewById(R.id.btEdit);
        tvTitulo.setText(pedidos.get(position).getTitulo());
        tvMontoPedido.setText("$" + String.format("%.2f", pedidos.get(position).getMontoPedido()));
        switch (pedidos.get(position).getEstado()) {
            case 0:
                tvEstado.setText("Borrador");
                break;
            case 1:
                tvEstado.setText("Confirmado");
                break;
            case 2:
                tvEstado.setText("En preparación");
                break;
            default :
                tvEstado.setText("Entregado");
        }
        SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
        String fechaParseada = formato.format(pedidos.get(position).getFechaPedido());
        tvFechaPedido.setText(fechaParseada);
        btCancelarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pedidosViewModel.cancelarPedido(pedidos.get(position).getId());
            }
        });
        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                Pedido pedido = pedidos.get(position);
                bundle.putSerializable("pedido", pedido);
                Navigation.findNavController( (Activity) context , R.id.nav_host_fragment).navigate(R.id.fragment_pedido, bundle);
            }
        });
        return viewPedido;
    }

}

