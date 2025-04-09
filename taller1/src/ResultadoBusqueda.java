//resultadobusqueda.java

public class ResultadoBusqueda extends ResultadoOperacion {
    @SuppressWarnings("FieldMayBeFinal")
    private int comparaciones;
    
    public ResultadoBusqueda(boolean exito, String mensaje, int posicion, int comparaciones) {
        super(exito, mensaje, posicion);
        this.comparaciones = comparaciones;
    }
    
    public int getComparaciones() {
        return comparaciones;
    }
}
