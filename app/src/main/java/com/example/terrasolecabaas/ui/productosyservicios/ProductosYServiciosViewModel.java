package com.example.terrasolecabaas.ui.productosyservicios;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.example.terrasolecabaas.modelo.PedidoLinea;
import com.example.terrasolecabaas.modelo.Producto_Servicio;
import com.example.terrasolecabaas.request.ApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductosYServiciosViewModel extends AndroidViewModel {

    Context context;
    private MutableLiveData<ArrayList<Producto_Servicio>> mutableProductos;
    private MutableLiveData<ArrayList<Producto_Servicio>> mutableServicios;
    SharedPreferences sharedPreferences;


    public ProductosYServiciosViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
        sharedPreferences = context.getSharedPreferences("cabaña", Context.MODE_PRIVATE);
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
    public void agregarProducto(Producto_Servicio producto_servicio, int cantidad) {
        if (cantidad < 1) {
            Toast.makeText(context, "Agregue al menos una unidad", Toast.LENGTH_LONG).show();
            return;
        }
        PedidoLinea pedidoLinea = new PedidoLinea();
        pedidoLinea.setCantidad(cantidad);
        pedidoLinea.setProducto_Servicio(producto_servicio);
        pedidoLinea.setProducto_ServicioId(producto_servicio.getId());
        pedidoLinea.setPrecioPorUnidad(producto_servicio.getPrecio());
        ArrayList<PedidoLinea> pedidoLineas;
        Gson gson = new Gson();
        String jsonPedidoLineas = sharedPreferences.getString("pedidolineas", null);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Type type = new TypeToken<ArrayList<PedidoLinea>>() {}.getType();
        pedidoLineas = gson.fromJson(jsonPedidoLineas, type);
        if (pedidoLineas == null) {
            pedidoLineas = new ArrayList<>();
        }
        boolean esItemNuevo = true;
        if(pedidoLineas.size() > 0) {
            for (PedidoLinea linea: pedidoLineas) {
                if(pedidoLinea.getProducto_ServicioId() == linea.getProducto_ServicioId()) {
                    linea.setCantidad(linea.getCantidad() + pedidoLinea.getCantidad());
                    esItemNuevo = false;
                    break;
                }
            }
        } else {
            pedidoLineas.add(pedidoLinea);
            esItemNuevo = false;
        }
        if(esItemNuevo) pedidoLineas.add(pedidoLinea);
        jsonPedidoLineas = gson.toJson(pedidoLineas);
        editor.putString("pedidolineas", jsonPedidoLineas);
        Toast.makeText(context, pedidoLineas.size() + "", Toast.LENGTH_LONG).show();
        editor.apply();
    }
}