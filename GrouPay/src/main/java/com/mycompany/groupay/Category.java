package com.mycompany.groupay;

public class Category {
    private int idCategoria;
    private String tCategoria;

    public Category(int idCategoria, String tCategoria) {
        this.idCategoria = idCategoria;
        this.tCategoria = tCategoria;
    }

    public int getIdCategoria() { return idCategoria; }
    public String getTCategoria() { return tCategoria; }
    @Override
    public String toString() { return tCategoria; }
}
