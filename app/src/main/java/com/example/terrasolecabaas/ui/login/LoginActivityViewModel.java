package com.example.terrasolecabaas.ui.login;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.terrasolecabaas.MainActivity;
import com.example.terrasolecabaas.request.ApiClient;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivityViewModel extends AndroidViewModel {
    Context context;
    private MutableLiveData<String> mensaje;
    private ApiClient apiClient;
    public LoginActivityViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();

    }

    public LiveData<String> getError() {
        if (mensaje == null) {
            mensaje = new MutableLiveData<>();
        }
        return mensaje;
    }

    public void autenticar(final String usuario, String contraseña) {
        Call<String> token = ApiClient.getMyApiClient().Login(usuario, contraseña);
        token.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("cabaña", context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("token", "Bearer " + response.body());
                    editor.commit();
                    mensaje.postValue("Inicio de sesión exitoso");
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("login", true);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                } else {
                    try {
                        mensaje.postValue(response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
