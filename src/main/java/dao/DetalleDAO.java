package dao;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DetalleDAO {

    public void guardarLocal(int numped, int codart, int cantidad, double subtotal) {
        try (Connection conn = DBConnection.getLocal()) {

            String sql = "INSERT INTO Dped (numped,codart,cantidad,subtotal,estado) VALUES (?,?,?,?,0)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, numped);
            ps.setInt(2, codart);
            ps.setInt(3, cantidad);
            ps.setDouble(4, subtotal);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarRemoto(int numped, int codart, int cantidad, double subtotal) {
        try (Connection conn = DBConnection.getRemote()) {

            String sql = "INSERT INTO Dped (numped,codart,cantidad,subtotal) VALUES (?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, numped);
            ps.setInt(2, codart);
            ps.setInt(3, cantidad);
            ps.setDouble(4, subtotal);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 sincronización segura
    public void sincronizarDetalle() {

        try (
                Connection local = DBConnection.getLocal();
                Connection remote = DBConnection.getRemote()
        ) {

            ResultSet rs = local.createStatement().executeQuery(
                    "SELECT * FROM Dped WHERE estado = 0"
            );

            while (rs.next()) {

                int id = rs.getInt("iddetalle");

                PreparedStatement ps = remote.prepareStatement(
                        "INSERT INTO Dped (numped,codart,cantidad,subtotal) VALUES (?,?,?,?)"
                );

                ps.setInt(1, rs.getInt("numped"));
                ps.setInt(2, rs.getInt("codart"));
                ps.setInt(3, rs.getInt("cantidad"));
                ps.setDouble(4, rs.getDouble("subtotal"));

                ps.executeUpdate();

                // marcar sincronizado
                local.createStatement().executeUpdate(
                        "UPDATE Dped SET estado=1 WHERE iddetalle=" + id
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}