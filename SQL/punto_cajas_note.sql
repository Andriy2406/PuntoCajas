-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema punto_cajas
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema punto_cajas
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `punto_cajas` DEFAULT CHARACTER SET utf8 ;
USE `punto_cajas` ;

-- -----------------------------------------------------
-- Table `punto_cajas`.`tipos_de_documentos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `punto_cajas`.`tipos_de_documentos` (
  `id_documento` INT NOT NULL AUTO_INCREMENT,
  `descripcion_tipo` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_documento`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `punto_cajas`.`roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `punto_cajas`.`roles` (
  `id_rol` INT NOT NULL AUTO_INCREMENT,
  `detalle_rol` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_rol`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `punto_cajas`.`usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `punto_cajas`.`usuarios` (
  `id_usuario` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `apellido` VARCHAR(45) NOT NULL,
  `identificacion_usuario` VARCHAR(45) NOT NULL,
  `direccion` VARCHAR(45) NOT NULL,
  `telefono` VARCHAR(45) NOT NULL,
  `correo` VARCHAR(45) NOT NULL,
  `clave` VARCHAR(45) NOT NULL,
  `fecha_de_nacimiento` DATE NOT NULL,
  `fecha_de_vencimiento_clave` DATE NOT NULL,
  `autorizacion_datos` TINYINT NOT NULL,
  `id_documento` INT NOT NULL,
  `id_rol` INT NOT NULL,
  PRIMARY KEY (`id_usuario`),
  INDEX `fk_Usuario_TipoDocumento1_idx` (`id_documento` ) ,
  INDEX `fk_Usuario_Rol1_idx` (`id_rol` ) ,
  UNIQUE INDEX `correo_UNIQUE` (`correo` ) ,
  CONSTRAINT `fk_Usuario_TipoDocumento1`
    FOREIGN KEY (`id_documento`)
    REFERENCES `punto_cajas`.`tipos_de_documentos` (`id_documento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Usuario_Rol1`
    FOREIGN KEY (`id_rol`)
    REFERENCES `punto_cajas`.`roles` (`id_rol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `punto_cajas`.`tipos_doc_con`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `punto_cajas`.`tipos_doc_con` (
  `id_doc_con` INT NOT NULL AUTO_INCREMENT,
  `codigo_con` INT NOT NULL,
  `numero_actual` INT NOT NULL,
  PRIMARY KEY (`id_doc_con`),
  UNIQUE INDEX `codigo_con_UNIQUE` (`codigo_con` ) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `punto_cajas`.`cotizaciones_cabeceras`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `punto_cajas`.`cotizaciones_cabeceras` (
  `id_cotizacion` INT NOT NULL AUTO_INCREMENT,
  `fecha` DATE NOT NULL,
  `valor_unitario` DECIMAL NOT NULL,
  `iva` DECIMAL NOT NULL,
  `subtotal` DECIMAL NOT NULL,
  `total` DECIMAL NOT NULL,
  `id_usuario` INT NOT NULL,
  `id_doc_con` INT NOT NULL,
  PRIMARY KEY (`id_cotizacion`, `id_doc_con`),
  INDEX `fk_CotizacionCabecera_Usuario1_idx` (`id_usuario` ) ,
  INDEX `fk_CotizacionCabecera_TiposDocCon1_idx` (`id_doc_con` ) ,
  CONSTRAINT `fk_CotizacionCabecera_Usuario1`
    FOREIGN KEY (`id_usuario`)
    REFERENCES `punto_cajas`.`usuarios` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CotizacionCabecera_TiposDocCon1`
    FOREIGN KEY (`id_doc_con`)
    REFERENCES `punto_cajas`.`tipos_doc_con` (`id_doc_con`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `punto_cajas`.`detalles_cotizaciones`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `punto_cajas`.`detalles_cotizaciones` (
  `id_detalle` INT NOT NULL AUTO_INCREMENT,
  `cantidad` INT NOT NULL,
  `alto` DECIMAL NOT NULL,
  `largo` DECIMAL NOT NULL,
  `ancho` DECIMAL NOT NULL,
  `color` VARCHAR(45) NOT NULL,
  `acabado` VARCHAR(45) NOT NULL,
  `descripcion_uso_caja` VARCHAR(45) NOT NULL,
  `id_cotizacion` INT NOT NULL,
  PRIMARY KEY (`id_detalle`, `id_cotizacion`),
  INDEX `fk_DetalleCotizacion_CotizacionCabecera1_idx` (`id_cotizacion` ) ,
  CONSTRAINT `fk_DetalleCotizacion_CotizacionCabecera1`
    FOREIGN KEY (`id_cotizacion`)
    REFERENCES `punto_cajas`.`cotizaciones_cabeceras` (`id_cotizacion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `punto_cajas`.`catalogos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `punto_cajas`.`catalogos` (
  `id_catalogo` INT NOT NULL AUTO_INCREMENT,
  `stock_actual` VARCHAR(45) NOT NULL,
  `url` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_catalogo`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `punto_cajas`.`productos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `punto_cajas`.`productos` (
  `id_producto` INT NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(45) NOT NULL,
  `precio` FLOAT NOT NULL,
  `id_catalogo` INT NOT NULL,
  PRIMARY KEY (`id_producto`),
  INDEX `fk_Producto_Catalogo1_idx` (`id_catalogo` ) ,
  CONSTRAINT `fk_Producto_Catalogo1`
    FOREIGN KEY (`id_catalogo`)
    REFERENCES `punto_cajas`.`catalogos` (`id_catalogo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `punto_cajas`.`pedidos_cabeceras`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `punto_cajas`.`pedidos_cabeceras` (
  `id_pedido` INT NOT NULL AUTO_INCREMENT,
  `fecha` DATE NOT NULL,
  `direccion_envio` VARCHAR(45) NOT NULL,
  `total` FLOAT NOT NULL,
  `estado_pedido` VARCHAR(45) NOT NULL,
  `id_cotizacion` INT NOT NULL,
  PRIMARY KEY (`id_pedido`, `id_cotizacion`),
  INDEX `fk_PedidoCabecera_CotizacionCabecera1_idx` (`id_cotizacion` ) ,
  CONSTRAINT `fk_PedidoCabecera_CotizacionCabecera1`
    FOREIGN KEY (`id_cotizacion`)
    REFERENCES `punto_cajas`.`cotizaciones_cabeceras` (`id_cotizacion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `punto_cajas`.`facturas_cabeceras`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `punto_cajas`.`facturas_cabeceras` (
  `id_factura` INT NOT NULL AUTO_INCREMENT,
  `numero_factura` INT NOT NULL,
  `total` DECIMAL NOT NULL,
  `id_pedido` INT NOT NULL,
  `id_cotizacion` INT NOT NULL,
  PRIMARY KEY (`id_factura`),
  INDEX `fk_FacturaCabecera_PedidoCabecera1_idx` (`id_pedido` , `id_cotizacion` ) ,
  CONSTRAINT `fk_FacturaCabecera_PedidoCabecera1`
    FOREIGN KEY (`id_pedido` , `id_cotizacion`)
    REFERENCES `punto_cajas`.`pedidos_cabeceras` (`id_pedido` , `id_cotizacion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `punto_cajas`.`detalles_facturas`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `punto_cajas`.`detalles_facturas` (
  `id_detalle_factura` INT NOT NULL AUTO_INCREMENT,
  `cantidad` INT NOT NULL,
  `valor_unitario` DECIMAL NOT NULL,
  `subtotal` DECIMAL NOT NULL,
  `id_factura` INT NOT NULL,
  `id_producto` INT NOT NULL,
  PRIMARY KEY (`id_detalle_factura`, `id_factura`),
  INDEX `fk_DetalleFactura_FacturaCabecera1_idx` (`id_factura` ) ,
  INDEX `fk_DetalleFactura_Producto1_idx` (`id_producto` ) ,
  CONSTRAINT `fk_DetalleFactura_FacturaCabecera1`
    FOREIGN KEY (`id_factura`)
    REFERENCES `punto_cajas`.`facturas_cabeceras` (`id_factura`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_DetalleFactura_Producto1`
    FOREIGN KEY (`id_producto`)
    REFERENCES `punto_cajas`.`productos` (`id_producto`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `punto_cajas`.`medios_de_pagos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `punto_cajas`.`medios_de_pagos` (
  `id_medio_pago` INT NOT NULL AUTO_INCREMENT,
  `seleccionar_pago` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id_medio_pago`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `punto_cajas`.`pagos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `punto_cajas`.`pagos` (
  `id_pago` INT NOT NULL AUTO_INCREMENT,
  `monto` DECIMAL NOT NULL,
  `fecha` DATE NOT NULL,
  `total` DECIMAL NOT NULL,
  `referencia_pago` VARCHAR(45) NOT NULL,
  `id_factura` INT NOT NULL,
  `id_medio_pago` INT NOT NULL,
  PRIMARY KEY (`id_pago`),
  INDEX `fk_Pago_FacturaCabecera1_idx` (`id_factura` ) ,
  INDEX `fk_Pago_MedioPago1_idx` (`id_medio_pago` ) ,
  CONSTRAINT `fk_Pago_FacturaCabecera1`
    FOREIGN KEY (`id_factura`)
    REFERENCES `punto_cajas`.`facturas_cabeceras` (`id_factura`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Pago_MedioPago1`
    FOREIGN KEY (`id_medio_pago`)
    REFERENCES `punto_cajas`.`medios_de_pagos` (`id_medio_pago`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `punto_cajas`.`pedidos_detalles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `punto_cajas`.`pedidos_detalles` (
  `id_pedido_detalle` INT NOT NULL AUTO_INCREMENT,
  `cantidad` INT NOT NULL,
  `subtotal` DECIMAL NOT NULL,
  `id_pedido` INT NOT NULL,
  `id_cotizacion` INT NOT NULL,
  PRIMARY KEY (`id_pedido_detalle`, `id_pedido`, `id_cotizacion`),
  INDEX `fk_PedidoDetalle_PedidoCabecera1_idx` (`id_pedido` , `id_cotizacion` ) ,
  CONSTRAINT `fk_PedidoDetalle_PedidoCabecera1`
    FOREIGN KEY (`id_pedido` , `id_cotizacion`)
    REFERENCES `punto_cajas`.`pedidos_cabeceras` (`id_pedido` , `id_cotizacion`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `punto_cajas`.`permisos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `punto_cajas`.`permisos` (
  `id_permiso` INT NOT NULL AUTO_INCREMENT,
  `descripcion` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id_permiso`),
  UNIQUE INDEX `idPermiso_UNIQUE` (`id_permiso` ) )
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `punto_cajas`.`roles_permisos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `punto_cajas`.`roles_permisos` (
  `id_rol` INT NOT NULL,
  `id_permiso` INT NOT NULL,
  PRIMARY KEY (`id_rol`, `id_permiso`),
  INDEX `fk_Rol_has_Permiso_Permiso1_idx` (`id_permiso` ) ,
  INDEX `fk_Rol_has_Permiso_Rol1_idx` (`id_rol` ) ,
  CONSTRAINT `fk_Rol_has_Permiso_Rol1`
    FOREIGN KEY (`id_rol`)
    REFERENCES `punto_cajas`.`roles` (`id_rol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Rol_has_Permiso_Permiso1`
    FOREIGN KEY (`id_permiso`)
    REFERENCES `punto_cajas`.`permisos` (`id_permiso`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
