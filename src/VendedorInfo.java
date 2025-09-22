/**
 * Clase para almacenar la información de un vendedor y sus ventas totales.
 * Contiene los datos personales del vendedor y el total de dinero recaudado.
 *
 * @author FABIAN ESTIBEN ROMERO VILLAMIL
 * @author JOHANNA LONDOÑO ALZATE
 * @author MAURICIO FIGUEREDO TORRES
 * @author SEBASTIAN GUTIERREZ ROJAS
 * @author WILLIAM CASTELLANOS CALDERÓN
 * @version v2.0.0
 */
public class VendedorInfo {

    /** Tipo de documento del vendedor (CC, CE, TI) */
    private String tipoDocumento;

    /** Número de documento único del vendedor */
    private long numeroDocumento;

    /** Nombres del vendedor */
    private String nombres;

    /** Apellidos del vendedor */
    private String apellidos;

    /** Total de dinero recaudado por el vendedor */
    private double totalVentas;

    /**
     * Constructor para crear un objeto VendedorInfo.
     *
     * @param tipoDocumento tipo de documento (CC, CE, TI)
     * @param numeroDocumento número de documento único
     * @param nombres nombres del vendedor
     * @param apellidos apellidos del vendedor
     */
    public VendedorInfo(String tipoDocumento, long numeroDocumento, String nombres, String apellidos) {
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.totalVentas = 0.0;
    }

    // Getters

    /**
     * Obtiene el tipo de documento del vendedor.
     *
     * @return tipo de documento
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * Obtiene el número de documento del vendedor.
     *
     * @return número de documento
     */
    public long getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * Obtiene los nombres del vendedor.
     *
     * @return nombres del vendedor
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Obtiene los apellidos del vendedor.
     *
     * @return apellidos del vendedor
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Obtiene el total de ventas del vendedor.
     *
     * @return total de dinero recaudado
     */
    public double getTotalVentas() {
        return totalVentas;
    }

    // Setters

    /**
     * Establece el total de ventas del vendedor.
     *
     * @param totalVentas nuevo total de ventas
     */
    public void setTotalVentas(double totalVentas) {
        this.totalVentas = totalVentas;
    }

    /**
     * Añade un monto al total de ventas del vendedor.
     *
     * @param monto monto a añadir al total
     */
    public void agregarVenta(double monto) {
        this.totalVentas += monto;
    }

    /**
     * Obtiene el nombre completo del vendedor (nombres + apellidos).
     *
     * @return nombre completo
     */
    public String getNombreCompleto() {
        return nombres + " " + apellidos;
    }

    /**
     * Representación en cadena del vendedor.
     *
     * @return información del vendedor como cadena
     */
    @Override
    public String toString() {
        return String.format("VendedorInfo{documento=%s-%d, nombre='%s', apellidos='%s', totalVentas=%.2f}",
                tipoDocumento, numeroDocumento, nombres, apellidos, totalVentas);
    }
}