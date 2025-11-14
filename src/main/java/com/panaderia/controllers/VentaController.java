package com.panaderia.controllers;

import com.panaderia.config.Conexion;
import com.panaderia.models.Venta;
import io.javalin.http.Context;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentaController {

    private static String validarVenta(Venta v) {
        if (v.getIdCliente() <= 0) return "ID de Cliente inválido.";
        if (v.getIdPan() <= 0) return "ID de Pan inválido.";
        if (v.getTotal() < 0) return "El total no puede ser negativo.";
        return null;
    }

    public static void listar(Context ctx) {
        List<Venta> lista = new ArrayList<>();
        try (Connection conn = Conexion.getConexion();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM ventas ORDER BY id")) {
            while (rs.next()) {
                lista.add(new Venta(rs.getInt("id"), rs.getInt("id_cliente"), rs.getInt("id_pan"), rs.getDouble("total")));
            }
            ctx.json(lista);
        } catch (SQLException e) { ctx.status(500).result("Error DB"); }
    }

    public static void crear(Context ctx) {
        Venta v = ctx.bodyAsClass(Venta.class);
        
        // --- VALIDACIÓN ---
        String error = validarVenta(v);
        if (error != null) {
            ctx.status(400).result(error);
            return;
        }

        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ventas (id_cliente, id_pan, total) VALUES (?, ?, ?)")) {
            pstmt.setInt(1, v.getIdCliente());
            pstmt.setInt(2, v.getIdPan());
            pstmt.setDouble(3, v.getTotal());
            pstmt.executeUpdate();
            ctx.status(201).result("Venta registrada");
        } catch (SQLException e) { ctx.status(500).result("Error DB: Verifique que el cliente y pan existan."); }
    }

    public static void borrar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ventas WHERE id = ?")) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            ctx.status(204);
        } catch (SQLException e) { ctx.status(500).result("Error DB"); }
    }

    public static void actualizar(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Venta v = ctx.bodyAsClass(Venta.class);
        
        // --- VALIDACIÓN ---
        String error = validarVenta(v);
        if (error != null) {
            ctx.status(400).result(error);
            return;
        }

        try (Connection conn = Conexion.getConexion();
             PreparedStatement pstmt = conn.prepareStatement("UPDATE ventas SET id_cliente = ?, id_pan = ?, total = ? WHERE id = ?")) {
            pstmt.setInt(1, v.getIdCliente());
            pstmt.setInt(2, v.getIdPan());
            pstmt.setDouble(3, v.getTotal());
            pstmt.setInt(4, id);
            pstmt.executeUpdate();
            ctx.status(200).result("Venta corregida");
        } catch (SQLException e) { ctx.status(500).result("Error DB"); }
    }
}