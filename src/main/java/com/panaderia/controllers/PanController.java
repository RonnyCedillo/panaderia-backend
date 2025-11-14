package com.panaderia.controllers;

import com.panaderia.config.Conexion;
import com.panaderia.models.Pan;
import io.javalin.http.Context;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PanController {

    // Método auxiliar para validar (Así no repetimos código)
    private static String validarPan(Pan p) {
        if (p.getNombre() == null || p.getNombre().trim().isEmpty()) {
            return "El nombre del pan no puede estar vacío.";
        }
        if (p.getPrecio() <= 0) {
            return "El precio debe ser mayor a 0.";
        }
        return null; // Si retorna null, es que todo está bien
    }

    public static void listar(Context ctx) {
        List<Pan> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM panes ORDER BY id")) {
            while (rs.next()) {
                lista.add(new Pan(rs.getInt("id"), rs.getString("nombre"), rs.getDouble("precio")));
            }
            ctx.json(lista);
        } catch (SQLException e) { ctx.status(500).result("Error: " + e.getMessage()); }
    }

    public static void crear(Context ctx) {
        Pan p = ctx.bodyAsClass(Pan.class);
        
        // --- VALIDACIÓN ---
        String error = validarPan(p);
        if (error != null) {
            ctx.status(400).result(error); // 400 Bad Request
            return; // ¡IMPORTANTE! Detenemos la ejecución aquí
        }

        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO panes (nombre, precio) VALUES (?, ?)")) {
            pstmt.setString(1, p.getNombre());
            pstmt.setDouble(2, p.getPrecio());
            pstmt.executeUpdate();
            ctx.status(201).result("Pan horneado con éxito");
        } catch (SQLException e) { ctx.status(500).result("Error: " + e.getMessage()); }
    }

    public static void borrar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM panes WHERE id = ?")) {
            pstmt.setInt(1, id);
            int afectados = pstmt.executeUpdate();
            if (afectados > 0) ctx.status(204);
            else ctx.status(404).result("Pan no encontrado");
        } catch (SQLException e) { ctx.status(500).result("Error: " + e.getMessage()); }
    }

    public static void actualizar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Pan p = ctx.bodyAsClass(Pan.class);
        
        // --- VALIDACIÓN ---
        String error = validarPan(p);
        if (error != null) {
            ctx.status(400).result(error);
            return;
        }

        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE panes SET nombre = ?, precio = ? WHERE id = ?")) {
            pstmt.setString(1, p.getNombre());
            pstmt.setDouble(2, p.getPrecio());
            pstmt.setInt(3, id);
            int afectados = pstmt.executeUpdate();
            if (afectados > 0) ctx.status(200).result("Pan actualizado");
            else ctx.status(404).result("Pan no encontrado");
        } catch (SQLException e) { ctx.status(500).result("Error: " + e.getMessage()); }
    }
}