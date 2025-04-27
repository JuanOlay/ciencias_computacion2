// persistenciaUtil.java

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PersistenciaUtil {
    private static final String ARCHIVO_DATOS = "tabla_hash_datos.dat";
    
    // Guardar datos de la aplicación
    public static void guardarDatos(TablaHash tablaHash, List<Integer> clavesOriginales, String metodoHash) {
        try (ObjectOutputStream out = new ObjectOutputStream(
                new FileOutputStream(ARCHIVO_DATOS))) {
            
            DatosAplicacion datos = new DatosAplicacion(tablaHash, clavesOriginales, metodoHash);
            out.writeObject(datos);
            
        } catch (IOException e) {
            System.err.println("Error al guardar los datos: " + e.getMessage());
        }
    }
    
    // Cargar datos de la aplicación
    public static DatosAplicacion cargarDatos() {
        try (ObjectInputStream in = new ObjectInputStream(
                new FileInputStream(ARCHIVO_DATOS))) {
            
            return (DatosAplicacion) in.readObject();
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("No se encontraron datos guardados o error al cargar: " + e.getMessage());
            return null;
        }
    }
    
    // Verificar si existen datos guardados
    public static boolean existenDatosGuardados() {
        File archivo = new File(ARCHIVO_DATOS);
        return archivo.exists() && archivo.length() > 0;
    }
    
    // Clase para almacenar todos los datos necesarios
    public static class DatosAplicacion implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private TablaHash tablaHash;
        private List<Integer> clavesOriginales;
        private String metodoHash;
        
        public DatosAplicacion(TablaHash tablaHash, List<Integer> clavesOriginales, String metodoHash) {
            this.tablaHash = tablaHash;
            this.clavesOriginales = clavesOriginales;
            this.metodoHash = metodoHash;
        }
        
        public TablaHash getTablaHash() {
            return tablaHash;
        }
        
        public List<Integer> getClavesOriginales() {
            return clavesOriginales;
        }
        
        public String getMetodoHash() {
            return metodoHash;
        }
    }
}