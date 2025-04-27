//talahashcontrolador.java

import java.util.List;
import javax.swing.JOptionPane;  
import java.io.Serializable;


public class TablaHashControlador {
    private TablaHashApp vista;
    private TablaHash modelo;
    private String metodoHash;
    private String metodoInsercion;
    private String metodoColision;

    public TablaHashControlador(TablaHashApp vista) {
        this.vista = vista;
    }

    public void crearTablaHash(int numClaves, int digitosClaves, String metodoHash, String metodoInsercion, String metodoColision) {
        try {
            if (numClaves <= 0 || digitosClaves <= 0) {
                vista.mostrarMensaje("Error: Los valores deben ser mayores que 0.");
                return;
            }

            // Guardar los métodos seleccionados
            this.metodoHash = metodoHash;
            this.metodoInsercion = metodoInsercion;
            this.metodoColision = metodoColision;
            
            // Crear el modelo de tabla hash
            modelo = new TablaHash(numClaves, digitosClaves, metodoInsercion, metodoColision);
            vista.mostrarMensaje("Tabla hash creada con éxito. Tamaño: " + numClaves + 
                              ", Dígitos por clave: " + digitosClaves + 
                              ", Método Hash: " + metodoHash + 
                              ", Método Inserción: " + metodoInsercion + 
                              ", Método Colisión: " + metodoColision);

        } catch (Exception ex) {
            vista.mostrarMensaje("Error al crear la tabla hash: " + ex.getMessage());
        }
    }

    public ResultadoOperacion insertarClave(int clave, String metodo) {
        if (modelo == null) {
            vista.mostrarMensaje("Error: Primero debe crear la tabla hash.");
            return new ResultadoOperacion(false, "Error: Tabla hash no inicializada.", -1);
        }

        // Validar que se use el método hash seleccionado al crear la tabla
        if (!metodo.equals(metodoHash)) {
            String mensaje = "Error: Debe usar el método '" + metodoHash + "' seleccionado al crear la tabla.";
            vista.mostrarMensaje(mensaje);
            return new ResultadoOperacion(false, mensaje, -1);
        }

        // Validar clave
        String claveStr = String.valueOf(clave);
        if (claveStr.length() > modelo.getDigitosClaves()) {
            String mensaje = "Error: La clave debe tener máximo " + modelo.getDigitosClaves() + " dígitos.";
            vista.mostrarMensaje(mensaje);
            return new ResultadoOperacion(false, mensaje, -1);
        }

        // Insertar en el modelo
        ResultadoOperacion resultado = modelo.insertarClave(clave, metodo);
        vista.mostrarMensaje(resultado.getMensaje());

        return resultado;
    }

    public ResultadoBusqueda buscarClave(int clave) {
        if (modelo == null) {
            vista.mostrarMensaje("Error: Primero debe crear la tabla hash.");
            return new ResultadoBusqueda(false, "Error: Tabla hash no inicializada.", -1, 0);
        }

        // Usar el método de búsqueda correspondiente al método de inserción
        return modelo.buscarClave(clave, metodoHash);
    }

    public ResultadoOperacion borrarClave(int clave) {
        if (modelo == null) {
            vista.mostrarMensaje("Error: Primero debe crear la tabla hash.");
            return new ResultadoOperacion(false, "Error: Tabla hash no inicializada.", -1);
        }

        return modelo.borrarClave(clave, metodoHash);
    }

    public TablaHash getTablaHash() {
        return modelo;
    }
    
    public String getMetodoHash() {
        return metodoHash;
    }
    
    public String getMetodoInsercion() {
        return metodoInsercion;
    }
    
    public String getMetodoColision() {
        return metodoColision;
    }
    
    public void setModelo(TablaHash modelo) {
        this.modelo = modelo;
    }
    
    public void setMetodoHash(String metodoHash) {
        this.metodoHash = metodoHash;
    }
    
    public void setMetodoInsercion(String metodoInsercion) {
        this.metodoInsercion = metodoInsercion;
    }
    
    public void setMetodoColision(String metodoColision) {
        this.metodoColision = metodoColision;
    }
    
    // Guardar datos
    public void guardarDatos(List<Integer> clavesOriginales) {
        if (modelo != null) {
            PersistenciaUtil.guardarDatos(modelo, clavesOriginales, metodoHash);
            vista.mostrarMensaje("Datos guardados correctamente.");
        }
    }
    
    // Cargar datos
    public boolean cargarDatos() {
        if (PersistenciaUtil.existenDatosGuardados()) {
            PersistenciaUtil.DatosAplicacion datos = PersistenciaUtil.cargarDatos();
            if (datos != null) {
                this.modelo = datos.getTablaHash();
                this.metodoHash = datos.getMetodoHash();
                this.metodoInsercion = modelo.getMetodoInsercion();
                this.metodoColision = modelo.getMetodoColision();
                vista.mostrarMensaje("Datos cargados correctamente.");
                return true;
            }
        }
        return false;
    }
}