// pantallaInicio.java

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PantallaInicio extends JFrame {
    public PantallaInicio() {
        super("Tabla Hash - Bienvenida");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal con gradiente
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(240, 240, 255),
                    0, h, new Color(220, 220, 245)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                g2d.dispose();
            }
        };
        panelPrincipal.setLayout(new BorderLayout(20, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        // Título de la aplicación
        JLabel lblTitulo = new JLabel("Tabla Hash");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setForeground(new Color(50, 50, 100));
        
        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Aplicación para gestión de estructuras de datos");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.ITALIC, 18));
        lblSubtitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblSubtitulo.setForeground(new Color(80, 80, 130));
        
        // Panel superior
        JPanel panelSuperior = new JPanel();
        panelSuperior.setLayout(new BoxLayout(panelSuperior, BoxLayout.Y_AXIS));
        panelSuperior.setOpaque(false);
        panelSuperior.add(lblTitulo);
        panelSuperior.add(Box.createRigidArea(new Dimension(0, 10)));
        panelSuperior.add(lblSubtitulo);
        
        // Funcionalidades
        JPanel panelFuncionalidades = new JPanel();
        panelFuncionalidades.setLayout(new BoxLayout(panelFuncionalidades, BoxLayout.Y_AXIS));
        panelFuncionalidades.setOpaque(false);
        panelFuncionalidades.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 200), 1, true),
                "Funcionalidades",
                0,
                0,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(70, 70, 120)
            ),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Lista de funcionalidades
        String[] funcionalidades = {
            "• Creación y visualización de tablas hash",
            "• Múltiples métodos de transformación (Módulo, Cuadrado, Plegamiento, Truncamiento)",
            "• Gestión avanzada de colisiones (Lineal, Cuadrática, Doble Hash)",
            "• Operaciones de inserción, búsqueda y borrado de claves",
            "• Búsqueda por hash, lineal y binaria",
            "• Persistencia de datos entre sesiones",
            "• Información detallada sobre operaciones y comparaciones"
        };
        
        for (String func : funcionalidades) {
            JLabel lblFunc = new JLabel(func);
            lblFunc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblFunc.setForeground(new Color(50, 50, 90));
            lblFunc.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            panelFuncionalidades.add(lblFunc);
            panelFuncionalidades.add(Box.createRigidArea(new Dimension(0, 5)));
        }
        
        // Botón de inicio
        JButton btnIniciar = new JButton("Comenzar");
        btnIniciar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnIniciar.setBackground(new Color(100, 130, 220));
        btnIniciar.setForeground(Color.WHITE);
        btnIniciar.setFocusPainted(false);
        btnIniciar.setPreferredSize(new Dimension(200, 50));
        btnIniciar.setBorderPainted(false);
        btnIniciar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover para el botón
        btnIniciar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnIniciar.setBackground(new Color(80, 110, 200));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnIniciar.setBackground(new Color(100, 130, 220));
            }
        });
        
        btnIniciar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                SwingUtilities.invokeLater(() -> new TablaHashApp());
            }
        });
        
        // Panel inferior con botón centrado
        JPanel panelBoton = new JPanel();
        panelBoton.setOpaque(false);
        panelBoton.add(btnIniciar);
        
        // Añadir componentes al panel principal
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
        panelPrincipal.add(panelFuncionalidades, BorderLayout.CENTER);
        panelPrincipal.add(panelBoton, BorderLayout.SOUTH);
        
        // Añadir panel principal al frame
        add(panelPrincipal);
        setVisible(true);
    }
}