package com.example.terrasolecabaas.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.terrasolecabaas.modelo.Usuario;
import com.example.terrasolecabaas.request.ApiClient;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilViewModel extends AndroidViewModel {
    Context context;
    MutableLiveData<Usuario> mutableUsuario;
    public PerfilViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }
    public LiveData<Usuario> getUsuario() {
        if (mutableUsuario == null) {
            mutableUsuario = new MutableLiveData<>();
        }
        return mutableUsuario;
    }
    public void cargarUsuario(boolean login) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cabaña", context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        if(login) {
            Call<Usuario> callUsuario = ApiClient.getMyApiClient().getUsuario(token);
            callUsuario.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if(response.isSuccessful()) {
                        SharedPreferences sharedPreferences = context.getSharedPreferences("cabaña", context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        Usuario usuario = response.body();
                        editor.putInt("id", usuario.getId());
                        editor.putString("nombre", usuario.getNombre());
                        Log.d("Salida: ", usuario.getEmail());
                        Toast.makeText(context, usuario.getEmail(), Toast.LENGTH_LONG).show();
                        editor.putString("apellido", usuario.getApellido());
                        editor.putString("email", usuario.getEmail());
                        editor.putInt("rol", -1);
                        editor.commit();
                        mutableUsuario.postValue(usuario);
                    } else {
                        try {
                            Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {
                    Toast.makeText(context, "No fue posible cargar el usuario", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Usuario usuario = new Usuario(sharedPreferences.getInt("id", -1),
                    sharedPreferences.getString("nombre", "-"),
                    sharedPreferences.getString("apellido", "-"),
                    sharedPreferences.getString("email", "-"),
                    null,
                    sharedPreferences.getInt("rol", -1));
            mutableUsuario.setValue(usuario);
        }
    }
    public void editarUsuario(Usuario usuario) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cabaña", context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Call<Usuario> callUsuario = ApiClient.getMyApiClient().putUsuario(token, usuario);
        callUsuario.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.isSuccessful()) {
                    SharedPreferences sharedPreferences = context.getSharedPreferences("cabaña", context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Usuario usuario = response.body();
                    editor.putInt("id", usuario.getId());
                    editor.putString("nombre", usuario.getNombre());
                    editor.putString("apellido", usuario.getApellido());
                    editor.putString("email", usuario.getEmail());
                    editor.commit();
                    mutableUsuario.postValue(usuario);
                    Toast.makeText(context, "Perfil actualizado", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                Toast.makeText(context, "Error al editar perfil", Toast.LENGTH_LONG).show();
            }
        });

    }

}