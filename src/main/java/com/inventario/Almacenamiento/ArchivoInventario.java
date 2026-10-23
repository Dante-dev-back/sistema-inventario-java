package com.inventario.Almacenamiento;

import com.inventario.modelo.Categoria;
import com.inventario.modelo.Producto;
import com.inventario.modelo.ProductoConVencimiento;
import com.inventario.modelo.ProductoElectronico;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Guarda y carga el inventario en un archivo csv (separado por ';')
 */
public class ArchivoInventario {

    public final Path ruta;

    public ArchivoInventario(Path ruta) {
        this.ruta = ruta;
    }

    public boolean existe() {
        return Files.exists(ruta);
    }

    public void guardar(List<Producto> productos) throws IOException {
        List<String> lineas = new ArrayList<>();
        for (Producto p : productos) {
            lineas.add(p.toCsv());
        }
        Files.write(ruta, lineas);
    }

    public List<Producto> cargar() throws IOException {
        List<Producto> productos = new ArrayList<>();
        for (String linea : Files.readAllLines(ruta)) {
            if (linea.isBlank()) {
                continue;
            }
            productos.add(convertir(linea));
        }
        return productos;
    }

    private Producto convertir(String linea) {
        String[] c = linea.split(";", -1);
        if (c.length < 7) {
            throw new IllegalArgumentException("Linea inválida en el archivo: " + linea);
        }
        int id = Integer.parseInt(c[1]);
        String nombre = c[2];
        double precio = Double.parseDouble(c[3]);
        int cantidad = Integer.parseInt(c[4]);
        Categoria categoria = new Categoria(c[5], "");

        return switch (c[0]) {
            case "CON_VENCIMIENTO" ->
                    new ProductoConVencimiento(id, nombre, precio, cantidad, categoria, LocalDate.parse(c[6]));
            case "ELECTRONICO" ->
                    new ProductoElectronico(id, nombre, precio, cantidad, categoria, Integer.parseInt(c[6]));
            default -> throw new IllegalArgumentException("Tipo desconocido: " + c[0]);
        };
    }
}
