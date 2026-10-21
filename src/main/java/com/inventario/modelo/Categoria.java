package com.inventario.modelo;

public class Categoria {
    private final String nombre;
    private final String descripcion;

    public Categoria(String nombre, String descripcion) {

        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("La categoria necesita un nombre");
        }
        this.nombre = nombre.trim();
        this.descripcion = descripcion == null ? "" : descripcion.trim();
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
