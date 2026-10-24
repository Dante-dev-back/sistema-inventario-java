package com.inventario;

import com.inventario.excepciones.ProductoNoEncontradoException;
import com.inventario.excepciones.StockInsuficienteException;
import com.inventario.modelo.Categoria;
import com.inventario.modelo.Producto;
import com.inventario.modelo.ProductoConVencimiento;
import com.inventario.modelo.ProductoElectronico;
import com.inventario.servicio.Inventario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InventarioTest {

    private Inventario inventario;
    private final Categoria cat = new Categoria("General", "");

    @BeforeEach
    void preparar() {
        inventario = new Inventario();
        inventario.agregar(new ProductoElectronico(inventario.generarId(), "Laptop", 2500.00, 10, cat, 12));
        inventario.agregar(new ProductoConVencimiento(inventario.generarId(), "Leche", 4.5, 3, cat,
                LocalDate.now().plusDays(3)));
    }

    @Test
    void retirarStockDescuentaUnidades() throws Exception {
        inventario.retirarStock(1, 4);
        assertEquals(6, inventario.buscarPorId(1).getCantidad());
    }

    @Test
    void retirarMasDeLoDisponibleLanzaExcepcion() {
        assertThrows(StockInsuficienteException.class, () -> inventario.retirarStock(1, 99));
    }

    @Test
    void buscarIdInexistenteLanzaExcepcion() {
        assertThrows(ProductoNoEncontradoException.class, () -> inventario.buscarPorId(999));
    }

    @Test
    void detectaStockBajo() {
        assertEquals(1, inventario.productosConStockBajo().size());
        assertEquals("Leche", inventario.productosConStockBajo().get(0).getNombre());
    }

    @Test
    void detectarProductosPorVencer() {
        assertEquals(1, inventario.productosPorVencer().size());
    }

    @Test
    void calculaValorTotal() {
        assertEquals(25013.5, inventario.valorTotal(), 0.001);
    }

    @Test
    void precioNegativoNoEsPermitido() {
        assertThrows(IllegalArgumentException.class,
                () -> new ProductoElectronico(5, "x", -1, 1, cat, 6));
    }
    @Test
    void eliminarQuitalProducto() throws Exception{
        inventario.eliminar(1);
        assertEquals(1, inventario.listar().size());
    }
    @Test
    void masCarosVieneOrdenado(){
        Producto primero = inventario.masCaros(2).get(0);
        assertEquals("Laptop", primero.getNombre());
    }
}

