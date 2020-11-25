package com.example.terrasolecabaas.ui.pedidos;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.terrasolecabaas.modelo.Pedido;
import com.example.terrasolecabaas.request.ApiClient;

import java.io.IOException;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidosViewModel extends AndroidViewModel {

    private  MutableLiveData<ArrayList<Pedido>> mutablePedidos;
    private  MutableLiveData<Boolean> mutableHayPedidos;
    private Context context;
    SharedPreferences sharedPreferences;
    public PedidosViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        sharedPreferences = context.getSharedPreferences("cabaña", Context.MODE_PRIVATE);
    }


    public LiveData<ArrayList<Pedido>> getPedidos() {
        if(mutablePedidos == null) {
            mutablePedidos = new MutableLiveData<>();
        }
        return mutablePedidos;
    }
    public LiveData<Boolean> hayPedidos() {
        if (mutableHayPedidos == null) {
            mutableHayPedidos = new MutableLiveData<>();
        }
        return mutableHayPedidos;
    }
    public void cargarPedidos() {
        String token = sharedPreferences.getString("token", "");
        String rol = sharedPreferences.getString("rol", "");
        if(rol.equals("inquilino")) {
            Call<ArrayList<Pedido>> callPedidos = ApiClient.getMyApiClient().getPedidosPorInquilino(token);
            callPedidos.enqueue(new Callback<ArrayList<Pedido>>() {
                @Override
                public void onResponse(Call<ArrayList<Pedido>> call, Response<ArrayList<Pedido>> response) {
                    if(response.isSuccessful()) {
                        mutablePedidos.postValue(response.body());
                    } else {
                        try {
                            mutablePedidos.postValue(new ArrayList<Pedido>());
                            Log.d("Error al traer pedidos", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<Pedido>> call, Throwable t) {
                    Log.d("Error servidor", t.getMessage());
                }
            });
        }
        else {
            Call<ArrayList<Pedido>> callPedidos = ApiClient.getMyApiClient().getPedidosPendientes(token);
            callPedidos.enqueue(new Callback<ArrayList<Pedido>>() {
                @Override
                public void onResponse(Call<ArrayList<Pedido>> call, Response<ArrayList<Pedido>> response) {
                    if(response.isSuccessful()) {
                        mutablePedidos.postValue(response.body());
                    } else {
                        try {
                            mutablePedidos.postValue(new ArrayList<Pedido>());
                            Log.d("Error al traer pedidos", response.errorBody().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<ArrayList<Pedido>> call, Throwable t) {
                    Log.d("Error servidor", t.getMessage());
                }
            });
        }

    }
    public void cancelarPedido(int id) {
        String token = sharedPreferences.getString("token", "");
        Call<ResponseBody> callDeletePedido = ApiClient.getMyApiClient().deletePedido(token, id);
        callDeletePedido.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show();
                    cargarPedidos();
                } else {

                    Log.d("Error al borrar", response.message());
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Error del servidor", t.getMessage());
            }
        });
    }
    public void cambiarEstadoPedido(Pedido pedido) {
        String token = sharedPreferences.getString("token", "");
        if(pedido.getEstado() == 1) {
            pedido.setEstado(2);
        } else {
            pedido.setEstado(1);
        }
        Call<Pedido> callEditarPedido = ApiClient.getMyApiClient().putPedido(token, pedido);
        callEditarPedido.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show();
                    cargarPedidos();
                } else {

                    Log.d("Error al borrar", response.message());
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Log.d("Error del servidor", t.getMessage());
            }
        });
    }
    public void entregarPedido(Pedido pedido) {
        String token = sharedPreferences.getString("token", "");
        pedido.setEstado(3);
        Call<Pedido> callEditarPedido = ApiClient.getMyApiClient().putPedido(token, pedido);
        callEditarPedido.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if(response.isSuccessful()) {
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show();
                    cargarPedidos();
                } else {

                    Log.d("Error al borrar", response.message());
                    Toast.makeText(context, response.message(), Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Log.d("Error del servidor", t.getMessage());
            }
        });
        cargarPedidos();
    }
}