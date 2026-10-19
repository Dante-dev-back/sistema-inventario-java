package com.inventario.excepciones;

// ** Se muestra cuando no se encuentra un producto con el id correcto  */
public class ProductoNoEncontradoException extends Exception{
    public ProductoNoEncontradoException(int id){
        super("No existe un producto con id " + id);
    }
}
