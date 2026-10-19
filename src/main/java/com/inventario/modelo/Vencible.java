package com.inventario.modelo;

/** Interfaz para los productos que tienen fecha de caducidad */
public interface Vencible {

    int DIAS_ALERTA = 7;

    long diasParaVencer();

    default boolean estaVencido(){
        return diasParaVencer() < 0;
    }

    default boolean estaPorVencer(){
        return !estaVencido() && diasParaVencer() <= DIAS_ALERTA;
    }
}
