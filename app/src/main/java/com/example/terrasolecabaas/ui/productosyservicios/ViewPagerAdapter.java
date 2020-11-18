package com.example.terrasolecabaas.ui.productosyservicios;

import com.example.terrasolecabaas.ui.productosyservicios.ProductosFragment;
import com.example.terrasolecabaas.ui.productosyservicios.ServiciosFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ViewPagerAdapter extends FragmentStateAdapter {


    public ViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ProductosFragment();
            default :
                return new ServiciosFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
