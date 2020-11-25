package com.example.terrasolecabaas.ui.pedido;

import android.app.Activity;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.icu.number.LocalizedNumberFormatter;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.terrasolecabaas.R;
import com.example.terrasolecabaas.modelo.Pedido;
import com.example.terrasolecabaas.modelo.PedidoLinea;
import com.example.terrasolecabaas.modelo.Producto_Servicio;
import com.example.terrasolecabaas.request.ApiClient;
import com.example.terrasolecabaas.ui.productosyservicios.ProductosYServiciosViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.security.acl.LastOwnerException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PedidoViewModel extends AndroidViewModel {
    private Context context;
    private Gson gson;
    private SharedPreferences sharedPreferences;
    private MutableLiveData<Pedido> mutablePedido;
    private MutableLiveData<String> mutableStringFecha;
    private MutableLiveData<Boolean> mutablePedidoExitoso;
    public PedidoViewModel(@NonNull Application application) {
        super(application);
        context = getApplication().getApplicationContext();
        sharedPreferences = context.getSharedPreferences("cabaña", Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public LiveData<Pedido> getPedido() {
        if (mutablePedido == null) {
            mutablePedido = new MutableLiveData<>();
        }
        return mutablePedido;
    }
    public LiveData<String> getFecha() {
        if (mutableStringFecha == null) {
            mutableStringFecha = new MutableLiveData<>();
        }
        return mutableStringFecha;
    }
    public LiveData<Boolean> pedidoEsExitoso() {
        if (mutablePedidoExitoso == null) {
            mutablePedidoExitoso = new MutableLiveData<>();
        }
        return mutablePedidoExitoso;
    }

    public void cargarPedido(Bundle bundle) {
        if(bundle != null) {
            if((bundle.getSerializable("servicio") != null)) { //Es un servicio (no tiene líneas pedido)
                Producto_Servicio servicio = (Producto_Servicio) bundle.getSerializable("servicio");
                Pedido pedido = new Pedido();
                PedidoLinea pedidoLinea = new PedidoLinea(0, 0, servicio.getId(), 0, 1, servicio);
                ArrayList<PedidoLinea> pedidoLineas = new ArrayList<>();
                pedidoLineas.add(pedidoLinea);
                pedido.setPedidoLineas(pedidoLineas);
                pedido.setMontoPedido(0);
                mutablePedido.setValue(pedido);
                return;
            } else { // Ser carga un pedido para ver/editar
                Pedido pedido = (Pedido) bundle.getSerializable("pedido");
                sharedPreferences.edit().putInt("idPedido", pedido.getId()).commit();
                if(pedido.getPedidoLineas().get(0).getProducto_Servicio().getConsumible() == 1){
                    String jsonPedidoLineas = gson.toJson(pedido.getPedidoLineas());
                    sharedPreferences.edit().putString("pedidolineas", jsonPedidoLineas).commit();
                }
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
                String fechaParseada = formato.format(pedido.getFechaPedido());
                String jsonPedido = gson.toJson(pedido);
                sharedPreferences.edit().putString("pedido", jsonPedido).commit();
                mutablePedido.setValue(pedido);
                mutableStringFecha.setValue(fechaParseada);
                return;
            }

        }
        String jsonPedidoLineas = sharedPreferences.getString("pedidolineas", null);
        Type type = new TypeToken<ArrayList<PedidoLinea>>() {}.getType();
        ArrayList<PedidoLinea> pedidoLineas = gson.fromJson(jsonPedidoLineas, type);
        if (pedidoLineas == null) {
            mutablePedido.setValue(null);
            return;
        }
        String jsonPedido = sharedPreferences.getString("pedido", null);
        Type tipoPedido = new TypeToken<Pedido>() {}.getType();
        Pedido pedido = gson.fromJson(jsonPedido, tipoPedido);

        pedido.setPedidoLineas(pedidoLineas);
        double montoTotal = 0;
        for (PedidoLinea pedidoLinea: pedidoLineas) {
            montoTotal += pedidoLinea.getPrecioPorUnidad() * pedidoLinea.getCantidad();
        }
        if(pedido !=null) {
            pedido.setMontoPedido(montoTotal);
            mutablePedido.setValue(pedido);
            if(pedido.getFechaPedido() != null) {
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
                String fechaParseada = formato.format(pedido.getFechaPedido());
                mutableStringFecha.setValue(fechaParseada);
                jsonPedido = gson.toJson(pedido);
                sharedPreferences.edit().putString("pedido", jsonPedido).commit();
            }
        }
    }
    public void eliminarItem(int indiceItem) {
        ArrayList<PedidoLinea> pedidoLineas;
        Gson gson = new Gson();
        String jsonPedidoLineas = sharedPreferences.getString("pedidolineas", null);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Type type = new TypeToken<ArrayList<PedidoLinea>>() {}.getType();
        pedidoLineas = gson.fromJson(jsonPedidoLineas, type);
        pedidoLineas.remove(indiceItem);
        jsonPedidoLineas = gson.toJson(pedidoLineas);
        editor.putString("pedidolineas", jsonPedidoLineas);
        editor.apply();
        cargarPedido(null);
    }
    public void confirmarPedido(String fecha, String titulo) throws ParseException {
        String token = sharedPreferences.getString("token", "");
        Pedido pedido = getPedido().getValue();
        //Fecha
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date date = formatter.parse(fecha);
        formatter = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        String stringFechaJson = formatter.format(date);
        Date dateFormatoJson = formatter.parse(stringFechaJson);
        //String formattedDateString = formatter.format(date);
        pedido.setFechaPedido(dateFormatoJson);
        pedido.setTitulo(titulo);
        int estado = 1;
        int idEstadia = sharedPreferences.getInt("idEstadia", -1);
        pedido.setEstado(estado);
        pedido.setEstadiaId(idEstadia);
        int idPedido = sharedPreferences.getInt("idPedido", -1);
        if(idPedido != -1) {
            pedido.setId(idPedido);
            checkearCampos(pedido);
            Call<Pedido> callPedido = ApiClient.getMyApiClient().putPedido(token, pedido);
            callPedido.enqueue(new Callback<Pedido>() {
                @Override
                public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                    if (response.isSuccessful()) {
                        limpiarDatos();
                        Toast.makeText(context, "Tu pedido fue modificado exitosamente", Toast.LENGTH_LONG).show();
                        mutablePedidoExitoso.postValue(true);
                    } else {
                        try {
                            Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_LONG).show();
                            Log.d("Error al modificar", response.errorBody().string());
                            mutablePedidoExitoso.postValue(false);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(Call<Pedido> call, Throwable t) {
                    Log.d("Error servidor: ", t.getMessage());
                    mutablePedidoExitoso.postValue(false);
                }
            });
            return;
        }
        checkearCampos(pedido);
        Call<Pedido> callPedido = ApiClient.getMyApiClient().postPedido(token, pedido);
        callPedido.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if (response.isSuccessful()) {
                    limpiarDatos();
                    Toast.makeText(context, "Tu pedido fue registrado exitosamente", Toast.LENGTH_LONG).show();
                    mutablePedidoExitoso.postValue(true);
                } else {
                    try {
                        Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_LONG).show();
                        Log.d("Error al postear pedido", response.errorBody().string());
                        mutablePedidoExitoso.postValue(false);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<Pedido> call, Throwable t) {
                Log.d("Error servidor: ", t.getMessage());
                mutablePedidoExitoso.postValue(false);
            }
        });
    }
    public void checkearCampos(Pedido pedido) {
        if(pedido.getTitulo() == "" || pedido.getTitulo() == null) {
            Toast.makeText(context, "El campo título es obligatorio", Toast.LENGTH_LONG).show();
            return;
        }
    }
    public void guardarDatos(String titulo, String fecha) throws ParseException {
        String jsonPedido = sharedPreferences.getString("pedido", null);
        Type tipoPedido = new TypeToken<Pedido>() {}.getType();
        Pedido pedido = gson.fromJson(jsonPedido, tipoPedido);
        if(pedido != null) {
            pedido.setTitulo(titulo);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = formatter.parse(fecha);
            formatter = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
            String stringFechaJson = formatter.format(date);
            Date dateFormatoJson = formatter.parse(stringFechaJson);
            pedido.setFechaPedido(dateFormatoJson);
            jsonPedido = gson.toJson(pedido);
            sharedPreferences.edit().putString("pedido", jsonPedido).apply();
        }

    }
    public void limpiarDatos() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("pedidolineas");
        editor.remove("pedido");
        editor.remove("idPedido");
        editor.apply();
    }
}