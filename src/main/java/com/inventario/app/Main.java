package com.inventario.app;

import com.inventario.Almacenamiento.ArchivoInventario;
import com.inventario.excepciones.ProductoNoEncontradoException;
import com.inventario.excepciones.StockInsuficienteException;
import com.inventario.modelo.Categoria;
import com.inventario.modelo.Producto;
import com.inventario.modelo.ProductoConVencimiento;
import com.inventario.modelo.ProductoElectronico;
import com.inventario.servicio.Inventario;

import java.io.IOException;
import java.nio.file.Path;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    private static final Scanner teclado = new Scanner(System.in);
    private static final Inventario inventario = new Inventario();
    private static final ArchivoInventario archivo = new ArchivoInventario(Path.of("inventario.csv"));

    public static void main(String[] args) {
        cargarDatos();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Elige una opción: ");
            try {
                ejecutar(opcion);
            } catch (ProductoNoEncontradoException | StockInsuficienteException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("Dato invalido: " + e.getMessage());
            }
        } while (opcion != 0);

        guardarDatos();
        System.out.println("¡Hasta luego!");
    }

    private static void mostrarMenu() {
        System.out.println("\n===== SISTEMA DE INVENTARIO =====");
        System.out.println("1. Agregar producto");
        System.out.println("2. Listar productos");
        System.out.println("3. Buscar por nombre");
        System.out.println("4. Eliminar producto");
        System.out.println("5. Vender / retirar stock");
        System.out.println("6. Reponer stock");
        System.out.println("7. Alertas (stock bajo y vencimientos)");
        System.out.println("8. Reportes");
        System.out.println("0. Guardar y salir");
    }

    private static void ejecutar(int opcion)
            throws ProductoNoEncontradoException, StockInsuficienteException {
        switch (opcion) {
            case 1 -> agregarProducto();
            case 2 -> imprimir(inventario.ordenadosPorNombre());
            case 3 -> imprimir(inventario.buscarPorNombre(leerTexto("Nombre a buscar: ")));
            case 4 -> {
                inventario.eliminar(leerEntero("Id del producto: "));
                System.out.println("Producto eliminado.");
            }
            case 5 -> {
                inventario.retirarStrock(leerEntero("Id del producto: "), leerEntero("Unidades: "));
                System.out.println("Stock actualizado.");
            }
            case 6 -> {
                inventario.reponerStock(leerEntero("Id del producto: "), leerEntero("Unidades: "));
                System.out.println("Stock actualizado.");
            }
            case 7 -> mostrarAlertas();
            case 8 -> mostrarReportes();
            case 0 -> {
            }
            default -> System.out.println("Opción no válida.");
        }
    }

    private static void agregarProducto() {
        System.out.println("Tipo: 1)Caducable  2)Electrónico");
        int tipo = leerEntero("Tipo: ");
        if (tipo != 1 && tipo != 2) {
            System.out.println("Tipo no válido.");
            return;
        }
        String nombre = leerTexto("Nombre: ");
        double precio = leerDouble("Precio: ");
        int cantidad = leerEntero("Cantidad: ");
        Categoria categoria = new Categoria(leerTexto("Categoría: "), "");
        int id = inventario.generarId();

        if (tipo == 1) {
            try {
                LocalDate vence = LocalDate.parse(leerTexto("Fecha de vencimiento (AAAA-MM-DD): "));
                inventario.agregar(new ProductoConVencimiento(id, nombre, precio, cantidad, categoria, vence));
            } catch (DateTimeException e) {
                System.out.println("Fecha inválida. Usa el formato AAAA-MM-DD.");
                return;
            }
        } else {
            int garantia = leerEntero("Garantía (meses): ");
            inventario.agregar(new ProductoElectronico(id, nombre, precio, cantidad, categoria, garantia));
        }
        System.out.println("Producto agregado con id " + id + ".");
    }

    private static void mostrarAlertas() {
        System.out.println("\n--- Stock bajo (<= " + Producto.STOCK_MINIMO + " unidades) ---");
        imprimir(inventario.productosConStockBajo());
        System.out.println("\n--- Productos vencidos o por vencer ---");
        imprimir(inventario.productosPorVencer());
    }

    private static void mostrarReportes() {
        System.out.printf("%nValor total del inventario : S/ %.2f%n", inventario.valorTotal());

        System.out.println("\n--- Top 3 productos mas caros ---");
        imprimir(inventario.masCaros(3));

        System.out.println("\n--- Productos por categoría ---");
        for (Map.Entry<String, List<Producto>> e : inventario.agruparPorCategoria().entrySet()) {
            System.out.println(e.getKey() + ": " + e.getValue().size() + " producto(s)");
        }
    }

    // -------- Almacenamiento --------

    private static void cargarDatos() {
        if (!archivo.existe()) {
            return;
        }
        try {
            inventario.reemplazarTodo(archivo.cargar());
            System.out.println("Datos cargados desde inventario.csv");
        } catch (IOException | RuntimeException e) {
            System.out.println("No se pudo leer el archivo: " + e.getMessage());
        }
    }

    private static void guardarDatos() {
        try {
            archivo.guardar(inventario.listar());
            System.out.println("Datos guardados en inventario.csv");
        } catch (IOException e) {
            System.out.println("No se pudo guardar: " + e.getMessage());
        }
    }

    // -------- Utilidades de entrada -------

    private static void imprimir(List<Producto> lista) {
        if (lista.isEmpty()) {
            System.out.println("(sin resultados)");
            return;
        }
        lista.forEach(System.out::println);
    }

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                return Integer.parseInt(leerTexto(mensaje).trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número entero válido.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                return Double.parseDouble(leerTexto(mensaje).trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un número válido.");
            }
        }
    }
}
