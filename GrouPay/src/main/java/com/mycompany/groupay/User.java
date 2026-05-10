package com.mycompany.groupay;

public class User {
    private int idCuenta;
    private String nombre;
    private String apellido;
    private String email;
    private double saldo;
    private Integer idFamilia;
    private Integer rol;

    public User(int idCuenta, String nombre, String apellido, String email, double saldo, Integer idFamilia, Integer rol) {
        this.idCuenta = idCuenta;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.saldo = saldo;
        this.idFamilia = idFamilia;
        this.rol = rol;
    }

    public int getIdCuenta() { return idCuenta; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getEmail() { return email; }
    public double getSaldo() { return saldo; }
    public Integer getIdFamilia() { return idFamilia; }
    public Integer getRol() { return rol; }
    
    public void setSaldo(double saldo) { this.saldo = saldo; }
    public void setIdFamilia(Integer idFamilia) { this.idFamilia = idFamilia; }
    public void setRol(Integer rol) { this.rol = rol; }
}
