// tablahashcontrolador.java

public class TablaHashControlador {
    @SuppressWarnings("FieldMayBeFinal")
    private TablaHashApp vista;
    private TablaHash modelo;
    private String metodoHash;

    public TablaHashControlador(TablaHashApp vista) {
        this.vista = vista;
    }

    public void crearTablaHash(int numClaves, int digitosClaves, String metodoHash) {
        try {
            if (numClaves <= 0 || digitosClaves <= 0) {
                vista.mostrarMensaje("Error: Los valores deben ser mayores que 0.");
                return;
            }

            // Guardar el método hash seleccionado
            this.metodoHash = metodoHash;
            
            // Crear el modelo de tabla hash
            modelo = new TablaHash(numClaves, digitosClaves);
            vista.mostrarMensaje("Tabla hash creada con éxito. Tamaño: " + numClaves + 
                              ", Dígitos por clave: " + digitosClaves + 
                              ", Método: " + metodoHash);

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

    public ResultadoBusqueda buscarClaveHash(int clave, String metodo) {
        if (modelo == null) {
            vista.mostrarMensaje("Error: Primero debe crear la tabla hash.");
            return new ResultadoBusqueda(false, "Error: Tabla hash no inicializada.", -1, 0);
        }

        // Validar que se use el método hash seleccionado al crear la tabla
        if (!metodo.equals(metodoHash)) {
            String mensaje = "Error: Debe usar el método '" + metodoHash + "' seleccionado al crear la tabla.";
            vista.mostrarMensaje(mensaje);
            return new ResultadoBusqueda(false, mensaje, -1, 0);
        }

        return modelo.buscarClaveHash(clave, metodo);
    }

    public ResultadoBusqueda buscarClaveLineal(int clave) {
        if (modelo == null) {
            vista.mostrarMensaje("Error: Primero debe crear la tabla hash.");
            return new ResultadoBusqueda(false, "Error: Tabla hash no inicializada.", -1, 0);
        }

        return modelo.buscarClaveLineal(clave);
    }

    public ResultadoBusqueda buscarClaveBinaria(int clave) {
        if (modelo == null) {
            vista.mostrarMensaje("Error: Primero debe crear la tabla hash.");
            return new ResultadoBusqueda(false, "Error: Tabla hash no inicializada.", -1, 0);
        }

        return modelo.buscarClaveBinaria(clave);
    }

    public ResultadoOperacion borrarClave(int clave, String metodo) {
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

        return modelo.borrarClave(clave, metodo);
    }

    public TablaHash getTablaHash() {
        return modelo;
    }
    
    public String getMetodoHash() {
        return metodoHash;
    }
}