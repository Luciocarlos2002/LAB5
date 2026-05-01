package dao;

import config.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class PedidoDAO {

    // 🔴 GUARDAR LOCAL
    public int guardarLocal(String codcli, double importe) {

        try (Connection conn = DBConnection.getLocal()) {

            String sql = "INSERT INTO Pedido (fecreg, codcli, importe) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setNull(1, Types.TIMESTAMP);
            ps.setString(2, codcli);
            ps.setDouble(3, importe);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // 🟢 GUARDAR REMOTO
    public int guardarRemoto(String codcli, double importe) {

        try (Connection conn = DBConnection.getRemote()) {

            String sql = "INSERT INTO Pedido (fecreg, codcli, importe) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setTimestamp(1,
                    Timestamp.valueOf(LocalDateTime.now(ZoneId.of("America/Lima")))
            );

            ps.setString(2, codcli);
            ps.setDouble(3, importe);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return -1;
    }

    // 🔁 SINCRONIZAR PEDIDOS PENDIENTES
    public void sincronizarPedidos() {

        try (
                Connection local = DBConnection.getLocal();
                Connection remote = DBConnection.getRemote()
        ) {

            String selectSQL = "SELECT * FROM Pedido WHERE fecreg IS NULL";
            ResultSet rs = local.createStatement().executeQuery(selectSQL);

            while (rs.next()) {

                int numpedLocal = rs.getInt("numped");
                String codcli = rs.getString("codcli");
                double importe = rs.getDouble("importe");

                // 🔥 fecha actual (Lima)
                Timestamp ahora = Timestamp.valueOf(
                        LocalDateTime.now(ZoneId.of("America/Lima"))
                );

                // 1️⃣ INSERTAR EN VPS
                PreparedStatement ps = remote.prepareStatement(
                        "INSERT INTO Pedido (fecreg, codcli, importe) VALUES (?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );

                ps.setTimestamp(1, ahora);
                ps.setString(2, codcli);
                ps.setDouble(3, importe);

                ps.executeUpdate();

                ResultSet gen = ps.getGeneratedKeys();
                int nuevoId = -1;
                if (gen.next()) nuevoId = gen.getInt(1);

                // 2️⃣ ACTUALIZAR DETALLE CON NUEVO ID
                PreparedStatement updDetalle = local.prepareStatement(
                        "UPDATE Dped SET numped=? WHERE numped=?"
                );
                updDetalle.setInt(1, nuevoId);
                updDetalle.setInt(2, numpedLocal);
                updDetalle.executeUpdate();

                // 3️⃣ 🔥 MARCAR COMO SINCRONIZADO (CLAVE)
                PreparedStatement updPedido = local.prepareStatement(
                        "UPDATE Pedido SET fecreg=? WHERE numped=?"
                );
                updPedido.setTimestamp(1, ahora);
                updPedido.setInt(2, numpedLocal);
                updPedido.executeUpdate();

                System.out.println("✅ Pedido sincronizado: " + numpedLocal);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}