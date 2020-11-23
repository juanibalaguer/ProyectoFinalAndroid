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
    SharedPreferences sharedPreferences;
    private MutableLiveData<Pedido> mutablePedido;
    private MutableLiveData<String> mutableStringFecha;
    private MutableLiveData<String> mutableStringHora;
    private MutableLiveData<Boolean> mutablePedidoExitoso;
    private MutableLiveData<Boolean> mutableHayItems;
    public PedidoViewModel(@NonNull Application application) {
        super(application);
        context = getApplication().getApplicationContext();
        sharedPreferences = context.getSharedPreferences("cabaña", Context.MODE_PRIVATE);
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
    public LiveData<String> getHora() {
        if (mutableStringHora == null) {
            mutableStringHora = new MutableLiveData<>();
        }
        return mutableStringHora;
    }
    public LiveData<Boolean> pedidoEsExitoso() {
        if (mutablePedidoExitoso == null) {
            mutablePedidoExitoso = new MutableLiveData<>();
        }
        return mutablePedidoExitoso;
    }

    public void cargarItems(Bundle bundle) {
        if(bundle != null) {
            if((bundle.getSerializable("servicio") != null)) {
                Producto_Servicio servicio = (Producto_Servicio) bundle.getSerializable("servicio");
                Pedido pedido = new Pedido();
                PedidoLinea pedidoLinea = new PedidoLinea(0, 0, servicio.getId(), 0, 1, servicio);
                ArrayList<PedidoLinea> pedidoLineas = new ArrayList<>();
                pedidoLineas.add(pedidoLinea);
                pedido.setPedidoLineas(pedidoLineas);
                pedido.setMontoPedido(0);
                mutablePedido.setValue(pedido);
                return;
            } else {
                Pedido pedido = (Pedido) bundle.getSerializable("pedido");
                SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy' 'HH:mm");
                String fechaParseada = formato.format(pedido.getFechaPedido());
                String fecha = fechaParseada.substring(0, 10);
                String hora = fechaParseada.substring(10, fechaParseada.length());
                mutablePedido.setValue(pedido);
                mutableStringFecha.setValue(fecha);
                mutableStringHora.setValue(hora);
                return;
            }

        }
        Gson gson = new Gson();
        String jsonPedidoLineas = sharedPreferences.getString("pedidolineas", null);
        Type type = new TypeToken<ArrayList<PedidoLinea>>() {}.getType();
        ArrayList<PedidoLinea> pedidoLineas = gson.fromJson(jsonPedidoLineas, type);
        if (pedidoLineas == null) {
            mutablePedido.setValue(null);
            return;
        }
        Pedido pedido = new Pedido();
        pedido.setPedidoLineas(pedidoLineas);
        double montoTotal = 0;
        for (PedidoLinea pedidoLinea: pedidoLineas) {
            montoTotal += pedidoLinea.getPrecioPorUnidad() * pedidoLinea.getCantidad();
        }
        pedido.setMontoPedido(montoTotal);
        pedido.setTitulo("");
        mutablePedido.setValue(pedido);
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
        cargarItems(null);
    }
    public void confirmarPedido(String fecha, String hora, String titulo) throws ParseException {
        String token = sharedPreferences.getString("token", "");
        Pedido pedido = getPedido().getValue();
        //Fecha
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String dateInString = fecha + " " + hora;
        Date date = formatter.parse(dateInString);
        formatter = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss");
        String stringFechaJson = formatter.format(date);
        Date dateJson = formatter.parse(stringFechaJson);
        //String formattedDateString = formatter.format(date);
        pedido.setFechaPedido(dateJson);
        int estado = 1;
        int idEstadia = sharedPreferences.getInt("idEstadia", -1);
        pedido.setTitulo(titulo);
        pedido.setEstado(estado);
        pedido.setEstadiaId(idEstadia);
        Call<Pedido> callPedido = ApiClient.getMyApiClient().postPedido(token, pedido);
        callPedido.enqueue(new Callback<Pedido>() {
            @Override
            public void onResponse(Call<Pedido> call, Response<Pedido> response) {
                if (response.isSuccessful()) {
                    sharedPreferences.edit().remove("pedidolineas").commit();
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

}