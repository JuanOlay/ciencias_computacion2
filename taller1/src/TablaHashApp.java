//tablahashapp.java

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
    private TablaHashControlador controlador;
    
    // Lista para almacenar claves originales
    private List<Integer> clavesOriginales;
    
    // Método hash seleccionado
    private String metodoHash;
    
    public TablaHashApp() {
        super("Sistema de Tabla Hash");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Inicializar lista de claves
        clavesOriginales = new ArrayList<>();
        
        // Inicializar controlador
        controlador = new TablaHashControlador(this);
        
        // Mostrar diálogo de configuración inicial
        mostrarDialogoConfiguracion();
        
        setVisible(true);
    }
    
    private void inicializarComponentes(int numClaves) {
        // Panel principal con BorderLayout
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
        
        panelInfo.add(lblTitulo);
        panelInfo.add(Box.createRigidArea(new Dimension(0, 5)));
        panelInfo.add(lblMetodo);
        
        // Crear modelo de tabla horizontal (con columnas para los índices)
        modeloTabla = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No permitir edición de celdas
            }
        };
        
        modeloTabla.addColumn("Índice");
        modeloTabla.addColumn("Valor");
        
        // Añadir filas para cada posición
        for (int i = 0; i < numClaves; i++) {
            modeloTabla.addRow(new Object[]{i + 1, ""});
        }
        
        // Crear tabla
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
        
        // Centrar contenido de celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabla.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        tabla.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
        
        // Ajustar ancho de columnas
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
        
        // Crear botones con estilo
        JButton btnInsertar = crearBoton("Insertar Claves", "Añadir nuevas claves a la tabla");
        btnInsertar.addActionListener(e -> mostrarDialogoInsercion());
        
        JButton btnBuscar = crearBoton("Buscar Clave", "Buscar una clave en la tabla hash");
        btnBuscar.addActionListener(e -> mostrarDialogoBusqueda());
        
        JButton btnBorrar = crearBoton("Borrar Clave", "Eliminar una clave de la tabla");
        btnBorrar.addActionListener(e -> mostrarDialogoBorrado());
        
        JButton btnReiniciar = crearBoton("Reiniciar", "Crear una nueva tabla hash");
        btnReiniciar.addActionListener(e -> reiniciarAplicacion());
        
        panelBotones.add(btnInsertar);
        panelBotones.add(btnBuscar);
        panelBotones.add(btnBorrar);
        panelBotones.add(btnReiniciar);
        
        // Agregar componentes al panel principal
        panelPrincipal.add(panelInfo, BorderLayout.NORTH);
        panelPrincipal.add(scrollTabla, BorderLayout.CENTER);
        panelPrincipal.add(scrollResultado, BorderLayout.SOUTH);
        panelPrincipal.add(panelBotones, BorderLayout.PAGE_END);
        
        // Limpiar y agregar el panel principal
        getContentPane().removeAll();
        getContentPane().add(panelPrincipal);
        revalidate();
        repaint();
    }
    
    // Método para crear botones con estilo
    private JButton crearBoton(String texto, String tooltip) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setBackground(new Color(230, 230, 245));
        boton.setForeground(new Color(60, 60, 80));
        boton.setFocusPainted(false);
        boton.setToolTipText(tooltip);
        
        // Efectos al pasar el mouse
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(210, 210, 230));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(230, 230, 245));
            }
        });
        
        return boton;
    }
    
    // Métodos para mostrar diálogos
    
    private void mostrarDialogoConfiguracion() {
        JDialog dialogo = new JDialog(this, "Configuración de Tabla Hash", true);
        dialogo.setSize(450, 350);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());
        
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelContenido.setBackground(new Color(250, 250, 255));
        
        JLabel lblTitulo = new JLabel("Configuración de la Tabla Hash");
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
        
        JButton btnCrear = new JButton("Crear Tabla Hash");
        btnCrear.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCrear.setBackground(new Color(100, 150, 220));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.setBorderPainted(false);
        btnCrear.setAlignmentX(Component.LEFT_ALIGNMENT);
        
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
        
        // Efectos al pasar el mouse
        btnCrear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCrear.setBackground(new Color(80, 130, 200));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCrear.setBackground(new Color(100, 150, 220));
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
        // Diálogo para insertar claves
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
        
        // Lista de claves insertadas
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
                    // Guardar la clave original si no existe
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
        
        // Efectos al pasar el mouse
        btnAgregar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnAgregar.setBackground(new Color(80, 130, 200));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAgregar.setBackground(new Color(100, 150, 220));
            }
        });
        
        btnFinalizar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnFinalizar.setBackground(new Color(210, 210, 230));
            }

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
    
    // Método mostrarDialogoBusqueda mejorado con botón continuar más explícito
    private void mostrarDialogoBusqueda() {
        // Crear el diálogo principal
        JDialog dialogoMetodo = new JDialog(this, "Método de Búsqueda", true);
        dialogoMetodo.setSize(400, 300);  // Un poco más alto para acomodar mejor el botón
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
        
        // Panel para los radio buttons
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
        
        // Seleccionar la primera opción por defecto
        radioBotones[0].setSelected(true);
        
        // Panel para el botón continuar
        JPanel panelBoton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBoton.setBackground(new Color(250, 250, 255));
        panelBoton.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Botón continuar con texto más descriptivo
        JButton btnContinuar = new JButton("Continuar con la búsqueda");
        btnContinuar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnContinuar.setBackground(new Color(100, 150, 220));
        btnContinuar.setForeground(Color.WHITE);
        btnContinuar.setFocusPainted(false);
        btnContinuar.setBorderPainted(false);
        btnContinuar.setPreferredSize(new Dimension(200, 40)); // Botón más grande
        
        btnContinuar.addActionListener(e -> {
            String opcionSeleccionada = "";
            for (int i = 0; i < radioBotones.length; i++) {
                if (radioBotones[i].isSelected()) {
                    opcionSeleccionada = opciones[i];
                    break;
                }
            }
            
            dialogoMetodo.dispose();
            // Llamamos explícitamente al método para pedir la clave
            pedirClaveBusqueda(opcionSeleccionada);
        });
        
        // Efectos al pasar el mouse
        btnContinuar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnContinuar.setBackground(new Color(80, 130, 200));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnContinuar.setBackground(new Color(100, 150, 220));
            }
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
            
            // Destacar la posición en la tabla
            destacarPosicion(resultado.getPosicion());
            
            // Registrar el resultado en el área de texto
            mostrarMensaje(resultado.getMensaje());
            
            // Mostrar ventana emergente con información detallada
            String titulo = resultado.isExito() ? 
                "¡Clave Encontrada!" : "Clave No Encontrada";
            
            String mensaje = "<html><body style='width: 300px'>";
            if (resultado.isExito()) {
                mensaje += "<h3 style='color: green;'>Resultado de la búsqueda</h3>";
                mensaje += "<p><b>Clave:</b> " + clave + "</p>";
                mensaje += "<p><b>Posición:</b> " + (resultado.getPosicion() + 1) + "</p>";
                mensaje += "<p><b>Método:</b> " + opcion + "</p>";
                mensaje += "<p><b>Comparaciones realizadas:</b> " + resultado.getComparaciones() + "</p>";
            } else {
                mensaje += "<h3 style='color: red;'>Clave no encontrada</h3>";
                mensaje += "<p><b>Clave buscada:</b> " + clave + "</p>";
                mensaje += "<p><b>Método:</b> " + opcion + "</p>";
                mensaje += "<p><b>Comparaciones realizadas:</b> " + resultado.getComparaciones() + "</p>";
            }
            mensaje += "</body></html>";
            
            // Usar un JOptionPane para mostrar la información en una ventana emergente
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
            
            // Volvemos a pedir la clave si hubo error
            pedirClaveBusqueda(opcion);
        }
    }
        
    private void realizarBusqueda(String opcion) {
        // Crear un diálogo para ingresar la clave
        JDialog dialogoClave = new JDialog(this, "Ingrese Clave", true);
        dialogoClave.setSize(350, 200);
        dialogoClave.setLocationRelativeTo(this);
        dialogoClave.setLayout(new BorderLayout());
        
        JPanel panelContenido = new JPanel();
        panelContenido.setLayout(new BoxLayout(panelContenido, BoxLayout.Y_AXIS));
        panelContenido.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelContenido.setBackground(new Color(250, 250, 255));
        
        JLabel lblTitulo = new JLabel("Ingrese la clave a buscar:");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JTextField txtClave = new JTextField(10);
        txtClave.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtClave.setAlignmentX(Component.LEFT_ALIGNMENT);
        txtClave.setMaximumSize(new Dimension(Integer.MAX_VALUE, txtClave.getPreferredSize().height));
        
        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnBuscar.setBackground(new Color(100, 150, 220));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        btnBuscar.addActionListener(e -> {
            String claveStr = txtClave.getText().trim();
            if (claveStr.isEmpty()) {
                JOptionPane.showMessageDialog(dialogoClave, 
                    "Ingrese una clave válida.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            try {
                int clave = Integer.parseInt(claveStr);
                dialogoClave.dispose();
                
                // Realizar búsqueda según el método seleccionado
                if (opcion.equals("Búsqueda por Hash")) {
                    ResultadoBusqueda resultado = controlador.buscarClaveHash(clave, metodoHash);
                    mostrarMensaje(resultado.getMensaje());
                    destacarPosicion(resultado.getPosicion());
                } else if (opcion.equals("Búsqueda Lineal")) {
                    ResultadoBusqueda resultado = controlador.buscarClaveLineal(clave);
                    mostrarMensaje(resultado.getMensaje());
                    destacarPosicion(resultado.getPosicion());
                } else if (opcion.equals("Búsqueda Binaria")) {
                    ResultadoBusqueda resultado = controlador.buscarClaveBinaria(clave);
                    mostrarMensaje(resultado.getMensaje());
                    destacarPosicion(resultado.getPosicion());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogoClave, 
                    "Ingrese una clave numérica válida.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
        
        // Efectos al pasar el mouse
        btnBuscar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnBuscar.setBackground(new Color(80, 130, 200));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnBuscar.setBackground(new Color(100, 150, 220));
            }
        });
        
        panelContenido.add(lblTitulo);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 15)));
        panelContenido.add(txtClave);
        panelContenido.add(Box.createRigidArea(new Dimension(0, 20)));
        panelContenido.add(btnBuscar);
        
        dialogoClave.add(panelContenido, BorderLayout.CENTER);
        dialogoClave.setResizable(false);
        dialogoClave.setVisible(true);
    }
    
    private void destacarPosicion(int posicion) {
        if (posicion >= 0 && posicion < tabla.getRowCount()) {
            tabla.setRowSelectionInterval(posicion, posicion);
            tabla.scrollRectToVisible(tabla.getCellRect(posicion, 0, true));
        } else {
            tabla.clearSelection();
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
                // Remover de la lista de claves originales
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
    
    // Método para actualizar la visualización de la tabla
    public void actualizarTabla() {
        int[] tablaHash = controlador.getTablaHash().getTablaHash();
        
        for (int i = 0; i < tablaHash.length; i++) {
            if (tablaHash[i] != -1) {
                modeloTabla.setValueAt(tablaHash[i], i, 1);
            } else {
                modeloTabla.setValueAt("", i, 1); // Celda vacía
            }
        }
    }
    
    // Método para mostrar mensajes en el área de resultados
    public void mostrarMensaje(String mensaje) {
        txtResultado.append(mensaje + "\n");
        // Desplazar al final
        txtResultado.setCaretPosition(txtResultado.getDocument().getLength());
    }
    
    // Getter para el método hash seleccionado
    public String getMetodoHash() {
        return metodoHash;
    }
}