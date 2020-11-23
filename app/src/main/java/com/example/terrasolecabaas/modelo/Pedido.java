package com.example.terrasolecabaas.modelo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Pedido implements Serializable {
    private int id;
    private String titulo;
    private Date fechaPedido;
    private int estado;
    private double montoPedido;
    private int estadiaId;
    private ArrayList<PedidoLinea> pedidoLineas;

    public Pedido(int id, String titulo, Date fechaPedido, int estado, double montoPedido, int estadiaId, ArrayList<PedidoLinea> pedidoLineas) {
        this.id = id;
        this.titulo = titulo;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.montoPedido = montoPedido;
        this.estadiaId = estadiaId;
        this.pedidoLineas = pedidoLineas;
    }
    public Pedido() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(Date fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public double getMontoPedido() {
        return montoPedido;
    }

    public void setMontoPedido(double montoPedido) {
        this.montoPedido = montoPedido;
    }

    public int getEstadiaId() {
        return estadiaId;
    }

    public void setEstadiaId(int estadiaId) {
        this.estadiaId = estadiaId;
    }

    public ArrayList<PedidoLinea> getPedidoLineas() {
        return pedidoLineas;
    }

    public void setPedidoLineas(ArrayList<PedidoLinea> pedidoLineas) {
        this.pedidoLineas = pedidoLineas;
    }
}
