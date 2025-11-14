package com.panaderia;

import com.panaderia.controllers.*; // Importamos todos los controladores
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class Main {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        }).start(7071);

        System.out.println("--- PANADERÍA PRO CONECTADA A POSTGRES ---");

        // RUTAS DE PANES
        app.get("/panes", PanController::listar);
        app.post("/panes", PanController::crear);
        app.put("/panes/{id}", PanController::actualizar); // <-- Nuevo PUT
        app.delete("/panes/{id}", PanController::borrar);

        // RUTAS DE CLIENTES
        app.get("/clientes", ClienteController::listar);
        app.post("/clientes", ClienteController::crear);
        app.put("/clientes/{id}", ClienteController::actualizar);
        app.delete("/clientes/{id}", ClienteController::borrar);

        // RUTAS DE VENTAS
        app.get("/ventas", VentaController::listar);
        app.post("/ventas", VentaController::crear);
        app.put("/ventas/{id}", VentaController::actualizar);
        app.delete("/ventas/{id}", VentaController::borrar);
    }
}