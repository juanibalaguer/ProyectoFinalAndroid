package com.example.terrasolecabaas.ui.productosyservicios;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.terrasolecabaas.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class ProductosYServiciosFragment extends Fragment {

    ViewPager2 viewPager2;
    TabLayout tabLayout;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_productosyservicios, container, false);
        inicializar(root);
        return root;
    }

    private void inicializar(View view) {
        viewPager2 = view.findViewById(R.id.vpProductosYservicios);
        viewPager2.setAdapter(new ViewPagerAdapter(this));
        tabLayout = view.findViewById(R.id.tlProductosYServicios);
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(
                tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText("Productos");
                        break;
                    default :
                        tab.setText("Servicios");
                        break;

                }
            }
        }
        );
        tabLayoutMediator.attach();
    }
}