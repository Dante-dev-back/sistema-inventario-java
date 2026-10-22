package com.inventario.excepciones;

/**
 * Se muestra cuando intentas retirar mas unidades de las disponibles
 */
public class StockInsuficienteException extends Exception {
    public StockInsuficienteException(String nombreProducto, int disponible, int solicitado) {
        super("Stock insuficiente de '" + nombreProducto + "': disponible " + disponible
                + ", solicitado " + solicitado);
    }

}
