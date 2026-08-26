DROP DATABASE punto_cajas_db;
CREATE DATABASE punto_cajas_db;
USE punto_cajas_db;

CREATE TABLE TipoDocumento (
  id_documento INT NOT NULL AUTO_INCREMENT,
  descripcion_tipo VARCHAR(45) NOT NULL,
  PRIMARY KEY (id_documento)
);

CREATE TABLE Rol (
  id_rol INT NOT NULL AUTO_INCREMENT,
  detalle_rol VARCHAR(45) NOT NULL,
  PRIMARY KEY (id_rol)
);

CREATE TABLE TiposDocCon (
  id_doc_con INT NOT NULL AUTO_INCREMENT,
  codigo_con INT NOT NULL,
  numero_actual INT NOT NULL,
  PRIMARY KEY (id_doc_con)
);

CREATE TABLE Catalogo (
  id_catalogo INT NOT NULL AUTO_INCREMENT,
  url VARCHAR(45) NULL,
  PRIMARY KEY (id_catalogo)
);

CREATE TABLE MedioPago (
  id_medio_pago INT NOT NULL AUTO_INCREMENT,
  seleccionar_pago VARCHAR(45) NOT NULL,
  PRIMARY KEY (id_medio_pago)
);

CREATE TABLE Usuario (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(45) NOT NULL,
  apellido VARCHAR(45) NOT NULL,
  identificacion_usuario VARCHAR(45) NOT NULL,
  direccion VARCHAR(45) NULL,
  telefono VARCHAR(45) NULL,
  correo VARCHAR(45) NOT NULL,
  clave VARCHAR(45) NOT NULL,
  TipoDocumento_id_documento INT NOT NULL,
  Rol_id_rol INT NOT NULL,
  PRIMARY KEY (id_usuario),
  CONSTRAINT fk_Usuario_TipoDocumento FOREIGN KEY (TipoDocumento_id_documento) REFERENCES TipoDocumento (id_documento),
  CONSTRAINT fk_Usuario_Rol FOREIGN KEY (Rol_id_rol) REFERENCES Rol (id_rol)
);

CREATE TABLE Producto (
  id_producto INT NOT NULL AUTO_INCREMENT,
  descripcion VARCHAR(45) NOT NULL,
  precio FLOAT NOT NULL,
  Catalogo_id_catalogo INT NOT NULL,
  PRIMARY KEY (id_producto),
  CONSTRAINT fk_Producto_Catalogo FOREIGN KEY (Catalogo_id_catalogo) REFERENCES Catalogo (id_catalogo)
);

CREATE TABLE CotizacionCabecera (
  id_cotizacion INT NOT NULL AUTO_INCREMENT,
  fecha DATE NOT NULL,
  valor_unitario DECIMAL(10,2) NOT NULL,
  iva DECIMAL(10,2) NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  Usuario_id_usuario INT NOT NULL,
  TiposDocCon_id_doc_con INT NOT NULL,
  PRIMARY KEY (id_cotizacion),
  CONSTRAINT fk_Cotizacion_Usuario FOREIGN KEY (Usuario_id_usuario) REFERENCES Usuario (id_usuario),
  CONSTRAINT fk_Cotizacion_TiposDocCon FOREIGN KEY (TiposDocCon_id_doc_con) REFERENCES TiposDocCon (id_doc_con)
);

CREATE TABLE DetalleCotizacion (
  id_detalle INT NOT NULL AUTO_INCREMENT,
  cantidad INT NOT NULL,
  alto DECIMAL(10,2) NOT NULL,
  largo DECIMAL(10,2) NOT NULL,
  ancho DECIMAL(10,2) NOT NULL,
  color VARCHAR(45) NULL,
  acabado VARCHAR(45) NULL,
  descripcion_uso_caja VARCHAR(45) NULL,
  CotizacionCabecera_id_cotizacion INT NOT NULL,
  PRIMARY KEY (id_detalle),
  CONSTRAINT fk_DetalleCot_Cabecera FOREIGN KEY (CotizacionCabecera_id_cotizacion) REFERENCES CotizacionCabecera (id_cotizacion)
);

CREATE TABLE PedidoCabecera (
  id_pedido INT NOT NULL AUTO_INCREMENT,
  fecha DATE NOT NULL,
  direccion_envio VARCHAR(45) NOT NULL,
  total FLOAT NOT NULL,
  estado_pedido VARCHAR(45) NOT NULL,
  CotizacionCabecera_id_cotizacion INT NOT NULL,
  PRIMARY KEY (id_pedido),
  CONSTRAINT fk_Pedido_Cotizacion FOREIGN KEY (CotizacionCabecera_id_cotizacion) REFERENCES CotizacionCabecera (id_cotizacion)
);


CREATE TABLE FacturaCabecera (
  id_factura INT NOT NULL AUTO_INCREMENT,
  numero_factura INT NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  PedidoCabecera_id_pedido INT NOT NULL,
  PedidoCabecera_CotizacionCabecera_id_cotizacion INT NOT NULL, 
  PRIMARY KEY (id_factura),
  CONSTRAINT fk_Factura_Pedido FOREIGN KEY (PedidoCabecera_id_pedido) 
    REFERENCES PedidoCabecera (id_pedido),
  CONSTRAINT fk_Factura_Cotizacion FOREIGN KEY (PedidoCabecera_CotizacionCabecera_id_cotizacion) 
    REFERENCES CotizacionCabecera (id_cotizacion) 
);


CREATE TABLE PedidoDetalle (
  id_pedido_detalle INT NOT NULL AUTO_INCREMENT,
  cantidad INT NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  PedidoCabecera_id_pedido INT NOT NULL,
  PedidoCabecera_CotizacionCabecera_id_cotizacion INT NOT NULL,
  PRIMARY KEY (id_pedido_detalle),
  CONSTRAINT fk_PedidoDetalle_Pedido FOREIGN KEY (PedidoCabecera_id_pedido) 
    REFERENCES PedidoCabecera (id_pedido),
  CONSTRAINT fk_PedidoDetalle_Cotizacion FOREIGN KEY (PedidoCabecera_CotizacionCabecera_id_cotizacion) 
    REFERENCES CotizacionCabecera (id_cotizacion)
);
CREATE TABLE DetalleFactura (
  id_detalle_factura INT NOT NULL AUTO_INCREMENT,
  cantidad INT NOT NULL,
  valor_unitario DECIMAL(10,2) NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  FacturaCabecera_id_factura INT NOT NULL,
  Producto_id_producto INT NOT NULL,
  PRIMARY KEY (id_detalle_factura),
  CONSTRAINT fk_DetalleFactura_Factura FOREIGN KEY (FacturaCabecera_id_factura) REFERENCES FacturaCabecera (id_factura),
  CONSTRAINT fk_DetalleFactura_Producto FOREIGN KEY (Producto_id_producto) REFERENCES Producto (id_producto)
);

CREATE TABLE Pago (
  id_pago INT NOT NULL AUTO_INCREMENT,
  monto DECIMAL(10,2) NOT NULL,
  fecha DATE NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  referencia_pago VARCHAR(45) NULL,
  FacturaCabecera_id_factura INT NOT NULL,
  MedioPago_id_medio_pago INT NOT NULL,
  PRIMARY KEY (id_pago),
  CONSTRAINT fk_Pago_Factura FOREIGN KEY (FacturaCabecera_id_factura) REFERENCES FacturaCabecera (id_factura),
  CONSTRAINT fk_Pago_MedioPago FOREIGN KEY (MedioPago_id_medio_pago) REFERENCES MedioPago (id_medio_pago)
);

/*SCRIPTS*/
show tables;
describe tipodocumento;
insert into tipodocumento (descripcion_tipo)
values ('cedula'),
('Tarjeta de identidad'),
('Pasaporte'),
('Cedula extranjera');

describe rol;
insert into rol (detalle_rol)
values ('Administrador'),
('Cliente');

describe tiposdoccon;
insert into tiposdoccon (codigo_con, numero_actual)
values (101, 1001),
(102, 1002),
(103, 1003),
(104, 1004),
(105, 1005);

describe mediopago;
insert into mediopago (seleccionar_pago)
values ('tarjeta de credito'),
('tarjeta de debito'),
('pse');

describe catalogo;
insert into catalogo (url)
values ('www.google.drive');

describe usuario;
insert into usuario (nombre, apellido, identificacion_usuario, direccion, telefono, correo, clave, TipoDocumento_id_documento, Rol_id_rol)
values
('Andres', 'Gomez', 85948394, 'cra 7 G 58-50', '315694850', 'amiguito@gmail.com', 'amiguito1234', 3, 2),
('Dylan', 'Guzman', 45654347, 'cra 4 K 17-59', '315694850', 'amiguito@gmail.com', 'amiguito1234', 4, 2),
('Andriy', 'Castillo', 65657575, 'cra 86 K 87-59', '315694850', 'amiguito@gmail.com', 'amiguito1234', 1, 2),
('Arley', 'Giraldo', 375354657, 'cra 89 K 27-59', '315694850', 'amiguito@gmail.com', 'amiguito1234', 1, 2);

describe producto;
insert into producto (descripcion, precio, catalogo_id_catalogo)
values ('caja para archivo', 5500, 1),
('caja para gallos', 5000, 1),
('caja para mudanza', 6000, 1);

describe cotizacioncabecera;
insert into cotizacioncabecera (fecha, valor_unitario, iva, subtotal, total, Usuario_id_usuario, TiposDocCon_id_doc_con)
values ('2026-04-10', 50000, 9500, 50000, 59500, 1, 2),
('2026-04-11', 120000, 22800, 120000, 142800, 2, 2),
('2026-04-12', 75000, 14250, 75000, 89250, 3, 2);

describe detallecotizacion;
insert into detallecotizacion (cantidad, alto, largo, ancho, color, acabado, descripcion_uso_caja, CotizacionCabecera_id_cotizacion)
values (10, 10.5, 5.5, 5.5, 'rojo', 'pizza', 'para empacar pizzas', 1),
(15, 20.5, 10.5, 9.5, 'verde', 'gallos', 'para empacar gallos', 2),
(20, 15.5, 7.5, 4.5, 'azul', 'computadores', 'para empacar computadores', 3);

describe pedidocabecera;
insert into pedidocabecera (fecha, direccion_envio, total, estado_pedido, CotizacionCabecera_id_cotizacion)
values ('2026-04-10', 'cra 7 G 58-50', 59500, 'entregado', 1),
('2026-04-11', 'cra 4 K 17-59', 142800, 'en proceso', 2),
('2026-04-12', 'cra 86 K 87-59', 89250, 'entregado', 3);

describe facturacabecera;
insert into facturacabecera (numero_factura, total, PedidoCabecera_id_pedido, PedidoCabecera_CotizacionCabecera_id_cotizacion)
values (10, 59500, 1, 1),
(11, 142800, 2, 2),
(12, 89250, 3, 3);

describe pedidodetalle;
insert into pedidodetalle (cantidad, subtotal, PedidoCabecera_id_pedido, PedidoCabecera_CotizacionCabecera_id_cotizacion)
values (10, 50000, 1, 1),
(15, 120000, 2, 2),
(20, 75000, 3, 3);

describe detallefactura;
insert into detallefactura (cantidad, valor_unitario, subtotal, FacturaCabecera_id_factura, Producto_id_producto)
values (10, 50000, 50000, 1, 1),
(15, 25000, 25000, 2, 2),
(12, 100000, 100000, 3, 3);

describe pago;
insert into pago (monto, fecha, total, referencia_pago, FacturaCabecera_id_factura, MedioPago_id_medio_pago)
values (50000, '2026/04/13', 50000, 1, 1, 3),
(10000, '2026/04/10', 10000, 2, 1, 1),
(20000, '2026/04/05', 20000, 3, 2, 2);




