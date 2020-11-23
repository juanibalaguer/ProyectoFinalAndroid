package com.example.terrasolecabaas.ui.productosyservicios;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.terrasolecabaas.R;
import com.example.terrasolecabaas.modelo.Producto_Servicio;

import java.util.ArrayList;

public class ServiciosFragment extends Fragment {
    RecyclerView rvServicios;
    private ProductosYServiciosViewModel productosYServiciosViewModel;
    private ServicioAdapter servicioAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_servicios, container, false);
        inicializar(root);
        return root;
    }

    private void inicializar(View view) {
        rvServicios = view.findViewById(R.id.rvServicios);
        productosYServiciosViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(ProductosYServiciosViewModel.class);
        productosYServiciosViewModel.getServicios().observe(getViewLifecycleOwner(), new Observer<ArrayList<Producto_Servicio>>() {
            @Override
            public void onChanged(ArrayList<Producto_Servicio> servicios) {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2, RecyclerView.VERTICAL, false);
                rvServicios.setLayoutManager(gridLayoutManager);
                servicioAdapter = new ServicioAdapter(view.getContext(), servicios, getLayoutInflater(), productosYServiciosViewModel);
                rvServicios.setAdapter(servicioAdapter);
            }
        });
        productosYServiciosViewModel.cargarServicios();
    }
}