package com.inventario.modelo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ProductoConVencimiento extends Producto implements Vencible {

    private final LocalDate fechaVencimiento;

    public ProductoConVencimiento(int id, String nombre, double precio, int cantidad,
                                     Categoria categoria, LocalDate fechaVencimiento) {
        super(id, nombre, precio, cantidad, categoria);
        if (fechaVencimiento == null) {
            throw new IllegalArgumentException("Falta la fecha de vencimiento");
        }
        this.fechaVencimiento = fechaVencimiento;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    @Override
    public long diasParaVencer() {
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
    }

    @Override
    public String getTipo() {
        return "CON_VENCIMIENTO";
    }

    @Override
    public String getDetalle() {
        String estado = estaVencido() ? " (VENCIDO) " : estaPorVencer() ? " (por vencer) " : "";
        return "Vence: " + fechaVencimiento + estado;
    }

    @Override
    protected String getExtraCsv() {
        return fechaVencimiento.toString();
    }
}
