// ResultadoOperacion.java
import java.io.Serializable;

public class ResultadoOperacion implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private boolean exito;
    private String mensaje;
    private int posicion;
    
    public ResultadoOperacion(boolean exito, String mensaje, int posicion) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.posicion = posicion;
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
}