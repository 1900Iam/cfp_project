import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Clase principal para procesar los archivos de ventas y generar reportes.
 * Esta clase lee los archivos generados por GenerateInfoFiles y produce
 * reportes de vendedores y productos ordenados.
 *
 * Procesa archivos desde:
 * - datos/vendedores/vendedores.txt
 * - datos/productos/productos.txt
 * - datos/ventas/*.txt
 *
 * Genera reportes en:
 * - datos/reportes/reporte_vendedores.csv
 * - datos/reportes/reporte_productos.csv
 *
 * @author FABIAN ESTIBEN ROMERO VILLAMIL
 * @author JOHANNA LONDOÑO ALZATE
 * @author MAURICIO FIGUEREDO TORRES
 * @author SEBASTIAN GUTIERREZ ROJAS
 * @author WILLIAM CASTELLANOS CALDERÓN
 * @version v2.0.0
 */
public class Main {

    // Constantes para rutas de archivos (deben coincidir con GenerateInfoFiles)
    private static final String CARPETA_DATOS = "datos";
    private static final String CARPETA_VENDEDORES = CARPETA_DATOS + "/vendedores";
    private static final String CARPETA_PRODUCTOS = CARPETA_DATOS + "/productos";
    private static final String CARPETA_VENTAS = CARPETA_DATOS + "/ventas";
    private static final String CARPETA_REPORTES = CARPETA_DATOS + "/reportes";

    // Estructuras de datos para almacenar la información
    private static Map<Long, VendedorInfo> vendedores = new HashMap<>();
    private static Map<String, ProductoInfo> productos = new HashMap<>();

    // Formateador para números decimales
    private static final DecimalFormat formatoDecimal = new DecimalFormat("#.00");

    /**
     * Metodo principal que procesa los archivos de entrada y genera los reportes.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        try {
            System.out.println("=== PROCESADOR DE ARCHIVOS DE VENTAS ===");
            System.out.println("Iniciando procesamiento de archivos de ventas...\n");

            // Verificar que los archivos de entrada existan
            verificarArchivosEntrada();

            // Crear carpeta de reportes
            crearCarpetaReportes();
            System.out.println("✓ Carpeta de reportes preparada");

            // Procesar archivos de entrada
            leerArchivoVendedores();
            System.out.println("✓ Archivo de vendedores procesado (" + vendedores.size() + " vendedores)");

            leerArchivoProductos();
            System.out.println("✓ Archivo de productos procesado (" + productos.size() + " productos)");

            int archivosVentas = procesarArchivosVentas();
            System.out.println("✓ Archivos de ventas procesados (" + archivosVentas + " archivos)");

            // Generar reportes
            generarReporteVendedores();
            System.out.println("✓ Reporte de vendedores generado");

            generarReporteProductos();
            System.out.println("✓ Reporte de productos generado");

            // Mostrar resumen
            mostrarResumenProcesamiento();

        } catch (Exception e) {
            System.err.println("\n ERROR durante el procesamiento:");
            System.err.println("   " + e.getMessage());
            e.printStackTrace();
            System.err.println("\nVerifica que los archivos de entrada existan y tengan el formato correcto.");
            System.err.println("Ejecuta primero GenerateInfoFiles para crear los archivos necesarios.");
        }
    }

    /**
     * Verifica que los archivos de entrada necesarios existan.
     *
     * @throws IOException si algún archivo requerido no existe
     */
    private static void verificarArchivosEntrada() throws IOException {
        String archivoVendedores = CARPETA_VENDEDORES + "/vendedores.txt";
        String archivoProductos = CARPETA_PRODUCTOS + "/productos.txt";

        if (!Files.exists(Paths.get(archivoVendedores))) {
            throw new IOException("No se encontró el archivo: " + archivoVendedores);
        }

        if (!Files.exists(Paths.get(archivoProductos))) {
            throw new IOException("No se encontró el archivo: " + archivoProductos);
        }

        if (!Files.exists(Paths.get(CARPETA_VENTAS))) {
            throw new IOException("No se encontró la carpeta de ventas: " + CARPETA_VENTAS);
        }
    }

    /**
     * Crea la carpeta de reportes si no existe.
     *
     * @throws IOException si no se puede crear la carpeta
     */
    private static void crearCarpetaReportes() throws IOException {
        File carpetaReportes = new File(CARPETA_REPORTES);
        if (!carpetaReportes.exists() && !carpetaReportes.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta de reportes: " + CARPETA_REPORTES);
        }
    }

    /**
     * Lee y procesa el archivo de vendedores.
     * Formato esperado: TipoDocumento;NúmeroDocumento;NombresVendedor;ApellidosVendedor
     *
     * @throws IOException si ocurre un error al leer el archivo
     */
    private static void leerArchivoVendedores() throws IOException {
        String rutaArchivo = CARPETA_VENDEDORES + "/vendedores.txt";
        List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo));

        for (int numeroLinea = 0; numeroLinea < lineas.size(); numeroLinea++) {
            String linea = lineas.get(numeroLinea).trim();

            if (linea.isEmpty()) {
                continue; // Saltar líneas vacías
            }

            String[] partes = linea.split(";");
            if (partes.length == 4) {
                try {
                    String tipoDocumento = partes[0].trim();
                    long numeroDocumento = Long.parseLong(partes[1].trim());
                    String nombres = partes[2].trim();
                    String apellidos = partes[3].trim();

                    VendedorInfo vendedor = new VendedorInfo(tipoDocumento, numeroDocumento, nombres, apellidos);
                    vendedores.put(numeroDocumento, vendedor);

                } catch (NumberFormatException e) {
                    System.err.println("Advertencia: Error en línea " + (numeroLinea + 1) +
                            " del archivo vendedores.txt - Número de documento inválido");
                }
            } else {
                System.err.println("Advertencia: Formato incorrecto en línea " + (numeroLinea + 1) +
                        " del archivo vendedores.txt");
            }
        }

        if (vendedores.isEmpty()) {
            throw new IOException("No se pudo cargar ningún vendedor válido del archivo");
        }
    }

    /**
     * Lee y procesa el archivo de productos.
     * Formato esperado: IDProducto;NombreProducto;PrecioPorUnidadProducto
     *
     * @throws IOException si ocurre un error al leer el archivo
     */
    private static void leerArchivoProductos() throws IOException {
        String rutaArchivo = CARPETA_PRODUCTOS + "/productos.txt";
        List<String> lineas = Files.readAllLines(Paths.get(rutaArchivo));

        for (int numeroLinea = 0; numeroLinea < lineas.size(); numeroLinea++) {
            String linea = lineas.get(numeroLinea).trim();

            if (linea.isEmpty()) {
                continue; // Saltar líneas vacías
            }

            String[] partes = linea.split(";");
            if (partes.length == 3) {
                try {
                    String idProducto = partes[0].trim();
                    String nombreProducto = partes[1].trim();
                    double precio = Double.parseDouble(partes[2].trim());

                    if (precio < 0) {
                        System.err.println("Advertencia: Precio negativo en producto " + idProducto +
                                " - se usará valor absoluto");
                        precio = Math.abs(precio);
                    }

                    ProductoInfo producto = new ProductoInfo(idProducto, nombreProducto, precio);
                    productos.put(idProducto, producto);

                } catch (NumberFormatException e) {
                    System.err.println("Advertencia: Error en línea " + (numeroLinea + 1) +
                            " del archivo productos.txt - Precio inválido");
                }
            } else {
                System.err.println("Advertencia: Formato incorrecto en línea " + (numeroLinea + 1) +
                        " del archivo productos.txt");
            }
        }

        if (productos.isEmpty()) {
            throw new IOException("No se pudo cargar ningún producto válido del archivo");
        }
    }

    /**
     * Procesa todos los archivos de ventas en la carpeta de ventas.
     *
     * @return número de archivos procesados exitosamente
     * @throws IOException si ocurre un error al acceder a los archivos
     */
    private static int procesarArchivosVentas() throws IOException {
        File carpetaVentas = new File(CARPETA_VENTAS);
        File[] archivosVentas = carpetaVentas.listFiles((dir, name) -> name.endsWith(".txt"));

        if (archivosVentas == null) {
            throw new IOException("No se pudo acceder a la carpeta de ventas");
        }

        if (archivosVentas.length == 0) {
            throw new IOException("No se encontraron archivos de ventas en la carpeta");
        }

        int archivosExitosos = 0;
        for (File archivo : archivosVentas) {
            try {
                procesarArchivoVenta(archivo);
                archivosExitosos++;
            } catch (Exception e) {
                System.err.println("Error procesando " + archivo.getName() + ": " + e.getMessage());
            }
        }

        return archivosExitosos;
    }

    /**
     * Procesa un archivo individual de ventas de un vendedor.
     * Formato: Primera línea: TipoDocumento;NúmeroDocumento
     *         Líneas siguientes: IDProducto1;Cantidad1;IDProducto2;Cantidad2;...
     *
     * @param archivo archivo de ventas a procesar
     * @throws IOException si ocurre un error al leer el archivo
     */
    private static void procesarArchivoVenta(File archivo) throws IOException {
        List<String> lineas = Files.readAllLines(archivo.toPath());

        if (lineas.isEmpty()) {
            throw new IOException("El archivo " + archivo.getName() + " está vacío");
        }

        // Primera línea: información del vendedor
        String[] infoVendedor = lineas.get(0).split(";");
        if (infoVendedor.length < 2) {
            throw new IOException("Formato incorrecto en primera línea de " + archivo.getName());
        }

        long documentoVendedor;
        try {
            documentoVendedor = Long.parseLong(infoVendedor[1].trim());
        } catch (NumberFormatException e) {
            throw new IOException("Número de documento inválido en " + archivo.getName());
        }

        // Verificar que el vendedor exista
        VendedorInfo vendedor = vendedores.get(documentoVendedor);
        if (vendedor == null) {
            System.err.println("Advertencia: Vendedor " + documentoVendedor +
                    " no existe en archivo de vendedores (" + archivo.getName() + ")");
            return;
        }

        // Procesar líneas de ventas
        for (int i = 1; i < lineas.size(); i++) {
            String lineaVenta = lineas.get(i).trim();
            if (!lineaVenta.isEmpty()) {
                procesarLineaVenta(lineaVenta, documentoVendedor, archivo.getName(), i + 1);
            }
        }
    }

    /**
     * Procesa una línea de venta individual.
     *
     * @param lineaVenta línea con información de productos y cantidades
     * @param documentoVendedor documento del vendedor que hizo la venta
     * @param nombreArchivo nombre del archivo para mensajes de error
     * @param numeroLinea número de línea para mensajes de error
     */
    private static void procesarLineaVenta(String lineaVenta, long documentoVendedor,
                                           String nombreArchivo, int numeroLinea) {
        String[] elementos = lineaVenta.split(";");

        if (elementos.length % 2 != 0) {
            System.err.println("Advertencia: Número impar de elementos en línea " + numeroLinea +
                    " de " + nombreArchivo);
            return;
        }

        double totalVenta = 0.0;

        // Procesar pares de (IDProducto, Cantidad)
        for (int i = 0; i < elementos.length; i += 2) {
            if (i + 1 < elementos.length) {
                String idProducto = elementos[i].trim();

                try {
                    int cantidad = Integer.parseInt(elementos[i + 1].trim());

                    if (cantidad < 0) {
                        System.err.println("Advertencia: Cantidad negativa (" + cantidad +
                                ") en " + nombreArchivo + " línea " + numeroLinea +
                                " - se usará valor absoluto");
                        cantidad = Math.abs(cantidad);
                    }

                    // Verificar que el producto exista
                    ProductoInfo producto = productos.get(idProducto);
                    if (producto == null) {
                        System.err.println("Advertencia: Producto " + idProducto +
                                " no existe en archivo de productos (" + nombreArchivo +
                                " línea " + numeroLinea + ")");
                        continue;
                    }

                    // Actualizar cantidad vendida del producto
                    producto.agregarVenta(cantidad);

                    // Calcular valor de la venta
                    totalVenta += producto.getPrecio() * cantidad;

                } catch (NumberFormatException e) {
                    System.err.println("Advertencia: Cantidad inválida en " + nombreArchivo +
                            " línea " + numeroLinea + " para producto " + idProducto);
                }
            }
        }

        // Actualizar total de ventas del vendedor
        VendedorInfo vendedor = vendedores.get(documentoVendedor);
        if (vendedor != null) {
            vendedor.agregarVenta(totalVenta);
        }
    }

    /**
     * Genera el reporte de vendedores ordenado por ventas (mayor a menor).
     * Formato: NombresVendedor;ApellidosVendedor;TotalRecaudado
     *
     * @throws IOException si ocurre un error al escribir el archivo
     */
    private static void generarReporteVendedores() throws IOException {
        String rutaArchivo = CARPETA_REPORTES + "/reporte_vendedores.csv";

        // Convertir a lista y ordenar por ventas (mayor a menor)
        List<VendedorInfo> vendedoresOrdenados = new ArrayList<>(vendedores.values());
        vendedoresOrdenados.sort((v1, v2) -> Double.compare(v2.getTotalVentas(), v1.getTotalVentas()));

        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            // Escribir encabezado
            writer.println("NombresVendedor;ApellidosVendedor;TotalRecaudado");

            // Escribir datos de vendedores
            for (VendedorInfo vendedor : vendedoresOrdenados) {
                String linea = String.format("%s;%s;%s",
                        vendedor.getNombres(),
                        vendedor.getApellidos(),
                        formatoDecimal.format(vendedor.getTotalVentas())
                );
                writer.println(linea);
            }
        }

        System.out.println("   → Archivo creado: " + rutaArchivo);
    }

    /**
     * Genera el reporte de productos ordenado por cantidad vendida (mayor a menor).
     * Formato: NombreProducto;PrecioUnitario;CantidadVendida
     *
     * @throws IOException si ocurre un error al escribir el archivo
     */
    private static void generarReporteProductos() throws IOException {
        String rutaArchivo = CARPETA_REPORTES + "/reporte_productos.csv";

        // Convertir a lista y ordenar por cantidad vendida (mayor a menor)
        List<ProductoInfo> productosOrdenados = new ArrayList<>(productos.values());
        productosOrdenados.sort((p1, p2) -> Integer.compare(p2.getCantidadVendida(), p1.getCantidadVendida()));

        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            // Escribir encabezado
            writer.println("NombreProducto;PrecioUnitario;CantidadVendida");

            // Escribir datos de productos
            for (ProductoInfo producto : productosOrdenados) {
                String linea = String.format("%s;%s;%d",
                        producto.getNombreProducto(),
                        formatoDecimal.format(producto.getPrecio()),
                        producto.getCantidadVendida()
                );
                writer.println(linea);
            }
        }

        System.out.println("   → Archivo creado: " + rutaArchivo);
    }

    /**
     * Muestra un resumen detallado del procesamiento completado.
     */
    private static void mostrarResumenProcesamiento() {
        // Calcular estadísticas
        double totalRecaudado = vendedores.values().stream()
                .mapToDouble(VendedorInfo::getTotalVentas)
                .sum();

        int totalProductosVendidos = productos.values().stream()
                .mapToInt(ProductoInfo::getCantidadVendida)
                .sum();

        VendedorInfo mejorVendedor = vendedores.values().stream()
                .max((v1, v2) -> Double.compare(v1.getTotalVentas(), v2.getTotalVentas()))
                .orElse(null);

        ProductoInfo productoMasVendido = productos.values().stream()
                .max((p1, p2) -> Integer.compare(p1.getCantidadVendida(), p2.getCantidadVendida()))
                .orElse(null);

        // Mostrar resumen
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                PROCESAMIENTO COMPLETADO EXITOSAMENTE");
        System.out.println("=".repeat(60));

        System.out.println("\n Archivos de reporte generados:");
        System.out.println("   " + CARPETA_REPORTES + "/");
        System.out.println("   ├──  reporte_vendedores.csv");
        System.out.println("   └──  reporte_productos.csv");

        System.out.println("\n Estadísticas del procesamiento:");
        System.out.println("   • Total de vendedores: " + vendedores.size());
        System.out.println("   • Total de productos: " + productos.size());
        System.out.println("   • Total recaudado: $" + formatoDecimal.format(totalRecaudado));
        System.out.println("   • Total productos vendidos: " + totalProductosVendidos);

        if (mejorVendedor != null) {
            System.out.println("   • Mejor vendedor: " + mejorVendedor.getNombreCompleto() +
                    " ($" + formatoDecimal.format(mejorVendedor.getTotalVentas()) + ")");
        }

        if (productoMasVendido != null) {
            System.out.println("   • Producto más vendido: " + productoMasVendido.getNombreProducto() +
                    " (" + productoMasVendido.getCantidadVendida() + " unidades)");
        }

        System.out.println("\n Los reportes CSV están listos para su análisis");
        System.out.println("\n" + "=".repeat(60));
    }
}