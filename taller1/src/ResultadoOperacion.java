//resultadooperacion.java

public class ResultadoOperacion {
    @SuppressWarnings("FieldMayBeFinal")
    private boolean exito;
    @SuppressWarnings("FieldMayBeFinal")
    private String mensaje;
    @SuppressWarnings("FieldMayBeFinal")
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
