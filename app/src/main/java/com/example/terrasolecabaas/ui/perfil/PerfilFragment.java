package com.example.terrasolecabaas.ui.perfil;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.example.terrasolecabaas.modelo.Usuario;

public class PerfilFragment extends Fragment {

    private PerfilViewModel perfilViewModel;
    EditText etNombre, etApellido, etEmail;
    TextView tvId, tvRol;
    Button btEditar, btGuardar;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);
        inicializar(root);
        return root;
    }

    private void inicializar(View view) {
        etNombre = view.findViewById(R.id.etNombre);
        etApellido = view.findViewById(R.id.etApellido);
        etEmail = view.findViewById(R.id.etEmail);
        btEditar = view.findViewById(R.id.btEditar);
        btGuardar = view.findViewById(R.id.btGuardar);
        tvId = view.findViewById(R.id.tvId);
        tvRol = view.findViewById(R.id.tvRol);
        perfilViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PerfilViewModel.class);
        perfilViewModel.getUsuario().observe(getViewLifecycleOwner(), new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                tvId.setText(usuario.getId() + "");
                etNombre.setText(usuario.getNombre());
                etApellido.setText(usuario.getApellido());
                etEmail.setText(usuario.getEmail());
                String rol = usuario.getRol() == 2 ? "Empleado" : "Inquilino";
                tvRol.setText(tvRol.getText().toString() +  rol);
            }
        });
        perfilViewModel.cargarUsuario(getActivity().getIntent().getBooleanExtra("login", false));
        getActivity().getIntent().removeExtra("login");
        btEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etNombre.setEnabled(true);
                etApellido.setEnabled(true);
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
                        etEmail.getText().toString(),
                        "contraseña no cambia",
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