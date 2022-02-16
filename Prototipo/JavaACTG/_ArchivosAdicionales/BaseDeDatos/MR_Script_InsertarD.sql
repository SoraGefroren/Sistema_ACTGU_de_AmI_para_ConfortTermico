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
INSERT INTO `sistema_actgu`.`Roles`
  (`id_rol`, `rol`)
    VALUES
      (3, 'Coordinador');

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
      (1, 'Clase A');

INSERT INTO `sistema_actgu`.`Grupos`
  (`id_grupo`, `nombre`)
    VALUES
      (2, 'Clase B');

-- -----------------------------------------------------
-- Table `sistema_actgu`.`Usuarios`
-- -----------------------------------------------------

-- -- Clase A

INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (1, 'Luz', 'm', 24, 1.50, 60, 1, 5,
        10, 75, 30, 10);
INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (2, 'Juan', 'h', 27, 1.60, 65, 1, 4,
        14, 72, 31, 15);
INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (3, 'Irene', 'm', 27, 1.58, 55, 1, 3,
        13, 60, 28, 12);
INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (4, 'María', 'm', 28, 1.50, 55, 1, 5,
        15, 65, 25, 4);
INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (5, 'Pedro', 'h', 24, 1.65, 70, 1, 3,
        22, 55, 25, 45);
INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (6, 'Samanta', 'm', 25, 1.60, 65, 1, 7,
        23, 52, 24, 18);
INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (7, 'Miguel', 'h', 26, 1.65, 65, 1, 6,
        20, 52, 22, 35);
INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (8, 'Alejandro', 'h', 25, 1.70, 65, 1, 6,
        21, 51, 23, 43);
INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (9, 'Luis', 'h', 40, 1.70, 80, 2, 5,
        15, 65, 25, 40);
INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (10, 'Karen', 'm', 25, 1.60, 60, 1, 3,
        12, 90, 30, 40);
INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (11, 'Raúl', 'h', 22, 1.70, 85, 1, 4,
        11, 85, 28, 35);
INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (12, 'Sofía', 'm', 30, 1.62, 70, 1, 2,
        13, 75, 31, 32);

INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (13, 'Sonia', 'm', 28, 1.60, 65, 1, 3,
        23, 52, 24, 28);
INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (14, 'Julio', 'h', 40, 1.65, 65, 1, 5,
        20, 52, 22, 45);

INSERT INTO `sistema_actgu`.`Usuarios`
  (`id_usuario`, `etiqueta`, `sexo`, `edad`, `altura`, `peso`, `id_rol`, `id_equipamiento`,
    `condicion_top`, `tendencia_top`, `condicion_bottom`, `tendencia_bottom`)
    VALUES
      (15, 'Esteban', 'h', 50, 1.66, 66, 3, 6,
        15, 55, 30, 10);

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
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (4, 1);
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (5, 1);
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (6, 1);
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (7, 1);
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (8, 1);
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (9, 1);
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (9, 2);
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (10, 1);
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (11, 1);
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (12, 1);
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (12, 2);
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (13, 1);
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (14, 1);
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (15, 1);
INSERT INTO `sistema_actgu`.`Usuarios_pertenecen_a_Grupos`
  (`id_usuario`, `id_grupo`)
    VALUES
      (15, 2);

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