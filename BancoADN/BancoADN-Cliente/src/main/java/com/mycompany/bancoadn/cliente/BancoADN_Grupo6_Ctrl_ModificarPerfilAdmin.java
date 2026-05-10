package com.mycompany.bancoadn.cliente;


public class BancoADN_Grupo6_Ctrl_ModificarPerfilAdmin {

    private final BancoADN_Grupo6_Pant_ModificarPerfilAdmin vista;
    private final BancoADN_Grupo6_ClienteSocket             clienteSocket;
    private final String                    emailTitular;
    private final BancoADN_Grupo6_Pant_AdminPerfiles        vistaListado;

    public BancoADN_Grupo6_Ctrl_ModificarPerfilAdmin(BancoADN_Grupo6_Pant_ModificarPerfilAdmin vista,
                                     BancoADN_Grupo6_ClienteSocket clienteSocket,
                                     String emailTitular,
                                     BancoADN_Grupo6_Pant_AdminPerfiles vistaListado) {
        this.vista         = vista;
        this.clienteSocket = clienteSocket;
        this.emailTitular  = emailTitular;
        this.vistaListado  = vistaListado;

        vista.agregarListenerCancelar(e -> vista.dispose());
        vista.agregarListenerModificar(e -> manejarModificar());
    }

    private void manejarModificar() {
        String nombre  = vista.getNombrePerfil();
        String codigo  = vista.getCodigoSecuencia();
        String desc    = vista.getDescripcion();
        String fecha   = vista.getFechaMuestra();

        // ── Validaciones ──────────────────────────────────
        if (nombre.isEmpty()) {
            vista.mostrarError("El nombre del perfil es obligatorio.");
            return;
        }
        if (nombre.length() < 3) {
            vista.mostrarError("El nombre debe tener al menos 3 caracteres.");
            return;
        }
        if (codigo.isEmpty()) {
            vista.mostrarError("El código de secuencia es obligatorio.");
            return;
        }
        if (codigo.length() < 3) {
            vista.mostrarError("El código debe tener al menos 3 caracteres.");
            return;
        }
        if (desc.isEmpty()) {
            vista.mostrarError("La descripción es obligatoria.");
            return;
        }
        if (fecha.isEmpty()) {
            vista.mostrarError("La fecha es obligatoria (formato: yyyy-MM-dd).");
            return;
        }
        if (!fecha.matches("\\d{4}-\\d{2}-\\d{2}")) {
            vista.mostrarError("La fecha debe estar en formato yyyy-MM-dd (Ej: 2026-04-23).");
            return;
        }

        if (!clienteSocket.estaConectado()) {
            vista.mostrarError("No hay conexión con el servidor.");
            return;
        }

        // ── Enviar al servidor ────────────────────────────
        String mensaje = "ModificarAdmin - " + emailTitular + " - "
                       + nombre + " - " + codigo + " - " + desc + " - " + fecha;

        System.out.println("→ ModificarAdmin: " + mensaje);
        String respuesta = clienteSocket.enviarYRecibir(mensaje);
        System.out.println("← " + respuesta);

        if (respuesta == null) {
            vista.mostrarError("Sin respuesta del servidor. Intentá de nuevo.");
            return;
        }

        if (respuesta.toLowerCase().contains("exito")) {
            vista.mostrarMensaje("¡Perfil modificado correctamente!");
            vista.dispose();
            // Refrescar el listado de perfiles
            if (vistaListado != null) {
                // Re-ejecutar búsqueda con los filtros actuales
                new BancoADN_Grupo6_Ctrl_AdminPerfiles(vistaListado, clienteSocket,"");
            }
        } else {
            vista.mostrarError("El servidor respondió:\n" + respuesta);
        }
    }
}
