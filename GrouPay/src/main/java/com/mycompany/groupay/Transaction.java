package com.mycompany.groupay;

import java.sql.Date;

public class Transaction {
    private double monto;
    private String metodoNombre;
    private String formatoTipo;
    private Date fecha;
    private String categoria;
    private String userName;
    private String descripcion;

    public Transaction(double monto, String metodoNombre, String formatoTipo, Date fecha, String categoria, String userName, String descripcion) {
        this.monto = monto;
        this.metodoNombre = metodoNombre;
        this.formatoTipo = formatoTipo;
        this.fecha = fecha;
        this.categoria = categoria;
        this.userName = userName;
        this.descripcion = descripcion;
    }

    public double getMonto() { return monto; }
    public String getMetodoNombre() { return metodoNombre; }
    public String getFormatoTipo() { return formatoTipo; }
    public Date getFecha() { return fecha; }
    public String getCategoria() { return categoria; }
    public String getUserName() { return userName; }
    public String getDescripcion() { return descripcion; }
}
