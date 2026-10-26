DROP DATABASE IF EXISTS techfix_db;
CREATE DATABASE techfix_db;
USE techfix_db;

CREATE TABLE clientes (
  id BIGINT NOT NULL AUTO_INCREMENT,
  dni VARCHAR(8) NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  correo VARCHAR(120),
  telefono VARCHAR(15),
  CONSTRAINT pk_clientes PRIMARY KEY (id),
  CONSTRAINT uq_clientes_dni UNIQUE (dni),
  CONSTRAINT uq_clientes_correo UNIQUE (correo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE equipos (
  id BIGINT NOT NULL AUTO_INCREMENT,
  marca VARCHAR(50) NOT NULL,
  modelo VARCHAR(100) NOT NULL,
  numero_serie VARCHAR(50),
  tipo ENUM('LAPTOP', 'DESKTOP') NOT NULL,
  cliente_id BIGINT NOT NULL,
  CONSTRAINT pk_equipos PRIMARY KEY (id),
  CONSTRAINT uq_equipos_serie UNIQUE (numero_serie),
  CONSTRAINT fk_equipos_cliente FOREIGN KEY (cliente_id) REFERENCES clientes(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE roles (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(30) NOT NULL,
  CONSTRAINT pk_roles PRIMARY KEY (id),
  CONSTRAINT uq_roles_nombre UNIQUE (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE usuarios (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(255) NOT NULL, 
  nombre_completo VARCHAR(100) NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT pk_usuarios PRIMARY KEY (id),
  CONSTRAINT uq_usuarios_username UNIQUE (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE usuario_roles (
  usuario_id BIGINT NOT NULL,
  rol_id BIGINT NOT NULL,
  CONSTRAINT pk_usuario_roles PRIMARY KEY (usuario_id, rol_id),
  CONSTRAINT fk_ur_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
  CONSTRAINT fk_ur_rol FOREIGN KEY (rol_id) REFERENCES roles(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE ordenes_servicio (
  id BIGINT NOT NULL AUTO_INCREMENT,
  fecha_ingreso DATE NOT NULL,
  descripcion_falla TEXT NOT NULL,
  diagnostico TEXT,
  estado ENUM('PENDIENTE', 'EN_PROCESO', 'LISTO', 'ENTREGADO', 'CANCELADO') NOT NULL,
  motivo_cancelacion VARCHAR(255),
  fecha_actualizacion DATETIME NOT NULL,
  equipo_id BIGINT NOT NULL,
  usuario_id BIGINT NOT NULL, 
  CONSTRAINT pk_ordenes PRIMARY KEY (id),
  CONSTRAINT fk_ordenes_equipo FOREIGN KEY (equipo_id) REFERENCES equipos(id),
  CONSTRAINT fk_ordenes_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE usuario_roles;
TRUNCATE TABLE roles;
TRUNCATE TABLE usuarios;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO roles (nombre) VALUES 
('ROLE_ADMIN'), 
('ROLE_TECNICO');

INSERT INTO usuarios (username, password, nombre_completo, activo) VALUES 
('Jorge', '$2a$10$dJ1zMT2qMogUAuGY4MHuGeYHo9ApQTJnZ1jJLw29h/wKGmi2LMzAi', 'Jorge López - Gerente de Tienda', true),
('Marta', '$2a$10$dJ1zMT2qMogUAuGY4MHuGeYHo9ApQTJnZ1jJLw29h/wKGmi2LMzAi', 'Marta Ramos - Jefa de Operaciones', true),
('Carlos', '$2a$10$dJ1zMT2qMogUAuGY4MHuGeYHo9ApQTJnZ1jJLw29h/wKGmi2LMzAi', 'Carlos Silva - Coordinador de Taller', true),
('Juan', '$2a$10$dJ1zMT2qMogUAuGY4MHuGeYHo9ApQTJnZ1jJLw29h/wKGmi2LMzAi', 'Juan Pérez', true),
('Ana', '$2a$10$dJ1zMT2qMogUAuGY4MHuGeYHo9ApQTJnZ1jJLw29h/wKGmi2LMzAi', 'Ana Torres', true),
('Luis', '$2a$10$dJ1zMT2qMogUAuGY4MHuGeYHo9ApQTJnZ1jJLw29h/wKGmi2LMzAi', 'Luis Vega', true);


INSERT INTO usuario_roles (usuario_id, rol_id) VALUES 
(1, 1), (2, 1), (3, 1);

INSERT INTO usuario_roles (usuario_id, rol_id) VALUES 
(4, 2), (5, 2), (6, 2);

INSERT INTO clientes (dni, nombre, correo, telefono) VALUES
('71000001', 'Carlos Mendoza', 'carlos.mendoza@mail.com', '987654321'),
('71000002', 'Laura García', 'laura.garcia@mail.com', '987654322'),
('71000003', 'Luis Ramírez', 'luis.ramirez@mail.com', '987654323'),
('71000004', 'Andrea Paredes', 'andrea.paredes@mail.com', '987654324'),
('71000005', 'José Castillo', 'jose.castillo@mail.com', '987654325'),
('71000006', 'María Fernández', 'maria.fernandez@mail.com', '987654326'),
('71000007', 'Jorge Rojas', 'jorge.rojas@mail.com', '987654327'),
('71000008', 'Ana Torres', 'ana.torres@mail.com', '987654328'),
('71000009', 'Miguel Vargas', 'miguel.vargas@mail.com', '987654329'),
('71000010', 'Elena Castro', 'elena.castillo@mail.com', '987654330');

INSERT INTO equipos (marca, modelo, numero_serie, tipo, cliente_id) VALUES
('HP', 'Pavilion 15', 'SN-4521', 'LAPTOP', 1),
('Dell', 'Inspiron 15', 'SN-8843', 'LAPTOP', 2),
('Lenovo', 'IdeaPad 3', 'SN-6710', 'LAPTOP', 3),
('Asus', 'ROG Strix', 'SN-9012', 'LAPTOP', 4),
('Acer', 'Aspire 5', 'SN-3008', 'LAPTOP', 5),
('Custom Build', 'Gamer X', 'SN-1122', 'DESKTOP', 6),
('Lenovo', 'ThinkCentre', 'SN-3344', 'DESKTOP', 7),
('HP', 'Omen 16', 'SN-5566', 'LAPTOP', 8),
('Apple', 'MacBook Air M1', 'SN-7788', 'LAPTOP', 9),
('Dell', 'OptiPlex 3080', 'SN-9900', 'DESKTOP', 10);

INSERT INTO ordenes_servicio (fecha_ingreso, descripcion_falla, diagnostico, estado, motivo_cancelacion, fecha_actualizacion, equipo_id, usuario_id) VALUES
-- Órdenes Antiguas (ENTREGADOS / CANCELADOS)
('2026-06-01', 'Lentitud extrema', 'Cambio a disco SSD y RAM', 'ENTREGADO', NULL, '2026-06-05 10:30:00', 1, 4),
('2026-06-03', 'Pantalla parpadea', 'Flex de video dañado, se reemplazó', 'ENTREGADO', NULL, '2026-06-08 16:45:00', 2, 5),
('2026-06-10', 'No enciende', 'Placa madre en cortocircuito', 'CANCELADO', 'Cliente no aceptó presupuesto de placa', '2026-06-12 09:15:00', 3, 6),
('2026-06-15', 'Teclado no funciona', 'Cambio de teclado completo', 'ENTREGADO', NULL, '2026-06-18 11:20:00', 4, 4),
('2026-06-20', 'Se apaga sola a los 10 min', 'Mantenimiento térmico', 'ENTREGADO', NULL, '2026-06-22 14:10:00', 5, 5),
('2026-06-25', 'Disco duro hace ruido', 'Disco mecánico dañado irreversible', 'CANCELADO', 'Cliente compró equipo nuevo', '2026-06-26 10:05:00', 6, 6),

-- Órdenes Recientes (LISTOS / EN_PROCESO)
('2026-07-01', 'Actualización de Windows 11', 'Instalación limpia y drivers', 'LISTO', NULL, '2026-07-03 09:30:00', 7, 4),
('2026-07-02', 'Bisagra rota', 'Reparación y soldadura de bisagra izquierda', 'LISTO', NULL, '2026-07-05 15:40:00', 8, 5),
('2026-07-05', 'Batería inflada', 'Retiro de batería dañada, esperando repuesto', 'EN_PROCESO', NULL, '2026-07-06 11:00:00', 9, 6),
('2026-07-08', 'No da video', 'Reballing al chip de video', 'EN_PROCESO', NULL, '2026-07-10 16:30:00', 10, 4),
('2026-07-10', 'Lentitud al abrir programas', 'Clonación de disco a SSD NVMe', 'LISTO', NULL, '2026-07-12 10:20:00', 1, 5),
('2026-07-11', 'Ventilador suena muy fuerte', 'Limpieza profunda de cooler', 'EN_PROCESO', NULL, '2026-07-13 14:50:00', 2, 6),

-- Órdenes Nuevas (PENDIENTES)
('2026-07-14', 'Pantalla azul al iniciar', NULL, 'PENDIENTE', NULL, '2026-07-14 09:00:00', 3, 4),
('2026-07-14', 'Puertos USB no reconocen', NULL, 'PENDIENTE', NULL, '2026-07-14 10:15:00', 4, 5),
('2026-07-15', 'Derramó agua en el teclado', NULL, 'PENDIENTE', NULL, '2026-07-15 08:45:00', 5, 6),
('2026-07-15', 'Pide contraseña de BIOS', NULL, 'PENDIENTE', NULL, '2026-07-15 11:30:00', 6, 4),
('2026-07-16', 'No se conecta al WiFi', NULL, 'PENDIENTE', NULL, '2026-07-16 09:10:00', 7, 5),
('2026-07-16', 'Recuperación de datos', NULL, 'PENDIENTE', NULL, '2026-07-16 10:25:00', 8, 6),
('2026-07-16', 'Instalación de Office y Antivirus', NULL, 'PENDIENTE', NULL, '2026-07-16 12:00:00', 9, 4),
('2026-07-16', 'Armado de PC nueva', NULL, 'PENDIENTE', NULL, '2026-07-16 15:30:00', 10, 5);
