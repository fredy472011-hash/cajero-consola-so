-- ====================================================================
--  PASO 8 - La base de datos del cajero
--
--  Hasta el paso 7 las cuentas vivian en una lista dentro del programa.
--  Eso significa que al cerrar el programa SE PERDIA TODO.
--
--  Esto es la MEMORIA DE LARGO PLAZO del sistema: sobrevive al apagado,
--  a la cancelacion de una sesion y a un corte de energia.
--
--  Se contrapone con la lista en memoria (la RAM), que es VOLATIL.
--  Esa diferencia es uno de los temas de la materia.
--
--  Para crearla:
--      mysql -u root -p < esquema.sql
-- ====================================================================

CREATE DATABASE IF NOT EXISTS cajero_simple
    DEFAULT CHARACTER SET utf8mb4;

USE cajero_simple;

-- --------------------------------------------------------------------
-- Las cuentas de los clientes
-- --------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS cuentas (
    numero    VARCHAR(20)  NOT NULL,
    titular   VARCHAR(100) NOT NULL,
    pin       CHAR(4)      NOT NULL,
    saldo     BIGINT       NOT NULL DEFAULT 0,

    -- La CLAVE PRIMARIA identifica a cada fila de forma unica.
    -- No pueden existir dos cuentas con el mismo numero.
    PRIMARY KEY (numero)
);

-- --------------------------------------------------------------------
-- El historial de operaciones
--
-- Esta tabla es la que hace que el cajero "se acuerde". Cada retiro o
-- deposito queda registrado para siempre, aunque el programa se cierre.
-- --------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS movimientos (
    id                BIGINT AUTO_INCREMENT,   -- se numera solo: 1, 2, 3...
    numero_cuenta     VARCHAR(20) NOT NULL,
    tipo              VARCHAR(10) NOT NULL,    -- RETIRO o DEPOSITO
    monto             BIGINT      NOT NULL,
    saldo_resultante  BIGINT      NOT NULL,
    ocurrido_en       TIMESTAMP   NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),

    -- La CLAVE FORANEA obliga a que numero_cuenta exista en cuentas.
    -- Si se intenta guardar un movimiento de una cuenta que no existe,
    -- MySQL lo rechaza. Asi la base se protege sola de datos incoherentes.
    FOREIGN KEY (numero_cuenta) REFERENCES cuentas(numero)
);

-- --------------------------------------------------------------------
-- Datos de prueba
-- --------------------------------------------------------------------

-- Se borran primero para poder correr este archivo varias veces
-- sin que se dupliquen las filas.
DELETE FROM movimientos;
DELETE FROM cuentas;

INSERT INTO cuentas (numero, titular, pin, saldo) VALUES
    ('1001', 'Fredy Canete',  '1234', 2500000),
    ('1002', 'Elias Benitez', '4321', 1800000),
    ('1003', 'Cuenta Vacia',  '0000',   30000);

-- --------------------------------------------------------------------
-- Consultas para probar a mano antes de escribir el Java
-- --------------------------------------------------------------------
--
--   SELECT * FROM cuentas;
--   SELECT * FROM movimientos ORDER BY id DESC;
--
--   -- el saldo de una cuenta
--   SELECT saldo FROM cuentas WHERE numero = '1001';
--
--   -- los ultimos 5 movimientos de una cuenta
--   SELECT tipo, monto, saldo_resultante, ocurrido_en
--   FROM movimientos
--   WHERE numero_cuenta = '1001'
--   ORDER BY ocurrido_en DESC
--   LIMIT 5;
--
--   -- cuanto dinero hay en todo el banco
--   SELECT SUM(saldo) FROM cuentas;
-- --------------------------------------------------------------------
