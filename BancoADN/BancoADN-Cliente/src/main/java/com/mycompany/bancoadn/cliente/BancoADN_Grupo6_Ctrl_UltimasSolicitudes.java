package com.mycompany.bancoadn.cliente;

import java.util.List;


public class BancoADN_Grupo6_Ctrl_UltimasSolicitudes {

    private final BancoADN_Grupo6_Pant_UltimasSolicitudes vista;
    private final BancoADN_Grupo6_ClienteSocket           clienteSocket;

    public BancoADN_Grupo6_Ctrl_UltimasSolicitudes(BancoADN_Grupo6_Pant_UltimasSolicitudes vista, BancoADN_Grupo6_ClienteSocket clienteSocket) {
        this.vista         = vista;
        this.clienteSocket = clienteSocket;

        vista.agregarListenerCerrar(e -> vista.dispose());
        cargarUltimasSolicitudes();
    }

    private void cargarUltimasSolicitudes() {
        vista.limpiarSolicitudes();

        if (!clienteSocket.estaConectado()) {
            vista.mostrarError("No hay conexión con el servidor.");
            return;
        }

        List<String> lineas = clienteSocket.enviarYSolicitarLista("UltSol");

        if (lineas == null) {
            vista.mostrarError("Error al conectar con el servidor.");
            return;
        }

        if (lineas.isEmpty()) {
            vista.mostrarSinSolicitudes();
            return;
        }

        int mostradas = 0;

        for (String linea : lineas) {
            String[] p = linea.split(" - ", -1);
            if (p.length < 9) {
                System.err.println("Línea malformada (UltSol), ignorada: " + linea);
                continue;
            }

            int    estado        = parsearInt(p[2]);
            String datos         = p[3].trim();
            int    idPerfil      = parsearInt(p[6]);
            String fechaCreacion  = p[7].trim();
            String fechaResolucion = p[8].trim();
            String tipo          = p[1].trim();

            // Solo resueltas (estado 1 o 2)
            if (estado == 0) continue;

            String[] tokens = datos.split(" _ ", -1);

            String mailMostrar;
            String lineaExtra;
            if (tipo.equals("registrar") || tipo.equals("modificar")) {
                mailMostrar = tokens.length > 0 ? tokens[0].trim() : "—";
                lineaExtra  = tokens.length > 3 ? tokens[3].trim() : "—";
            } else {
                // baja/restaurar: el email del titular se obtiene por idPerfil
                // y se muestra en línea 1 Y línea 4
                String emailTitular = obtenerEmailPorPerfil(idPerfil);
                mailMostrar = emailTitular;
                lineaExtra  = emailTitular;
            }

            String estadoTexto = (estado == 1) ? "✓ Aceptada" : "✗ Rechazada";
            java.awt.Color estadoColor = (estado == 1)
                ? vista.getColorAceptada()
                : vista.getColorRechazada();

            vista.agregarTarjetaSolicitud(
                parsearInt(p[0]),
                mailMostrar,
                fechaCreacion,
                fechaResolucion,
                tipo,
                estadoTexto,
                estadoColor,
                lineaExtra
            );
            mostradas++;
        }

        if (mostradas == 0) {
            vista.mostrarSinSolicitudes();
        }
    }

    private String obtenerEmailPorPerfil(int idPerfil) {
        if (idPerfil <= 0) return "—";
        String respuesta = clienteSocket.enviarYRecibir("EmailPorPerfil - " + idPerfil);
        return (respuesta == null || respuesta.isBlank()) ? "—" : respuesta.trim();
    }

    private int parsearInt(String s) {
        try   { return Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { return -1; }
    }
}
