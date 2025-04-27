// TablaHashApp.java

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class TablaHashApp extends JFrame {
    // Componentes gráficos principales
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;
    private JTextArea txtResultado;
    private JScrollPane scrollResultado;
    private JPanel panelBotones;
    
    // Controlador
    private TablaHashControlador controlador;
    
    /** Actualiza la tabla con los datos actuales del controlador */
    private void actualizarTabla() {
        if (controlador != null && controlador.getTablaHash() != null) {
            int[] tablaHash = controlador.getTablaHash().getTablaHash();
            for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                modeloTabla.setValueAt(tablaHash[i] == -1 ? "" : tablaHash[i], i, 1);
            }
        }
    }
    
    // Lista para almacenar claves originales
    private List<Integer> clavesOriginales;
    
    // Método hash seleccionado
    private String metodoHash;
    // Método de inserción
    private String metodoInsercion;
    // Método de colisión
    private String metodoColision;

    private List<String> pendingMessages = new ArrayList<>();

    public void mostrarMensaje(String mensaje) {
        if (txtResultado != null) {
            txtResultado.append(mensaje + "\n");
        } else {
            // Guardar el mensaje para mostrarlo después de inicializar txtResultado
            pendingMessages.add(mensaje);
        }
    }
    public TablaHashApp() {
        super("Tabla Hash");
        pendingMessages = new ArrayList<>();
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Detectar cierre de ventana para guardar datos
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (controlador != null && controlador.getTablaHash() != null) {
                    controlador.guardarDatos(clavesOriginales);
                }
            }
        });
        
        // Se aplica el Look & Feel Nimbus (si está disponible)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Se usa el Look & Feel por defecto si Nimbus no está disponible
        }
        
        // Inicializar lista de claves
        clavesOriginales = new ArrayList<>();
        
        // Inicializar controlador
        controlador = new TablaHashControlador(this);
        
        // Intentar cargar datos guardados
        if (PersistenciaUtil.existenDatosGuardados() && controlador.cargarDatos()) {
            // Si hay datos guardados y se cargaron correctamente
            PersistenciaUtil.DatosAplicacion datos = PersistenciaUtil.cargarDatos();
            this.metodoHash = datos.getMetodoHash();
            this.metodoInsercion = datos.getTablaHash().getMetodoInsercion();
            this.metodoColision = datos.getTablaHash().getMetodoColision();
            this.clavesOriginales = datos.getClavesOriginales();
            
            // Inicializar la interfaz con los datos cargados
            inicializarComponentes(datos.getTablaHash().getNumClaves());
            actualizarTabla();
        } else {
            // Mostrar diálogo de configuración si no hay datos guardados
            mostrarDialogoConfiguracion();
        }
        
        setVisible(true);
    }
// Reemplazar el método buscarSegunMetodo() en TablaHashApp
private void buscarSegunMetodo() {
    String input = JOptionPane.showInputDialog(this, "Ingrese la clave a buscar:", "Buscar Clave", JOptionPane.QUESTION_MESSAGE);
    if (input != null && !input.trim().isEmpty()) {
        try {
            int clave = Integer.parseInt(input.trim());
            ResultadoBusqueda resultado = controlador.buscarClave(clave);
            
            if (resultado.isExito()) {
                // Añadir al área de texto
                txtResultado.append("\nBúsqueda: Clave " + clave + " encontrada en posición " + 
                    (resultado.getPosicion() + 1) + " (comparaciones: " + resultado.getComparaciones() + ")\n");
                
                // Resaltar visualmente la fila en la tabla
                tabla.setRowSelectionInterval(resultado.getPosicion(), resultado.getPosicion());
                
                // Mostrar un diálogo con el resultado
                JOptionPane.showMessageDialog(this,
                    "La clave " + clave + " se encuentra en la posición " + (resultado.getPosicion() + 1) + 
                    "\nComparaciones realizadas: " + resultado.getComparaciones(),
                    "Búsqueda exitosa", JOptionPane.INFORMATION_MESSAGE);
                
                // Asegurar que la fila sea visible en la tabla
                tabla.scrollRectToVisible(tabla.getCellRect(resultado.getPosicion(), 0, true));
            } else {
                txtResultado.append("\nBúsqueda: " + resultado.getMensaje() + "\n");
                JOptionPane.showMessageDialog(this,
                    "La clave " + clave + " no fue encontrada.\n" + resultado.getMensaje(),
                    "Clave no encontrada", JOptionPane.WARNING_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
    // Métodos agregados para corregir errores graves

    private void mostrarDialogoBorrado() {
        String input = JOptionPane.showInputDialog(this, "Ingrese la clave a borrar:", "Borrar Clave", JOptionPane.QUESTION_MESSAGE);
        if (input != null && !input.trim().isEmpty()) {
            try {
                int clave = Integer.parseInt(input.trim());
                ResultadoOperacion resultado = controlador.borrarClave(clave);
                
                if (resultado.isExito()) {
                    txtResultado.append("\nBorrado: Clave " + clave + " eliminada de la posición " + 
                        (resultado.getPosicion() + 1) + "\n");
                    // Actualizar la tabla
                    actualizarTabla();
                } else {
                    txtResultado.append("\nBorrado: " + resultado.getMensaje() + "\n");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Ingrese un valor numérico válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

private void reiniciarAplicacion() {
    javax.swing.JOptionPane.showMessageDialog(null, "La aplicación se reiniciará.", "Reiniciar", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    this.dispose(); // Cierra la ventana actual (si TablaHashApp extiende de JFrame)
    new TablaHashApp(); // Crea una nueva instancia de la app
}

// Agregar después del método mostrarDialogoSimularColision() en TablaHashApp
private void salirYGuardar() {
    if (controlador != null && controlador.getTablaHash() != null) {
        controlador.guardarDatos(clavesOriginales);
        JOptionPane.showMessageDialog(this, 
            "Datos guardados correctamente.\nLa aplicación se cerrará.", 
            "Salir y Guardar", 
            JOptionPane.INFORMATION_MESSAGE);
    } else {
        JOptionPane.showMessageDialog(this, 
            "No hay datos que guardar.\nLa aplicación se cerrará.", 
            "Salir", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    dispose();
    System.exit(0);
}

private int calcularSiguientePosicion(int posicionOriginal, int intento, String metodoColision, int clave) {
    if (metodoColision.equalsIgnoreCase("Lineal")) {
        return (posicionOriginal + intento) % controlador.getTablaHash().getNumClaves();
    } else if (metodoColision.equalsIgnoreCase("Cuadrática")) {
        return (posicionOriginal + intento * intento) % controlador.getTablaHash().getNumClaves();
    } else if (metodoColision.equalsIgnoreCase("Doble Hash")) {
        // Un algoritmo simplificado de doble hash
        int hash2 = 1 + (clave % (controlador.getTablaHash().getNumClaves() - 1));
        return (posicionOriginal + intento * hash2) % controlador.getTablaHash().getNumClaves();
    } else {
        // Método de resolución no reconocido, usar lineal por defecto
        return (posicionOriginal + intento) % controlador.getTablaHash().getNumClaves();
    }
}

            
    private void inicializarComponentes(int numClaves) {
        // Panel principal con BorderLayout y márgenes ajustados
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(245, 245, 250));
        
        // Panel superior con información
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(new Color(245, 245, 250));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel lblTitulo = new JLabel("Tabla Hash");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblMetodo = new JLabel("Método de transformación: " + metodoHash);
        lblMetodo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMetodo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblInsercion = new JLabel("Método de inserción: " + metodoInsercion);
        lblInsercion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblInsercion.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblColision = new JLabel("Método de colisión: " + metodoColision);
        lblColision.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblColision.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelInfo.add(lblTitulo);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(lblMetodo);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(lblInsercion);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(lblColision);
        
        // Creación de la tabla de forma vertical
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modeloTabla.addColumn("Índice");
        modeloTabla.addColumn("Valor");
        
        for (int i = 0; i < numClaves; i++) {
            modeloTabla.addRow(new Object[]{i + 1, ""});
        }
        
        tabla = new JTable(modeloTabla);
        tabla.setRowHeight(35);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tabla.getTableHeader().setBackground(new Color(230, 230, 245));
        tabla.getTableHeader().setForeground(new Color(60, 60, 80));
        tabla.setGridColor(new Color(210, 210, 230));
        tabla.setRowSelectionAllowed(true);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setSelectionBackground(new Color(210, 230, 255));
        
        // Centrar contenido en ambas columnas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabla.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabla.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        
        // Ajuste opcional del ancho de cada columna
        tabla.getColumnModel().getColumn(0).setPreferredWidth(100);
        tabla.getColumnModel().getColumn(1).setPreferredWidth(300);
        
        scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220), 1, true));
        
        // Área de resultados
        txtResultado = new JTextArea(8, 20);
        txtResultado.setEditable(false);
        txtResultado.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtResultado.setBackground(new Color(250, 250, 255));
        txtResultado.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        scrollResultado = new JScrollPane(txtResultado);
        scrollResultado.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1, true),
                "Resultados",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(80, 80, 100)
            ),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        // Panel de botones
        panelBotones = new JPanel(new GridLayout(1, 5, 10, 10));
        panelBotones.setBackground(new Color(245, 245, 250));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JButton btnInsertar = crearBoton("Insertar Claves", "Añadir nuevas claves a la tabla");
        btnInsertar.addActionListener(e -> mostrarDialogoInsercion());
        
        JButton btnSimularColision = crearBoton("Simular Colisión", "Simular colisión sin insertar");
        btnSimularColision.addActionListener(e -> mostrarDialogoSimularColision());
        
        JButton btnBuscar = crearBoton("Buscar Clave", "Buscar una clave en la tabla");
        btnBuscar.addActionListener(e -> buscarSegunMetodo());
        

        JButton btnBorrar = crearBoton("Borrar Clave", "Eliminar una clave de la tabla");
        btnBorrar.addActionListener(e -> mostrarDialogoBorrado());
        
        JButton btnReiniciar = crearBoton("Reiniciar", "Crear una nueva tabla");
        btnReiniciar.addActionListener(e -> reiniciarAplicacion());

        JButton btnSalirGuardar = crearBoton("Salir y Guardar", "Guardar datos y salir de la aplicación");
        btnSalirGuardar.addActionListener(e -> salirYGuardar());
        
        panelBotones.add(btnInsertar);
        panelBotones.add(btnSimularColision);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnBorrar);
        panelBotones.add(btnReiniciar);
        // Agregar este botón al panelBotones, junto a los demás
        panelBotones.add(btnSalirGuardar);
        
        panelPrincipal.add(panelInfo, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(scrollResultado, BorderLayout.SOUTH);
        panelPrincipal.add(panelBotones, BorderLayout.PAGE_END);
        
        getContentPane().removeAll();
        getContentPane().add(panelPrincipal);
        revalidate();
        repaint();
        mostrarMensajesPendientes();
    }

    // Al final del método inicializarComponentes(), agregar este código para mostrar los mensajes pendientes
private void mostrarMensajesPendientes() {
    if (!pendingMessages.isEmpty() && txtResultado != null) {
        for (String mensaje : pendingMessages) {
            txtResultado.append(mensaje + "\n");
        }
        pendingMessages.clear();
    }
}
    
    // Método para crear botones con mejoras visuales
    private JButton crearBoton(String texto, String tooltip) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(new Color(230, 230, 245));
        boton.setForeground(new Color(60, 60, 80));
        boton.setFocusPainted(false);
        boton.setToolTipText(tooltip);
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(210, 210, 230));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(230, 230, 245));
            }
        });
        return boton;
    }
    
    // Diálogo de configuración actualizado para incluir métodos de inserción y colisión
    private void mostrarDialogoConfiguracion() {
        JDialog dialogo = new JDialog(this, "Configuración de Tabla Hash", true);
        dialogo.setSize(500, 450);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());
        
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelContenido.setBackground(new Color(250, 250, 255));
        
        JLabel lblTitulo = new JLabel("Configuración de Tabla Hash");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel panelParametros = new JPanel(new GridLayout(5, 2, 10, 15));
        panelParametros.setBackground(new Color(250, 250, 255));
        panelParametros.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panelParametros.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblNumClaves = new JLabel("Tamaño de la tabla:");
        lblNumClaves.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField txtNumClaves = new JTextField("10");
        txtNumClaves.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel lblDigitosClaves = new JLabel("Dígitos por clave:");
        lblDigitosClaves.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField txtDigitosClaves = new JTextField("4");
        txtDigitosClaves.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel lblMetodo = new JLabel("Método de transformación:");
        lblMetodo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        String[] metodos = {"Módulo", "Cuadrado", "Plegamiento", "Truncamiento"};
        JComboBox<String> cmbMetodos = new JComboBox<>(metodos);
        cmbMetodos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel lblInsercion = new JLabel("Método de inserción:");
        lblInsercion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        String[] tiposInsercion = {"Hash", "Lineal", "Binaria"};
        JComboBox<String> cmbInsercion = new JComboBox<>(tiposInsercion);
        cmbInsercion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JLabel lblColision = new JLabel("Método de colisión:");
        lblColision.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        String[] tiposColision = {"Lineal", "Cuadrática", "Doble Hash"};
        JComboBox<String> cmbColision = new JComboBox<>(tiposColision);
        cmbColision.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        panelParametros.add(lblNumClaves);
        panelParametros.add(txtNumClaves);
        panelParametros.add(lblDigitosClaves);
        panelParametros.add(txtDigitosClaves);
        panelParametros.add(lblMetodo);
        panelParametros.add(cmbMetodos);
        panelParametros.add(lblInsercion);
        panelParametros.add(cmbInsercion);
        panelParametros.add(lblColision);
        panelParametros.add(cmbColision);
        
        JButton btnCrear = new JButton("Crear Tabla");
        btnCrear.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCrear.setBackground(new Color(100, 150, 220));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.setBorderPainted(false);
        btnCrear.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCrear.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCrear.setBackground(new Color(80, 130, 200));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCrear.setBackground(new Color(100, 150, 220));
            }
        });
        
        btnCrear.addActionListener(e -> {
            try {
                int numClaves = Integer.parseInt(txtNumClaves.getText());
                int digitosClaves = Integer.parseInt(txtDigitosClaves.getText());
                metodoHash = (String) cmbMetodos.getSelectedItem();
                metodoInsercion = (String) cmbInsercion.getSelectedItem();
                metodoColision = (String) cmbColision.getSelectedItem();
                
                if (numClaves <= 0 || digitosClaves <= 0) {
                    JOptionPane.showMessageDialog(dialogo, 
                        "Los valores deben ser mayores que 0.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                inicializarComponentes(numClaves);
                controlador.crearTablaHash(numClaves, digitosClaves, metodoHash, metodoInsercion, metodoColision);
                dialogo.dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, 
                    "Ingrese valores numéricos válidos.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        panelContenido.add(lblTitulo);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 10)));
        panelContenido.add(panelParametros);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 20)));
        panelContenido.add(btnCrear);
        
        dialogo.add(panelContenido, BorderLayout.CENTER);
        dialogo.setResizable(false);
        dialogo.setVisible(true);
    }
    
    private void mostrarDialogoInsercion() {
        JDialog dialogo = new JDialog(this, "Insertar Claves", true);
        dialogo.setSize(500, 400);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout(10, 10));
        
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelNorte.setBackground(new Color(250, 250, 255));
        JLabel lblClave = new JLabel("Clave:");
        lblClave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField txtClave = new JTextField(10);
        txtClave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton btnAgregar = new JButton("Insertar");
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAgregar.setBackground(new Color(100, 150, 220));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        
        JButton btnFinalizar = new JButton("Finalizar");
        btnFinalizar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnFinalizar.setBackground(new Color(230, 230, 245));
        btnFinalizar.setForeground(new Color(60, 60, 80));
        btnFinalizar.setFocusPainted(false);
        
        panelNorte.add(lblClave);
        panelNorte.add(txtClave);
        panelNorte.add(btnAgregar);
        panelNorte.add(btnFinalizar);
        
        DefaultListModel<String> modeloLista = new DefaultListModel<>();
        JList<String> listaClaves = new JList<>(modeloLista);
        listaClaves.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        listaClaves.setBackground(new Color(250, 250, 255));
        listaClaves.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollLista = new JScrollPane(listaClaves);
        scrollLista.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1, true), 
                "Claves insertadas",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(80, 80, 100)
            ),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        btnAgregar.addActionListener(e -> {
            try {
                String claveStr = txtClave.getText().trim();
                if (claveStr.isEmpty()) {
                    JOptionPane.showMessageDialog(dialogo, "Ingrese una clave válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                int clave = Integer.parseInt(claveStr);
                
                // Validación básica de longitud de clave
                if (controlador.getTablaHash() != null && 
                    String.valueOf(clave).length() > controlador.getTablaHash().getDigitosClaves()) {
                    JOptionPane.showMessageDialog(dialogo, 
                        "La clave debe tener máximo " + controlador.getTablaHash().getDigitosClaves() + " dígitos.", 
                        "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Calcula posición sin resolver colisiones
                int posicion = controlador.getTablaHash().transformarClave(clave, metodoHash);
                int[] tablaHash = controlador.getTablaHash().getTablaHash();
                
                if (tablaHash[posicion] != -1 && tablaHash[posicion] != clave) {
                    // Solo indicar colisión
                    JOptionPane.showMessageDialog(dialogo, 
                        "¡Colisión detectada! La posición " + (posicion + 1) + 
                        " ya está ocupada por la clave: " + tablaHash[posicion], 
                        "Colisión", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                
                // Proceder con inserción normal si no hay colisión
                ResultadoOperacion resultado = controlador.insertarClave(clave, metodoHash);
                
                if (resultado.isExito()) {
                    modeloLista.addElement("Clave: " + clave + " → Posición: " + (resultado.getPosicion() + 1));
                    if (!clavesOriginales.contains(clave)) {
                        clavesOriginales.add(clave);
                    }
                    actualizarTabla();
                } else {
                    JOptionPane.showMessageDialog(dialogo, resultado.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                
                txtClave.setText("");
                txtClave.requestFocus();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "Ingrese una clave numérica válida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        
        btnFinalizar.addActionListener(e -> dialogo.dispose());
        
        btnAgregar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAgregar.setBackground(new Color(80, 130, 200));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAgregar.setBackground(new Color(100, 150, 220));
            }
        });
        
        btnFinalizar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFinalizar.setBackground(new Color(210, 210, 230));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFinalizar.setBackground(new Color(230, 230, 245));
            }
        });
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.setBackground(new Color(250, 250, 255));
        panelPrincipal.add(panelNorte, BorderLayout.NORTH);
        panelPrincipal.add(scrollLista, BorderLayout.CENTER);
        
        dialogo.add(panelPrincipal);
        dialogo.setVisible(true);
    }
    
    // Método para simular colisiones sin insertar claves
    private void mostrarDialogoSimularColision() {
        JDialog dialogo = new JDialog(this, "Simular Colisión", true);
        dialogo.setSize(500, 400);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout(10, 10));
        
        JPanel panelNorte = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelNorte.setBackground(new Color(250, 250, 255));
        JLabel lblClave = new JLabel("Clave a simular:");
        lblClave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        JTextField txtClave = new JTextField(10);
        txtClave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        JButton btnSimular = new JButton("Simular");
        btnSimular.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnSimular.setBackground(new Color(100, 150, 220));
        btnSimular.setForeground(Color.WHITE);
        btnSimular.setFocusPainted(false);
        
        JButton btnFinalizar = new JButton("Finalizar");
        btnFinalizar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnFinalizar.setBackground(new Color(230, 230, 245));
        btnFinalizar.setForeground(new Color(60, 60, 80));
        btnFinalizar.setFocusPainted(false);
        
        panelNorte.add(lblClave);
        panelNorte.add(txtClave);
        panelNorte.add(btnSimular);
        panelNorte.add(btnFinalizar);
        
        // Área para mostrar resultados de simulación
        JTextArea txtSimulacion = new JTextArea(10, 40);
        txtSimulacion.setEditable(false);
        txtSimulacion.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtSimulacion.setBackground(new Color(250, 250, 255));
        txtSimulacion.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        JScrollPane scrollSimulacion = new JScrollPane(txtSimulacion);
        scrollSimulacion.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 220), 1, true), 
                "Resultados de simulación",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(80, 80, 100)
            ),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
 btnSimular.addActionListener(e -> {
            try {
                String claveStr = txtClave.getText().trim();
                if (claveStr.isEmpty()) {
                    JOptionPane.showMessageDialog(dialogo, "Ingrese una clave válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int clave = Integer.parseInt(claveStr);

                if (controlador.getTablaHash() != null) {
                    int indiceOriginal = controlador.getTablaHash().transformarClave(clave, metodoHash);
                    StringBuilder resultado = new StringBuilder();
                    resultado.append("Clave: ").append(clave).append("\n");
                    resultado.append("Método Hash: ").append(metodoHash).append("\n");
                    resultado.append("Posición inicial: ").append(indiceOriginal + 1).append("\n\n");

                    int[] tablaHash = controlador.getTablaHash().getTablaHash();
                    if (tablaHash[indiceOriginal] != -1 && tablaHash[indiceOriginal] != clave) {
                        resultado.append("¡COLISIÓN DETECTADA!\n")
                                 .append("La posición ").append(indiceOriginal + 1)
                                 .append(" ya está ocupada por la clave: ")
                                 .append(tablaHash[indiceOriginal]).append("\n\n")
                                 .append("Resolución según método: ").append(metodoColision).append("\n")
                                 .append("----------------------------------\n");

                        int intentos = 1;
                        int indice = indiceOriginal;
                        int numClaves = controlador.getTablaHash().getNumClaves();

                        while (intentos < numClaves) {
                            indice = calcularSiguientePosicion(indiceOriginal, intentos, metodoColision, clave) % numClaves;
                            resultado.append("Intento ").append(intentos)
                                     .append(": Posición ").append(indice + 1);
                            if (tablaHash[indice] == -1) {
                                resultado.append(" [Vacía – aquí iría la clave]");
                                break;
                            } else if (tablaHash[indice] == clave) {
                                resultado.append(" [La clave ya existe]");
                                break;
                            } else {
                                resultado.append(" [Ocupada por: ").append(tablaHash[indice]).append("]");
                            }
                            resultado.append("\n");
                            intentos++;
                        }

                        if (intentos >= numClaves) {
                            resultado.append("No se pudo resolver la colisión tras ")
                                     .append(numClaves).append(" intentos.\n");
                        }
                    } else {
                        resultado.append("No hay colisión: la posición está libre o contiene la misma clave.\n");
                    }

                    txtSimulacion.setText(resultado.toString());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "Ingrese una clave numérica válida.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Sólo cierra el diálogo, no inserta nada
        btnFinalizar.addActionListener(e -> dialogo.dispose());

        // (Opcional) efectos visuales de hover para Finalizar
        btnFinalizar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFinalizar.setBackground(new Color(210, 210, 230));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnFinalizar.setBackground(new Color(230, 230, 245));
            }
        });

        // Reensamblar el panel principal y mostrar diálogo
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelPrincipal.setBackground(new Color(250, 250, 255));
        panelPrincipal.add(panelNorte, BorderLayout.NORTH);
        panelPrincipal.add(scrollSimulacion, BorderLayout.CENTER);

        dialogo.getContentPane().removeAll();
        dialogo.add(panelPrincipal);
        dialogo.setVisible(true);
    }
}