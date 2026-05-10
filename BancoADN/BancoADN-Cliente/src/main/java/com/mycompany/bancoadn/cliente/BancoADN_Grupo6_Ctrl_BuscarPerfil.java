/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bancoadn.cliente;
 
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
 
/**
 * Controlador para la pantalla de Buscar Perfil. 
 * Responsabilidades:
 *   1. Gestionar búsquedas por ID o Nombre
 *   2. Filtrar perfiles inactivos (estado = 0)
 *   3. Mostrar detalles del perfil al hacer click en "Ver Perfil"
 *   4. Navegar de vuelta al menú
 *
 * Protocolo servidor:
 *   BuscarIDNOM - NULL - nombre
 *   BuscarIDNOM - id - NULL
 *   Respuesta: idPerfil - nombreCompleto - códigoSecuencia - descripción - estado - fechaMuestra - email
 *   Termina con: FINISH
 */
public class BancoADN_Grupo6_Ctrl_BuscarPerfil {
 
    private BancoADN_Grupo6_Pant_BuscarPerfil vista;
    private BancoADN_Grupo6_ClienteSocket clienteSocket;
    private String emailUsuario;
 
    public BancoADN_Grupo6_Ctrl_BuscarPerfil(BancoADN_Grupo6_Pant_BuscarPerfil vista, BancoADN_Grupo6_ClienteSocket clienteSocket, String emailUsuario) {
        this.vista = vista;
        this.clienteSocket = clienteSocket;
        this.emailUsuario = emailUsuario;
        this.vista.limpiarResultados();
        initListeners();
    }
 
    private void initListeners() {
        this.vista.btnAtras.addActionListener(e -> manejarVolver());
        this.vista.btnBuscar.addActionListener(e -> manejarBuscar());
    }
 
    // ── VOLVER AL MENÚ ───
    private void manejarVolver() {
        vista.dispose();
        BancoADN_Grupo6_MenuUsuario menuVista = new BancoADN_Grupo6_MenuUsuario();
        menuVista.setEmailUsuario(emailUsuario);
        menuVista.setNombreUsuario(emailUsuario.split("@")[0]);
        BancoADN_Grupo6_Ctrl_MenuUsuario ctrlMenuUsuario = new BancoADN_Grupo6_Ctrl_MenuUsuario(menuVista, clienteSocket, emailUsuario);
        menuVista.setVisible(true);
    }
 
    // ── BUSCAR PERFILES ───
    private void manejarBuscar() {
        // Obtener datos de la vista
        String tipoFiltro = vista.getTipoFiltro();
        String texto = vista.getTextoBusqueda().trim();
 
        if (texto.isEmpty() || texto.equals("Buscar por ID / Nombre")) {
            vista.mostrarError("Por favor ingresa un ID o un Nombre para buscar.");
            return;
        }
 
        // Armar mensaje según filtro
        String mensaje = "";
        if (tipoFiltro.equals("Nombre")) {
            // "BuscarIDNOM - NULL - [NombreCompleto]"
            mensaje = "BuscarIDNOM - NULL - " + texto;
        } else if (tipoFiltro.equals("ID")) {
            // VERIFICACIÓN: Solo permite números si el filtro es ID
            if (!texto.matches("\\d+")) {
                vista.mostrarError("El ID de perfil debe ser un valor numérico.");
                return;
            }
            // "BuscarIDNOM - [idPerfil] - NULL"
            mensaje = "BuscarIDNOM - " + texto + " - NULL";
        }
 
        vista.limpiarResultados();
 
        // Enviar solicitud y recibir lista de resultados
        List<String> respuestas = clienteSocket.enviarYSolicitarLista(mensaje);
        procesarRespuesta(respuestas);
    }
 
    /**
     * Procesa la respuesta del servidor.
     * 
     * Formato esperado por línea:
     *   idPerfil - nombreCompleto - códigoSecuencia - descripción - estado - fechaMuestra - email
     * 
     * FILTRADO:
     *   - Solo muestra perfiles ACTIVOS (estado = 1)
     *   - Ignora perfiles INACTIVOS (estado = 0)
     */
    private void procesarRespuesta(List<String> respuestas) {
        if (respuestas == null) {
            vista.mostrarError("Error al conectar con el servidor.\nVerificá que el servidor esté activo.");
            return;
        }
 
        if (respuestas.isEmpty()) {
            vista.mostrarError("No se encontraron perfiles que coincidan con la búsqueda.");
            return;
        }
 
        int perfilesActivos = 0;
 
        for (String linea : respuestas) {
            // Saltar línea FINISH
            if (linea.equals("FINISH")) {
                continue;
            }
 
            String[] partes = linea.split(" - ");
 
            // Manejo de errores
            if (linea.equals("No se encontro el perfil") || 
                (partes.length > 0 && partes[0].equals("Comando Invalido bot"))) {
                vista.mostrarError(linea);
                vista.limpiarResultados();
                return;
            }
 
            // Validar que la línea tenga todos los campos
            // Formato: idPerfil(0) - nombreCompleto(1) - códigoSecuencia(2) - descripción(3) - estado(4) - fechaMuestra(5) - email(6)
            if (partes.length >= 6) {
                String idPerfil = partes[0].trim();
                String nombreCompleto = partes[1].trim();
                String codigoSecuencia = partes[2].trim();
                String descripcion = partes[3].trim();
                String estado = partes[4].trim();
                String fechaMuestra = partes[5].trim();
 
                // FILTRO: Solo agregar si está ACTIVO (estado = 1)
                if (estado.equals("1")) {
                    vista.agregarResultado(idPerfil, nombreCompleto, codigoSecuencia, descripcion, fechaMuestra);
                    perfilesActivos++;
                }
                // Si estado = 0 (INACTIVO), lo ignoramos sin mostrar error
 
            } else {
                vista.mostrarError("Respuesta inesperada del servidor: \n" + linea);
            }
        }
        // Asignamos la acción a los botones que se acaban de crear
        vista.configurarAccionesVerPerfil(e -> manejarVerDetalle((JButton) e.getSource()));
 
        // Si no hay perfiles activos
        if (perfilesActivos == 0) {
            vista.mostrarError("No se encontraron perfiles ACTIVOS que coincidan con la búsqueda.\n"
                    + "(Los perfiles desactivados no se muestran)");
        }
    }
    private void manejarVerDetalle(JButton boton) {
        // Recuperamos el mapa de datos que la vista guardó para este botón específico
        Map<String, String> datos = vista.getPerfilData(boton);
    
        if (datos != null) {
            String detalle = "<html>" +
                    "<body style='width: 300px; font-family: sans-serif;'>" +
                    "<h2 style='color: #4A90E2;'>Detalles del Perfil</h2>" +
                    "<hr>" +
                    "<b>ID del Perfil:</b> " + datos.get("idPerfil") + "<br><br>" +
                    "<b>Nombre Completo:</b> " + datos.get("nombreCompleto") + "<br><br>" +
                    "<b>Código de Secuencia:</b><br>" +
                    "<div style='background: #eeeeee; padding: 5px;'>" + datos.get("codigoSecuencia") + "</div><br>" +
                    "<b>Fecha de Muestra:</b> " + datos.get("fechaMuestra") + "<br><br>" +
                    "<b>Descripción:</b><br><i>" + datos.get("descripcion") + "</i>" +
                    "</body></html>";
        
            // Lo mostramos usando el método que ya tienes en la vista
            vista.mostrarMensaje(detalle);
        }
    }
}