package com.example.terrasolecabaas.ui.perfil;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.terrasolecabaas.R;
import com.example.terrasolecabaas.modelo.Inquilino;
import com.example.terrasolecabaas.modelo.Usuario;

public class PerfilFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private PerfilViewModel perfilViewModel;
    private EditText etNombre, etApellido, etEmail, etTelefono, etDni;
    private TextView tvId, tvRol;
    private Button btEditar, btGuardar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);
        sharedPreferences = getContext().getSharedPreferences("cabaña", Context.MODE_PRIVATE);
        inicializar(root);
        return root;
    }

    private void inicializar(View view) {
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etEmail = view.findViewById(R.id.etEmail);
        etTelefono = view.findViewById(R.id.etTelefono);
        etDni = view.findViewById(R.id.etDni);
        btEditar = view.findViewById(R.id.btEditar);
        btGuardar = view.findViewById(R.id.btGuardar);
        tvId = view.findViewById(R.id.tvId);
        tvRol = view.findViewById(R.id.tvRol);
        perfilViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PerfilViewModel.class);
        String rol = sharedPreferences.getString("rol", "");
        if (rol.equals("inquilino")) {
            perfilViewModel.getInquilino().observe(getViewLifecycleOwner(), new Observer<Inquilino>() {
                @Override
                public void onChanged(Inquilino inquilino) {
                    tvId.setText(inquilino.getId() + "");
                    etNombre.setText(inquilino.getNombre());
                    etApellido.setText(inquilino.getApellido());
                    etEmail.setText(inquilino.getEmail());
                    etTelefono.setText(inquilino.getTelefono());
                    etDni.setText(inquilino.getDni());
                    tvRol.setText(tvRol.getText().toString() + sharedPreferences.getString("rol", "-"));
                }
            });
        } else {
            perfilViewModel.getUsuario().observe(getViewLifecycleOwner(), new Observer<Usuario>() {
                @Override
                public void onChanged(Usuario usuario) {
                    tvId.setText(usuario.getId() + "");
                    etNombre.setText(usuario.getNombre());
                    etApellido.setText(usuario.getApellido());
                    etEmail.setText(usuario.getEmail());
                    etTelefono.setText(usuario.getTelefono());
                    etDni.setText(usuario.getDni());
                    tvRol.setText(tvRol.getText().toString() + sharedPreferences.getString("rol", "-"));
                }
            });
        }
        perfilViewModel.cargarUsuario(getActivity().getIntent().getBooleanExtra("login", false));
        getActivity().getIntent().removeExtra("login");
        btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNombre.setEnabled(true);
                etApellido.setEnabled(true);
                etTelefono.setEnabled(true);
                btEditar.setVisibility(View.GONE);
                btGuardar.setVisibility(View.VISIBLE);
            }
        });
        btGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario usuario = new Usuario(
                        Integer.valueOf(tvId.getText().toString()),
                        etNombre.getText().toString(),
                        etApellido.getText().toString(),
                        etDni.getText().toString(),
                        etTelefono.getText().toString(),
                        etEmail.getText().toString(),
                        -1
                );
                perfilViewModel.editarUsuario(usuario);
                etNombre.setEnabled(false);
                etApellido.setEnabled(false);
                btGuardar.setVisibility(View.GONE);
                btEditar.setVisibility(View.VISIBLE);
            }
        });

    }


}