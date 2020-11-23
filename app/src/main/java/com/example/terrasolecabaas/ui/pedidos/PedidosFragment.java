package com.example.terrasolecabaas.ui.pedidos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.terrasolecabaas.R;
import com.example.terrasolecabaas.modelo.Pedido;

import java.util.ArrayList;

public class PedidosFragment extends Fragment {

    private PedidosViewModel pedidosViewModel;
    private TextView tvNoHayPedidos;
    private ListView lvPedidos;
    private PedidoAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_pedidos, container, false);
        inicializar(root);
        return root;
    }
    private void inicializar(View view) {
        lvPedidos = view.findViewById(R.id.lvPedidos);
        tvNoHayPedidos = view.findViewById(R.id.tvNoHayPedidos);
        pedidosViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PedidosViewModel.class);
        pedidosViewModel.getPedidos().observe(getViewLifecycleOwner(), new Observer<ArrayList<Pedido>>() {
            @Override
            public void onChanged(ArrayList<Pedido> pedidos) {
                if(pedidos == null || pedidos.size() == 0) {
                    tvNoHayPedidos.setVisibility(View.VISIBLE);
                    lvPedidos.setVisibility(View.GONE);
                } else {
                    lvPedidos.setVisibility(View.VISIBLE);
                    tvNoHayPedidos.setVisibility(View.GONE);
                    adapter = new PedidoAdapter(getContext(), R.layout.fragment_item_pedido, pedidos, getLayoutInflater(), pedidosViewModel);
                    lvPedidos.setAdapter(adapter);
                }
            }
        });
        pedidosViewModel.cargarPedidos();
    }
}