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
        context.getSharedPreferences("cabaña", Context.MODE_PRIVATE).edit().clear().commit();
    }
}