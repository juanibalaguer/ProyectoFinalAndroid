package com.example.terrasolecabaas.modelo;

import java.io.Serializable;

public class PedidoLinea implements Serializable {
    private int id;
    private int pedidoId;
    private int producto_ServicioId;
    private double precioPorUnidad;
    public int cantidad;
    private Producto_Servicio producto_Servicio;
    public PedidoLinea() {}
    public PedidoLinea(int id, int pedidoId, int producto_ServicioId, double precioPorUnidad, int cantidad, Producto_Servicio producto_Servicio) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.producto_ServicioId = producto_ServicioId;
        this.precioPorUnidad = precioPorUnidad;
        this.cantidad = cantidad;
        this.producto_Servicio = producto_Servicio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(int pedidoId) {
        this.pedidoId = pedidoId;
    }

    public int getProducto_ServicioId() {
        return producto_ServicioId;
    }

    public void setProducto_ServicioId(int producto_ServicioId) {
        this.producto_ServicioId = producto_ServicioId;
    }

    public double getPrecioPorUnidad() {
        return precioPorUnidad;
    }

    public void setPrecioPorUnidad(double precioPorUnidad) {
        this.precioPorUnidad = precioPorUnidad;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public Producto_Servicio getProducto_Servicio() {
        return producto_Servicio;
    }

    public void setProducto_Servicio(Producto_Servicio producto_Servicio) {
        this.producto_Servicio = producto_Servicio;
    }
}
