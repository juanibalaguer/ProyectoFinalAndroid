package com.example.terrasolecabaas.ui.pedido;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.terrasolecabaas.R;
import com.example.terrasolecabaas.modelo.Pedido;

public class DetallePedido extends Fragment {

    TextView tvTitulo, tvMonto, tvFecha, tvEstado;
    ListView lvItems;
    DetallePedidoViewModel detallePedidoViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detalle_pedido, container, false);
        inicializar(root);
        return root;
    }

    private void inicializar(View view) {
        tvTitulo = view.findViewById(R.id.tvTituloDetalle);
        tvMonto =  view.findViewById(R.id.tvMontoTotalDetalle);
        tvFecha =  view.findViewById(R.id.tvFechaEntregaDetalle);
        tvEstado =  view.findViewById(R.id.tvEstadoDetalle);
        lvItems =  view.findViewById(R.id.lvItemsPedidoDetalle);
        detallePedidoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(DetallePedidoViewModel.class);
        detallePedidoViewModel.getPedido().observe(getViewLifecycleOwner(), new Observer<Pedido>() {
            @Override
            public void onChanged(Pedido pedido) {
                PedidoLineaNoEditableAdapter adapter = new PedidoLineaNoEditableAdapter(getContext(), R.layout.frament_item_linea, pedido.getPedidoLineas(), getLayoutInflater());
                lvItems.setAdapter(adapter);
                tvMonto.setText(pedido.getMontoPedido() + "");
                tvTitulo.setText(pedido.getTitulo());
                tvEstado.setText(pedido.getEstado() == 2 ? "Confirmado" : "En preparación");

            }
        });
        detallePedidoViewModel.cargarPedido(getArguments());
    }


}