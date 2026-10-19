package com.inventario.excepciones;

public class StockInsuficienteException extends Exception{
    public StockInsuficienteException(String nombreProducto, int disponible, int solicitado){
        super("Stock insuficiente de '" + nombreProducto + "': disponible " + disponible
                + ", solicitado " + solicitado);
    }

}
