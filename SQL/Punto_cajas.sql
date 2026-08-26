CREATE DATABASE punto_cajas_db;
USE punto_cajas_db;

CREATE TABLE tipos_de_documentos (
  id_documento INT NOT NULL AUTO_INCREMENT,
  descripcion_tipo VARCHAR(45) NOT NULL,
  PRIMARY KEY (id_documento)
);

CREATE TABLE roles (
  id_rol INT NOT NULL AUTO_INCREMENT,
  detalle_rol VARCHAR(45) NOT NULL,
  PRIMARY KEY (id_rol)
);

CREATE TABLE tipos_doc_con (
  id_doc_con INT NOT NULL AUTO_INCREMENT,
  codigo_con INT NOT NULL,
  numero_actual INT NOT NULL,
  PRIMARY KEY (id_doc_con)
);

CREATE TABLE catalogos (
  id_catalogo INT NOT NULL AUTO_INCREMENT,
  stock_actual VARCHAR(45) NOT NULL,
  url VARCHAR(45) NULL,
  PRIMARY KEY (id_catalogo)
);

CREATE TABLE medios_de_pagos (
  id_medio_pago INT NOT NULL AUTO_INCREMENT,
  seleccionar_pago VARCHAR(45) NOT NULL,
  PRIMARY KEY (id_medio_pago)
);

CREATE TABLE permisos (
  id_permiso INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100),
  descripcion VARCHAR(100),
  PRIMARY KEY (id_permiso)
);

CREATE TABLE rol_permisos (
  id_rol INT NOT NULL,
  id_permiso INT NOT NULL,
  CONSTRAINT fk_rol_permisos_roles FOREIGN KEY (id_rol) REFERENCES roles (id_rol),
  CONSTRAINT fk_rol_permisos_permisos FOREIGN KEY (id_permiso) REFERENCES permisos (id_permiso)
);
  
  
  

CREATE TABLE usuarios (
  id_usuario INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(45) NOT NULL,
  apellido VARCHAR(45) NOT NULL,
  identificacion_usuario VARCHAR(45) NOT NULL,
  direccion VARCHAR(45) NULL,
  telefono VARCHAR(45) NULL,
  correo VARCHAR(45) NOT NULL,
  clave VARCHAR(45) NOT NULL,
  fecha_de_nacimiento DATE,
  fecha_de_vencimiento DATE,
  autorizacionDatos TINYINT,
  id_documento INT NOT NULL,
  id_rol INT NOT NULL,
  PRIMARY KEY (id_usuario),
  CONSTRAINT fk_usuario_tipo_de_documentos FOREIGN KEY (id_documento) REFERENCES tipos_de_documentos (idDocumento),
  CONSTRAINT fk_usuario_roles FOREIGN KEY (id_rol) REFERENCES roles (id_rol)
);

CREATE TABLE productos (
  id_producto INT NOT NULL AUTO_INCREMENT,
  descripcion VARCHAR(45) NOT NULL,
  precio FLOAT NOT NULL,
  id_catalogo INT NOT NULL,
  PRIMARY KEY (id_producto),
  CONSTRAINT fk_producto_catalogo FOREIGN KEY (id_catalogo) REFERENCES catalogos (id_catalogo)
);

CREATE TABLE cotizaciones_cabeceras (
  id_cotizacion INT NOT NULL AUTO_INCREMENT,
  fecha DATE NOT NULL,
  valor_unitario DECIMAL(10,2) NOT NULL,
  iva DECIMAL(10,2) NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  id_usuario INT NOT NULL,
  id_doc_con INT NOT NULL,
  PRIMARY KEY (id_cotizacion),
  CONSTRAINT fk_cotizacion_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios (id_usuario),
  CONSTRAINT fk_cotizacion_tipos_doc_con FOREIGN KEY (id_doc_con) REFERENCES tipos_doc_con (id_doc_con)
);

CREATE TABLE detalles_cotizaciones (
  id_detalle INT NOT NULL AUTO_INCREMENT,
  cantidad INT NOT NULL,
  alto DECIMAL(10,2) NOT NULL,
  largo DECIMAL(10,2) NOT NULL,
  ancho DECIMAL(10,2) NOT NULL,
  color VARCHAR(45) NULL,
  acabado VARCHAR(45) NULL,
  descripcion_uso_caja VARCHAR(45) NULL,
  id_cotizacion INT NOT NULL,
  PRIMARY KEY (id_detalle),
  CONSTRAINT fk_detalle_cot_cabecera FOREIGN KEY (id_cotizacion) REFERENCES cotizaciones_cabeceras (id_cotizacion)
);

CREATE TABLE pedidos_cabeceras (
  id_pedido INT NOT NULL AUTO_INCREMENT,
  fecha DATE NOT NULL,
  direccion_envio VARCHAR(45) NOT NULL,
  total FLOAT NOT NULL,
  estado_pedido VARCHAR(45) NOT NULL,
  id_cotizacion INT NOT NULL,
  PRIMARY KEY (id_pedido),
  CONSTRAINT fk_pedido_cotizacion FOREIGN KEY (id_cotizacion) REFERENCES cotizaciones_cabeceras (id_cotizacion)
);


CREATE TABLE facturas_cabeceras (
  id_factura INT NOT NULL AUTO_INCREMENT,
  numero_factura INT NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  id_pedido INT NOT NULL,
  id_cotizacion INT NOT NULL, 
  PRIMARY KEY (id_factura),
  CONSTRAINT fk_factura_pedido FOREIGN KEY (id_pedido) 
    REFERENCES pedidos_cabeceras (id_pedido),
  CONSTRAINT fk_factura_cotizacion FOREIGN KEY (id_cotizacion) 
    REFERENCES cotizaciones_cabeceras (id_cotizacion) 
);


CREATE TABLE pedidos_detalles (
  id_pedido_detalle INT NOT NULL AUTO_INCREMENT,
  cantidad INT NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  id_pedido INT NOT NULL,
  id_cotizacion INT NOT NULL,
  PRIMARY KEY (id_pedido_detalle),
  CONSTRAINT fk_pedido_detalle_Pedido FOREIGN KEY (id_pedido) 
    REFERENCES pedidos_cabeceras (id_pedido),
  CONSTRAINT fk_pedido_detalle_cotizacion FOREIGN KEY (id_cotizacion) 
    REFERENCES cotizaciones_cabeceras (id_cotizacion)
);
CREATE TABLE detalles_facturas (
  id_detalle_factura INT NOT NULL AUTO_INCREMENT,
  cantidad INT NOT NULL,
  valor_unitario DECIMAL(10,2) NOT NULL,
  subtotal DECIMAL(10,2) NOT NULL,
  id_factura INT NOT NULL,
  id_producto INT NOT NULL,
  PRIMARY KEY (id_detalle_factura),
  CONSTRAINT fk_detalle_factura_factura FOREIGN KEY (id_factura) REFERENCES facturas_cabeceras (id_factura),
  CONSTRAINT fk_detalle_factura_producto FOREIGN KEY (id_producto) REFERENCES productos (id_producto)
);

CREATE TABLE pagos (
  id_pago INT NOT NULL AUTO_INCREMENT,
  monto DECIMAL(10,2) NOT NULL,
  fecha DATE NOT NULL,
  total DECIMAL(10,2) NOT NULL,
  referencia_pago VARCHAR(45) NULL,
  id_factura INT NOT NULL,
  id_medio_pago INT NOT NULL,
  PRIMARY KEY (id_pago),
  CONSTRAINT fk_pago_factura FOREIGN KEY (id_factura) REFERENCES facturas_cabeceras (id_factura),
  CONSTRAINT fk_pago_medio_pago FOREIGN KEY (id_medio_pago) REFERENCES medios_de_pagos (id_medio_pago)
);


