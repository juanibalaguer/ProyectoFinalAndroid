package com.example.terrasolecabaas.modelo;

import java.io.Serializable;

public class Cabaña implements Serializable {
    private int id;
    private int categoria;
    private int capacidad;
    private double montoPorDia;

    public Cabaña() {}

    public Cabaña(int id, int categoria, int capacidad, double montoPorDia) {
        this.id = id;
        this.categoria = categoria;
        this.capacidad = capacidad;
        this.montoPorDia = montoPorDia;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoria() {
        return categoria;
    }

    public void setCategoria(int categoria) {
        this.categoria = categoria;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public double getMontoPorDia() {
        return montoPorDia;
    }

    public void setMontoPorDia(double montoPorDia) {
        this.montoPorDia = montoPorDia;
    }
}
