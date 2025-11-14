package com.panaderia;

import com.panaderia.controllers.*; // Importamos todos los controladores
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class App {
    public static void main(String[] args) {

        // 1. CONFIGURACIÓN DEL PUERTO DINÁMICO
        // Render nos da el puerto en una variable de entorno llamada "PORT".
        // Si esa variable existe, la usamos. Si no (estamos en tu PC), usamos el 7071.
        int puerto = 7071;
        if (System.getenv("PORT") != null) {
            puerto = Integer.parseInt(System.getenv("PORT"));
        }

        // 2. CREAR E INICIAR EL SERVIDOR
        Javalin app = Javalin.create(config -> {
            // Servir el Frontend (página web)
            config.staticFiles.add("/public", Location.CLASSPATH);

            // Habilitar CORS (Para que no bloqueen la conexión desde otros lados)
            config.bundledPlugins.enableCors(cors -> {
                cors.addRule(it -> {
                    it.anyHost();
                });
            });

        }).start(puerto); // <--- ¡Aquí usamos el puerto dinámico!

        System.out.println("--- PANADERÍA ARRANCANDO EN PUERTO " + puerto + " ---");

        // 3. RUTAS (ENDPOINTS)
        // Redireccionar la raíz al index.html
        app.get("/", ctx -> ctx.redirect("/index.html"));

        // --- RUTAS DE PANES ---
        app.get("/panes", PanController::listar);
        app.post("/panes", PanController::crear);
        app.put("/panes/{id}", PanController::actualizar);
        app.delete("/panes/{id}", PanController::borrar);

        // --- RUTAS DE CLIENTES ---
        app.get("/clientes", ClienteController::listar);
        app.post("/clientes", ClienteController::crear);
        app.put("/clientes/{id}", ClienteController::actualizar);
        app.delete("/clientes/{id}", ClienteController::borrar);

        // --- RUTAS DE VENTAS ---
        app.get("/ventas", VentaController::listar);
        app.post("/ventas", VentaController::crear);
        app.put("/ventas/{id}", VentaController::actualizar);
        app.delete("/ventas/{id}", VentaController::borrar);
    }
}