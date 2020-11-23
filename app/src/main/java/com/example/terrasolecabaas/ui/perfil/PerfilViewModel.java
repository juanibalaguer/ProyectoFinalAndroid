package com.example.terrasolecabaas.ui.perfil;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.example.terrasolecabaas.modelo.Estadia;
import com.example.terrasolecabaas.modelo.Inquilino;
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
    MutableLiveData<Inquilino> mutableInquilino;
    SharedPreferences sharedPreferences;
    public PerfilViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        sharedPreferences = context.getSharedPreferences("cabaña", context.MODE_PRIVATE);
    }
    public LiveData<Inquilino> getInquilino() {
        if (mutableInquilino == null) {
            mutableInquilino = new MutableLiveData<>();
        }
        return mutableInquilino;
    }
    public void cargarUsuario(boolean login) {
        String token = sharedPreferences.getString("token", "");
        if(login) {
            pedirDatos();
        } else {
            if(sharedPreferences.getInt("idInquilino", -1) == -1) {
                pedirDatos();
            }
            else {
                Inquilino inquilino = new Inquilino(
                        sharedPreferences.getInt("idInquilino", -1),
                        sharedPreferences.getString("nombre", "-"),
                        sharedPreferences.getString("apellido", "-"),
                        sharedPreferences.getString("dni", "-"),
                        sharedPreferences.getString("email", "-"),
                        sharedPreferences.getString("telefono", "-"));
                mutableInquilino.setValue(inquilino);
            }
        }
    }
    public void pedirDatos() {
        String token = sharedPreferences.getString("token", "");
        Call<Usuario> callUsuario = ApiClient.getMyApiClient().getUsuario(token);
        callUsuario.enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                if(response.isSuccessful()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    Usuario usuario = response.body();
                    editor.putInt("idUsuario", usuario.getId());
                    editor.putString("email", usuario.getEmail());
                    editor.putString("rol", usuario.getRol() == 2 ? "empleado" : "inquilino");
                    if(usuario.getRol() == 3) {
                        Call<Estadia> callEstadia = ApiClient.getMyApiClient().getEstadia(token);
                        callEstadia.enqueue(new Callback<Estadia>() {
                            @Override
                            public void onResponse(Call<Estadia> call, Response<Estadia> response) {
                                if (response.isSuccessful()) {
                                    Estadia estadia = response.body();
                                    mutableInquilino.postValue(estadia.getInquilino());
                                    editor.putInt("idCabaña", estadia.getCabañaId());
                                    editor.putInt("idEstadia", estadia.getId());
                                    editor.putInt("idInquilino", estadia.getInquilinoId());
                                    editor.putString("nombre", estadia.getInquilino().getNombre());
                                    editor.putString("apellido", estadia.getInquilino().getApellido());
                                    editor.putString("dni", estadia.getInquilino().getDni());
                                    editor.putString("telefono", estadia.getInquilino().getTelefono());
                                    editor.commit();
                                } else {
                                    try {
                                        Log.d("Salida",  response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                            @Override
                            public void onFailure(Call<Estadia> call, Throwable t) {
                                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    } else {
                        try {
                            Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    editor.commit();
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
    }
    public void editarUsuario(Usuario usuario) {/*
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
*/
    }

}