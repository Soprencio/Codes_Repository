package com.mycompany.groupay;

public class Family {
    private int idFamilia;
    private String nombre;
    private int permitir;
    private String contrasenaFam;
    private String descripcion;
    private double fondoFam;

    public Family(int idFamilia, String nombre, int permitir, String contrasenaFam, String descripcion, double fondoFam) {
        this.idFamilia = idFamilia;
        this.nombre = nombre;
        this.permitir = permitir;
        this.contrasenaFam = contrasenaFam;
        this.descripcion = descripcion;
        this.fondoFam = fondoFam;
    }

    public int getIdFamilia() { return idFamilia; }
    public String getNombre() { return nombre; }
    public int getPermitir() { return permitir; }
    public String getContrasenaFam() { return contrasenaFam; }
    public String getDescripcion() { return descripcion; }
    public double getFondoFam() { return fondoFam; }
}
