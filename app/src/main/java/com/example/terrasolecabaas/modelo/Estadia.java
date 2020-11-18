package com.example.terrasolecabaas.modelo;

import java.io.Serializable;
import java.util.Date;

public class Estadia implements Serializable {

    private int id;
    private int cabañaId;
    private Cabaña cabaña;
    private int inquilinoId;
    private Inquilino inquilino;
    private Date fechaDesde;
    private Date fechaHasta;
    private double montoTotal;

    public Estadia() {}

    public Estadia(int id, int cabañaId, Cabaña cabaña, int inquilinoId, Inquilino inquilino, Date fechaDesde, Date fechaHasta, double montoTotal) {
        this.id = id;
        this.cabañaId = cabañaId;
        this.cabaña = cabaña;
        this.inquilinoId = inquilinoId;
        this.inquilino = inquilino;
        this.fechaDesde = fechaDesde;
        this.fechaHasta = fechaHasta;
        this.montoTotal = montoTotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCabañaId() {
        return cabañaId;
    }

    public void setCabañaId(int cabañaId) {
        this.cabañaId = cabañaId;
    }

    public Cabaña getCabaña() {
        return cabaña;
    }

    public void setCabaña(Cabaña cabaña) {
        this.cabaña = cabaña;
    }

    public int getInquilinoId() {
        return inquilinoId;
    }

    public void setInquilinoId(int inquilinoId) {
        this.inquilinoId = inquilinoId;
    }

    public Inquilino getInquilino() {
        return inquilino;
    }

    public void setInquilino(Inquilino inquilino) {
        this.inquilino = inquilino;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public double getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(double montoTotal) {
        this.montoTotal = montoTotal;
    }
}
