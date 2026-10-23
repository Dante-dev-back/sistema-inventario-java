package com.inventario.servicio;

import com.inventario.excepciones.ProductoNoEncontradoException;
import com.inventario.excepciones.StockInsuficienteException;
import com.inventario.modelo.Producto;
import com.inventario.modelo.Vencible;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Inventario {

    private final List<Producto> productos = new ArrayList<>();
    private int siguienteId = 1;

    // --------- Operaciones basicas --------

    public int generarId() {
        return siguienteId++;
    }

    public void agregar(Producto producto) {
        productos.add(producto);
        siguienteId = Math.max(siguienteId, producto.getId() + 1);
    }

    public void eliminar(int id) throws ProductoNoEncontradoException {
        productos.remove(buscarPorId(id));
    }

    public Producto buscarPorId(int id) throws ProductoNoEncontradoException {
        return productos.stream()
                .filter(p -> p.getId() == id)
                .findFirst()
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
    }

    public List<Producto> buscarPorNombre(String texto) {
        String filtro = texto.toLowerCase();
        return productos.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(filtro))
                .collect(Collectors.toList());
    }

    public List<Producto> listar() {
        return Collections.unmodifiableList(productos);
    }

    // --------- Lógica de negocio --------

    public void retirarStrock(int id, int unidades)
            throws ProductoNoEncontradoException, StockInsuficienteException {
        buscarPorId(id).retirarStock(unidades);
    }

    public void reponerStock(int id, int unidades) throws ProductoNoEncontradoException {
        buscarPorId(id).agregarStock(unidades);
    }

    public List<Producto> productosConStockBajo() {
        return productos.stream()
                .filter(Producto::tieneStockBajo)
                .collect(Collectors.toList());
    }

    // --------- polimorfismo / interfaces --------

    public List<Producto> productosPorVencer() {
        return productos.stream()
                .filter(p -> p instanceof Vencible v && (v.estaPorVencer() || v.estaVencido()))
                .collect(Collectors.toList());
    }

    // -------- Reportes con Streams --------

    public double valorTotal() {
        return productos.stream().mapToDouble(Producto::valorTotal).sum();
    }

    public List<Producto> masCaros(int limite) {
        return productos.stream()
                .sorted(Comparator.comparingDouble(Producto::getPrecio).reversed())
                .limit(limite)
                .collect(Collectors.toList());
    }

    public List<Producto> ordenadosPorNombre() {
        return productos.stream()
                .sorted(Comparator.comparing(Producto::getNombre, String.CASE_INSENSITIVE_ORDER))
                .collect(Collectors.toList());
    }

    public Map<String, List<Producto>> agruparPorCategoria() {
        return productos.stream()
                .collect(Collectors.groupingBy(p -> p.getCategoria().getNombre()));
    }

    // -------- Usado por la Persistencia --------
    public void reemplazarTodo(List<Producto> nuevos) {
        productos.clear();
        siguienteId = 1;
        nuevos.forEach(this::agregar);
    }
}
