package com.panaderia.controllers;

import com.panaderia.config.Conexion;
import com.panaderia.models.Cliente;
import io.javalin.http.Context;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteController {

    private static String validarCliente(Cliente c) {
        if (c.getNombre() == null || c.getNombre().trim().isEmpty()) {
            return "El nombre es obligatorio.";
        }
        if (c.getCedula() == null || c.getCedula().length() != 10) {
            return "La cédula debe tener exactamente 10 dígitos.";
        }
        return null;
    }

    public static void listar(Context ctx) {
        List<Cliente> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM clientes ORDER BY id")) {
            while (rs.next()) {
                lista.add(new Cliente(rs.getInt("id"), rs.getString("nombre"), rs.getString("cedula")));
            }
            ctx.json(lista);
        } catch (SQLException e) { ctx.status(500).result("Error DB"); }
    }

    public static void crear(Context ctx) {
        Cliente c = ctx.bodyAsClass(Cliente.class);
        
        // --- VALIDACIÓN ---
        String error = validarCliente(c);
        if (error != null) {
            ctx.status(400).result(error);
            return;
        }

        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO clientes (nombre, cedula) VALUES (?, ?)")) {
            pstmt.setString(1, c.getNombre());
            pstmt.setString(2, c.getCedula());
            pstmt.executeUpdate();
            ctx.status(201).result("Cliente registrado");
        } catch (SQLException e) { ctx.status(500).result("Error DB: Posible cédula duplicada"); }
    }

    public static void borrar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM clientes WHERE id = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            ctx.status(204);
        } catch (SQLException e) { ctx.status(500).result("Error DB"); }
    }

    public static void actualizar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Cliente c = ctx.bodyAsClass(Cliente.class);

        // --- VALIDACIÓN ---
        String error = validarCliente(c);
        if (error != null) {
            ctx.status(400).result(error);
            return;
        }

        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE clientes SET nombre = ?, cedula = ? WHERE id = ?")) {
            pstmt.setString(1, c.getNombre());
            pstmt.setString(2, c.getCedula());
            pstmt.setInt(3, id);
            pstmt.executeUpdate();
            ctx.status(200).result("Cliente actualizado");
        } catch (SQLException e) { ctx.status(500).result("Error DB"); }
    }
}