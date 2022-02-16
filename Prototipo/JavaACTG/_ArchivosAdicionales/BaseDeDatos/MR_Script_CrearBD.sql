-- MySQL Workbench Forward Engineering - Adaptado

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema sistema_actgu
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sistema_actgu` DEFAULT CHARACTER SET utf8 ;
USE `sistema_actgu` ;

-- -----------------------------------------------------
-- Table `sistema_actgu`.`Roles`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sistema_actgu`.`Roles` (
  `id_rol` INT NOT NULL,
  `rol` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id_rol`)
)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `sistema_actgu`.`Tipos_de_Equipamientos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sistema_actgu`.`Tipos_de_Equipamientos` (
  `id_tipo_de_equipamiento` INT NOT NULL,
  `nombre` VARCHAR(30) NOT NULL,
  `valor_clo` FLOAT NOT NULL,
  `descripcion` VARCHAR(200) NOT NULL,
  `suma_clo_para_m` FLOAT NOT NULL,
  `descripcion_para_m` VARCHAR(200) NOT NULL,
  `suma_clo_para_h` FLOAT NOT NULL,
  `descripcion_para_h` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`id_tipo_de_equipamiento`)
)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `sistema_actgu`.`Grupos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sistema_actgu`.`Grupos` (
  `id_grupo` INT NOT NULL,
  `nombre` VARCHAR(20) NOT NULL,
  PRIMARY KEY (`id_grupo`)
)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `sistema_actgu`.`Usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sistema_actgu`.`Usuarios` (
  `id_usuario` INT NOT NULL,
  `etiqueta` VARCHAR(50) NOT NULL,
  `sexo` VARCHAR(1) NOT NULL,
  `edad` INT NOT NULL,
  `altura` FLOAT NOT NULL,
  `peso` FLOAT NOT NULL,
  `condicion_top` FLOAT NOT NULL,
  `tendencia_top` FLOAT NOT NULL,
  `condicion_bottom` FLOAT NOT NULL,
  `tendencia_bottom` FLOAT NOT NULL,
  `id_rol` INT NOT NULL,
  `id_equipamiento` INT NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE (`etiqueta`),
  FOREIGN KEY (`id_rol`)
    REFERENCES `sistema_actgu`.`Roles` (`id_rol`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  FOREIGN KEY (`id_equipamiento`)
    REFERENCES `sistema_actgu`.`Tipos_de_Equipamientos` (`id_tipo_de_equipamiento`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `sistema_actgu`.`Usuarios_de_Grupos_ocupan_Lugares`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sistema_actgu`.`Usuarios_pertenecen_a_Grupos` (
  `id_usuario` INT NOT NULL,
  `id_grupo` INT NOT NULL,
  FOREIGN KEY (`id_grupo`)
    REFERENCES `sistema_actgu`.`Grupos` (`id_grupo`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  FOREIGN KEY (`id_usuario`)
    REFERENCES `sistema_actgu`.`Usuarios` (`id_usuario`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `sistema_actgu`.`Tipos_de_Mecanismos_HVAC`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sistema_actgu`.`Tipos_de_Mecanismos_HVAC` (
  `id_tipo_de_mecanismo_hvac` INT NOT NULL,
  `tipo_de_mecanismo` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id_tipo_de_mecanismo_hvac`)
)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `sistema_actgu`.`Mecanismos_HVAC`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sistema_actgu`.`Mecanismos_HVAC` (
  `id_mecanismo_hvac` INT NOT NULL,
  `estado` VARCHAR(1) NOT NULL,
  `temperatura` FLOAT NULL,
  `humedad` FLOAT NULL,
  `velocidad_del_aire` FLOAT NULL,
  `concentracion_de_gas` FLOAT NULL,
  `id_tipo_de_mecanismo_hvac` INT NOT NULL,
  PRIMARY KEY (`id_mecanismo_hvac`),
  FOREIGN KEY (`id_tipo_de_mecanismo_hvac`)
    REFERENCES `sistema_actgu`.`Tipos_de_Mecanismos_HVAC` (`id_tipo_de_mecanismo_hvac`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
)
ENGINE = InnoDB;

-- -----------------------------------------------------
-- -----------------------------------------------------
-- -----------------------------------------------------

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;