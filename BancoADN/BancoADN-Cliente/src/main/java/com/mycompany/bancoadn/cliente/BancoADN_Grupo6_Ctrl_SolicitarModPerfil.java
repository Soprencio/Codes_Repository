/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bancoadn.cliente;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
/**
 * Controlador para solicitar modificación de un perfil genético.
 *
 *   Solicitud → "CrearSolPer - emailUsuario - emailUsuario _ nombre _ codigo _ descripcion _ fecha - modificar"
 *
 *   Ejemplo real:
 *   "CrearSolPer - usuario@mail.com - usuario@mail.com _ Juan Pérez _ ATCG123456 _ Perfil actualizado _ 2026-04-23 - modificar"
 *
 *   IMPORTANTE:
 *   - El separador principal es " - " (espacio-guión-espacio)
 *   - El tipo de solicitud (modificar) va al final
 */
public class BancoADN_Grupo6_Ctrl_SolicitarModPerfil implements ActionListener {
 
    private BancoADN_Grupo6_Pant_SolicitarModPerfil vista;
    private BancoADN_Grupo6_ClienteSocket clienteSocket;
    private String emailUsuario;
 
    public BancoADN_Grupo6_Ctrl_SolicitarModPerfil(BancoADN_Grupo6_Pant_SolicitarModPerfil vista, BancoADN_Grupo6_ClienteSocket clienteSocket, String emailUsuario) {
        this.vista = vista;
        this.clienteSocket = clienteSocket;
        this.emailUsuario = emailUsuario;
 
        this.vista.agregarListenerEnviarSolicitud(this);
        this.vista.agregarListenerVolver(e -> volverAlMenu());
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        String nombrePerfil    = vista.getNombrePerfil();
        String codigoSecuencia = vista.getCodigoSecuencia();
        String descripcion     = vista.getDescripcion();
        String fechaMuestra    = vista.getFechaMuestra();
 
        // --- VALIDACIONES LOCALES ---
        if (nombrePerfil.isEmpty()) {
            vista.mostrarError("El nombre del perfil es obligatorio.");
            return;
        }
 
        if (codigoSecuencia.isEmpty()) {
            vista.mostrarError("El código de secuencia es obligatorio.");
            return;
        }
 
        if (descripcion.isEmpty()) {
            vista.mostrarError("La descripción es obligatoria.");
            return;
        }
 
        if (fechaMuestra.isEmpty()) {
            vista.mostrarError("La fecha de muestra es obligatoria (formato: yyyy-MM-dd).");
            return;
        }
 
        if (nombrePerfil.length() < 3) {
            vista.mostrarError("El nombre del perfil debe tener al menos 3 caracteres.");
            return;
        }
 
        if (codigoSecuencia.length() < 3) {
            vista.mostrarError("El código de secuencia debe tener al menos 3 caracteres.");
            return;
        }
 
        if (!validarFormatoFecha(fechaMuestra)) {
            vista.mostrarError("La fecha debe estar en formato yyyy-MM-dd (Ej: 2026-04-23).");
            return;
        }
 
        solicitarModificarPerfil(nombrePerfil, codigoSecuencia, descripcion, fechaMuestra);
    }
 
    /**
     * Envía la solicitud de modificación al servidor.
     *
     * Parseado así:
     *   Partes[0] = "CrearSolPer"
     *   Partes[1] = emailUsuario
     *   Partes[2] = "emailUsuario _ nombre _ codigo _ descripcion _ fecha"
     *   Partes[3] = "modificar"
     */
    private void solicitarModificarPerfil(String nombre, String codigo, String descripcion, String fecha) {
        if (!clienteSocket.estaConectado()) {
            vista.mostrarError("No hay conexión con el servidor.\nVerificá que el servidor esté activo.");
            return;
        }
        
        String datosModificacion = emailUsuario + " _ " + nombre + " _ " + codigo + " _ " + descripcion + " _ " + fecha;
        String mensaje = "CrearSolPer - " + emailUsuario + " - " + datosModificacion + " - modificar";
 
        System.out.println("Enviando modificación: " + mensaje);  // Debug
 
        String respuesta = clienteSocket.enviarYRecibir(mensaje);
 
        procesarRespuesta(respuesta);
    }
 
    /**
     * Interpreta la respuesta del servidor.
     */
    private void procesarRespuesta(String respuesta) {
        if (respuesta == null) {
            vista.mostrarError("No se recibió respuesta del servidor.\nIntentá de nuevo.");
            return;
        }
 
        System.out.println("Respuesta del servidor: " + respuesta);  // Debug
 
        // Verificar si la respuesta indica éxito
        if (respuesta.toLowerCase().contains("exito") ||
            respuesta.toLowerCase().contains("registrada") ||
            respuesta.toLowerCase().contains("completado") ||
            respuesta.toLowerCase().contains("modificada")) {
 
            vista.mostrarMensaje("¡Solicitud de modificación enviada exitosamente!\n"
                    + "El administrador revisará tu solicitud.");
            vista.limpiarCampos();
            volverAlMenu();
 
        } else if (respuesta.toLowerCase().contains("error")) {
            vista.mostrarError("Error del servidor:\n" + respuesta);
 
        } else {
            vista.mostrarMensaje("Servidor respondió:\n" + respuesta);
        }
    }
 
    private void volverAlMenu() {
        vista.dispose();
        BancoADN_Grupo6_MenuUsuario menu = new BancoADN_Grupo6_MenuUsuario();
        menu.setEmailUsuario(emailUsuario);
        menu.setNombreUsuario(emailUsuario.split("@")[0]);
        BancoADN_Grupo6_Ctrl_MenuUsuario ctrlMenu = new BancoADN_Grupo6_Ctrl_MenuUsuario(menu, clienteSocket, emailUsuario);
        menu.setVisible(true);
    }
 
    private boolean validarFormatoFecha(String fecha) {
        return fecha.matches("\\d{4}-\\d{2}-\\d{2}");
    }
}
