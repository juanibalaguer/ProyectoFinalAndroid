package com.example.terrasolecabaas.modelo;

import java.io.Serializable;

public class PedidoLinea implements Serializable {
    private int id;
    private int pedidoId;
    private int producto_servicioId;
    private double precioPorUnidad;
    public int cantidad;
    private Producto_Servicio producto_servicio;

    public PedidoLinea(int id, int pedidoId, int producto_servicioId, double precioPorUnidad, int cantidad, Producto_Servicio producto_servicio) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.producto_servicioId = producto_servicioId;
        this.precioPorUnidad = precioPorUnidad;
        this.cantidad = cantidad;
        this.producto_servicio = producto_servicio;
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

    public int getProducto_servicioId() {
        return producto_servicioId;
    }

    public void setProducto_servicioId(int producto_servicioId) {
        this.producto_servicioId = producto_servicioId;
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

    public Producto_Servicio getProducto_servicio() {
        return producto_servicio;
    }

    public void setProducto_servicio(Producto_Servicio producto_servicio) {
        this.producto_servicio = producto_servicio;
    }
}
