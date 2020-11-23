package com.example.terrasolecabaas.ui.logout;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.terrasolecabaas.R;

public class LogoutFragment extends Fragment {

    public static LogoutFragment newInstance() {
        return new LogoutFragment();
    }
    private LogoutViewModel logoutViewModel;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.fragment_logout, container, false);
        cerrarSesión();
        return root;
    }

    public void cerrarSesión() {
        logoutViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(LogoutViewModel.class);
        new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme)
                .setTitle("Cierre de sesión")
                .setMessage("Está seguro de que desea cerrar la sesión?")
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        logoutViewModel.cerrarSesion();
                        System.exit(0);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.fragment_perfil);
                    }
                }).show();
    }

}