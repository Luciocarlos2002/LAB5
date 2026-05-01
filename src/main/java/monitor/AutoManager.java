package monitor;

import dao.PedidoDAO;
import dao.DetalleDAO;
import view.MainView;

import javax.swing.JOptionPane;

public class AutoManager {

    private MonitorService monitor = new MonitorService();
    private boolean estadoAnterior = true;
    private boolean yaSincronizo = false;
    private MainView view;

    public AutoManager(MainView view) {
        this.view = view;
    }

    public void iniciar() {

        while (true) {
            try {

                // 1) Primer intento
                boolean estado = monitor.verificar();

                // 2) Retry a los 5s si falla
                if (!estado) {
                    Thread.sleep(5000);
                    estado = monitor.verificar();
                }

                // 🔥 actualizar UI
                view.actualizarEstado(estado);

                // 3) Transiciones de estado (alertas + acciones)
                if (!estado && estadoAnterior) {
                    JOptionPane.showMessageDialog(null,
                            "⚠️ Servidor caído (guardando en LOCAL)");
                    yaSincronizo = false;
                }

                if (estado && !estadoAnterior && !yaSincronizo) {
                    JOptionPane.showMessageDialog(null,
                            "✅ Servidor recuperado → sincronizando datos...");

                    new PedidoDAO().sincronizarPedidos();
                    new DetalleDAO().sincronizarDetalle();
                    yaSincronizo = true;
                }

                estadoAnterior = estado;

                // 4) Ciclo cada 10s
                Thread.sleep(10000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}