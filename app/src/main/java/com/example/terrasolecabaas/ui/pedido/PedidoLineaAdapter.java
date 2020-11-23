package com.example.terrasolecabaas.ui.pedido;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.terrasolecabaas.R;
import com.example.terrasolecabaas.modelo.PedidoLinea;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class PedidoLineaAdapter extends ArrayAdapter<PedidoLinea> {
    private ArrayList<PedidoLinea> pedidoLineas;
    private Context context;
    private LayoutInflater inflater;
    PedidoViewModel pedidoViewModel;
    public PedidoLineaAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PedidoLinea> pedidoLineas, LayoutInflater inflater, PedidoViewModel pedidoViewModel) {
        super(context, resource, pedidoLineas);
        this.context = context;
        this.pedidoLineas = pedidoLineas;
        this.inflater = inflater;
        this.pedidoViewModel = pedidoViewModel;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View viewPedidoLinea = convertView;
        if (viewPedidoLinea == null) {
            viewPedidoLinea = inflater.inflate(R.layout.frament_item_linea, parent, false);
        }
        TextView tvCantidadLinea = viewPedidoLinea.findViewById(R.id.tvCantidadLinea);
        TextView tvNombreProductoLinea = viewPedidoLinea.findViewById(R.id.tvNombreProducto);
        TextView tvPrecioLinea = viewPedidoLinea.findViewById(R.id.tvPrecioLinea);
        ImageButton btEliminarLinea = viewPedidoLinea.findViewById(R.id.btEliminarLinea);
        tvCantidadLinea.setText(pedidoLineas.get(position).getCantidad() + "");
        tvNombreProductoLinea.setText(pedidoLineas.get(position).getProducto_Servicio().getNombre());
        tvPrecioLinea.setText(pedidoLineas.get(position).getPrecioPorUnidad() * pedidoLineas.get(position).getCantidad() + "");
        btEliminarLinea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pedidoViewModel.eliminarItem(position);
            }
        });
        return viewPedidoLinea;
    }
}
