package monitor;

import dao.PedidoDAO;
import util.HttpUtil;

public class AutoManager {

    private MonitorService monitor = new MonitorService();
    private boolean estadoAnterior = true;

    public void iniciar() {

        while (true) {
            try {
                boolean estado = monitor.verificarServidor();

                if (estado) {
                    System.out.println("Servidor en línea");
                } else {
                    System.out.println("Primer intento fallido...");

                    Thread.sleep(5000);

                    boolean segundoIntento = monitor.verificarServidor();

                    if (!segundoIntento) {
                        System.out.println("Servidor CAÍDO");
                        estado = false;
                    } else {
                        estado = true;
                    }
                }

                // registrar monitoreo
                HttpUtil.post(
                        "http://34.176.161.147:5000/monitoreo",
                        "{ \"estadoVPS\": " + (estado ? 1 : 0) + " }"
                );

                if (!estado && estadoAnterior) {
                    System.out.println("⚠️ VPS CAÍDO -> SERVIDOR NO RESPONDE...");
                }

                if (estado && !estadoAnterior) {
                    System.out.println("✅ VPS RECUPERADO → SINCRONIZANDO...");
                    new PedidoDAO().sincronizar();
                }

                estadoAnterior = estado;

                Thread.sleep(10000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}