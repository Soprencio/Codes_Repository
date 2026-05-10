package com.mycompany.groupay;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    
    public boolean emailExists(String email) {
        String query = "SELECT 1 FROM Cuenta WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean registerUser(String name, String surname, String email, String password) {
        if (emailExists(email)) return false;
        String query = "INSERT INTO Cuenta (Nombre, Apellido, email, Contrasena, Saldo, idFamilia, Rol) VALUES (?, ?, ?, ?, 0.00, NULL, NULL)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, name);
            pstmt.setString(2, surname);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticateUser(String email, String password) {
        String query = "SELECT 1 FROM Cuenta WHERE email = ? AND Contrasena = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUserInfo(String email) {
        String query = "SELECT * FROM Cuenta WHERE email = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int idFam = rs.getInt("idFamilia");
                    Integer idFamilia = rs.wasNull() ? null : idFam;
                    int r = rs.getInt("Rol");
                    Integer rol = rs.wasNull() ? null : r;
                    return new User(rs.getInt("idCuenta"), rs.getString("Nombre"), rs.getString("Apellido"), rs.getString("email"), rs.getDouble("Saldo"), idFamilia, rol);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Transaction> getRecentTransactions(int idCuenta) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT p.Monto, mp.Nombre as Metodo, fp.Tipo as Formato, c.FechaCompra, cat.idCategoria, cat.T_Categoria, cu.Nombre as UserNombre, cu.Apellido as UserApellido, c.descripcion " +
                       "FROM Pago p " +
                       "JOIN MetodoPago mp ON p.idMetodo = mp.idMetodo " +
                       "JOIN FormatoPago fp ON mp.idFormato = fp.idFormato " +
                       "JOIN Comprobante c ON p.idPago = c.idPago " +
                       "JOIN Categoria cat ON c.idCategoria = cat.idCategoria " +
                       "JOIN Cuenta cu ON p.idCuenta = cu.idCuenta " +
                       "WHERE p.idCuenta = ? " +
                       "ORDER BY c.FechaCompra DESC, p.idPago DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idCuenta);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(new Transaction(
                        rs.getDouble("Monto"),
                        rs.getString("Metodo"),
                        rs.getString("Formato"),
                        rs.getDate("FechaCompra"),
                        rs.getString("T_Categoria") + ":" + rs.getInt("idCategoria"),
                        rs.getString("UserNombre") + " " + rs.getString("UserApellido"),
                        rs.getString("descripcion")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public List<Transaction> getFamilyTransactions(int idFamilia) {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT p.Monto, mp.Nombre as Metodo, fp.Tipo as Formato, c.FechaCompra, cat.idCategoria, cat.T_Categoria, cu.Nombre as UserNombre, cu.Apellido as UserApellido, c.descripcion " +
                       "FROM Pago p " +
                       "JOIN MetodoPago mp ON p.idMetodo = mp.idMetodo " +
                       "JOIN FormatoPago fp ON mp.idFormato = fp.idFormato " +
                       "JOIN Comprobante c ON p.idPago = c.idPago " +
                       "JOIN Categoria cat ON c.idCategoria = cat.idCategoria " +
                       "JOIN Cuenta cu ON p.idCuenta = cu.idCuenta " +
                       "WHERE cu.idFamilia = ? " +
                       "ORDER BY c.FechaCompra DESC, p.idPago DESC LIMIT 20";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idFamilia);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(new Transaction(
                        rs.getDouble("Monto"),
                        rs.getString("Metodo"),
                        rs.getString("Formato"),
                        rs.getDate("FechaCompra"),
                        rs.getString("T_Categoria") + ":" + rs.getInt("idCategoria"),
                        rs.getString("UserNombre") + " " + rs.getString("UserApellido"),
                        rs.getString("descripcion")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public List<PaymentMethod> getPaymentMethods() {
        List<PaymentMethod> methods = new ArrayList<>();
        String query = "SELECT mp.idMetodo, mp.Nombre, fp.Tipo FROM MetodoPago mp JOIN FormatoPago fp ON mp.idFormato = fp.idFormato";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                methods.add(new PaymentMethod(rs.getInt("idMetodo"), rs.getString("Nombre"), rs.getString("Tipo")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return methods;
    }

    public List<Category> getPaymentCategories() {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT idCategoria, T_Categoria FROM Categoria WHERE idCategoria NOT IN (1, 2)";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                categories.add(new Category(rs.getInt("idCategoria"), rs.getString("T_Categoria")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return categories;
    }

    public boolean processPayment(User user, int idMetodo, int idCategoria, double amount, String description) {
        ensureMasterDataExists();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            String upd = "UPDATE Cuenta SET Saldo = Saldo - ? WHERE idCuenta = ?";
            try (PreparedStatement ps = conn.prepareStatement(upd)) {
                ps.setDouble(1, amount); ps.setInt(2, user.getIdCuenta()); ps.executeUpdate();
            }

            String insP = "INSERT INTO Pago (Monto, idCuenta, idMetodo) VALUES (?, ?, ?)";
            int idPago = -1;
            try (PreparedStatement ps = conn.prepareStatement(insP, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDouble(1, amount); ps.setInt(2, user.getIdCuenta()); ps.setInt(3, idMetodo); ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) idPago = rs.getInt(1); }
            }

            String insC = "INSERT INTO Comprobante (FechaCompra, idCategoria, idPago, descripcion) VALUES (NOW(), ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insC)) {
                ps.setInt(1, idCategoria); ps.setInt(2, idPago); ps.setString(3, description); ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace(); return false;
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ex) {}
        }
    }

    public List<User> getFamilyMembers(int idFamilia) {
        List<User> members = new ArrayList<>();
        String query = "SELECT * FROM Cuenta WHERE idFamilia = ? ORDER BY Rol DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idFamilia);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    members.add(new User(rs.getInt("idCuenta"), rs.getString("Nombre"), rs.getString("Apellido"), rs.getString("email"), rs.getDouble("Saldo"), rs.getInt("idFamilia"), rs.getInt("Rol")));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return members;
    }

    public Family getFamilyInfo(int idFamilia) {
        String query = "SELECT * FROM Familia WHERE idFamilia = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, idFamilia);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Family(rs.getInt("idFamilia"), rs.getString("Nombre"), rs.getInt("Permitir"), String.valueOf(rs.getInt("contrasenaFam")), rs.getString("Descripcion"), rs.getDouble("FondoFam"));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public boolean processTransfer(User sender, User receiver, double amount, boolean toFamilyFund) {
        ensureMasterDataExists();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            String updS = "UPDATE Cuenta SET Saldo = Saldo - ? WHERE idCuenta = ?";
            try (PreparedStatement ps = conn.prepareStatement(updS)) { ps.setDouble(1, amount); ps.setInt(2, sender.getIdCuenta()); ps.executeUpdate(); }
            String desc;
            if (toFamilyFund) {
                String updF = "UPDATE Familia SET FondoFam = FondoFam + ? WHERE idFamilia = ?";
                try (PreparedStatement ps = conn.prepareStatement(updF)) { ps.setDouble(1, amount); ps.setInt(2, sender.getIdFamilia()); ps.executeUpdate(); }
                desc = "Fondo Familiar";
            } else {
                String updR = "UPDATE Cuenta SET Saldo = Saldo + ? WHERE idCuenta = ?";
                try (PreparedStatement ps = conn.prepareStatement(updR)) { ps.setDouble(1, amount); ps.setInt(2, receiver.getIdCuenta()); ps.executeUpdate(); }
                desc = receiver.getNombre() + " " + receiver.getApellido();
            }
            String insP = "INSERT INTO Pago (Monto, idCuenta, idMetodo) VALUES (?, ?, 1)";
            int idPago = -1;
            try (PreparedStatement ps = conn.prepareStatement(insP, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDouble(1, amount); ps.setInt(2, sender.getIdCuenta()); ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) idPago = rs.getInt(1); }
            }
            String insC = "INSERT INTO Comprobante (FechaCompra, idCategoria, idPago, descripcion) VALUES (NOW(), 1, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insC)) { ps.setInt(1, idPago); ps.setString(2, desc); ps.executeUpdate(); }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace(); return false;
        } finally { if (conn != null) try { conn.close(); } catch (SQLException ex) {} }
    }

    public boolean processWithdrawal(User triggerUser, User receiver, double amount) {
        ensureMasterDataExists();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            String updF = "UPDATE Familia SET FondoFam = FondoFam - ? WHERE idFamilia = ?";
            try (PreparedStatement ps = conn.prepareStatement(updF)) { ps.setDouble(1, amount); ps.setInt(2, triggerUser.getIdFamilia()); ps.executeUpdate(); }
            String updR = "UPDATE Cuenta SET Saldo = Saldo + ? WHERE idCuenta = ?";
            try (PreparedStatement ps = conn.prepareStatement(updR)) { ps.setDouble(1, amount); ps.setInt(2, receiver.getIdCuenta()); ps.executeUpdate(); }
            String insP = "INSERT INTO Pago (Monto, idCuenta, idMetodo) VALUES (?, ?, 1)";
            int idPago = -1;
            try (PreparedStatement ps = conn.prepareStatement(insP, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDouble(1, amount); ps.setInt(2, triggerUser.getIdCuenta()); ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) idPago = rs.getInt(1); }
            }
            String desc = receiver.getNombre() + " " + receiver.getApellido();
            String insC = "INSERT INTO Comprobante (FechaCompra, idCategoria, idPago, descripcion) VALUES (NOW(), 2, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insC)) { ps.setInt(1, idPago); ps.setString(2, desc); ps.executeUpdate(); }
            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace(); return false;
        } finally { if (conn != null) try { conn.close(); } catch (SQLException ex) {} }
    }

    public boolean createFamily(User admin, String nombre, String pass, String desc, int permitir) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            String insF = "INSERT INTO Familia (Nombre, Permitir, contrasenaFam, Descripcion, FondoFam) VALUES (?, ?, ?, ?, 0.0)";
            int idFam = -1;
            try (PreparedStatement ps = conn.prepareStatement(insF, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, nombre); ps.setInt(2, permitir); ps.setInt(3, Integer.parseInt(pass)); ps.setString(4, desc); ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) idFam = rs.getInt(1); }
            }
            String updC = "UPDATE Cuenta SET idFamilia = ?, Rol = 3 WHERE idCuenta = ?";
            try (PreparedStatement ps = conn.prepareStatement(updC)) { ps.setInt(1, idFam); ps.setInt(2, admin.getIdCuenta()); ps.executeUpdate(); }
            conn.commit();
            admin.setIdFamilia(idFam); admin.setRol(3);
            return true;
        } catch (Exception e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace(); return false;
        } finally { if (conn != null) try { conn.close(); } catch (SQLException ex) {} }
    }

    public int joinFamily(User user, String nombre, String pass) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            String selF = "SELECT idFamilia, Permitir FROM Familia WHERE Nombre = ? AND contrasenaFam = ?";
            int idF = -1, p = -1;
            try (PreparedStatement ps = conn.prepareStatement(selF)) {
                ps.setString(1, nombre);
                try { ps.setInt(2, Integer.parseInt(pass)); } catch (NumberFormatException e) { return 0; }
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) { idF = rs.getInt("idFamilia"); p = rs.getInt("Permitir"); } else return 0; }
            }
            int rol = (p == 1) ? 1 : 2;
            String countQuery = "SELECT COUNT(*) FROM Cuenta WHERE idFamilia = ?";
            try (PreparedStatement psCount = conn.prepareStatement(countQuery)) {
                psCount.setInt(1, idF);
                try (ResultSet rsCount = psCount.executeQuery()) { if (rsCount.next() && rsCount.getInt(1) >= 6) return -1; }
            }
            String updC = "UPDATE Cuenta SET idFamilia = ?, Rol = ? WHERE idCuenta = ?";
            try (PreparedStatement ps = conn.prepareStatement(updC)) { ps.setInt(1, idF); ps.setInt(2, rol); ps.setInt(3, user.getIdCuenta()); ps.executeUpdate(); }
            conn.commit();
            user.setIdFamilia(idF); user.setRol(rol);
            return 1;
        } catch (Exception e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace(); return -2;
        } finally { if (conn != null) try { conn.close(); } catch (SQLException ex) {} }
    }

    public boolean processDeposit(User user, double amount) {
        ensureMasterDataExists();
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);
            
            String upd = "UPDATE Cuenta SET Saldo = Saldo + ? WHERE idCuenta = ?";
            try (PreparedStatement ps = conn.prepareStatement(upd)) {
                ps.setDouble(1, amount); ps.setInt(2, user.getIdCuenta()); ps.executeUpdate();
            }

            String insP = "INSERT INTO Pago (Monto, idCuenta, idMetodo) VALUES (?, ?, 5)";
            int idPago = -1;
            try (PreparedStatement ps = conn.prepareStatement(insP, Statement.RETURN_GENERATED_KEYS)) {
                ps.setDouble(1, amount); ps.setInt(2, user.getIdCuenta()); ps.executeUpdate();
                try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) idPago = rs.getInt(1); }
            }

            String insC = "INSERT INTO Comprobante (FechaCompra, idCategoria, idPago, descripcion) VALUES (NOW(), 6, ?, 'Ingreso de dinero')";
            try (PreparedStatement ps = conn.prepareStatement(insC)) {
                ps.setInt(1, idPago); ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) try { conn.rollback(); } catch (SQLException ex) {}
            e.printStackTrace(); return false;
        } finally {
            if (conn != null) try { conn.close(); } catch (SQLException ex) {}
        }
    }

    private void ensureMasterDataExists() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("INSERT IGNORE INTO FormatoPago (idFormato, Tipo) VALUES (1, 'DineroEnCuenta'), (2, 'Debito'), (3, 'Credito'), (4, 'Efectivo')")) { ps.executeUpdate(); }
            try (PreparedStatement ps = conn.prepareStatement("INSERT IGNORE INTO MetodoPago (idMetodo, Nombre, idFormato) VALUES (1, 'GrouPay', 1), (2, 'MasterCard', 3), (3, 'VISA', 2), (4, 'Mercado Pago', 1), (5, 'Depósito', 1)")) { ps.executeUpdate(); }
            try (PreparedStatement ps = conn.prepareStatement("INSERT IGNORE INTO Categoria (idCategoria, T_Categoria) VALUES (1, 'Transferencia'), (2, 'Retirar'), (3, 'Comida'), (4, 'Transporte'), (5, 'Streaming'), (6, 'Ingreso')")) { ps.executeUpdate(); }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public User findUserByEmail(String email) { return getUserInfo(email); }
}
