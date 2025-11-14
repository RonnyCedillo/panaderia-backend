package com.panaderia.models;

public class Pan {
    private int id;
    private String nombre;
    private double precio;

    // Constructor vacío
    public Pan() {}

    // Constructor lleno
    public Pan(int id, String nombre, double precio) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
    }

    // Getters y Setters (Necesarios para que Javalin funcione bien)
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
}