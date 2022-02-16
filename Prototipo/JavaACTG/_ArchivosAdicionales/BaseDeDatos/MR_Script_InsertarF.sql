-- Datos iniciales

-- -----------------------------------------------------
-- Schema sistema_actgu
-- -----------------------------------------------------
USE `sistema_actgu` ;

-- -----------------------------------------------------
-- Table `sistema_actgu`.`Roles`
-- -----------------------------------------------------
INSERT INTO `sistema_actgu`.`Roles`
  (`id_rol`, `rol`)
    VALUES
      (1, 'Alumno');
INSERT INTO `sistema_actgu`.`Roles`
  (`id_rol`, `rol`)
    VALUES
      (2, 'Docente');

-- -----------------------------------------------------
-- Table `sistema_actgu`.`Tipos_de_Equipamientos`
-- -----------------------------------------------------
INSERT INTO `sistema_actgu`.`Tipos_de_Equipamientos`
  (`id_tipo_de_equipamiento`, `nombre`,
    `valor_clo`, `descripcion`,
    `suma_clo_para_m`, `descripcion_para_m`,
    `suma_clo_para_h`, `descripcion_para_h`)
    VALUES
      (1, 'Ropa pesada para invierno',
        1.38, 'prendas de tela gruesa (Pantalón, 0.24 clo; Suéter, 0.36; Chamarra, 0.44), prendas normales (Camisa o Blusa de manga larga, 0.34)',
        0.35, 'prendas interiores (varias, 0.25 clo) y calzado (Botas, 0.10)',
        0.77, 'prendas interiores (varias, 0.67 clo) y calzado (Botas, 0.10)');
INSERT INTO `sistema_actgu`.`Tipos_de_Equipamientos`
  (`id_tipo_de_equipamiento`, `nombre`,
    `valor_clo`, `descripcion`,
    `suma_clo_para_m`, `descripcion_para_m`,
    `suma_clo_para_h`, `descripcion_para_h`)
    VALUES
      (2, 'Ropa normal para invierno',
        1.21, 'prendas de tela gruesa (Pantalón, 0.24; Suéter, 0.36; Chamarra, 0.44), prendas normales (Camisa o Blusa de manga corta, 0.17)',
        0.20, 'prendas interiores (varias, 0.10 clo) y calzado (Botas, 0.10)',
        0.20, 'prendas interiores (varias, 0.10 clo) y calzado (Botas, 0.10)');
INSERT INTO `sistema_actgu`.`Tipos_de_Equipamientos`
  (`id_tipo_de_equipamiento`, `nombre`,
    `valor_clo`, `descripcion`,
    `suma_clo_para_m`, `descripcion_para_m`,
    `suma_clo_para_h`, `descripcion_para_h`)
    VALUES
      (3, 'Ropa ligera para invierno',
        0.77, 'prendas de tela gruesa (Pantalón, 0.24; Suéter, 0.36), prendas normales (Camisa o Blusa de manga corta, 0.17)',
        0.17, 'prendas interiores (varias, 0.07 clo) y calzado (Botas, 0.10)',
        0.09, 'prendas interiores (varias, 0.07 clo) y calzado (Zapatos, 0.02)');
INSERT INTO `sistema_actgu`.`Tipos_de_Equipamientos`
  (`id_tipo_de_equipamiento`, `nombre`,
    `valor_clo`, `descripcion`,
    `suma_clo_para_m`, `descripcion_para_m`,
    `suma_clo_para_h`, `descripcion_para_h`)
    VALUES
      (4, 'Ropa neutral',
        0.57, 'prendas de tela delgada (Pantalón, 0.15; Suéter, 0.25), prendas normales (Camisa o Blusa de manga corta, 0.17)',
        0.10, 'prendas interiores (varias, 0.07 clo) y calzado (Zapatillas, 0.03)',
        0.09, 'prendas interiores (varias, 0.07 clo) y calzado (Zapatos, 0.02)');
INSERT INTO `sistema_actgu`.`Tipos_de_Equipamientos`
  (`id_tipo_de_equipamiento`, `nombre`,
    `valor_clo`, `descripcion`,
    `suma_clo_para_m`, `descripcion_para_m`,
    `suma_clo_para_h`, `descripcion_para_h`)
    VALUES
      (5, 'Ropa pesada para verano',
        0.32, 'prendas de tela delgada (Pantalón, 0.15), prendas normales (Camisa o Blusa de manga corta, 0.17)',
        0.09, 'prendas interiores (varias, 0.06 clo) y calzado (Zapatillas, 0.03)',
        0.08, 'prendas interiores (varias, 0.06 clo) y calzado (Zapatos, 0.02)');
INSERT INTO `sistema_actgu`.`Tipos_de_Equipamientos`
  (`id_tipo_de_equipamiento`, `nombre`,
    `valor_clo`, `descripcion`,
    `suma_clo_para_m`, `descripcion_para_m`,
    `suma_clo_para_h`, `descripcion_para_h`)
    VALUES
      (6, 'Ropa normal para verano',
        0.32, 'prendas de tela delgada (Pantalón, 0.15), prendas normales (Camisa o Blusa de manga corta, 0.17)',
        0.08, 'prendas interiores (varias, 0.06 clo) y calzado (Sandalias, 0.02)',
        0.08, 'prendas interiores (varias, 0.06 clo) y calzado (Tenis o Zapatos, 0.02)');
INSERT INTO `sistema_actgu`.`Tipos_de_Equipamientos`
  (`id_tipo_de_equipamiento`, `nombre`,
    `valor_clo`, `descripcion`,
    `suma_clo_para_m`, `descripcion_para_m`,
    `suma_clo_para_h`, `descripcion_para_h`)
    VALUES
      (7, 'Ropa ligera para verano',
        0.25, 'prendas inferiores (Short o Bermuda, 0.08) y prendas superiores (Camisa o Blusa de manga corta, 0.17',
        0.08, 'prendas interiores (varias, 0.06 clo) y calzado (Sandalias, 0.02)',
        0.08, 'prendas interiores (varias, 0.06 clo) y calzado (Tenis o Zapatos, 0.02)');

-- -----------------------------------------------------
-- Table `sistema_actgu`.`Grupos`
-- -----------------------------------------------------
INSERT INTO `sistema_actgu`.`Grupos`
  (`id_grupo`, `nombre`)
    VALUES
      (1, 'Clase H');

-- -----------------------------------------------------
-- Table `sistema_actgu`.`Usuarios`
-- -----------------------------------------------------

-- -- Clase A

INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (1, 'P00A', 'm', 25, 1.55, 60, 1, 3,
        15, 35, 28, -10);
INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (2, 'P00B', 'h', 26, 1.63, 65, 1, 2,
        15, 35, 28, -20);
INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (3, 'P00C', 'm', 55, 1.70, 80, 2, 4,
        15, 20, 28, -20);

-- -----------------------------------------------------
-- Table `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
-- -----------------------------------------------------

-- -- 7a

INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (1, 1);
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (2, 1);
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (3, 1);

-- -----------------------------------------------------
-- Table `sistema_actgu`.`Tipos_de_Mecanismos_HVAC`
-- -----------------------------------------------------
INSERT INTO `sistema_actgu`.`Tipos_de_Mecanismos_HVAC`
  (`id_tipo_de_mecanismo_hvac`, `tipo_de_mecanismo`)
    VALUES
      (1, 'Aire Acondicionado');
INSERT INTO `sistema_actgu`.`Tipos_de_Mecanismos_HVAC`
  (`id_tipo_de_mecanismo_hvac`, `tipo_de_mecanismo`)
    VALUES
      (2, 'Calefactor');
INSERT INTO `sistema_actgu`.`Tipos_de_Mecanismos_HVAC`
  (`id_tipo_de_mecanismo_hvac`, `tipo_de_mecanismo`)
    VALUES
      (3, 'Ventilador');
INSERT INTO `sistema_actgu`.`Tipos_de_Mecanismos_HVAC`
  (`id_tipo_de_mecanismo_hvac`, `tipo_de_mecanismo`)
    VALUES
      (4, 'Ventanas');
INSERT INTO `sistema_actgu`.`Tipos_de_Mecanismos_HVAC`
  (`id_tipo_de_mecanismo_hvac`, `tipo_de_mecanismo`)
    VALUES
      (5, 'Puerta');

-- -----------------------------------------------------
-- Table `sistema_actgu`.`Mecanismos_HVAC`
-- -----------------------------------------------------
INSERT INTO `sistema_actgu`.`Mecanismos_HVAC`
  (`id_mecanismo_hvac`, `id_tipo_de_mecanismo_hvac`, `estado`,
    `temperatura`, `humedad`, `concentracion_de_gas`, `velocidad_del_aire`)
    VALUES
      (1, 1, 'a', 20, NULL, NULL, NULL);
INSERT INTO `sistema_actgu`.`Mecanismos_HVAC`
  (`id_mecanismo_hvac`, `id_tipo_de_mecanismo_hvac`, `estado`,
    `temperatura`, `humedad`, `concentracion_de_gas`, `velocidad_del_aire`)
    VALUES
      (2, 2, 'a', 30, NULL, NULL, NULL);
INSERT INTO `sistema_actgu`.`Mecanismos_HVAC`
  (`id_mecanismo_hvac`, `id_tipo_de_mecanismo_hvac`, `estado`,
    `temperatura`, `humedad`, `concentracion_de_gas`, `velocidad_del_aire`)
    VALUES
      (3, 3, 'a', NULL, NULL, NULL, NULL);
INSERT INTO `sistema_actgu`.`Mecanismos_HVAC`
  (`id_mecanismo_hvac`, `id_tipo_de_mecanismo_hvac`, `estado`,
    `temperatura`, `humedad`, `concentracion_de_gas`, `velocidad_del_aire`)
    VALUES
      (4, 4, 'a', NULL, NULL, NULL, NULL);
INSERT INTO `sistema_actgu`.`Mecanismos_HVAC`
  (`id_mecanismo_hvac`, `id_tipo_de_mecanismo_hvac`, `estado`,
    `temperatura`, `humedad`, `concentracion_de_gas`, `velocidad_del_aire`)
    VALUES
      (5, 5, 'a', NULL, NULL, NULL, NULL);

-- -----------------------------------------------------
-- -----------------------------------------------------
-- -----------------------------------------------------