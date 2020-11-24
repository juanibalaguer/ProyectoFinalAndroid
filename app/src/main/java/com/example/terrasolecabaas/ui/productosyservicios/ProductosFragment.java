package com.example.terrasolecabaas.ui.productosyservicios;

import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.terrasolecabaas.ui.pedido.PedidoViewModel;
import com.example.terrasolecabaas.R;
import com.example.terrasolecabaas.modelo.Producto_Servicio;

import java.util.ArrayList;

public class ProductosFragment extends Fragment {
    private RecyclerView rvProductos;
    private ProductosYServiciosViewModel productosYServiciosViewModel;
    private ProductoAdapter productoAdapter;
    private Button btComenzarPedido;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_productos, container, false);
        inicializar(root);
        return root;
    }
    private void inicializar(View view) {
        rvProductos = view.findViewById(R.id.rvProductos);
        btComenzarPedido = view.findViewById(R.id.btComenzarPedido);
        if(getContext().getSharedPreferences("cabaña", 0).getString("rol", "") == "inquilino") {
            btComenzarPedido.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.fragment_pedido);
                }
            });
        } else {
            btComenzarPedido.setVisibility(View.GONE);
        }
        productosYServiciosViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(ProductosYServiciosViewModel.class);
        productosYServiciosViewModel.getProductos().observe(getViewLifecycleOwner(), new Observer<ArrayList<Producto_Servicio>>() {
            @Override
            public void onChanged(ArrayList<Producto_Servicio> productos) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2, RecyclerView.VERTICAL, false);
                rvProductos.setLayoutManager(gridLayoutManager);
                productoAdapter = new ProductoAdapter(view.getContext(), productos, getLayoutInflater(), productosYServiciosViewModel);
                rvProductos.setAdapter(productoAdapter);
            }
        });
        productosYServiciosViewModel.cargarProductos();
    }
}