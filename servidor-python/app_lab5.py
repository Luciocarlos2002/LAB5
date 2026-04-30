from flask import Flask, request, jsonify
import mysql.connector
from datetime import datetime
from zoneinfo import ZoneInfo

app = Flask(__name__)

# CONFIG DB
db_config = {
    "host": "34.176.161.147",
    "user": "lucio",
    "password": "Lucio123!",
    "database": "sistema_pedidos"
}

def get_connection():
    return mysql.connector.connect(**db_config)

# =========================
# 1. CREAR PEDIDO (CABECERA)
# =========================
@app.route('/pedido', methods=['POST'])
def crear_pedido():
    data = request.json

    conn = get_connection()
    cursor = conn.cursor()

    sql = """
            INSERT INTO Pedido (fecreg, codcli, importe)
            VALUES (%s, %s, %s)
        """
    valores = (
        datetime.now(ZoneInfo("America/Lima")),
        data['codcli'],
        data['importe']
    )

    cursor.execute(sql, valores)
    conn.commit()

    pedido_id = cursor.lastrowid

    cursor.close()
    conn.close()

    return jsonify({
        "mensaje": "Pedido registrado",
        "pedido_id": pedido_id
    })


# =========================
# 2. PING (MONITOREO)
# =========================
@app.route('/ping', methods=['GET'])
def ping():
    return jsonify({
        "Estado": "OK",
        "Detalle": "El servidor se encuentra disponible",
        "Fecha": datetime.now(ZoneInfo("America/Lima")).isoformat()
    })


# =========================
# 3. REGISTRAR MONITOREO
# =========================
@app.route('/monitoreo', methods=['POST'])
def registrar_monitoreo():
    data = request.json

    conn = get_connection()
    cursor = conn.cursor()

    sql = """
            INSERT INTO Monitoreo (estadoVPS, fecreg)
            VALUES (%s, %s)
        """
    valores = (data['estadoVPS'], datetime.now(ZoneInfo("America/Lima")))

    cursor.execute(sql, valores)
    conn.commit()

    cursor.close()
    conn.close()

    return jsonify({"mensaje": "Monitoreo del servidor registrado"})


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)