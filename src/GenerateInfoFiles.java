import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Clase para generar archivos de información pseudoaleatorios para el sistema de ventas.
 * Esta clase crea archivos de vendedores, productos y ventas que servirán como entrada
 * para el programa principal de análisis de ventas.
 *
 * Estructura de carpetas generada:
 * - datos/vendedores/vendedores.txt
 * - datos/productos/productos.txt
 * - datos/ventas/vendedor_[documento].txt (múltiples archivos)
 *
 * @author FABIAN ESTIBEN ROMERO VILLAMIL
 * @author JOHANNA LONDOÑO ALZATE
 * @author MAURICIO FIGUEREDO TORRES
 * @author SEBASTIAN GUTIERREZ ROJAS
 * @author WILLIAM CASTELLANOS CALDERÓN
 * @version v2.0.0
 */
public class GenerateInfoFiles {

    // constantes para rutas de archivos
    private static final String CARPETA_DATOS = "datos";
    private static final String CARPETA_VENDEDORES = CARPETA_DATOS + "/vendedores";
    private static final String CARPETA_PRODUCTOS = CARPETA_DATOS + "/productos";
    private static final String CARPETA_VENTAS = CARPETA_DATOS + "/ventas";

    // constantes para la generacion de datos
    private static final String[] NOMBRES = {
            "Juan", "María", "Carlos", "Ana", "Luis", "Carmen", "Pedro", "Laura",
            "Miguel", "Sofia", "Diego", "Valentina", "Andrés", "Camila", "Felipe",
            "Isabella", "Santiago", "Natalia", "Sebastián", "Alejandra", "Daniel",
            "Gabriela", "Ricardo", "Paola", "Fernando", "Andrea", "Jorge", "Daniela",
            "Alejandro", "Juliana", "Esteban", "Carolina", "Mauricio", "Catalina"
    };

    private static final String[] APELLIDOS = {
            "García", "Rodríguez", "López", "Martínez", "González", "Pérez", "Sánchez",
            "Ramírez", "Cruz", "Flores", "Gómez", "Díaz", "Reyes", "Morales", "Jiménez",
            "Herrera", "Medina", "Castro", "Vargas", "Ortiz", "Rubio", "Marín", "Castillo",
            "Iglesias", "Ruiz", "Torres", "Alvarez", "Gil", "Mendoza", "Vega", "Silva",
            "Guerrero", "Muñoz", "Rojas", "Delgado", "Aguilar", "Jiménez", "Moreno"
    };

    private static final String[] TIPOS_DOCUMENTO = {"CC", "CE", "TI"};

    private static final String[] PRODUCTOS = {
            "Laptop Dell Inspiron", "Mouse Inalámbrico Logitech", "Teclado Mecánico Gaming",
            "Monitor LED 24 Pulgadas", "Auriculares Bluetooth Sony", "Webcam HD 1080p",
            "Disco Duro Externo 1TB", "Memoria USB 32GB", "Tablet Samsung Galaxy",
            "Smartphone iPhone 13", "Cargador Universal USB-C", "Cable HDMI 2.0",
            "Impresora Multifuncional HP", "Router WiFi Dual Band", "Parlantes Bluetooth JBL",
            "Micrófono Condensador", "Silla Ergonómica Oficina", "Escritorio Ajustable",
            "Lámpara LED Escritorio", "Organizador Escritorio", "Mousepad Gaming XL",
            "Soporte Monitor Ajustable", "Hub USB 3.0", "Adaptador Ethernet USB",
            "Protector Pantalla Laptop", "Funda Laptop 15 Pulgadas", "Base Refrigerante",
            "Kit Limpiador Pantallas", "Batería Externa 10000mAh", "Adaptador HDMI VGA"
    };

    private static final Random random = new Random();

    // listas para mantener coherencia entre archivos
    private static List<Long> documentosVendedores = new ArrayList<>();
    private static List<String> idsProductos = new ArrayList<>();

    /**
     * Metodo principal que coordina la generación de todos los archivos necesarios.
     * Crea las carpetas necesarias y genera los archivos organizados por tipo.
     *
     * @param args argumentos de línea de comandos (no utilizados)
     */
    public static void main(String[] args) {
        try {
            System.out.println("=== GENERADOR DE ARCHIVOS DE INFORMACIÓN ===");
            System.out.println("Iniciando generación de archivos de información...\n");

            // crear estructura de carpetas
            crearEstructuraCarpetas();
            System.out.println("✓ Estructura de carpetas creada");

            // configurar parametros de generación
            int numeroVendedores = 15;
            int numeroProductos = 25;

            // generar archivo de información de vendedores
            createSalesManInfoFile(numeroVendedores);
            System.out.println("✓ Archivo de vendedores generado (" + numeroVendedores + " vendedores)");

            // generar archivo de productos
            createProductsFile(numeroProductos);
            System.out.println("✓ Archivo de productos generado (" + numeroProductos + " productos)");

            // generar archivos de ventas para cada vendedor
            int totalVentas = 0;
            for (int i = 0; i < numeroVendedores; i++) {
                long documentoVendedor = documentosVendedores.get(i);
                String nombreArchivo = "vendedor_" + documentoVendedor;
                int numeroVentas = random.nextInt(20) + 10; // Entre 10 y 29 ventas
                totalVentas += numeroVentas;
                createSalesMenFile(numeroVentas, nombreArchivo, documentoVendedor);
            }
            System.out.println("✓ Archivos de ventas generados (" + totalVentas + " ventas totales)");

            // mostrar resumen final
            mostrarResumenGeneracion(numeroVendedores, numeroProductos, totalVentas);

        } catch (Exception e) {
            System.err.println("\n ERROR durante la generación de archivos:");
            System.err.println("   " + e.getMessage());
            e.printStackTrace();
            System.err.println("\nVerifica los permisos de escritura en el directorio actual.");
        }
    }

    /**
     * Crea un archivo con información pseudoaleatoria de vendedores.
     * Formato: TipoDocumento;NúmeroDocumento;NombresVendedor;ApellidosVendedor
     *
     * @param salesmanCount número de vendedores a generar
     * @throws IOException si ocurre un error al escribir el archivo
     * @throws IllegalArgumentException si salesmanCount es menor o igual a 0
     */
    public static void createSalesManInfoFile(int salesmanCount) throws IOException {
        if (salesmanCount <= 0) {
            throw new IllegalArgumentException("El número de vendedores debe ser positivo");
        }

        Set<Long> documentosUsados = new HashSet<>();
        String rutaArchivo = CARPETA_VENDEDORES + "/vendedores.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (int i = 0; i < salesmanCount; i++) {
                // generar tipo de documento aleatorio
                String tipoDocumento = TIPOS_DOCUMENTO[random.nextInt(TIPOS_DOCUMENTO.length)];

                // generar número de documento único
                long numeroDocumento;
                do {
                    numeroDocumento = generateDocumentNumber(tipoDocumento);
                } while (documentosUsados.contains(numeroDocumento));

                documentosUsados.add(numeroDocumento);
                documentosVendedores.add(numeroDocumento);

                // generar nombres y apellidos aleatorios
                String nombres = generateRandomName();
                String apellidos = generateRandomLastName();

                String linea = String.format("%s;%d;%s;%s",
                        tipoDocumento, numeroDocumento, nombres, apellidos);
                writer.println(linea);
            }
        }

        System.out.println("   → Archivo creado: " + rutaArchivo);
    }

    /**
     * Crea un archivo con información pseudoaleatoria de productos.
     * Formato: IDProducto;NombreProducto;PrecioPorUnidadProducto
     *
     * @param productsCount número de productos a generar
     * @throws IOException si ocurre un error al escribir el archivo
     * @throws IllegalArgumentException si productsCount es inválido
     */
    public static void createProductsFile(int productsCount) throws IOException {
        if (productsCount <= 0) {
            throw new IllegalArgumentException("El número de productos debe ser positivo");
        }

        if (productsCount > PRODUCTOS.length) {
            throw new IllegalArgumentException("Máximo " + PRODUCTOS.length + " productos disponibles");
        }

        // generar IDs únicos para productos
        Set<String> idsUsados = new HashSet<>();
        List<String> productosDisponibles = new ArrayList<>();

        for (String producto : PRODUCTOS) {
            productosDisponibles.add(producto);
        }

        String rutaArchivo = CARPETA_PRODUCTOS + "/productos.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            for (int i = 0; i < productsCount; i++) {
                // Generar ID único
                String idProducto;
                do {
                    idProducto = "PROD" + String.format("%03d", random.nextInt(1000));
                } while (idsUsados.contains(idProducto));

                idsUsados.add(idProducto);
                idsProductos.add(idProducto);

                int indiceProducto = random.nextInt(productosDisponibles.size());
                String nombreProducto = productosDisponibles.remove(indiceProducto);

                double precio = (random.nextDouble() * 3485000) + 15000;
                precio = Math.round(precio / 1000) * 1000; // Redondear a miles

                String linea = String.format("%s;%s;%.0f",
                        idProducto, nombreProducto, precio);
                writer.println(linea);
            }
        }

        System.out.println("   → Archivo creado: " + rutaArchivo);
    }

    /**
     * Crea un archivo de ventas pseudoaleatorio para un vendedor específico.
     * Primera línea: TipoDocumentoVendedor;NúmeroDocumentoVendedor
     * Líneas siguientes: IDProducto1;CantidadProducto1;IDProducto2;CantidadProducto2;...
     *
     * @param randomSalesCount número de ventas a generar
     * @param name nombre base para el archivo
     * @param id número de documento del vendedor
     * @throws IOException si ocurre un error al escribir el archivo
     * @throws IllegalArgumentException si randomSalesCount es inválido
     * @throws IllegalStateException si no se ha generado el archivo de productos
     */
    public static void createSalesMenFile(int randomSalesCount, String name, long id) throws IOException {
        if (randomSalesCount <= 0) {
            throw new IllegalArgumentException("El número de ventas debe ser positivo");
        }

        if (idsProductos.isEmpty()) {
            throw new IllegalStateException("Debe generar el archivo de productos primero");
        }

        String rutaArchivo = CARPETA_VENTAS + "/" + name + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            String tipoDocumento = getTipoDocumentoById(id);
            writer.println(tipoDocumento + ";" + id);

            // Generar las ventas
            for (int i = 0; i < randomSalesCount; i++) {
                List<String> ventaLinea = new ArrayList<>();

                int productosEnVenta = random.nextInt(6) + 1;
                Set<String> productosUsados = new HashSet<>();

                for (int j = 0; j < productosEnVenta; j++) {
                    String idProducto;
                    do {
                        idProducto = idsProductos.get(random.nextInt(idsProductos.size()));
                    } while (productosUsados.contains(idProducto));

                    productosUsados.add(idProducto);

                    int cantidad = random.nextInt(25) + 1;

                    ventaLinea.add(idProducto + ";" + cantidad);
                }

                writer.println(String.join(";", ventaLinea));
            }
        }
    }

    /**
     * Genera un número de documento aleatorio según el tipo.
     * CC: 10,000,000 - 99,999,999
     * CE: 1,000,000 - 9,999,999
     * TI: 1,000,000,000 - 1,999,999,999
     *
     * @param tipoDocumento tipo de documento (CC, CE, TI)
     * @return número de documento generado
     */
    private static long generateDocumentNumber(String tipoDocumento) {
        switch (tipoDocumento) {
            case "CC":

                return random.nextInt(90000000) + 10000000L;
            case "CE":
                return random.nextInt(9000000) + 1000000L;
            case "TI":
                return random.nextInt(1000000000) + 1000000000L;
            default:
                return random.nextInt(90000000) + 10000000L;
        }
    }

    /**
     * Genera un nombre aleatorio, posiblemente compuesto.
     * 30% de probabilidad de tener segundo nombre.
     *
     * @return nombre generado
     */
    private static String generateRandomName() {
        String primerNombre = NOMBRES[random.nextInt(NOMBRES.length)];

        if (random.nextDouble() < 0.3) {
            String segundoNombre;
            do {
                segundoNombre = NOMBRES[random.nextInt(NOMBRES.length)];
            } while (segundoNombre.equals(primerNombre));
            return primerNombre + " " + segundoNombre;
        }

        return primerNombre;
    }

    /**
     * Genera apellidos aleatorios (siempre dos apellidos diferentes).
     *
     * @return apellidos generados
     */
    private static String generateRandomLastName() {
        String primerApellido = APELLIDOS[random.nextInt(APELLIDOS.length)];
        String segundoApellido;

        do {
            segundoApellido = APELLIDOS[random.nextInt(APELLIDOS.length)];
        } while (segundoApellido.equals(primerApellido));

        return primerApellido + " " + segundoApellido;
    }

    /**
     * Obtiene el tipo de documento basado en el rango del número de documento.
     *
     * @param id número de documento
     * @return tipo de documento correspondiente
     */
    private static String getTipoDocumentoById(long id) {
        if (id >= 1000000000L) {
            return "TI";
        } else if (id >= 10000000L) {
            return "CC";
        } else {
            return "CE";
        }
    }

    /**
     * Crea la estructura de carpetas necesaria para organizar los archivos.
     * Crea las carpetas: datos/vendedores, datos/productos, datos/ventas
     *
     * @throws IOException si ocurre un error al crear las carpetas
     */
    private static void crearEstructuraCarpetas() throws IOException {
        File carpetaDatos = new File(CARPETA_DATOS);
        File carpetaVendedores = new File(CARPETA_VENDEDORES);
        File carpetaProductos = new File(CARPETA_PRODUCTOS);
        File carpetaVentas = new File(CARPETA_VENTAS);

        if (!carpetaDatos.exists() && !carpetaDatos.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta: " + CARPETA_DATOS);
        }

        if (!carpetaVendedores.exists() && !carpetaVendedores.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta: " + CARPETA_VENDEDORES);
        }

        if (!carpetaProductos.exists() && !carpetaProductos.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta: " + CARPETA_PRODUCTOS);
        }

        if (!carpetaVentas.exists() && !carpetaVentas.mkdirs()) {
            throw new IOException("No se pudo crear la carpeta: " + CARPETA_VENTAS);
        }
    }

    /**
     * Muestra un resumen detallado de la generación completada.
     *
     * @param numeroVendedores cantidad de vendedores generados
     * @param numeroProductos cantidad de productos generados
     * @param totalVentas total de ventas generadas
     */
    private static void mostrarResumenGeneracion(int numeroVendedores, int numeroProductos, int totalVentas) {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("           GENERACIÓN COMPLETADA EXITOSAMENTE");
        System.out.println("=".repeat(50));

        System.out.println("\n Estadísticas de generación:");
        System.out.println("   • Vendedores generados: " + numeroVendedores);
        System.out.println("   • Productos generados: " + numeroProductos);
        System.out.println("   • Total de ventas: " + totalVentas);
        System.out.println("   • Promedio ventas/vendedor: " + (totalVentas / numeroVendedores));

    }
}