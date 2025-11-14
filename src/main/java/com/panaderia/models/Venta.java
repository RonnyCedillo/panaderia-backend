package com.panaderia.models;

public class Venta {
    private int id;
    private int idCliente;
    private int idPan;
    private double total;

    public Venta() {}

    public Venta(int id, int idCliente, int idPan, double total) {
        this.id = id;
        this.idCliente = idCliente;
        this.idPan = idPan;
        this.total = total;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }
    public int getIdPan() { return idPan; }
    public void setIdPan(int idPan) { this.idPan = idPan; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
}