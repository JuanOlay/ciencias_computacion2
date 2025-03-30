import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TablaHashApp extends JFrame {
    // Componentes gráficos
    private JTextField txtNumClaves, txtDigitosClaves;
    private JTextField txtClave;
    private JComboBox<String> comboMetodo;
    private JTextArea txtResultado;
    private JTable tabla;
    private DefaultTableModel modeloTabla;
    private JScrollPane scrollTabla;
    private JButton btnCrearTabla, btnInsertar, btnBuscar, btnBorrar;
    
    // Controlador
    private TablaHashControlador controlador;
    
    public TablaHashApp() {
        super("Sistema de Tabla Hash");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        controlador = new TablaHashControlador(this);
        inicializarComponentes();
        configurarEventos();
        
        setVisible(true);
    }
    
    private void inicializarComponentes() {
        // Panel principal con BorderLayout
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de configuración
        JPanel panelConfig = new JPanel(new GridLayout(3, 2, 5, 5));
        panelConfig.setBorder(BorderFactory.createTitledBorder("Configuración"));
        
        JLabel lblNumClaves = new JLabel("Número de claves:");
        txtNumClaves = new JTextField("10");
        JLabel lblDigitosClaves = new JLabel("Dígitos por clave:");
        txtDigitosClaves = new JTextField("4");
        btnCrearTabla = new JButton("Crear Tabla Hash");
        
        panelConfig.add(lblNumClaves);
        panelConfig.add(txtNumClaves);
        panelConfig.add(lblDigitosClaves);
        panelConfig.add(txtDigitosClaves);
        panelConfig.add(new JLabel(""));  // Espacio en blanco
        panelConfig.add(btnCrearTabla);
        
        // Panel de operaciones
        JPanel panelOperaciones = new JPanel(new GridLayout(4, 2, 5, 5));
        panelOperaciones.setBorder(BorderFactory.createTitledBorder("Operaciones"));
        
        JLabel lblClave = new JLabel("Clave:");
        txtClave = new JTextField();
        JLabel lblMetodo = new JLabel("Método de transformación:");
        comboMetodo = new JComboBox<>(new String[]{"Módulo", "Cuadrado", "Plegamiento", "Truncamiento"});
        btnInsertar = new JButton("Insertar");
        btnBuscar = new JButton("Buscar");
        btnBorrar = new JButton("Borrar");
        
        panelOperaciones.add(lblClave);
        panelOperaciones.add(txtClave);
        panelOperaciones.add(lblMetodo);
        panelOperaciones.add(comboMetodo);
        panelOperaciones.add(btnInsertar);
        panelOperaciones.add(btnBuscar);
        panelOperaciones.add(btnBorrar);
        
        // Panel de control (unión de configuración y operaciones)
        JPanel panelControl = new JPanel(new BorderLayout(5, 5));
        panelControl.add(panelConfig, BorderLayout.NORTH);
        panelControl.add(panelOperaciones, BorderLayout.CENTER);
        
        // Panel para tabla y resultados
        JPanel panelResultados = new JPanel(new BorderLayout(5, 5));
        panelResultados.setBorder(BorderFactory.createTitledBorder("Resultados"));
        
        // Tabla
        modeloTabla = new DefaultTableModel(new Object[]{"Índice", "Valor"}, 0);
        tabla = new JTable(modeloTabla);
        scrollTabla = new JScrollPane(tabla);
        
        // Área de resultados
        txtResultado = new JTextArea(6, 20);
        txtResultado.setEditable(false);
        JScrollPane scrollResultado = new JScrollPane(txtResultado);
        
        // Agregar componentes al panel de resultados
        panelResultados.add(scrollTabla, BorderLayout.CENTER);
        panelResultados.add(scrollResultado, BorderLayout.SOUTH);
        
        // Agregar paneles al panel principal
        panelPrincipal.add(panelControl, BorderLayout.WEST);
        panelPrincipal.add(panelResultados, BorderLayout.CENTER);
        
        // Agregar panel principal a la ventana
        add(panelPrincipal);
        
        // Deshabilitar botones hasta que se cree la tabla
        btnInsertar.setEnabled(false);
        btnBuscar.setEnabled(false);
        btnBorrar.setEnabled(false);
    }
    
    private void configurarEventos() {
        btnCrearTabla.addActionListener(e -> controlador.crearTablaHash());
        btnInsertar.addActionListener(e -> controlador.insertarClave());
        btnBuscar.addActionListener(e -> controlador.buscarClave());
        btnBorrar.addActionListener(e -> controlador.borrarClave());
    }
    
    // Métodos getters para los componentes
    public JTextField getTxtNumClaves() { return txtNumClaves; }
    public JTextField getTxtDigitosClaves() { return txtDigitosClaves; }
    public JTextField getTxtClave() { return txtClave; }
    public JComboBox<String> getComboMetodo() { return comboMetodo; }
    public JTextArea getTxtResultado() { return txtResultado; }
    public DefaultTableModel getModeloTabla() { return modeloTabla; }
    
    // Métodos para actualizar la interfaz
    public void habilitarBotones(boolean estado) {
        btnInsertar.setEnabled(estado);
        btnBuscar.setEnabled(estado);
        btnBorrar.setEnabled(estado);
    }
    
    public void mostrarMensaje(String mensaje) {
        txtResultado.append(mensaje + "\n");
        // Desplazar al final
        txtResultado.setCaretPosition(txtResultado.getDocument().getLength());
    }
    
    // Método principal
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new TablaHashApp());
    }
}
