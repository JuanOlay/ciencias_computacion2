import javax.swing.table.DefaultTableModel;

public class TablaHashControlador {
    private TablaHashApp vista;
    private TablaHash modelo;
    
    public TablaHashControlador(TablaHashApp vista) {
        this.vista = vista;
    }
    
    public void crearTablaHash() {
        try {
            int numClaves = Integer.parseInt(vista.getTxtNumClaves().getText());
            int digitosClaves = Integer.parseInt(vista.getTxtDigitosClaves().getText());
            
            if (numClaves <= 0 || digitosClaves <= 0) {
                vista.mostrarMensaje("Error: Los valores deben ser mayores que 0.");
                return;
            }
            
            // Crear el modelo de tabla hash
            modelo = new TablaHash(numClaves, digitosClaves);
            
            // Actualizar modelo de tabla en la vista (índices mostrados de 1 a n)
            DefaultTableModel modeloTabla = vista.getModeloTabla();
            modeloTabla.setRowCount(0);
            for (int i = 0; i < numClaves; i++) {
                modeloTabla.addRow(new Object[]{i + 1, "Vacío"});
            }
            
            vista.habilitarBotones(true);
            vista.mostrarMensaje("Tabla hash creada con éxito.");
        } catch (NumberFormatException ex) {
            vista.mostrarMensaje("Error: Ingrese valores numéricos válidos.");
        }
    }
    
    public void insertarClave() {
        if (modelo == null) {
            vista.mostrarMensaje("Error: Primero debe crear la tabla hash.");
            return;
        }
        
        try {
            String claveStr = vista.getTxtClave().getText();
            if (!validarClave(claveStr)) {
                return;
            }
            
            int clave = Integer.parseInt(claveStr);
            String metodo = (String) vista.getComboMetodo().getSelectedItem();
            
            // Insertar en el modelo
            ResultadoOperacion resultado = modelo.insertarClave(clave, metodo);
            
            // Actualizar vista (se suma 1 a la posición para mostrar índices del 1 a n)
            if (resultado.isExito()) {
                vista.getModeloTabla().setValueAt(clave, resultado.getPosicion(), 1);
            }
            
            vista.mostrarMensaje(resultado.getMensaje());
        } catch (NumberFormatException ex) {
            vista.mostrarMensaje("Error: Ingrese una clave numérica válida.");
        }
    }
    
    public void buscarClave() {
        if (modelo == null) {
            vista.mostrarMensaje("Error: Primero debe crear la tabla hash.");
            return;
        }
        
        try {
            String claveStr = vista.getTxtClave().getText();
            if (!validarClave(claveStr)) {
                return;
            }
            
            int clave = Integer.parseInt(claveStr);
            String metodo = (String) vista.getComboMetodo().getSelectedItem();
            
            // Realizar búsquedas
            ResultadoBusqueda resultadoHash = modelo.buscarClaveHash(clave, metodo);
            ResultadoBusqueda resultadoLineal = modelo.buscarClaveLineal(clave);
            ResultadoBusqueda resultadoBinaria = modelo.buscarClaveBinaria(clave);
            
            // Mostrar resultados
            vista.mostrarMensaje(resultadoHash.getMensaje());
            vista.mostrarMensaje(resultadoLineal.getMensaje());
            vista.mostrarMensaje(resultadoBinaria.getMensaje());
        } catch (NumberFormatException ex) {
            vista.mostrarMensaje("Error: Ingrese una clave numérica válida.");
        }
    }
    
    public void borrarClave() {
        if (modelo == null) {
            vista.mostrarMensaje("Error: Primero debe crear la tabla hash.");
            return;
        }
        
        try {
            String claveStr = vista.getTxtClave().getText();
            if (!validarClave(claveStr)) {
                return;
            }
            
            int clave = Integer.parseInt(claveStr);
            String metodo = (String) vista.getComboMetodo().getSelectedItem();
            
            // Borrar del modelo
            ResultadoOperacion resultado = modelo.borrarClave(clave, metodo);
            
            // Actualizar vista (se suma 1 a la posición para mostrar índices del 1 a n)
            if (resultado.isExito()) {
                vista.getModeloTabla().setValueAt("Vacío", resultado.getPosicion(), 1);
            }
            
            vista.mostrarMensaje(resultado.getMensaje());
        } catch (NumberFormatException ex) {
            vista.mostrarMensaje("Error: Ingrese una clave numérica válida.");
        }
    }
    
    private boolean validarClave(String claveStr) {
        if (claveStr.isEmpty()) {
            vista.mostrarMensaje("Error: Ingrese una clave.");
            return false;
        }
        
        try {
            int clave = Integer.parseInt(claveStr);
            String claveCompleta = String.valueOf(clave);
            
            if (claveCompleta.length() > modelo.getDigitosClaves()) {
                vista.mostrarMensaje("Error: La clave debe tener máximo " + modelo.getDigitosClaves() + " dígitos.");
                return false;
            }
        } catch (NumberFormatException ex) {
            vista.mostrarMensaje("Error: Ingrese una clave numérica válida.");
            return false;
        }
        
        return true;
    }
}
