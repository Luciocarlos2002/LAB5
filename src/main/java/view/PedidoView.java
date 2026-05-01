package view;

import javax.swing.JOptionPane;
import service.PedidoService;

public class PedidoView {

    public void mostrar() {

        PedidoService service = new PedidoService();

        String codcli = JOptionPane.showInputDialog("Código cliente:");
        double importe = Double.parseDouble(JOptionPane.showInputDialog("Importe:"));

        int numped = service.crearPedido(codcli, importe);

        JOptionPane.showMessageDialog(null, "Pedido creado: " + numped);

        new DetalleView(numped).mostrar();
    }
}