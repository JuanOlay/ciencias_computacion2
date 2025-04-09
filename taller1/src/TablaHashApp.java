// TablaHashApp.java

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    @SuppressWarnings("FieldMayBeFinal")
    private TablaHashControlador controlador;
    
    // Lista para almacenar claves originales
    @SuppressWarnings("FieldMayBeFinal")
    private List<Integer> clavesOriginales;
    
    // Método hash seleccionado
    private String metodoHash;
    
    @SuppressWarnings("UseSpecificCatch")
    public TablaHashApp() {
        super("Tabla"); // Título de la ventana: "Tabla"
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
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
        
        // Mostrar diálogo de configuración
        mostrarDialogoConfiguracion();
        
        setVisible(true);
    }
    
    private void inicializarComponentes(int numClaves) {
        // Panel principal con BorderLayout y márgenes ajustados
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panelPrincipal.setBackground(new Color(245, 245, 250));
        
        // Panel superior con información (se muestra "Tabla")
        JPanel panelInfo = new JPanel();
        panelInfo.setLayout(new BoxLayout(panelInfo, BoxLayout.Y_AXIS));
        panelInfo.setBackground(new Color(245, 245, 250));
        panelInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 220), 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        
        JLabel lblTitulo = new JLabel("Tabla"); // Texto actualizado
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel lblMetodo = new JLabel("Método de transformación: " + metodoHash);
        lblMetodo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblMetodo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panelInfo.add(lblTitulo);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(lblMetodo);
        
        // --- Creación de la tabla de forma vertical ---
        // Se mantienen dos columnas: "Índice" y "Valor"
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
        panelBotones = new JPanel(new GridLayout(1, 4, 15, 10));
        panelBotones.setBackground(new Color(245, 245, 250));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JButton btnInsertar = crearBoton("Insertar Claves", "Añadir nuevas claves a la tabla");
        btnInsertar.addActionListener(e -> mostrarDialogoInsercion());
        
        JButton btnBuscar = crearBoton("Buscar Clave", "Buscar una clave en la tabla");
        btnBuscar.addActionListener(e -> mostrarDialogoBusqueda());
        
        JButton btnBorrar = crearBoton("Borrar Clave", "Eliminar una clave de la tabla");
        btnBorrar.addActionListener(e -> mostrarDialogoBorrado());
        
        JButton btnReiniciar = crearBoton("Reiniciar", "Crear una nueva tabla");
        btnReiniciar.addActionListener(e -> reiniciarAplicacion());
        
        panelBotones.add(btnInsertar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnBorrar);
        panelBotones.add(btnReiniciar);
        
        panelPrincipal.add(panelInfo, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(scrollResultado, BorderLayout.SOUTH);
        panelPrincipal.add(panelBotones, BorderLayout.PAGE_END);
        
        getContentPane().removeAll();
        getContentPane().add(panelPrincipal);
        revalidate();
        repaint();
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
            @SuppressWarnings("override")
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(210, 210, 230));
            }
            @SuppressWarnings("override")
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(230, 230, 245));
            }
        });
        return boton;
    }
    
    // Diálogo de configuración actualizado para utilizar "Tabla"
    private void mostrarDialogoConfiguracion() {
        JDialog dialogo = new JDialog(this, "Configuración de Tabla", true);
        dialogo.setSize(450, 350);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());
        
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelContenido.setBackground(new Color(250, 250, 255));
        
        JLabel lblTitulo = new JLabel("Configuración de Tabla");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel panelParametros = new JPanel(new GridLayout(3, 2, 10, 15));
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
        
        panelParametros.add(lblNumClaves);
        panelParametros.add(txtNumClaves);
        panelParametros.add(lblDigitosClaves);
        panelParametros.add(txtDigitosClaves);
        panelParametros.add(lblMetodo);
        panelParametros.add(cmbMetodos);
        
        JButton btnCrear = new JButton("Crear Tabla");
        btnCrear.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCrear.setBackground(new Color(100, 150, 220));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.setBorderPainted(false);
        btnCrear.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCrear.addMouseListener(new java.awt.event.MouseAdapter() {
            @SuppressWarnings("override")
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCrear.setBackground(new Color(80, 130, 200));
            }
            @SuppressWarnings("override")
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCrear.setBackground(new Color(100, 150, 220));
            }
        });
        
        btnCrear.addActionListener(e -> {
            try {
                int numClaves = Integer.parseInt(txtNumClaves.getText());
                int digitosClaves = Integer.parseInt(txtDigitosClaves.getText());
                metodoHash = (String) cmbMetodos.getSelectedItem();
                
                if (numClaves <= 0 || digitosClaves <= 0) {
                    JOptionPane.showMessageDialog(dialogo, 
                        "Los valores deben ser mayores que 0.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                inicializarComponentes(numClaves);
                controlador.crearTablaHash(numClaves, digitosClaves, metodoHash);
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
            @SuppressWarnings("override")
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAgregar.setBackground(new Color(80, 130, 200));
            }
            @SuppressWarnings("override")
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAgregar.setBackground(new Color(100, 150, 220));
            }
        });
        
        btnFinalizar.addMouseListener(new java.awt.event.MouseAdapter() {
            @SuppressWarnings("override")
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFinalizar.setBackground(new Color(210, 210, 230));
            }
            @SuppressWarnings("override")
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
    
    private void mostrarDialogoBusqueda() {
        JDialog dialogoMetodo = new JDialog(this, "Método de Búsqueda", true);
        dialogoMetodo.setSize(400, 300);
        dialogoMetodo.setLocationRelativeTo(this);
        dialogoMetodo.setLayout(new BorderLayout());
        
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelContenido.setBackground(new Color(250, 250, 255));
        
        JLabel lblTitulo = new JLabel("Seleccione el método de búsqueda");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        String[] opciones = {"Búsqueda por Hash", "Búsqueda Lineal", "Búsqueda Binaria"};
        JPanel panelOpciones = new JPanel(new GridLayout(3, 1, 0, 10));
        panelOpciones.setBackground(new Color(250, 250, 255));
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        panelOpciones.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        ButtonGroup grupo = new ButtonGroup();
        JRadioButton[] radioBotones = new JRadioButton[3];
        for (int i = 0; i < opciones.length; i++) {
            radioBotones[i] = new JRadioButton(opciones[i]);
            radioBotones[i].setFont(new Font("Segoe UI", Font.PLAIN, 14));
            radioBotones[i].setBackground(new Color(250, 250, 255));
            grupo.add(radioBotones[i]);
            panelOpciones.add(radioBotones[i]);
        }
        radioBotones[0].setSelected(true);
        
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(new Color(250, 250, 255));
        JButton btnContinuar = new JButton("Continuar con la búsqueda");
        btnContinuar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnContinuar.setBackground(new Color(100, 150, 220));
        btnContinuar.setForeground(Color.WHITE);
        btnContinuar.setFocusPainted(false);
        btnContinuar.setBorderPainted(false);
        btnContinuar.setPreferredSize(new Dimension(200, 40));
        
        btnContinuar.addMouseListener(new java.awt.event.MouseAdapter() {
            @SuppressWarnings("override")
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnContinuar.setBackground(new Color(80, 130, 200));
            }
            @SuppressWarnings("override")
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnContinuar.setBackground(new Color(100, 150, 220));
            }
        });
        
        btnContinuar.addActionListener(e -> {
            String opcionSeleccionada = "";
            for (int i = 0; i < radioBotones.length; i++) {
                if (radioBotones[i].isSelected()) {
                    opcionSeleccionada = opciones[i];
                    break;
                }
            }
            dialogoMetodo.dispose();
            pedirClaveBusqueda(opcionSeleccionada);
        });
        
        panelBoton.add(btnContinuar);
        
        panelContenido.add(lblTitulo);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 10)));
        panelContenido.add(panelOpciones);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 20)));
        panelContenido.add(panelBoton);
        
        dialogoMetodo.add(panelContenido, BorderLayout.CENTER);
        dialogoMetodo.setResizable(false);
        dialogoMetodo.setVisible(true);
    }
    
    @SuppressWarnings("ConvertToStringSwitch")
    private void pedirClaveBusqueda(String opcion) {
        String claveStr = JOptionPane.showInputDialog(this, 
            "Ingrese la clave a buscar:", 
            "Búsqueda de Clave - " + opcion, 
            JOptionPane.QUESTION_MESSAGE);
            
        if (claveStr == null || claveStr.trim().isEmpty()) return;
        
        try {
            int clave = Integer.parseInt(claveStr);
            ResultadoBusqueda resultado = null;
            
            if (opcion.equals("Búsqueda por Hash")) {
                resultado = controlador.buscarClaveHash(clave, metodoHash);
            } else if (opcion.equals("Búsqueda Lineal")) {
                resultado = controlador.buscarClaveLineal(clave);
            } else if (opcion.equals("Búsqueda Binaria")) {
                resultado = controlador.buscarClaveBinaria(clave);
            }
            
            destacarPosicion(resultado.getPosicion());
            mostrarMensaje(resultado.getMensaje());
            
            String titulo = resultado.isExito() ? 
                "¡Clave Encontrada!" : "Clave No Encontrada";
            String mensaje = "<html><body style='width: 300px'>";
            if (resultado.isExito()) {
                mensaje += "<h3 style='color: green;'>Resultado de la búsqueda</h3>";
                mensaje += "<p><b>Clave:</b> " + clave + "</p>";
                mensaje += "<p><b>Posición:</b> " + (resultado.getPosicion() + 1) + "</p>";
                mensaje += "<p><b>Método:</b> " + opcion + "</p>";
                mensaje += "<p><b>Comparaciones:</b> " + resultado.getComparaciones() + "</p>";
            } else {
                mensaje += "<h3 style='color: red;'>Clave no encontrada</h3>";
                mensaje += "<p><b>Clave buscada:</b> " + clave + "</p>";
                mensaje += "<p><b>Método:</b> " + opcion + "</p>";
                mensaje += "<p><b>Comparaciones:</b> " + resultado.getComparaciones() + "</p>";
            }
            mensaje += "</body></html>";
            
            JOptionPane.showMessageDialog(
                this,
                mensaje,
                titulo,
                resultado.isExito() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE
            );
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Ingrese una clave numérica válida.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            pedirClaveBusqueda(opcion);
        }
    }
    
    private void mostrarDialogoBorrado() {
        String claveStr = JOptionPane.showInputDialog(this, 
            "Ingrese la clave a borrar:", 
            "Borrado de Clave", 
            JOptionPane.QUESTION_MESSAGE);
            
        if (claveStr == null || claveStr.trim().isEmpty()) return;
        
        try {
            int clave = Integer.parseInt(claveStr);
            ResultadoOperacion resultado = controlador.borrarClave(clave, metodoHash);
            mostrarMensaje(resultado.getMensaje());
            
            if (resultado.isExito()) {
                actualizarTabla();
                clavesOriginales.remove(Integer.valueOf(clave));
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Ingrese una clave numérica válida.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void reiniciarAplicacion() {
        int respuesta = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de reiniciar la aplicación? Se perderán todas las claves y configuraciones.",
            "Confirmar Reinicio",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (respuesta == JOptionPane.YES_OPTION) {
            clavesOriginales.clear();
            txtResultado.setText("");
            mostrarDialogoConfiguracion();
        }
    }
    
    // Actualiza la tabla vertical: se colocan los valores en la columna "Valor"
    public void actualizarTabla() {
        int[] tablaHash = controlador.getTablaHash().getTablaHash();
        for (int i = 0; i < tablaHash.length; i++) {
            if (tablaHash[i] != -1) {
                modeloTabla.setValueAt(tablaHash[i], i, 1);
            } else {
                modeloTabla.setValueAt("", i, 1);
            }
        }
    }
    
    // Muestra mensajes en el área de resultados
    public void mostrarMensaje(String mensaje) {
        txtResultado.append(mensaje + "\n");
        txtResultado.setCaretPosition(txtResultado.getDocument().getLength());
    }
    
    // Destaca la fila correspondiente a la posición
    private void destacarPosicion(int posicion) {
        if (posicion >= 0 && posicion < tabla.getRowCount()) {
            tabla.setRowSelectionInterval(posicion, posicion);
            tabla.scrollRectToVisible(tabla.getCellRect(posicion, 0, true));
        } else {
            tabla.clearSelection();
        }
    }
    
    public String getMetodoHash() {
        return metodoHash;
    }
}
