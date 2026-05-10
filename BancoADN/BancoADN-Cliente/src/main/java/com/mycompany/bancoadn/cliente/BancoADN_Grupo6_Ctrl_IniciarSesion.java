/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bancoadn.cliente;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**Controlador de la pantalla Iniciar Sesión.*/
public class BancoADN_Grupo6_Ctrl_IniciarSesion implements ActionListener {

    private BancoADN_Grupo6_Pant_IniciarSesion vista;
    private BancoADN_Grupo6_ClienteSocket clienteSocket;

    public BancoADN_Grupo6_Ctrl_IniciarSesion(BancoADN_Grupo6_Pant_IniciarSesion vista, BancoADN_Grupo6_ClienteSocket clienteSocket) {
        this.vista = vista;
        this.clienteSocket = clienteSocket;

        this.vista.agregarListenerBotonIniciarSesion(this);
        this.vista.agregarListenerRegistro(e -> abrirRegistro());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String email      = vista.getEmail().trim();
        String contraseña = vista.getContraseña();

        if (email.isEmpty() || contraseña.isEmpty()) {
            vista.mostrarError("Por favor completa todos los campos.");
            return;
        }

        if (!validarEmail(email)) {
            vista.mostrarError("El formato del email es inválido.\nEjemplo: usuario@mail.com");
            return;
        }

        iniciarSesion(email, contraseña);
    }

    /* Envía la solicitud al servidor.
     *
     *
     * RequestHandler parsea:
     *   Partes[0] = "IniciarS"
     *   Partes[1] = email
     *   Partes[2] = contraseña
     *
     * Y responde simplemente: writer.println(resultado) → "true" o "false"*/
    private void iniciarSesion(String email, String contraseña) {
        if (!clienteSocket.estaConectado()) {
            vista.mostrarError("No hay conexión con el servidor.\nVerificá que el servidor esté activo.");
            return;
        }

        String mensaje = "IniciarS - " + email + " - " + contraseña;

        String respuesta = clienteSocket.enviarYRecibir(mensaje);

        procesarRespuesta(respuesta, email);
    }

    private void procesarRespuesta(String respuesta, String email) {
        if (respuesta == null) {
            vista.mostrarError("No se recibió respuesta del servidor.\nIntentá de nuevo.");
            return;
        }
        else{
            // Convertimos el texto "0", "1" o "-1" a un número real
            int codigo = Integer.parseInt(respuesta.trim());
            switch (codigo) {
            case 1: // ADMINISTRADOR
                vista.limpiarCampos();
                vista.dispose();
                BancoADN_Grupo6_MenuAdmin menuAdmin = new BancoADN_Grupo6_MenuAdmin();
                menuAdmin.setEmailAdmin(email);
                menuAdmin.setNombreAdmin(email.split("@")[0]);
                BancoADN_Grupo6_Ctrl_MenuAdmin ctrlMenuAdmin = new BancoADN_Grupo6_Ctrl_MenuAdmin(menuAdmin, clienteSocket, email);
                menuAdmin.setVisible(true);
                break;
            case 0: // USUARIO NORMAL
                vista.limpiarCampos();
                vista.dispose();
                // Abrimos el prototipo de MenuUsuario
                BancoADN_Grupo6_MenuUsuario menu = new BancoADN_Grupo6_MenuUsuario();
                menu.setEmailUsuario(email);  // Pasar el email
                menu.setNombreUsuario(email.split("@")[0]);  // Usuario (parte antes del @)
                BancoADN_Grupo6_Ctrl_MenuUsuario crtlMenuUsuario = new BancoADN_Grupo6_Ctrl_MenuUsuario(menu, clienteSocket, email);
                menu.setVisible(true);
                break;

            case -1: // NO ENCONTRADO O CONTRASEÑA MAL
                vista.mostrarError("Email o contraseña incorrectos.");
                vista.limpiarCampos();
                break;
            }
        }
    }

    private void abrirRegistro() {
        vista.dispose();
        BancoADN_Grupo6_Pant_CrearCuenta vistaRegistro = new BancoADN_Grupo6_Pant_CrearCuenta();
        BancoADN_Grupo6_Ctrl_CrearCuenta ctrlRegistro = new BancoADN_Grupo6_Ctrl_CrearCuenta(vistaRegistro, clienteSocket);
        vistaRegistro.setVisible(true);
    }

    private boolean validarEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}