package com.mycompany.groupay;

public class PaymentMethod {
    private int idMetodo;
    private String nombre;
    private String tipoFormato;

    public PaymentMethod(int idMetodo, String nombre, String tipoFormato) {
        this.idMetodo = idMetodo;
        this.nombre = nombre;
        this.tipoFormato = tipoFormato;
    }

    public int getIdMetodo() { return idMetodo; }
    @Override
    public String toString() { return nombre + " (" + tipoFormato + ")"; }
}
