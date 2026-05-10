package ladoserver;
import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 
public class BancoADN6RequestHandler implements Runnable {
 
    private final Socket socket;
 
    public BancoADN6RequestHandler(Socket socket) {
        this.socket = socket;
    }
 
    @Override
    public void run() {
        try (
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
 
                System.out.println("Recibido: " + line);
 
                String[] Partes = line.split(" - ");
                System.out.println("Partes: " + java.util.Arrays.toString(Partes));
 
                String URL      = "jdbc:mariadb://localhost:3306/db_genetica";
                String USER     = "root";
                String PASSWORD = "1234";
 
                Connection connection = null;
                try {
                    connection = DriverManager.getConnection(URL, USER, PASSWORD);
                    System.out.println("Connection to MariaDB successful!");
                } catch (SQLException e) {
                    System.err.println("Database connection failed: " + e.getMessage());
                    e.printStackTrace();
                }
 
                // BuscarIDNOM - <idPerfil|NULL> - <nombre|NULL>
                if(Partes.length > 1 && Partes[0].equals("BuscarIDNOM")){
                    
                    if(Partes[1].equals("NULL")){
                    
                        try{
                                String sql2 = "CALL BuscarPerfiles(null,?)";

                                PreparedStatement pst2 = connection.prepareStatement(sql2);
                                pst2.setString(1, Partes[2]);
                                ResultSet rs2 = pst2.executeQuery();
                                StringBuilder Respuesta = new StringBuilder();
                                int encontro=0;
                                
                                while(rs2.next()){
                                Respuesta.append(rs2.getInt("idPerfil")).append(" - ")
                                    .append(rs2.getString("nombreCompleto")).append(" - ")
                                    .append(rs2.getString("códigoSecuencia")).append(" - ")
                                    .append(rs2.getString("descripción")).append(" - ")
                                    .append(rs2.getString("estado")).append(" - ")
                                    .append(rs2.getDate("fechaMuestra")).append(" - ")
                                    .append(rs2.getString("email"));
                                    writer.println(Respuesta);
                                    encontro++;
                                    Respuesta = new StringBuilder();
                                }
                                if(encontro>0){
                                    writer.println("FINISH");
                                }else{
                                    writer.println("No se encontro el perfil");
                                    writer.println("FINISH");
                                    
                                }


                            }
                            catch(Exception ex){
                                ex.printStackTrace();
                            }
                        }
                    else{
                        try{
                            
                            String sql2 = "CALL BuscarPerfiles(?,null)";
                         
                            PreparedStatement pst2 = connection.prepareStatement(sql2);
                            pst2.setInt(1, Integer.valueOf(Partes[1]));
                            ResultSet rs2 = pst2.executeQuery();
                            StringBuilder Respuesta = new StringBuilder();
                            
                            if(rs2.next()){
                                Respuesta.append(rs2.getInt("idPerfil")).append(" - ")
                                    .append(rs2.getString("nombreCompleto")).append(" - ")
                                    .append(rs2.getString("códigoSecuencia")).append(" - ")
                                    .append(rs2.getString("descripción")).append(" - ")
                                    .append(rs2.getString("estado")).append(" - ")
                                    .append(rs2.getDate("fechaMuestra")).append(" - ")
                                    .append(rs2.getString("email"));
                                writer.println(Respuesta);
                                writer.println("FINISH");
                            }else {
                                writer.println("No se encontro el perfil");
                                writer.println("FINISH");
                            }
                            
                            
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                }
                
                else if(Partes.length > 1 && Partes[0].equals("BuscarDat")){
                    
                    try{
                        
                        String sql2 = "CALL BuscarPerfiles(obtenerIdPerfil(?),null)";
                     
                        PreparedStatement pst2 = connection.prepareStatement(sql2);
                        pst2.setString(1, Partes[1]);
                        ResultSet rs2 = pst2.executeQuery();
                        StringBuilder Respuesta = new StringBuilder();
                        
                        if(rs2.next()){
                            Respuesta
                                .append(rs2.getString("nombreCompleto")).append(" - ")
                                .append(rs2.getString("códigoSecuencia")).append(" - ")
                                .append(rs2.getString("descripción")).append(" - ")
                                .append(rs2.getDate("fechaMuestra")).append(" - ")
                                .append(rs2.getString("estado"));
                            writer.println(Respuesta);
                        }else {
                            writer.println("No se encontro el perfil");
                        }
                        
                        
                    }
                    catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
                
                else if(Partes.length > 3 && Partes[0].equals("CrearC")){
                    
                        try{
                            String sql2 = "CALL CrearCuenta(?,?,?)";
                         
                            PreparedStatement pst2 = connection.prepareStatement(sql2);
                            pst2.setString(1, Partes[1]);
                            pst2.setString(2, Partes[2]);
                            pst2.setString(3, Partes[3]);
                            
                            ResultSet rs2 = pst2.executeQuery();
                            rs2.next();
                            boolean resultado = rs2.getBoolean("resultado");
                            if(resultado==true){
                                writer.println("Creado completado con exito");
                            }
                            else{
                                writer.println("Ya existe una cuenta con ese email");
                            }
                            
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                
                //INICIAR SESION
                else if(Partes.length > 2 && Partes[0].equals("IniciarS")){
                    
                        try{
                            String sql2 = "CALL IniciarSesion(?,?)";
                         
                            PreparedStatement pst2 = connection.prepareStatement(sql2);
                            pst2.setString(1, Partes[1]);
                            pst2.setString(2, Partes[2]);
                            
                            ResultSet rs2 = pst2.executeQuery();
                            if (rs2.next()) {
                                int resultado = rs2.getInt("resultado");
                                writer.println(resultado);
                            }
                            
                            
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                
                else if(Partes.length > 1 && Partes[0].equals("DarDBaja")){
                    
                        try{
                            String sql2 = "CALL DarBajaPerfil(?)";
                         
                            PreparedStatement pst2 = connection.prepareStatement(sql2);
                            pst2.setString(1, Partes[1]);
                            
                            ResultSet rs2 = pst2.executeQuery();
                            if (rs2.next()) {
                                boolean resultado = rs2.getBoolean("result");
                                writer.println(resultado);
                            }
                            
                            
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                
                
                else if(Partes.length > 1 && Partes[0].equals("DarDRestaur")){
                    
                        try{
                            String sql2 = "CALL RestaurarPerfil(?)";
                         
                            PreparedStatement pst2 = connection.prepareStatement(sql2);
                            pst2.setString(1, Partes[1]);
                            
                            ResultSet rs2 = pst2.executeQuery();
                            if (rs2.next()) {
                                boolean resultado = rs2.getBoolean("result");
                                writer.println(resultado);
                            }
                            
                            
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                
                else if(Partes.length > 4 && Partes[0].equals("ModificP")){
                    
                        try{
                            String sql2 = "CALL ModificarPerfil(?,?,?,?,?)";
                         
                            PreparedStatement pst2 = connection.prepareStatement(sql2);
                            pst2.setString(1, Partes[1]);
                            pst2.setString(2, Partes[2]);
                            pst2.setString(3, Partes[3]);
                            pst2.setString(4, Partes[4]);
                            pst2.setDate(5, java.sql.Date.valueOf(Partes[5]));
                            
                            ResultSet rs2 = pst2.executeQuery();
                            rs2.next();
                            boolean resultado = rs2.getBoolean("resultado");
                            if(resultado==true){
                                writer.println("Modificacion completada con exito");
                            }
                            else{
                                writer.println("No existe una cuenta con ese email");
                            }
                            
                            
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                
                else if(Partes.length > 3 && Partes[0].equals("ResSol")){
                    
                        try{
                            String sql2 = "CALL ResolverSolicitud(?,?,?)";
                         
                            PreparedStatement pst2 = connection.prepareStatement(sql2);
                            pst2.setInt(1, Integer.valueOf(Partes[1]));
                            pst2.setInt(2, Integer.valueOf(Partes[2]));
                            pst2.setString(3, Partes[3]);
                            
                            ResultSet rs2 = pst2.executeQuery();
                            rs2.next();
                            int resultado = rs2.getInt("resultado2");
                            
                            
                            if(resultado==1){
                                writer.println("Aceptado con exito");
                            }
                            else if (resultado==0){
                                writer.println("rechazado con exito");
                            }
                            else{
                                writer.println("No existe una solicitud con ese id");
                            }
                            
                            
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                
                
                else if(Partes.length > 3 && Partes[0].equals("CrearSolPer")){
                    
                        try{
                            String sql2 = "CALL CrearSolicitudP(?,?,?)";
                         
                            PreparedStatement pst2 = connection.prepareStatement(sql2);
                            pst2.setString(1, Partes[1]);
                            pst2.setString(2, Partes[2]);
                            pst2.setString(3, Partes[3]);
                            
                            ResultSet rs2 = pst2.executeQuery();
                            writer.println("Solicitud enviada con exito");
                            
                            
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                
                
                
                else if(Partes.length > 2 && Partes[0].equals("CrearPerfilSol")){
                    
                        try{
                            String sql2 = "CALL CrearPerfilSolicitud(?,?)";
                         
                            PreparedStatement pst2 = connection.prepareStatement(sql2);
                            pst2.setString(1, Partes[1]);
                            pst2.setString(2, Partes[2]);
                            
                            ResultSet rs2 = pst2.executeQuery();
                            writer.println("Solicitud enviada con exito");
                            
                            
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    }
                
                
                else if(Partes[0].equals("ListaSol")){
                    
                        try{
                            String sql2 = "CALL VerSol()";
                         
                            PreparedStatement pst2 = connection.prepareStatement(sql2);
                            
                            ResultSet rs2 = pst2.executeQuery();
                            
                            StringBuilder Respuesta = new StringBuilder();
                            
                            while(rs2.next()){
                            Respuesta.append(rs2.getInt("idSolicitud")).append(" - ")
                                    .append(rs2.getString("tipo")).append(" - ")
                                    .append(rs2.getString("estado")).append(" - ")
                                    .append(rs2.getString("datosSolicitud")).append(" - ")
                                    .append(rs2.getInt("IdAdmin")).append(" - ")
                                    .append(rs2.getInt("IdUsuario")).append(" - ")
                                    .append(rs2.getInt("idPerfil")).append(" - ")
                                    .append(rs2.getDate("fechaCreación")).append(" - ")
                                    .append(rs2.getDate("fechaResolución"));
                            writer.println(Respuesta);
                            Respuesta = new StringBuilder();
                            }
                            writer.println("FINISH");
                        }
                        catch(Exception ex){
                            ex.printStackTrace();
                        }
                    
 
                } else if (Partes.length > 1 && Partes[0].equals("EmailPorPerfil")) {
                    try {
                        String sql2 = "SELECT obtenerEmailPorPerfil(?) AS email";
                        PreparedStatement pst2 = connection.prepareStatement(sql2);
                        pst2.setInt(1, Integer.valueOf(Partes[1].trim()));
                        ResultSet rs2 = pst2.executeQuery();
                        if (rs2.next()) {
                            String email = rs2.getString("email");
                            writer.println(email != null ? email : "—");
                        } else {
                            writer.println("—");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        writer.println("—");
                    }
 
 
                } else if (Partes[0].equals("UltSol")) {
                    try {
                        String sql2 =
                            "SELECT idSolicitud, tipo, estado, datosSolicitud, " +
                            "       IdAdmin, IdUsuario, idPerfil, " +
                            "       fechaCreación, fechaResolución " +
                            "FROM Solicitud " +
                            "WHERE estado != 0 " +
                            "ORDER BY fechaResolución DESC " +
                            "LIMIT 10";
                        PreparedStatement pst2 = connection.prepareStatement(sql2);
                        ResultSet rs2 = pst2.executeQuery();
                        StringBuilder Respuesta = new StringBuilder();
                        while (rs2.next()) {
                            String fechaCre = rs2.getString("fechaCreación");
                            String fechaRes = rs2.getString("fechaResolución");
                            Respuesta.append(rs2.getInt("idSolicitud")).append(" - ")
                                .append(rs2.getString("tipo")).append(" - ")
                                .append(rs2.getString("estado")).append(" - ")
                                .append(rs2.getString("datosSolicitud")).append(" - ")
                                .append(rs2.getInt("IdAdmin")).append(" - ")
                                .append(rs2.getInt("IdUsuario")).append(" - ")
                                .append(rs2.getInt("idPerfil")).append(" - ")
                                .append(fechaCre != null ? fechaCre : "—").append(" - ")
                                .append(fechaRes != null ? fechaRes : "—");
                            writer.println(Respuesta);
                            Respuesta = new StringBuilder();
                        }
                        writer.println("FINISH");
                    } catch (Exception ex) { ex.printStackTrace(); }
 
                } else if (Partes.length > 2 && Partes[0].equals("BuscarPerfilesAdmin")) {
                    try {
                        PreparedStatement pst2;
                        if (Partes[1].trim().equals("NULL") && Partes[2].trim().equals("NULL")) {
                            // Traer todos
                            pst2 = connection.prepareStatement("CALL BuscarPerfiles(null, null)");
                        } else if (Partes[1].trim().equals("NULL")) {
                            pst2 = connection.prepareStatement("CALL BuscarPerfiles(null, ?)");
                            pst2.setString(1, Partes[2].trim());
                        } else {
                            pst2 = connection.prepareStatement("CALL BuscarPerfiles(?, null)");
                            pst2.setInt(1, Integer.valueOf(Partes[1].trim()));
                        }
 
                        ResultSet rs2 = pst2.executeQuery();
                        StringBuilder Respuesta = new StringBuilder();
                        int encontro = 0;
                        while (rs2.next()) {
                            Respuesta.append(rs2.getInt("idPerfil")).append(" - ")
                                .append(rs2.getString("nombreCompleto")).append(" - ")
                                .append(rs2.getString("códigoSecuencia")).append(" - ")
                                .append(rs2.getString("descripción")).append(" - ")
                                .append(rs2.getString("estado")).append(" - ")
                                .append(rs2.getDate("fechaMuestra")).append(" - ")
                                .append(rs2.getString("email"));
                            writer.println(Respuesta);
                            encontro++;
                            Respuesta = new StringBuilder();
                        }
                        if (encontro > 0) {
                            writer.println("FINISH");
                        } else {
                            writer.println("No se encontro el perfil");
                            writer.println("FINISH");
                        }
                    } catch (Exception ex) { ex.printStackTrace(); }
 
                } else if (Partes.length > 1 && Partes[0].equals("BajaAdmin")) {
                    try {
                        String sql2 = "CALL DarBajaPerfil(?)";
                        PreparedStatement pst2 = connection.prepareStatement(sql2);
                        pst2.setString(1, Partes[1].trim());
                        ResultSet rs2 = pst2.executeQuery();
                        rs2.next();
                        int resultado = rs2.getInt("result");
                        if (resultado == 1) {
                            writer.println("Baja completada con exito");
                        } else {
                            writer.println("No tiene perfil activo o no existe");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        writer.println("Error al dar de baja el perfil");
                    }
 
                } else if (Partes.length > 1 && Partes[0].equals("RestaurarAdmin")) {
                    try {
                        String sql2 = "CALL RestaurarPerfil(?)";
                        PreparedStatement pst2 = connection.prepareStatement(sql2);
                        pst2.setString(1, Partes[1].trim());
                        ResultSet rs2 = pst2.executeQuery();
                        rs2.next();
                        int resultado = rs2.getInt("result");
                        if (resultado == 1) {
                            writer.println("Restauracion completada con exito");
                        } else {
                            writer.println("No tiene perfil inactivo o no existe");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        writer.println("Error al restaurar el perfil");
                    }
 
                } else if (Partes.length > 5 && Partes[0].equals("ModificarAdmin")) {
                    try {
                        String sql2 = "CALL ModificarPerfil(?,?,?,?,?)";
                        PreparedStatement pst2 = connection.prepareStatement(sql2);
                        pst2.setString(1, Partes[1].trim()); // email
                        pst2.setString(2, Partes[2].trim()); // nombre
                        pst2.setString(3, Partes[3].trim()); // codigo
                        pst2.setString(4, Partes[4].trim()); // descripcion
                        pst2.setDate(5, java.sql.Date.valueOf(Partes[5].trim())); // fecha
                        ResultSet rs2 = pst2.executeQuery();
                        rs2.next();
                        // ModificarPerfil devuelve "resultado2" según el SP
                        int resultado = rs2.getInt("resultado2");
                        if (resultado == 1) {
                            writer.println("Modificacion completada con exito");
                        } else {
                            writer.println("No existe una cuenta con ese email");
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        writer.println("Error al modificar el perfil");
                    }
 
                } else {
                    writer.println("Comando Invalido");
                }
 
            } // fin while
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            try { socket.close(); } catch (IOException e) { /* ignore */ }
        }
    }
}