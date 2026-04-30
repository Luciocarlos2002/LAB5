package dao;

import config.DBConnection;
import util.HttpUtil;

import java.sql.*;

public class PedidoDAO {

    // guardar detalle local
    public void guardarDetalle(int numped, int codart, int cantidad, double subtotal) {

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "INSERT INTO Dped (numped, codart, cantidad, subtotal, estado) VALUES (?, ?, ?, ?, 0)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, numped);
            ps.setInt(2, codart);
            ps.setInt(3, cantidad);
            ps.setDouble(4, subtotal);

            ps.executeUpdate();

            System.out.println("Detalle guardado LOCAL");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // sincronizar pendientes
    public void sincronizar() {

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "SELECT * FROM Dped WHERE estado = 0";
            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int id = rs.getInt("iddetalle");
                int numped = rs.getInt("numped");

                boolean enviado = enviarPedido(numped);

                if (enviado) {
                    marcarEnviado(id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean enviarPedido(int numped) {

        String json = "{ \"codcli\": \"C001\", \"importe\": 100 }";

        return HttpUtil.post(
                "http://34.176.161.147:5000/pedido",
                json
        );
    }

    private void marcarEnviado(int id) throws Exception {

        try (Connection conn = DBConnection.getConnection()) {

            String sql = "UPDATE Dped SET estado = 1 WHERE iddetalle = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);
            ps.executeUpdate();

            System.out.println("Detalle sincronizado ID: " + id);
        }
    }
}