import dao.PedidoDAO;
import monitor.AutoManager;

public class Main {

    public static void main(String[] args) {

        // hilo monitoreo
        new Thread(() -> new AutoManager().iniciar()).start();

        PedidoDAO dao = new PedidoDAO();

        // simulación pedidos
        while (true) {
            try {
                dao.guardarDetalle(1, 1, 2, 500);
                Thread.sleep(15000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}