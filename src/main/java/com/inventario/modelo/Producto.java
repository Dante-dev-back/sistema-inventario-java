package com.inventario.modelo;

import com.inventario.excepciones.StockInsuficienteException;

public abstract class Producto {

    public static final int STOCK_MINIMO = 5;

    private final int id;
    private String nombre;
    private double precio;
    private int cantidad;
    private Categoria categoria;

    protected Producto(int id, String nombre, double precio, int cantidad, Categoria categoria) {
        this.id = id;
        setNombre(nombre);
        setPrecio(precio);
        if (cantidad < 0){
            throw new IllegalArgumentException("La cantidad no puede ser negativa");
        }
        this.cantidad = cantidad;
        this.categoria = categoria;
    }

    // -------- Comportamiento --------

    public void agregarStock(int unidades){
        if (unidades <= 0){
            throw new IllegalArgumentException("Las unidades deber ser mayores a 0");
        }
        cantidad += unidades;
    }

    public void retirarStock(int unidades) throws StockInsuficienteException {
        if (unidades <= 0){
            throw new IllegalArgumentException("Las unidades deben ser mayores a 0");
        }
        if (unidades > cantidad){
            throw new StockInsuficienteException(nombre, cantidad, unidades);
        }
        cantidad += unidades;
    }

    public boolean tieneStockBajo(){
        return cantidad <= STOCK_MINIMO;
    }
    public double valorTotal(){
        return precio * cantidad;
    }

    // -------- Polimorfismo --------

    public abstract String getTipo();

    /** Texto con los datos de cada producto */
    public abstract String getDetalle();

    /** Dato extra que se guarda en el CSV */
    protected abstract String getExtraCsv();

    public String toCsv(){
        return String.join(";", getTipo(), String.valueOf(id), nombre, String.valueOf(precio),
        String.valueOf(cantidad), categoria.getNombre(), getExtraCsv());
    }

    // --------- Getters y Setters --------

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()){
            throw new IllegalArgumentException("El nombre no puede estar vacio");
        }
        // Se quita para ';' para no romper el formato csv
        this.nombre = nombre.trim().replace(";", ",");
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        if (precio < 0){
            throw new IllegalArgumentException("El precio no puede ser negativo");
        }
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return String.format("[%d] %-22s %-11s s/ %9.2f x%-4d %-12s %s",
        id, nombre, getTipo(), precio, cantidad, categoria, getDetalle());
    }
}
