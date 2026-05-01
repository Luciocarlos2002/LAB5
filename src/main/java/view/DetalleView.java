package view;

import javax.swing.JOptionPane;
import service.PedidoService;

public class DetalleView {

    private int numped;

    public DetalleView(int numped) {
        this.numped = numped;
    }

    public void mostrar() {

        PedidoService service = new PedidoService();

        while (true) {

            int codart = Integer.parseInt(JOptionPane.showInputDialog("Código artículo:"));
            int cantidad = Integer.parseInt(JOptionPane.showInputDialog("Cantidad:"));

            double subtotal = cantidad * 100;

            service.agregarDetalle(numped, codart, cantidad, subtotal);

            int opc = JOptionPane.showConfirmDialog(null, "¿Agregar otro detalle?");
            if (opc != 0) break;
        }
    }
}