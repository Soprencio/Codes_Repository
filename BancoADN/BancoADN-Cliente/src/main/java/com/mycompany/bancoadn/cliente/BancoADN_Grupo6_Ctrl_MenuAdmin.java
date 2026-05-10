package com.mycompany.bancoadn.cliente;

import javax.swing.JOptionPane;
import java.util.List;


public class BancoADN_Grupo6_Ctrl_MenuAdmin {

    private final BancoADN_Grupo6_MenuAdmin     vista;
    private final BancoADN_Grupo6_ClienteSocket clienteSocket;
    private final String        emailAdmin;

    public BancoADN_Grupo6_Ctrl_MenuAdmin(BancoADN_Grupo6_MenuAdmin vista, BancoADN_Grupo6_ClienteSocket clienteSocket, String emailAdmin) {
        this.vista         = vista;
        this.clienteSocket = clienteSocket;
        this.emailAdmin    = emailAdmin;

        initListeners();
        cargarSolicitudes();
    }

    private void initListeners() {
        vista.agregarListenerActualizar(e   -> cargarSolicitudes());
        vista.agregarListenerCerrarSesion(e -> manejarCerrarSesion());

        // ── Nuevos botones ───────────────────────────────
        vista.agregarListenerUltimasSolicitudes(e  -> abrirUltimasSolicitudes());
        vista.agregarListenerAdministrarPerfiles(e -> abrirAdministrarPerfiles());
    }

    // ── Últimas solicitudes resueltas ─────────────────────
    private void abrirUltimasSolicitudes() {
        BancoADN_Grupo6_Pant_UltimasSolicitudes pantallaUlt = new BancoADN_Grupo6_Pant_UltimasSolicitudes();
        new BancoADN_Grupo6_Ctrl_UltimasSolicitudes(pantallaUlt, clienteSocket);
        pantallaUlt.setVisible(true);
    }

    // ── Administrar perfiles ──────────────────────────────
    private void abrirAdministrarPerfiles() {
        BancoADN_Grupo6_Pant_AdminPerfiles pantallaPerf = new BancoADN_Grupo6_Pant_AdminPerfiles();
        new BancoADN_Grupo6_Ctrl_AdminPerfiles(pantallaPerf, clienteSocket, emailAdmin);
        pantallaPerf.setVisible(true);
    }

    // ── Cargar y mostrar solicitudes pendientes ───────────
    private void cargarSolicitudes() {
        vista.limpiarSolicitudes();

        if (!clienteSocket.estaConectado()) {
            vista.mostrarError("No hay conexión con el servidor.");
            return;
        }

        List<String> lineas = clienteSocket.enviarYSolicitarLista("ListaSol");

        if (lineas == null) {
            vista.mostrarError("Error al conectar con el servidor.");
            return;
        }

        int pendientes = 0;

        for (String linea : lineas) {

            String[] p = linea.split(" - ", -1);

            if (p.length < 9) {
                System.err.println("Línea malformada, se ignora: " + linea);
                continue;
            }

            int    idSolicitud   = parsearInt(p[0]);
            String tipo          = p[1].trim();
            int    estado        = parsearInt(p[2]);
            String datos         = p[3].trim();
            int    idPerfil      = parsearInt(p[6]);
            String fechaCreacion = p[7].trim();

            // Solo pendientes (estado == 0)
            if (estado != 0) continue;

            String[] tokens = datos.split(" _ ", -1);

            String mailMostrar;
            String lineaExtra;
            if (tipo.equals("registrar") || tipo.equals("modificar")) {
 
                mailMostrar = tokens.length > 0 ? tokens[0].trim() : "—";
                lineaExtra  = tokens.length > 3 ? tokens[3].trim() : "—";
            } else {

                String emailTitular = obtenerEmailPorPerfil(idPerfil);
                mailMostrar = emailTitular;
                lineaExtra  = emailTitular;
            }

            pendientes++;
            final int idCapturado = idSolicitud;

            vista.agregarTarjetaSolicitud(
                idSolicitud,
                mailMostrar,
                fechaCreacion,
                tipo,
                lineaExtra,
                e -> manejarResolver(idCapturado, 1),
                e -> manejarResolver(idCapturado, 2)
            );
        }

        if (pendientes == 0) {
            vista.mostrarSinSolicitudes();
        }
    }

    // ── Obtener mail del dueño de un perfil ───────────────
    private String obtenerEmailPorPerfil(int idPerfil) {
        if (idPerfil <= 0) return "—";
        String mensaje = "EmailPorPerfil - " + idPerfil;
        String respuesta = clienteSocket.enviarYRecibir(mensaje);
        if (respuesta == null || respuesta.isBlank()) return "—";
        return respuesta.trim();
    }

    // ── Aprobar o rechazar una solicitud ──────────────────
    private void manejarResolver(int idSolicitud, int estadoNuevo) {
        String pregunta = (estadoNuevo == 1)
            ? "¿Confirmás la APROBACIÓN de la solicitud #" + idSolicitud + "?"
            : "¿Confirmás el RECHAZO de la solicitud #"   + idSolicitud + "?";

        if (!vista.confirmar(pregunta)) return;

        if (!clienteSocket.estaConectado()) {
            vista.mostrarError("No hay conexión con el servidor.");
            return;
        }

        String mensaje = "ResSol - " + idSolicitud + " - " + estadoNuevo + " - " + emailAdmin;
        String respuesta = clienteSocket.enviarYRecibir(mensaje);

        if (respuesta == null) {
            vista.mostrarError("Sin respuesta del servidor.\nIntentá de nuevo.");
            return;
        }

        switch (respuesta.trim()) {
            case "Aceptado con exito":
                vista.mostrarMensaje("✓ Solicitud #" + idSolicitud + " aprobada correctamente.");
                break;
            case "rechazado con exito":
                vista.mostrarMensaje("Solicitud #" + idSolicitud + " rechazada.");
                break;
            case "No existe una solicitud con ese id":
                vista.mostrarError("No se encontró la solicitud #" + idSolicitud + ".");
                break;
            default:
                vista.mostrarError("Respuesta inesperada del servidor:\n" + respuesta);
                break;
        }

        cargarSolicitudes();
    }

    // ── Cerrar sesión ─────────────────────────────────────
    private void manejarCerrarSesion() {
        int opc = JOptionPane.showConfirmDialog(vista,
            "¿Deseas cerrar sesión?", "Salir", JOptionPane.YES_NO_OPTION);
        if (opc == JOptionPane.YES_OPTION) {
            vista.dispose();
            BancoADN_Grupo6_Pant_IniciarSesion loginVista = new BancoADN_Grupo6_Pant_IniciarSesion();
            new BancoADN_Grupo6_Ctrl_IniciarSesion(loginVista, clienteSocket);
            loginVista.setVisible(true);
        }
    }

    // ── Utilidades ────────────────────────────────────────
    private int parsearInt(String s) {
        try   { return Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { return -1; }
    }
}
