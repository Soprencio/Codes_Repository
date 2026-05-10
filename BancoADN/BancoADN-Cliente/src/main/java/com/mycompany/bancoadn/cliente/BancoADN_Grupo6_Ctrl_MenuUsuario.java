package com.mycompany.bancoadn.cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class BancoADN_Grupo6_Ctrl_MenuUsuario {

    private BancoADN_Grupo6_MenuUsuario vista;
    private BancoADN_Grupo6_ClienteSocket clienteSocket;
    private String emailUsuario;  // Email del usuario logueado
    
    // Estados posibles del perfil
    private static final String ESTADO_ACTIVO = "ACTIVO";
    private static final String ESTADO_INACTIVO = "INACTIVO";
    private static final String ESTADO_SIN_PERFIL = "SIN_PERFIL";

    public BancoADN_Grupo6_Ctrl_MenuUsuario(BancoADN_Grupo6_MenuUsuario vista, BancoADN_Grupo6_ClienteSocket clienteSocket, String emailUsuario) {
        this.vista = vista;
        this.clienteSocket = clienteSocket;
        this.emailUsuario = emailUsuario;
        initListeners();
    }

    private void initListeners() {
        vista.agregarListenerBuscar(e -> manejarBuscar());
        vista.agregarListenerSolicitarPerfil(e -> manejarSolicitarPerfil());
        vista.agregarListenerSolicitarModificacion(e -> manejarSolicitarModificacion());
        vista.agregarListenerSolicitarDesactivar(e -> manejarSolicitarDesactivar());
        vista.agregarListenerSolicitarReactivar(e -> manejarSolicitarReactivar());
        vista.agregarListenerCerrarSesion(e -> manejarCerrarSesion());
        vista.agregarListenerVerPerfil(e -> manejarVerPerfil());
    }
    private String obtenerEstadoPerfil() {
    try {
        if (!clienteSocket.estaConectado()) return null;

        String comando = "BuscarDat - " + this.emailUsuario;
        String respuesta = clienteSocket.enviarYRecibir(comando);

        if (respuesta == null || respuesta.isEmpty() || 
            respuesta.equals("No se encontro el perfil")) {
            return ESTADO_SIN_PERFIL;
        }

        // El formato recibido es: nombre - codigo - descripcion - fecha - ESTADO
        // El estado es el quinto elemento (índice 4)
        String[] partes = respuesta.split(" - ");
        
        if (partes.length >= 5) {
            String estadoStr = partes[4].trim();
            
            // Verificamos si el estado es Activo (1 o "ACTIVO")
            if (estadoStr.equals("1") || estadoStr.equalsIgnoreCase("ACTIVO")) {
                return ESTADO_ACTIVO;
            } else {
                return ESTADO_INACTIVO;
            }
        }
        // Si por alguna razón la trama no tiene el formato esperado
        return ESTADO_SIN_PERFIL;
    } catch (Exception e) {
        System.err.println("Error al verificar estado del perfil: " + e.getMessage());
        return null;
        }
    }

    //Buscar otros perfiles
    private void manejarBuscar() {
        BancoADN_Grupo6_Pant_BuscarPerfil vistaBuscarPerfil = new BancoADN_Grupo6_Pant_BuscarPerfil();
        BancoADN_Grupo6_Ctrl_BuscarPerfil ctrlBuscarPeril = new BancoADN_Grupo6_Ctrl_BuscarPerfil(vistaBuscarPerfil, clienteSocket, emailUsuario);
        vistaBuscarPerfil.setVisible(true);
        vista.dispose();
    }

    //Registrar Perfil
    private void manejarSolicitarPerfil() {
        // Verificar si ya tiene un perfil
        String estado = obtenerEstadoPerfil();
        if (estado == null) {
            vista.mostrarError("No se pudo verificar el estado con el servidor.");
            return;
        }
 
        if (!estado.equals(ESTADO_SIN_PERFIL)) {
            vista.mostrarError("Ya tienes un perfil vinculado a tu cuenta.\n"
                    + "No puedes solicitar un nuevo perfil.");
            return;
        }
        // Si pasa la validación, abrir la pantalla
        BancoADN_Grupo6_Pant_SolicitarPerfil vistaSolicitarPerfil = new BancoADN_Grupo6_Pant_SolicitarPerfil();
        BancoADN_Grupo6_Ctrl_SolicitarPerfil ctrlSolicitarPerfil = new BancoADN_Grupo6_Ctrl_SolicitarPerfil( vistaSolicitarPerfil, clienteSocket, emailUsuario);
        vistaSolicitarPerfil.setVisible(true);
        vista.dispose();
    }
    private void manejarVerPerfil() {
        String comando = "BuscarDat - " + this.emailUsuario;
        String respuesta = clienteSocket.enviarYRecibir(comando);

        if (respuesta == null || respuesta.isEmpty()) {
            vista.mostrarError("Sin respuesta del servidor.");
            return;
        }

        if (respuesta.equals("No se encontro el perfil")) {
            vista.mostrarMensaje("No tienes un perfil registrado. Usa el botón 'Solicitar Perfil' de la izquierda.");
            vista.limpiarPerfil();
        } else {
            String[] datos = respuesta.split(" - ");
            if (datos.length >= 4) {
                String estado = obtenerEstadoPerfil();
                if (estado == null) {
                    estado = "ACTIVO";  // Por defecto
                }
                vista.mostrarPerfil(
                    datos[0],           // Nombre
                    datos[1],           // ADN
                    datos[2],           // Descripción
                    estado,       // Estado(manual)
                    datos[3]            // Fecha 
                );
            }
        }
    }

    //Solicitar Modificación Perfil
    private void manejarSolicitarModificacion() {
        // Verificar si tiene un perfil activo
        String estado = obtenerEstadoPerfil();
 
        if (estado == null) {
            vista.mostrarError("No se pudo verificar el estado con el servidor.");
            return;
        }
 
        if (estado.equals(ESTADO_SIN_PERFIL)) {
            vista.mostrarError("No tienes un perfil registrado.\n"
                    + "Primero debes solicitar un perfil para poder modificarlo.");
            return;
        }
 
        if (estado.equals(ESTADO_INACTIVO)) {
            vista.mostrarError("Tu perfil está desactivado.\n"
                    + "Debes restaurarlo antes de poder modificarlo.");
            return;
        }
        // Si pasa la validación, abrir la pantalla
        BancoADN_Grupo6_Pant_SolicitarModPerfil vistaSolicitarModPerfil = new BancoADN_Grupo6_Pant_SolicitarModPerfil();
        BancoADN_Grupo6_Ctrl_SolicitarModPerfil ctrlSolicitarModPerfil = new BancoADN_Grupo6_Ctrl_SolicitarModPerfil(
            vistaSolicitarModPerfil,
            clienteSocket,
            emailUsuario
        );
        vistaSolicitarModPerfil.setVisible(true);
        vista.dispose();
    }

    //Solicitar Desactivar Perfil
    private void manejarSolicitarDesactivar() {
        // Verificar si el perfil está activo
        String estado = obtenerEstadoPerfil();
 
        if (estado == null) {
            vista.mostrarError("No se pudo verificar el estado con el servidor.");
            return;
        }
 
        if (estado.equals(ESTADO_SIN_PERFIL)) {
            vista.mostrarError("No tienes un perfil registrado.\n"
                    + "No hay nada que desactivar.");
            return;
        }
 
        if (estado.equals(ESTADO_INACTIVO)) {
            vista.mostrarError("Tu perfil ya está desactivado.\n"
                    + "Si deseas reactivarlo, usa el botón 'Solicitar Reactivar Perfil'.");
            return;
        }
        // Si pasa la validación, solicitar confirmación
        int confirm = JOptionPane.showConfirmDialog(vista, 
            "¿Estás seguro de que deseas solicitar la desactivación de tu perfil?", 
            "Confirmar Solicitud", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (!clienteSocket.estaConectado()) {
                vista.mostrarError("No hay conexión con el servidor.\nVerificá que el servidor esté activo.");
                return;
            }
            
            String mensaje = "CrearSolPer - " + emailUsuario + " -   - baja";
 
            String respuesta = clienteSocket.enviarYRecibir(mensaje);
 
            procesarRespuestaDesactivar(respuesta);
        }
    }
    
    private void procesarRespuestaDesactivar(String respuesta) {
        
        if (respuesta == null) {
            vista.mostrarError("No se recibió respuesta del servidor.\nIntentá de nuevo.");
            return;
        }
 
        System.out.println("Respuesta del servidor (desactivación): " + respuesta);  // Debug
 
        // Verificar si la respuesta indica éxito
        if (respuesta.toLowerCase().contains("exito") ||
            respuesta.toLowerCase().contains("registrada") ||
            respuesta.toLowerCase().contains("completado") ||
            respuesta.toLowerCase().contains("solicitada")) {
 
            vista.mostrarMensaje("¡Solicitud de desactivación enviada exitosamente!\n"
                    + "El administrador procesará tu solicitud.");
            vista.limpiarPerfil();
 
        } else if (respuesta.toLowerCase().contains("error")) {
            vista.mostrarError("Error del servidor:\n" + respuesta);
 
        } else {
            vista.mostrarMensaje("Servidor respondió:\n" + respuesta);
        }
    }    

    //Solicitar Reactivar
    private void manejarSolicitarReactivar() {
        // Verificar si el perfil está inactivo
        String estado = obtenerEstadoPerfil();
 
        if (estado == null) {
            vista.mostrarError("No se pudo verificar el estado con el servidor.");
            return;
        }
 
        if (estado.equals(ESTADO_SIN_PERFIL)) {
            vista.mostrarError("No tienes un perfil registrado.\n"
                    + "Usa el botón 'Solicitar Perfil' para crear uno.");
            return;
        }
 
        if (estado.equals(ESTADO_ACTIVO)) {
            vista.mostrarError("Tu perfil ya está activo.\n"
                    + "Si deseas desactivarlo, usa el botón 'Solicitar Desactivar Perfil'.");
            return;
        }
        
        // Si pasa la validación, solicitar confirmación
        int confirm = JOptionPane.showConfirmDialog(vista, 
            "¿Estás seguro de que deseas solicitar la desactivación de tu perfil?", 
            "Confirmar Solicitud", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (!clienteSocket.estaConectado()) {
                vista.mostrarError("No hay conexión con el servidor.\nVerificá que el servidor esté activo.");
                return;
            }
 
            // Armar mensaje (pendiente de confirmar formato exacto)
            String mensaje = "CrearSolPer - " + emailUsuario + " -   - restaurar";
 
            System.out.println("Enviando solicitud de reactivación: " + mensaje);  // Debug
 
            String respuesta = clienteSocket.enviarYRecibir(mensaje);
 
            procesarRespuestaReactivar(respuesta);
        }       
    }
    private void procesarRespuestaReactivar(String respuesta) {
        if (respuesta == null) {
            vista.mostrarError("No se recibió respuesta del servidor.\nIntentá de nuevo.");
            return;
        }
 
        System.out.println("Respuesta del servidor (reactivación): " + respuesta);  // Debug
 
        // Verificar si la respuesta indica éxito
        if (respuesta.toLowerCase().contains("exito") ||
            respuesta.toLowerCase().contains("registrada") ||
            respuesta.toLowerCase().contains("completado") ||
            respuesta.toLowerCase().contains("reactivada")) {
 
            vista.mostrarMensaje("¡Solicitud de reactivación enviada exitosamente!\n"
                    + "El administrador procesará tu solicitud.");
 
        } else if (respuesta.toLowerCase().contains("error")) {
            vista.mostrarError("Error del servidor:\n" + respuesta);
 
        } else {
            vista.mostrarMensaje("Servidor respondió:\n" + respuesta);
        }
    }    
    
    
    
    //Cerrar Sesión
    private void manejarCerrarSesion() {
        int opc = JOptionPane.showConfirmDialog(vista, "¿Deseas cerrar sesión?", "Salir", JOptionPane.YES_NO_OPTION);
        if (opc == JOptionPane.YES_OPTION) {
            vista.dispose();
            // Volvemos al login
            BancoADN_Grupo6_Pant_IniciarSesion loginVista = new BancoADN_Grupo6_Pant_IniciarSesion();
            BancoADN_Grupo6_Ctrl_IniciarSesion CtrlIniciarSesion = new BancoADN_Grupo6_Ctrl_IniciarSesion(loginVista, clienteSocket);
            loginVista.setVisible(true);
        }
    }
}