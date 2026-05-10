package com.mycompany.bancoadn.cliente;

import java.util.List;

public class BancoADN_Grupo6_Ctrl_AdminPerfiles {

    private final BancoADN_Grupo6_Pant_AdminPerfiles vista;
    private final BancoADN_Grupo6_ClienteSocket      clienteSocket;
    private final String             emailAdmin;

    public BancoADN_Grupo6_Ctrl_AdminPerfiles(BancoADN_Grupo6_Pant_AdminPerfiles vista,
                              BancoADN_Grupo6_ClienteSocket clienteSocket,
                              String emailAdmin) {
        this.vista         = vista;
        this.clienteSocket = clienteSocket;
        this.emailAdmin    = emailAdmin;

        initListeners();
        buscarPerfiles("Todos", "");   // Carga inicial: todos los perfiles
    }

    private void initListeners() {
        vista.agregarListenerVolver(e -> vista.dispose());
        vista.agregarListenerBuscar(e -> {
            String filtro = vista.getTipoFiltro();
            String texto  = vista.getTextoBusqueda();
            buscarPerfiles(filtro, texto);
        });
    }

    // ── Buscar / listar perfiles ──────────────────────────
    private void buscarPerfiles(String filtro, String texto) {
        vista.limpiarPerfiles();

        if (!clienteSocket.estaConectado()) {
            vista.mostrarError("No hay conexión con el servidor.");
            return;
        }

        // Validar que si se busca por ID el texto sea numérico
        if (filtro.equals("ID") && !texto.isEmpty()) {
            try {
                Integer.parseInt(texto);
            } catch (NumberFormatException ex) {
                vista.mostrarError("Formato incorrecto: para buscar por ID debés ingresar solo números.");
                return;
            }
        }

        String mensaje;
        if (filtro.equals("ID") && !texto.isEmpty()) {
            mensaje = "BuscarPerfilesAdmin - " + texto + " - NULL";
        } else if (filtro.equals("Nombre") && !texto.isEmpty()) {
            mensaje = "BuscarPerfilesAdmin - NULL - " + texto;
        } else {
            // "Todos" o búsqueda vacía → traer todos
            mensaje = "BuscarPerfilesAdmin - NULL - NULL";
        }

        List<String> lineas = clienteSocket.enviarYSolicitarLista(mensaje);

        if (lineas == null) {
            vista.mostrarError("Error al conectar con el servidor.");
            return;
        }

        if (lineas.isEmpty() || (lineas.size() == 1 && lineas.get(0).startsWith("No se encontro"))) {
            vista.mostrarSinResultados();
            return;
        }

        int total = 0;
        for (String linea : lineas) {
            if (linea.startsWith("No se encontro")) {
                vista.mostrarSinResultados();
                return;
            }
            String[] p = linea.split(" - ", -1);
            if (p.length < 7) {
                System.err.println("Línea malformada (BuscarPerfilesAdmin), ignorada: " + linea);
                continue;
            }

            int    idPerfil     = parsearInt(p[0]);
            String nombre       = p[1].trim();
            String codigo       = p[2].trim();
            String descripcion  = p[3].trim();
            boolean activo      = p[4].trim().equals("1");
            String fechaMuestra = p[5].trim();
            String emailTitular = p[6].trim();

            final String emailCapturado = emailTitular;
            final int    idCapturado    = idPerfil;
            final boolean activoCapt    = activo;

            vista.agregarTarjetaPerfil(
                idPerfil, nombre, codigo, descripcion, fechaMuestra, emailTitular, activo,
                // Botón modificar
                e -> abrirModificarPerfil(emailCapturado, nombre, codigo, descripcion, fechaMuestra),
                // Botón baja / restaurar
                e -> {
                    if (activoCapt) {
                        manejarBaja(emailCapturado);
                    } else {
                        manejarRestaurar(emailCapturado);
                    }
                }
            );
            total++;
        }

        if (total == 0) {
            vista.mostrarSinResultados();
        }
    }

    // ── Dar de baja ───────────────────────────────────────
    private void manejarBaja(String emailTitular) {
        if (!vista.confirmar("¿Confirmás dar de baja el perfil de " + emailTitular + "?")) return;

        String respuesta = clienteSocket.enviarYRecibir("BajaAdmin - " + emailTitular);
        if (respuesta == null) {
            vista.mostrarError("Sin respuesta del servidor.");
            return;
        }
        if (respuesta.toLowerCase().contains("exito")) {
            vista.mostrarMensaje("Perfil dado de baja correctamente.");
        } else {
            vista.mostrarError("El servidor respondió:\n" + respuesta);
        }
        buscarPerfiles(vista.getTipoFiltro(), vista.getTextoBusqueda());
    }

    // ── Restaurar ─────────────────────────────────────────
    private void manejarRestaurar(String emailTitular) {
        if (!vista.confirmar("¿Confirmás restaurar el perfil de " + emailTitular + "?")) return;

        String respuesta = clienteSocket.enviarYRecibir("RestaurarAdmin - " + emailTitular);
        if (respuesta == null) {
            vista.mostrarError("Sin respuesta del servidor.");
            return;
        }
        if (respuesta.toLowerCase().contains("exito")) {
            vista.mostrarMensaje("Perfil restaurado correctamente.");
        } else {
            vista.mostrarError("El servidor respondió:\n" + respuesta);
        }
        buscarPerfiles(vista.getTipoFiltro(), vista.getTextoBusqueda());
    }

    // ── Abrir ventana modificar directo ───────────────────
    private void abrirModificarPerfil(String emailTitular, String nombre,
                                      String codigo, String descripcion, String fecha) {
        BancoADN_Grupo6_Pant_ModificarPerfilAdmin vistaModif = new BancoADN_Grupo6_Pant_ModificarPerfilAdmin();
        // Pre-cargar datos actuales
        vistaModif.setNombrePerfil(nombre);
        vistaModif.setCodigoSecuencia(codigo);
        vistaModif.setDescripcion(descripcion);
        vistaModif.setFechaMuestra(fecha);

        new BancoADN_Grupo6_Ctrl_ModificarPerfilAdmin(vistaModif, clienteSocket, emailTitular, vista);
        vistaModif.setVisible(true);
    }

    private int parsearInt(String s) {
        try   { return Integer.parseInt(s.trim()); }
        catch (NumberFormatException e) { return -1; }
    }
}
