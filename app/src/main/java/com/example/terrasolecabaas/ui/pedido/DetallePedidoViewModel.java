package com.example.terrasolecabaas.ui.pedido;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.example.terrasolecabaas.modelo.Pedido;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetallePedidoViewModel extends AndroidViewModel {
    Context context;
    MutableLiveData<Pedido> mutablePedido;

    public DetallePedidoViewModel(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }
    public LiveData<Pedido> getPedido() {
        if(mutablePedido == null) {
            mutablePedido = new MutableLiveData<>();
        }
        return mutablePedido;
    }

    public void cargarPedido(Bundle bundle) {
        Pedido pedido = (Pedido) bundle.getSerializable("pedido");
        mutablePedido.setValue(pedido);
    }
}