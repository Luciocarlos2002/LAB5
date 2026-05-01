package view;

import service.PedidoService;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

public class MainView extends JFrame {

    private PedidoService service = new PedidoService();

    private JTextField txtCodCli = new JTextField(10);
    private JTextField txtImporte = new JTextField(10);

    private JTextField txtCodArt = new JTextField(10);
    private JTextField txtCantidad = new JTextField(10);

    private JLabel lblEstado = new JLabel("● Desconectado");

    private int numpedActual = -1;

    public MainView() {

        setTitle("Sistema de Pedidos");
        setSize(480, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setLayout(new BorderLayout());
        add(header(), BorderLayout.NORTH);
        add(content(), BorderLayout.CENTER);

        setVisible(true);
    }

    // 🔷 HEADER
    private JPanel header() {

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(25, 118, 210));
        panel.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel titulo = new JLabel("Sistema de Pedidos");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));

        lblEstado.setForeground(Color.WHITE);

        panel.add(titulo, BorderLayout.WEST);
        panel.add(lblEstado, BorderLayout.EAST);

        return panel;
    }

    // 🔲 CONTENIDO
    private JPanel content() {

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        panel.add(panelPedido());
        panel.add(panelDetalle());

        return panel;
    }

    // 🧾 PANEL PEDIDO
    private JPanel panelPedido() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Pedido"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        panel.add(new JLabel("Cliente:"), c);

        c.gridx = 1;
        panel.add(txtCodCli, c);

        c.gridx = 0; c.gridy = 1;
        panel.add(new JLabel("Importe:"), c);

        c.gridx = 1;
        panel.add(txtImporte, c);

        JButton btnCrear = new JButton("Crear");
        btnCrear.setBackground(new Color(67, 160, 71));
        btnCrear.setForeground(Color.WHITE);

        btnCrear.addActionListener(e -> crearPedido());

        c.gridx = 1; c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        panel.add(btnCrear, c);

        return panel;
    }

    // 📦 PANEL DETALLE
    private JPanel panelDetalle() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Detalle"));

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        panel.add(new JLabel("Artículo:"), c);

        c.gridx = 1;
        panel.add(txtCodArt, c);

        c.gridx = 0; c.gridy = 1;
        panel.add(new JLabel("Cantidad:"), c);

        c.gridx = 1;
        panel.add(txtCantidad, c);

        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.setBackground(new Color(255, 143, 0));
        btnAgregar.setForeground(Color.WHITE);

        btnAgregar.addActionListener(e -> agregarDetalle());

        c.gridx = 1; c.gridy = 2;
        c.anchor = GridBagConstraints.EAST;
        panel.add(btnAgregar, c);

        return panel;
    }

    // 🧠 LOGICA

    private void crearPedido() {

        try {
            String codcli = txtCodCli.getText();
            double importe = Double.parseDouble(txtImporte.getText());

            numpedActual = service.crearPedido(codcli, importe);

            JOptionPane.showMessageDialog(this, "Pedido creado: " + numpedActual);

            // 🔥 LIMPIAR CAMPOS
            limpiarPedido();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos");
        }
    }

    private void agregarDetalle() {

        if (numpedActual == -1) {
            JOptionPane.showMessageDialog(this, "Primero crea un pedido");
            return;
        }

        try {
            int codart = Integer.parseInt(txtCodArt.getText());
            int cantidad = Integer.parseInt(txtCantidad.getText());

            double subtotal = cantidad * 100;

            service.agregarDetalle(numpedActual, codart, cantidad, subtotal);

            JOptionPane.showMessageDialog(this, "Detalle agregado");

            // 🔥 LIMPIAR CAMPOS
            limpiarDetalle();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Datos inválidos");
        }
    }

    private void limpiarPedido() {
        txtCodCli.setText("");
        txtImporte.setText("");
        txtCodCli.requestFocus();
    }

    private void limpiarDetalle() {
        txtCodArt.setText("");
        txtCantidad.setText("");
        txtCodArt.requestFocus();
    }

    // 🔔 ACTUALIZAR ESTADO
    public void actualizarEstado(boolean activo) {

        if (activo) {
            lblEstado.setText("● ONLINE");
            lblEstado.setForeground(new Color(144, 238, 144));
        } else {
            lblEstado.setText("● OFFLINE");
            lblEstado.setForeground(new Color(255, 102, 102));
        }
    }
}