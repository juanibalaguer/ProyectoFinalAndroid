package com.example.terrasolecabaas.ui.productosyservicios;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.terrasolecabaas.modelo.Producto_Servicio;
import com.example.terrasolecabaas.request.ApiClient;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductosYServiciosViewModel extends AndroidViewModel {

    Context context;
    private MutableLiveData<ArrayList<Producto_Servicio>> mutableProductos;
    private MutableLiveData<ArrayList<Producto_Servicio>> mutableServicios;

    public ProductosYServiciosViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public LiveData<ArrayList<Producto_Servicio>> getProductos() {
        if (mutableProductos == null) {
            mutableProductos = new MutableLiveData<>();
        }
        return mutableProductos;
    }
    public LiveData<ArrayList<Producto_Servicio>> getServicios() {
        if (mutableServicios == null) {
            mutableServicios = new MutableLiveData<>();
        }
        return mutableServicios;
    }

    public void cargarProductos() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cabaña", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Call<ArrayList<Producto_Servicio>> callProductos = ApiClient.getMyApiClient().getProductos(token);
        callProductos.enqueue(new Callback<ArrayList<Producto_Servicio>>() {
            @Override
            public void onResponse(Call<ArrayList<Producto_Servicio>> call, Response<ArrayList<Producto_Servicio>> response) {
                if (response.isSuccessful()) {
                    mutableProductos.postValue(response.body());
                } else {
                    try {
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Producto_Servicio>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void cargarServicios() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cabaña", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Call<ArrayList<Producto_Servicio>> callServicios = ApiClient.getMyApiClient().getServicios(token);
        callServicios.enqueue(new Callback<ArrayList<Producto_Servicio>>() {
            @Override
            public void onResponse(Call<ArrayList<Producto_Servicio>> call, Response<ArrayList<Producto_Servicio>> response) {
                if (response.isSuccessful()) {
                    mutableServicios.postValue(response.body());
                } else {
                    try {
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Producto_Servicio>> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    public void agregarProducto_Servicio(Producto_Servicio producto_servicio) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cabaña", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");


    }
}