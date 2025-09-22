/**
 * Clase para almacenar la información de un producto y sus ventas totales.
 * Contiene los datos del producto y la cantidad total vendida.
 *
 * @author FABIAN ESTIBEN ROMERO VILLAMIL
 * @author JOHANNA LONDOÑO ALZATE
 * @author MAURICIO FIGUEREDO TORRES
 * @author SEBASTIAN GUTIERREZ ROJAS
 * @author WILLIAM CASTELLANOS CALDERÓN
 * @version v2.0.0
 */
public class ProductoInfo {

    /** ID único del producto */
    private String idProducto;

    /** Nombre descriptivo del producto */
    private String nombreProducto;

    /** Precio por unidad del producto */
    private double precio;

    /** Cantidad total vendida del producto */
    private int cantidadVendida;

    /**
     * Constructor para crear un objeto ProductoInfo.
     *
     * @param idProducto ID único del producto
     * @param nombreProducto nombre descriptivo del producto
     * @param precio precio por unidad del producto
     */
    public ProductoInfo(String idProducto, String nombreProducto, double precio) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.precio = precio;
        this.cantidadVendida = 0;
    }

    // Getters

    /**
     * Obtiene el ID del producto.
     *
     * @return ID del producto
     */
    public String getIdProducto() {
        return idProducto;
    }

    /**
     * Obtiene el nombre del producto.
     *
     * @return nombre del producto
     */
    public String getNombreProducto() {
        return nombreProducto;
    }

    /**
     * Obtiene el precio por unidad del producto.
     *
     * @return precio por unidad
     */
    public double getPrecio() {
        return precio;
    }

    /**
     * Obtiene la cantidad total vendida del producto.
     *
     * @return cantidad vendida
     */
    public int getCantidadVendida() {
        return cantidadVendida;
    }

    // Setters

    /**
     * Establece la cantidad vendida del producto.
     *
     * @param cantidadVendida nueva cantidad vendida
     */
    public void setCantidadVendida(int cantidadVendida) {
        this.cantidadVendida = cantidadVendida;
    }

    /**
     * Añade una cantidad a la cantidad total vendida.
     *
     * @param cantidad cantidad a añadir
     */
    public void agregarVenta(int cantidad) {
        this.cantidadVendida += cantidad;
    }

    /**
     * Calcula el total de ingresos generados por este producto.
     *
     * @return total de ingresos (precio * cantidad vendida)
     */
    public double getTotalIngresos() {
        return precio * cantidadVendida;
    }

    /**
     * Representación en cadena del producto.
     *
     * @return información del producto como cadena
     */
    @Override
    public String toString() {
        return String.format("ProductoInfo{id='%s', nombre='%s', precio=%.2f, cantidadVendida=%d}",
                idProducto, nombreProducto, precio, cantidadVendida);
    }
}