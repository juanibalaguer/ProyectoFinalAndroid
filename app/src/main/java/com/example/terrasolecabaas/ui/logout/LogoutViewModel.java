package com.example.terrasolecabaas.ui.logout;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

public class LogoutViewModel extends AndroidViewModel {
    private Context context;
    public LogoutViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public void cerrarSesion() {
        SharedPreferences.Editor editor = context.getSharedPreferences("cabaña", 0).edit();
        editor.putInt("idCabaña", -1);
        editor.putInt("idUsuario", -1);
        editor.remove("pedido");
        editor.remove("pedidolineas");
        editor.putString("token", "");
        editor.putInt("idEstadia", -1);
        editor.putInt("idInquilino", -1);
        editor.putString("nombre", "-");
        editor.putString("apellido", "-");
        editor.putString("email", "-");
        editor.putString("rol", "-");
        editor.putString("dni", "-");
        editor.putString("telefono", "-");
        editor.commit();
    }
}