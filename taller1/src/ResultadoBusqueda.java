// ResultadoBusqueda.java
import java.io.Serializable;

public class ResultadoBusqueda implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private boolean exito;
    private String mensaje;
    private int posicion;
    private int comparaciones;
    
    public ResultadoBusqueda(boolean exito, String mensaje, int posicion, int comparaciones) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.posicion = posicion;
        this.comparaciones = comparaciones;
    }
    
    public boolean isExito() {
        return exito;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public int getPosicion() {
        return posicion;
    }
    
    public int getComparaciones() {
        return comparaciones;
    }
}