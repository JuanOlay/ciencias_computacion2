public class ResultadoOperacion {
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
