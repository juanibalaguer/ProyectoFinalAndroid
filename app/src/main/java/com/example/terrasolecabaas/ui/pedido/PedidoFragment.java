package com.example.terrasolecabaas.ui.pedido;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.terrasolecabaas.MainActivity;
import com.example.terrasolecabaas.R;
import com.example.terrasolecabaas.modelo.Pedido;
import com.example.terrasolecabaas.modelo.PedidoLinea;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PedidoFragment extends Fragment {

    private ListView lvPedidoLineas;
    private TextView tvMonto;
    private EditText etTitulo;
    private Button btSeleccionarFecha, btSeleccionarHora, btConfirmarPedido, btAgregarItems;
    private PedidoViewModel pedidoViewModel;
    private PedidoLineaAdapter pedidoLineaAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_pedido, container, false);
        inicializar(root);
        return root;
    }

    private void inicializar(View view) {
        lvPedidoLineas = view.findViewById(R.id.lvPedidoLineas);
        tvMonto = view.findViewById(R.id.tvMonto);
        etTitulo = view.findViewById(R.id.etTitulo);
        btSeleccionarHora = view.findViewById(R.id.btSeleccionarHora);
        btSeleccionarFecha = view.findViewById(R.id.btSeleccionarFecha);
        btConfirmarPedido = view.findViewById(R.id.btConfirmarPedido);
        btAgregarItems = view.findViewById(R.id.btAgregarItems);
        pedidoViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PedidoViewModel.class);
        btSeleccionarFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarFecha();
            }
        });
        btSeleccionarHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarHora();
            }
        });
        btAgregarItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.fragment_productosyservicios);
            }
        });
        btConfirmarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    pedidoViewModel.confirmarPedido(btSeleccionarFecha.getText().toString(), btSeleccionarHora.getText().toString(), etTitulo.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        pedidoViewModel.getPedido().observe(getViewLifecycleOwner(), new Observer<Pedido>() {
            @Override
            public void onChanged(Pedido pedido) {
                if(pedido != null){
                    PedidoLineaAdapter adapter = new PedidoLineaAdapter(getContext(), R.layout.frament_item_linea, pedido.getPedidoLineas(), getLayoutInflater(), pedidoViewModel);
                    lvPedidoLineas.setAdapter(adapter);
                    tvMonto.setText(pedido.getMontoPedido() + "");
                    etTitulo.setText(pedido.getTitulo());

                } else {
                    btConfirmarPedido.setEnabled(false);
                }
            }
        });
        pedidoViewModel.getFecha().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String fecha) {
                btSeleccionarFecha.setText(fecha);
            }
        });
        pedidoViewModel.getHora().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String hora) {
                btSeleccionarHora.setText(hora);
            }
        });

        pedidoViewModel.pedidoEsExitoso().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean esPedidoExitoso) {
                if(esPedidoExitoso) {
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.fragment_pedidos);
                }
            }
        });
        pedidoViewModel.cargarPedido(getArguments(), etTitulo.getText().toString());
    }
    public void seleccionarFecha() {
        Calendar calendar = Calendar.getInstance();
        int año = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendarPick = Calendar.getInstance();
                calendarPick.set(Calendar.YEAR, year);
                calendarPick.set(Calendar.MONTH, month);
                calendarPick.set(Calendar.DATE, date);
                String stringFecha = DateFormat.format("dd/MM/yyyy", calendarPick).toString();
                btSeleccionarFecha.setText(stringFecha);
            }
        }, año, mes, dia);

        datePickerDialog.show();
    }
    public void seleccionarHora() {
        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR);
        int minuto = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                Calendar calendarPick = Calendar.getInstance();
                calendarPick.set(Calendar.HOUR, hour);
                calendarPick.set(Calendar.MINUTE, minute);
                String stringHora = DateFormat.format("HH:mm", calendarPick).toString();
                btSeleccionarHora.setText(stringHora);
            }
        }, hora, minuto, true);


        timePickerDialog.show();
    }
}