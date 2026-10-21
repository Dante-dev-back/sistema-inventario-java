package com.inventario.modelo;

public class ProductoElectronico extends Producto {

    private final int garantiaMeses;

    public ProductoElectronico(int id, String nombre, double precio, int cantidad, Categoria categoria, int garantiaMeses) {
        super(id, nombre, precio, cantidad, categoria);
        if (garantiaMeses < 0) {
            throw new IllegalArgumentException("La garantia no puede ser negativa");
        }
        this.garantiaMeses = garantiaMeses;
    }

    public int getGarantiaMeses() {
        return garantiaMeses;
    }

    @Override
    public String getTipo() {
        return "ELECTRONICO";
    }

    @Override
    public String getDetalle() {
        return "GARANTIA" + garantiaMeses + " meses";
    }

    @Override
    protected String getExtraCsv() {
        return String.valueOf(garantiaMeses);
    }
}
