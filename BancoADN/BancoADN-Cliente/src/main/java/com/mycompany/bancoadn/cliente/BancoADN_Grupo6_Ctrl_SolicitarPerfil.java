package com.mycompany.bancoadn.cliente;
 
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
 
/**
 * Controlador para la pantalla Solicitar Perfil.
 *
 * Responsabilidad:
 *   1. Tomar los datos del formulario
 *   2. Validarlos localmente
 *   3. Enviar solicitud al servidor
 *   4. Mostrar respuesta al usuario*/

public class BancoADN_Grupo6_Ctrl_SolicitarPerfil implements ActionListener {
 
    private BancoADN_Grupo6_Pant_SolicitarPerfil vista;
    private BancoADN_Grupo6_ClienteSocket clienteSocket;
    private String emailUsuario;
 
    public BancoADN_Grupo6_Ctrl_SolicitarPerfil(BancoADN_Grupo6_Pant_SolicitarPerfil vista, BancoADN_Grupo6_ClienteSocket clienteSocket, String emailUsuario) {
        this.vista = vista;
        this.clienteSocket = clienteSocket;
        this.emailUsuario = emailUsuario;
 
        this.vista.agregarListenerEnviarSolicitud(this);
        this.vista.agregarListenerVolver(e -> volverAlMenu());
    }
 
    @Override
    public void actionPerformed(ActionEvent e) {
        String nombrePerfil = vista.getNombrePerfil();
        String codigoSecuencia = vista.getCodigoSecuencia();
        String descripcion = vista.getDescripcion();
        String fechaMuestra = vista.getFechaMuestra();
 
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
 
        solicitarRegistrarPerfil(nombrePerfil, codigoSecuencia, descripcion, fechaMuestra);
    }
 
    /**
     * Envía la solicitud al servidor.
     * Formato: "SolicitarPerfil - email - nombre - codigo - descripcion - fecha"
     */
    private void solicitarRegistrarPerfil(String nombre, String codigo, String descripcion, String fecha) {
        if (!clienteSocket.estaConectado()) {
            vista.mostrarError("No hay conexión con el servidor.\nVerificá que el servidor esté activo.");
            return;
        }
 
        // Armar mensaje con formato " - "
        String mensaje = "CrearPerfilSol - " + emailUsuario + " - " + emailUsuario + " _ " + nombre + " _ " 
                       + codigo + " _ " + descripcion + " _ " + fecha;
 
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
 
        // El servidor debería responder con un estado de la solicitud
        // Por ahora esperamos un mensaje de confirmación
        if (respuesta.toLowerCase().contains("exito") || 
            respuesta.toLowerCase().contains("exitoso")) {
            vista.mostrarMensaje("¡Solicitud enviada exitosamente!\nEl administrador revisará tu solicitud.");
            vista.limpiarCampos();
            volverAlMenu();
        } else {
            vista.mostrarError("El servidor respondió:\n" + respuesta);
        }
    }
 
    private void volverAlMenu() {
        vista.dispose();
        BancoADN_Grupo6_MenuUsuario menuVista = new BancoADN_Grupo6_MenuUsuario();
        menuVista.setEmailUsuario(emailUsuario);  // Pasar el email
        menuVista.setNombreUsuario(emailUsuario.split("@")[0]);  // Usuario (parte antes del @)
        BancoADN_Grupo6_Ctrl_MenuUsuario CtrlMenuUsuario = new BancoADN_Grupo6_Ctrl_MenuUsuario(menuVista, clienteSocket, emailUsuario);
        menuVista.setVisible(true);
    }
 
    private boolean validarFormatoFecha(String fecha) {
        return fecha.matches("\\d{4}-\\d{2}-\\d{2}");
    }
}
