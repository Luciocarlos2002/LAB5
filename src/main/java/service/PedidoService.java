package service;

import dao.PedidoDAO;
import dao.DetalleDAO;
import monitor.MonitorService;

public class PedidoService {

    private MonitorService monitor = new MonitorService();
    private PedidoDAO pedidoDAO = new PedidoDAO();
    private DetalleDAO detalleDAO = new DetalleDAO();

    public int crearPedido(String codcli, double importe) {

        boolean activo = monitor.verificar();

        if (activo) {
            return pedidoDAO.guardarRemoto(codcli, importe);
        } else {
            return pedidoDAO.guardarLocal(codcli, importe);
        }
    }

    public void agregarDetalle(int numped, int codart, int cantidad, double subtotal) {

        boolean activo = monitor.verificar();

        if (activo) {
            detalleDAO.guardarRemoto(numped, codart, cantidad, subtotal);
        } else {
            detalleDAO.guardarLocal(numped, codart, cantidad, subtotal);
        }
    }
}