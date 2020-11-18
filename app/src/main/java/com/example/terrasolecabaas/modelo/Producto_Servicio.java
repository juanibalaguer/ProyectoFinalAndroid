package com.example.terrasolecabaas.modelo;

import java.io.Serializable;

public class Producto_Servicio implements Serializable {
    private int id;
    private String nombre;
    private String descripion;
    private byte consumible;
    private double precio;
    private String foto;

    public Producto_Servicio() {}
    public Producto_Servicio(int id, String nombre, String descripion, byte consumible, double precio, String foto) {
        this.id = id;
        this.nombre = nombre;
        this.descripion = descripion;
        this.consumible = consumible;
        this.precio = precio;
        this.foto = foto;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripion() {
        return descripion;
    }

    public void setDescripion(String descripion) {
        this.descripion = descripion;
    }

    public byte getConsumible() {
        return consumible;
    }

    public void setConsumible(byte consumible) {
        this.consumible = consumible;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
