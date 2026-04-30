CREATE DATABASE sistema_pedidos;
USE sistema_pedidos;

-- Tabla: Articulo (Maestra)
CREATE TABLE Articulo (
    codart INT AUTO_INCREMENT PRIMARY KEY,
    nomarticulo VARCHAR(100),
    pvp DOUBLE(10,2),
    stock INT
);

-- Tabla: Cliente (Maestra)
CREATE TABLE Cliente (
    codcli VARCHAR(20) PRIMARY KEY,
    nomcliente VARCHAR(100),
    direccion VARCHAR(200),
    telefono VARCHAR(20)
);

-- Tabla: Pedido (Cabecera - Se graba en VPS o local según estado)
CREATE TABLE Pedido (
    numped INT AUTO_INCREMENT PRIMARY KEY,
    fecreg DATETIME DEFAULT CURRENT_TIMESTAMP,
    codcli VARCHAR(20),
    importe DOUBLE(10,2)
);

-- Tabla: Dped (Detalle - Se graba localmente)
CREATE TABLE Dped (
    iddetalle INT AUTO_INCREMENT PRIMARY KEY,
    numped INT,
    codart INT,
    cantidad INT,
    subtotal DOUBLE(10,2),
    estado INT DEFAULT 0 -- 0: Pendiente, 1: Sincronizado/Íntegro
);

-- Tabla: Monitoreo (Estado del Servidor)
CREATE TABLE Monitoreo (
    numreg INT AUTO_INCREMENT PRIMARY KEY,
    estadoVPS INT, -- 1: Online, 0: Offline
    fecreg DATETIME DEFAULT CURRENT_TIMESTAMP
);