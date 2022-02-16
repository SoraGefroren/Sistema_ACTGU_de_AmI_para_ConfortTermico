// Variables globales
var tablasACTGU = {};
var usarTablas = false;
var cambiarConfigGral = true;
var reservarSelecciones = false;

var objsDeAmbit = {
    buscar: false,
    velaire: "0.0",
    temperatura: "0.0",
    humedad: "0.0",
    gas: "0.0"
};

// Función que valida la correspondencia Grupo -- Hora y Día
var validarCorrespondenciaGrupoMomento = function() {
    // variables
    var tablaGruposMomentos = tablasACTGU["Grupos_ocupan_Momentos"],
        value_input_hora = document.getElementById("ctrl_input_hora").value,
        ctrl_sl_dia = document.getElementById("ctrl_sl_dia"),
        value_sl_dia = ctrl_sl_dia.options[ctrl_sl_dia.selectedIndex].value,
        ctrl_sl_grupo = document.getElementById("ctrl_sl_grupo"),
        value_sl_grupo = ctrl_sl_grupo.options[ctrl_sl_grupo.selectedIndex].value,
        esGrupoMomentoOk = false;
    // Recorrer Grupos - Momentoas
    tablaGruposMomentos.forEach(function(itemF, indexF) {
        // Validar si continuar
        if (value_sl_grupo.trim() !== itemF["id_grupo"].trim()) {
            return;
        } else {
            // Si el grupo seleccionado es igual a id_grupo de tablas
            // **************************************
            // Variables
            var intDiaSemana = 0,
                strDiaSemana = itemF["dia_semana"],
                int_sl_dia = parseInt(value_sl_dia);
            // Pasar día de la semana a un número
            switch (strDiaSemana) {
                case "lunes":
                    intDiaSemana = 1;
                    break;
                case "martes":
                    intDiaSemana = 2;
                    break;
                case "miércoles":
                    intDiaSemana = 3;
                    break;
                case "jueves":
                    intDiaSemana = 4;
                    break;
                case "viernes":
                    intDiaSemana = 5;
                    break;
                case "sabado":
                    intDiaSemana = 6;
                    break;
                case "domingo":
                    intDiaSemana = 7;
                    break;
            }
            // Validar coincidencia de día
            if (intDiaSemana === int_sl_dia) {
                // ** Selector dia
                var actualDate = new Date(),
                    actualMonth = actualDate.getMonth() + 1,
                    actualNumDay = actualDate.getDate(),
                    strActualDate = (actualDate.getFullYear() + "-" +
                        (actualMonth < 10 ? "0" + actualMonth.toString() : actualMonth.toString()) + "-" +
                        (actualNumDay < 10 ? "0" + actualNumDay.toString() : actualNumDay.toString())),
                    dateTimeIni = (new Date(strActualDate + " " + itemF["hora_inicia"])),
                    dateTimeFin = (new Date(strActualDate + " " + itemF["hora_termina"])),
                    dateTimeValue = (new Date(strActualDate + " " + value_input_hora + ":00"));
                // ----------------------------------
                // Validar coincidencia de tiempo
                if ((dateTimeIni <= dateTimeValue) && (dateTimeValue <= dateTimeFin)) {
                    esGrupoMomentoOk = true;
                }
            }
        }
    });
    // Regresar respuesta
    return esGrupoMomentoOk;
};

// Función que rellena la tabla Usuarios con usuarios
var rellenar_tabla_usuarios = function() {
    // Variables
    var aryDivsFilas = [],
        tablaUsuarios = tablasACTGU["Usuarios"],
        ctrl_sl_grupo = document.getElementById("ctrl_sl_grupo"),
        value_sl_grupo = ctrl_sl_grupo.options[ctrl_sl_grupo.selectedIndex].value,
        ctrl_tabla_usuarios = document.getElementById("ctrl_tabla_usuarios"),
        esGrupoMomentoOk = validarCorrespondenciaGrupoMomento();
    // Recorrer usuarios
    tablaUsuarios.forEach(function(itemF, indexF) {
        // Validar si continuar
        if (!esGrupoMomentoOk) {
            return;
        }

        // Variable "¿es usuario del grupo actual?"
        var esUsrDeGrupoActual = false;

        // Recorrer relación "Grupo - Lugar"
        itemF["grupo_lugar"].forEach(function(itmS /*OBJ*/ , indxS /*INT*/ ) {
            // Si el grupo seleccionado es igual a id_grupo de tablas
            if (value_sl_grupo.trim() === itmS["id_grupo"].trim()) {
                esUsrDeGrupoActual = true;
            }
        });
        // Validar si continuar
        if (!esUsrDeGrupoActual) {
            return;
        }

        // **************************************
        // Variables para construir div Fila
        var divFila = document.createElement("div"),
            divGrid = document.createElement("div"),
            // * Segmento 1
            seg1_Div = document.createElement("div"),
            seg1_Grid = document.createElement("div"),
            // *** Partes del Segmento 1
            seg1_GPrt1 = document.createElement("div"),
            seg1_GPrt1_Lbl = document.createElement("i"),
            seg1_GPrt2 = document.createElement("div"),
            seg1_GPrt2_Lbl = document.createElement("label"),
            // * Segmento 2
            seg2_Div = document.createElement("div"),
            seg2_Grid = document.createElement("div"),
            // *** Partes del Segmento 2
            seg2_GPrt1 = document.createElement("div"),
            seg2_GPrt1_Lbl = document.createElement("label"),
            // * Segmento 3
            seg3_Div = document.createElement("div"),
            seg3_Grid = document.createElement("div"),
            // *** Partes del Segmento 3
            seg3_GPrt1 = document.createElement("div"),
            seg3_GPrt1_Lbl = document.createElement("label"),
            // * Segmento 4
            seg4_Div = document.createElement("div"),
            seg4_Grid = document.createElement("div"),
            // *** Partes del Segmento 4
            seg4_GPrt2 = document.createElement("div"),
            seg4_GPrt2_Lbl = document.createElement("label"),
            // * Segmento 5
            seg5_Div = document.createElement("div"),
            seg5_Grid = document.createElement("div"),
            // *** Partes del Segmento 5
            seg5_GPrt1 = document.createElement("div"),
            seg5_GPrt1_Lbl = document.createElement("label");

        // Configurar divs
        divFila.setAttribute('class', 'small-12 medium-12 actgu-box-fila');
        divGrid.setAttribute('class', 'grid-x');
        // ------------------------------------------------
        // * Segmento 1
        seg1_Div.setAttribute('class', 'small-2 medium-2');
        seg1_Grid.setAttribute('class', 'grid-x');
        seg1_GPrt1.setAttribute('class', 'small-6 medium-6 text-center');
        // Validar icono a mostrar
        if (itemF["sexo"] === "h") {
            seg1_GPrt1_Lbl.setAttribute('class', 'fi-torso actgu-ctrl-icon-fila'); // torso y torso-female
        } else {
            seg1_GPrt1_Lbl.setAttribute('class', 'fi-torso-female actgu-ctrl-icon-fila'); // torso y torso-female
        }
        seg1_GPrt2.setAttribute('class', 'small-6 medium-6 align-self-middle');
        seg1_GPrt2_Lbl.setAttribute('class', 'text-center middle actgu-ctrl-label');
        seg1_GPrt2_Lbl.appendChild(document.createTextNode(itemF['id_usuario']));
        // * Segmento 2
        seg2_Div.setAttribute('class', 'small-2 medium-2');
        seg2_Grid.setAttribute('class', 'grid-x');
        seg2_GPrt1.setAttribute('class', 'small-12 medium-12 align-self-middle');
        seg2_GPrt1_Lbl.setAttribute('class', 'text-center middle actgu-ctrl-label');
        seg2_GPrt1_Lbl.appendChild(document.createTextNode(itemF['prioridad']));
        // * Segmento 3
        seg3_Div.setAttribute('class', 'small-2 medium-2');
        seg3_Grid.setAttribute('class', 'grid-x');
        seg3_GPrt1.setAttribute('class', 'small-12 medium-12 align-self-middle');
        seg3_GPrt1_Lbl.setAttribute('data-user-id', '' + itemF['id_usuario'] + '');
        seg3_GPrt1_Lbl.setAttribute('class', 'text-center middle actgu-ctrl-label actgu-es-lbl-pmv');
        seg3_GPrt1_Lbl.appendChild(document.createTextNode((itemF['data_sensores'] ? parseFloat(itemF['data_sensores']["met"] || "0").toFixed(2) : "---")));
        // * Segmento 4
        seg4_Div.setAttribute('class', 'small-3 medium-3');
        seg4_Grid.setAttribute('class', 'grid-x');
        seg4_GPrt2.setAttribute('class', 'small-12 medium-12 align-self-middle');
        seg4_GPrt2_Lbl.setAttribute('class', 'text-center middle actgu-ctrl-label');
        seg4_GPrt2_Lbl.appendChild(document.createTextNode((itemF['ashrae_pmv'] ? (itemF['ashrae_pmv'] + ": ") + (itemF['ashrae_confort'] ? itemF['ashrae_confort'] : "---") : "---")));
        // * Segmento 5
        seg5_Div.setAttribute('class', 'small-3 medium-3');
        seg5_Grid.setAttribute('class', 'grid-x');
        seg5_GPrt1.setAttribute('class', 'small-12 medium-12 text-right');
        seg5_GPrt1_Lbl.setAttribute('class', 'text-center middle actgu-ctrl-label');
        seg5_GPrt1_Lbl.appendChild(document.createTextNode((itemF['actgu_pmv'] ? (itemF['actgu_pmv'] + ": ") + (itemF['actgu_confort'] ? itemF['actgu_confort'] : "---") : "---")));

        // ********************************
        // Segmento 1
        seg1_GPrt1.appendChild(seg1_GPrt1_Lbl);
        seg1_GPrt2.appendChild(seg1_GPrt2_Lbl);
        seg1_Grid.appendChild(seg1_GPrt1);
        seg1_Grid.appendChild(seg1_GPrt2);
        seg1_Div.appendChild(seg1_Grid);
        // Segmento 2
        seg2_GPrt1.appendChild(seg2_GPrt1_Lbl);
        seg2_Grid.appendChild(seg2_GPrt1);
        seg2_Div.appendChild(seg2_Grid);
        // Segmento 3
        seg3_GPrt1.appendChild(seg3_GPrt1_Lbl);
        seg3_Grid.appendChild(seg3_GPrt1);
        seg3_Div.appendChild(seg3_Grid);
        // Segmento 4
        seg4_GPrt2.appendChild(seg4_GPrt2_Lbl);
        seg4_Grid.appendChild(seg4_GPrt2);
        seg4_Div.appendChild(seg4_Grid);
        // Segmento 5
        seg5_GPrt1.appendChild(seg5_GPrt1_Lbl);
        seg5_Grid.appendChild(seg5_GPrt1);
        seg5_Div.appendChild(seg5_Grid);

        // Combinar divs e items
        divGrid.appendChild(seg1_Div);
        divGrid.appendChild(seg2_Div);
        divGrid.appendChild(seg3_Div);
        divGrid.appendChild(seg4_Div);
        divGrid.appendChild(seg5_Div);

        // *******************************
        // Variables
        var divOculto = document.createElement("div"),
            itemOculto = document.createElement("input");
        // Configurar elementos
        divOculto.setAttribute('class', 'actgu-disappear');
        itemOculto.setAttribute('class', 'actgu-disappear actgu-data-en-tabla');
        itemOculto.setAttribute('value', JSON.stringify(itemF));
        itemOculto.setAttribute('type', 'text');
        // Combinar divs e items
        divOculto.appendChild(itemOculto);
        divGrid.appendChild(divOculto);

        // *******************************
        divFila.appendChild(divGrid);

        // ----------------------------------
        divFila.addEventListener("click", function(sender) {
            // Recupera tabla
            var mytabla = document.getElementById("ctrl_tabla_usuarios"),
                itemsInMyT = mytabla.getElementsByClassName("actgu-row-select");
            // recorrer elementos
            [].forEach.call(itemsInMyT, function(objetoSels, indiceSel) {
                // Remover clases
                objetoSels.classList.remove("actgu-row-select");
            });

            sender.currentTarget.classList.add("actgu-row-select");
        });

        // Agregar div fila en arreglo de divs
        aryDivsFilas.push(divFila);
    });
    // Eliminar contenido de tabla
    var filaTabla = ctrl_tabla_usuarios.lastChild;
    // **** Remover div de tabla
    while (filaTabla) {
        ctrl_tabla_usuarios.removeChild(filaTabla);
        filaTabla = ctrl_tabla_usuarios.lastChild;
    }
    // Recorrer divs contruidas y agregarlas a tabla
    aryDivsFilas.forEach(function(itemF, indexF) {
        // Agregar div fila en tabla
        ctrl_tabla_usuarios.appendChild(itemF);
    });
};

// Función que rellena la tabla HVAC con mecanismos
var rellenar_tabla_de_hvacs = function() {
    // Variables
    var aryDivsFilas = [],
        tablaHVACs = tablasACTGU["MecanismosHVAC"],
        ctrl_tabla_hvacs = document.getElementById("ctrl_tabla_hvacs");

    // Recorrer Mecanismos HVAC
    tablaHVACs.forEach(function(itemF, indexF) {
        // Variables
        var id_tipo_hvac = itemF["id_tipo_de_mecanismo_hvac"].trim(),
            estado_hvac = itemF["estado"].trim();

        // Variables para construir div Fila
        var divFila = document.createElement("div"),
            divGrid = document.createElement("div"),
            // * Segmento 1
            seg1_Div = document.createElement("div"),
            seg1_Grid = document.createElement("div"),
            // *** Partes del Segmento 1
            seg1_GPrt1 = document.createElement("div"),
            seg1_GPrt1_Lbl = document.createElement("label"),
            // * Segmento 2
            seg2_Div = document.createElement("div"),
            seg2_Grid = document.createElement("div"),
            // *** Partes del Segmento 2
            seg2_GPrt1 = document.createElement("div"),
            seg2_GPrt1_Lbl = document.createElement("label"),
            // * Segmento 3
            seg3_Div = document.createElement("div"),
            seg3_Grid = document.createElement("div"),
            // *** Partes del Segmento 3
            seg3_GPrt1 = document.createElement("div"),
            seg3_GPrt1_Lbl = document.createElement("label"),
            // * Segmento 3
            seg4_Div = document.createElement("div"),
            seg4_Grid = document.createElement("div"),
            // *** Partes del Segmento 4
            seg4_GPrt1 = document.createElement("div"),
            seg4_GPrt1_Lbl = document.createElement("i"),
            seg4_GPrt2 = document.createElement("div"),
            seg4_GPrt2_Lbl = document.createElement("label");

        // Configurar divs
        divFila.setAttribute('class', 'small-12 medium-12 actgu-box-fila');
        divGrid.setAttribute('class', 'grid-x');
        // ------------------------------------------------
        // * Segmento 1
        seg1_Div.setAttribute('class', 'small-2 medium-2');
        seg1_Grid.setAttribute('class', 'grid-x');
        seg1_GPrt1.setAttribute('class', 'small-12 medium-12 align-self-middle');
        seg1_GPrt1_Lbl.setAttribute('class', 'text-center middle actgu-ctrl-label');
        seg1_GPrt1_Lbl.appendChild(document.createTextNode(itemF['id_mecanismo_hvac']));
        // * Segmento 2
        seg2_Div.setAttribute('class', 'small-5 medium-5');
        seg2_Grid.setAttribute('class', 'grid-x');
        seg2_GPrt1.setAttribute('class', 'small-12 medium-12 align-self-middle');
        seg2_GPrt1_Lbl.setAttribute('class', 'text-center middle actgu-ctrl-label');
        seg2_GPrt1_Lbl.appendChild(document.createTextNode(
            ajustarTextoDeTipoHVAC(itemF['tipo_de_mecanismo'] || "")
        ));
        // * Segmento 3
        seg3_Div.setAttribute('class', 'small-2 medium-2');
        seg3_Grid.setAttribute('class', 'grid-x');
        seg3_GPrt1.setAttribute('class', 'small-12 medium-12 align-self-middle');
        seg3_GPrt1_Lbl.setAttribute('class', 'text-center middle actgu-ctrl-label');
        if ((id_tipo_hvac === "1") || (id_tipo_hvac === "2") || (id_tipo_hvac === "3")) {
            if (estado_hvac === "e") {
                seg3_GPrt1_Lbl.appendChild(document.createTextNode("Encendido"));
            } else {
                seg3_GPrt1_Lbl.appendChild(document.createTextNode("Apagado"));
            }
        } else {
            if (id_tipo_hvac === "4") {
                if (estado_hvac === "e") {
                    seg3_GPrt1_Lbl.appendChild(document.createTextNode("Abiertas"));
                } else {
                    seg3_GPrt1_Lbl.appendChild(document.createTextNode("Cerradas"));
                }
            } else {
                if (estado_hvac === "e") {
                    seg3_GPrt1_Lbl.appendChild(document.createTextNode("Abierta"));
                } else {
                    seg3_GPrt1_Lbl.appendChild(document.createTextNode("Cerrada"));
                }
            }
        }
        // * Segmento 4
        seg4_Div.setAttribute('class', 'small-3 medium-3');
        seg4_Grid.setAttribute('class', 'grid-x');
        seg4_GPrt1.setAttribute('class', 'small-6 medium-6 text-right');
        seg4_GPrt1_Lbl.setAttribute('class', 'fi-home actgu-ctrl-icon-fila');
        seg4_GPrt2.setAttribute('class', 'small-6 medium-6 align-self-middle');
        seg4_GPrt2_Lbl.setAttribute('class', 'text-center middle actgu-ctrl-label');
        if ((id_tipo_hvac === "1") || (id_tipo_hvac === "2")) {
            seg4_GPrt2_Lbl.appendChild(document.createTextNode((itemF['temperatura'] ? (itemF['temperatura'] + "°C") : "")));
        } else {
            if (id_tipo_hvac === "3") {
                seg4_GPrt2_Lbl.appendChild(document.createTextNode((itemF['velocidad_del_aire'] ? (itemF['velocidad_del_aire'] + " m/s") : "")));
            }
        }

        // ********************************
        // Segmento 1
        seg1_GPrt1.appendChild(seg1_GPrt1_Lbl);
        seg1_Grid.appendChild(seg1_GPrt1);
        seg1_Div.appendChild(seg1_Grid);
        // Segmento 2
        seg2_GPrt1.appendChild(seg2_GPrt1_Lbl);
        seg2_Grid.appendChild(seg2_GPrt1);
        seg2_Div.appendChild(seg2_Grid);
        // Segmento 3
        seg3_GPrt1.appendChild(seg3_GPrt1_Lbl);
        seg3_Grid.appendChild(seg3_GPrt1);
        seg3_Div.appendChild(seg3_Grid);
        // Segmento 4
        seg4_GPrt1.appendChild(seg4_GPrt1_Lbl);
        seg4_GPrt2.appendChild(seg4_GPrt2_Lbl);
        seg4_Grid.appendChild(seg4_GPrt1);
        seg4_Grid.appendChild(seg4_GPrt2);
        seg4_Div.appendChild(seg4_Grid);

        // Combinar divs e items
        divGrid.appendChild(seg1_Div);
        divGrid.appendChild(seg2_Div);
        divGrid.appendChild(seg3_Div);
        divGrid.appendChild(seg4_Div);

        // *******************************
        // Variables
        var divOculto = document.createElement("div"),
            itemOculto = document.createElement("input");
        // Configurar elementos
        divOculto.setAttribute('class', 'actgu-disappear');
        itemOculto.setAttribute('class', 'actgu-disappear actgu-data-en-tabla');
        itemOculto.setAttribute('value', JSON.stringify(itemF));
        itemOculto.setAttribute('type', 'text');
        // Combinar divs e items
        divOculto.appendChild(itemOculto);
        divGrid.appendChild(divOculto);

        // *******************************
        // Agregar a fila general
        divFila.appendChild(divGrid);

        // ----------------------------------
        divFila.addEventListener("click", function(sender) {
            // Recupera tabla
            var mytabla = document.getElementById("ctrl_tabla_hvacs"),
                itemsInMyT = mytabla.getElementsByClassName("actgu-row-select");
            // recorrer elementos
            [].forEach.call(itemsInMyT, function(objetoSels, indiceSel) {
                // Remover clases
                objetoSels.classList.remove("actgu-row-select");
            });

            sender.currentTarget.classList.add("actgu-row-select");
        });

        // Agregar div fila en arreglo de divs
        aryDivsFilas.push(divFila);
    });
    // Eliminar contenido de tabla
    var filaTabla = ctrl_tabla_hvacs.lastChild;
    // **** Remover div de tabla
    while (filaTabla) {
        ctrl_tabla_hvacs.removeChild(filaTabla);
        filaTabla = ctrl_tabla_hvacs.lastChild;
    }
    // Recorrer divs contruidas y agregarlas a tabla
    aryDivsFilas.forEach(function(itemF, indexF) {
        // Agregar div fila en tabla
        ctrl_tabla_hvacs.appendChild(itemF);
    });
};

// Función que reinicia los con controles y campos afectados por el calculo de confort térmico
var reiniciar_ctrls_por_calculo = function() {
    // Variables
    var tablaUsuarios = tablasACTGU["Usuarios"],
        ctrl_input = document.getElementById("ctrl_paf_detalle");
    // Recorrer tabla usuarios
    tablaUsuarios.forEach(function(itemF, indexF) {
        // Limpiar campos afectados
        itemF["ashrae_pmv"] = null;
        itemF["ashrae_confort"] = null;
        itemF["actgu_pmv"] = null;
        itemF["actgu_confort"] = null;
    });
    // **** Remover input
    var item_paf = ctrl_input.lastElementChild;
    while (item_paf) {
        ctrl_input.removeChild(item_paf);
        item_paf = ctrl_input.lastElementChild;
    }
    ctrl_input.textContent = '';
};

// ------------------------------------------------------------
// ------------------------------------------------------------
// ------------------------------------------------------------

// Función que calcula el índice de masa corporal
var calcular_indice_masa_corporal = function(peso, altura) {
    var bmi = 0;
    if ((peso === 0) && (altura === 0)) {
        bmi = 0;
    } else if (altura === 0) {
        bmi = 0;
    } else {
        bmi = peso / altura;
    }
    return bmi;
};

var calcularCloGeneralToVtn = function(strClo, strC_h, strC_m, strSexo) {
    // Variables
    var floResp = parseFloat(strClo);
    // Validar
    if (strSexo === 'h') {
        floResp += parseFloat(strC_h);
    } else {
        floResp += parseFloat(strC_m);
    }
    // Devolver respuesta
    return floResp.toFixed(2) + " clo";
};

var generarDescripcionDeClo = function(strIDEq, strSexo) {
    // Variables
    var tablaTiposEquipamientos = tablasACTGU["TiposEquipamientos"],
        strResp = "";
    // **** Recorrer ítems
    tablaTiposEquipamientos.forEach(function(itemG, indexG) {
        // Validar coincidencia
        if (itemG["id_tipo_de_equipamiento"] === strIDEq) {
            if (strSexo === 'h') {
                strResp = itemG["descripcion"] + ", " + itemG["descripcion_para_h"];
            } else {
                strResp = itemG["descripcion"] + ", " + itemG["descripcion_para_m"];
            }
        }
    });
    // Ajuste
    if (strResp !== "") {
        strResp = strResp[0].toUpperCase() + strResp.substring(1, strResp.length);
    }
    // Devolver respuesta
    return strResp;
};

var ajustarTextoDeTipoHVAC = function(strTipo) {
    // Variable
    var valTipoMeca = strTipo;
    // Modificar si
    if ((valTipoMeca === "Ventana") || (valTipoMeca === "ventana")) {
        valTipoMeca = "Ventanas";
    } else if ((valTipoMeca === "Puerta") || (valTipoMeca === "puerta")) {
        valTipoMeca = "Puerta";
    } else if ((valTipoMeca === "Ventilador") || (valTipoMeca === "ventilador")) {
        valTipoMeca = "Ventilador";
    } else if ((valTipoMeca === "Calefactor") || (valTipoMeca === "calefactor")) {
        valTipoMeca = "Calefactor";
    } else if ((valTipoMeca === "Aire acondicionado") || (valTipoMeca === "aire acondicionado")) {
        valTipoMeca = "Aire acondicionado";
    }
    // Devolver respuesta
    return valTipoMeca;
};

var recomponerTextoDeTipoHVAC = function(strTipo) {
    // Variable
    var valTipoMeca = strTipo;
    // Modificar si
    if (valTipoMeca === "Ventanas") {
        valTipoMeca = "Ventana";
    } else if (valTipoMeca === "Puerta") {
        valTipoMeca = "Puerta";
    } else if (valTipoMeca === "Ventilador") {
        valTipoMeca = "Ventilador";
    } else if (valTipoMeca === "Calefactor") {
        valTipoMeca = "Calefactor";
    } else if (valTipoMeca === "Aire acondicionado") {
        valTipoMeca = "Aire acondicionado";
    }
    // Devolver respuesta
    return valTipoMeca;
};

// ------------------------------------------------------------
// ------------------------------------------------------------
// ------------------------------------------------------------

// Función accionada por los controles superiores
function actualizar_configuracion_general() {
    // Cargar loader
    openLoader(function(closeLoader) {
        // Reiniciar controles y campos afectados por calculo
        reiniciar_ctrls_por_calculo();
        // Rellenar tabla de usuarios
        rellenar_tabla_usuarios();
        closeLoader();
    });
};

// Función accionada por el cambio de grupo de usuarios
function cambiar_de_grupo() {
    // Cargar loader
    openLoader(function(closeLoader) {
        // Reiniciar controles y campos afectados por calculo
        reiniciar_ctrls_por_calculo();
        // Rellenar tabla de usuarios
        rellenar_tabla_usuarios();
        closeLoader();
    });
};

// Función accionada por el botón "Calcula"
function calcular_confort_termico_grupal() {
    // Cargar loader
    openLoader(function(closeLoader) {
        // Variables
        var jsonDataEnv = {},
            esGrupoMomentoOk = validarCorrespondenciaGrupoMomento();
        // Validar si continuar
        if (!esGrupoMomentoOk) {
            // No continuar con el proceso
            closeLoader();
            return;
        }
        // ----------------------------------------------
        // ----------------------------------------------
        // PENDIENTE >> ACTUALIZAR: VALIDAR QUE EXISTAN USUARIOS PARA TRABAJAR
        // ----------------------------------------------
        // ----------------------------------------------
        // Asignar estrategia
        var ctrl_sl_estrategia = document.getElementById("ctrl_sl_estrategia"),
            value_sl_estrategia = ctrl_sl_estrategia.options[ctrl_sl_estrategia.selectedIndex].value;
        // Variables
        var ctrl_sl_grupo = document.getElementById("ctrl_sl_grupo"),
            texto_sl_grupo = ctrl_sl_grupo.options[ctrl_sl_grupo.selectedIndex].text;
        // *
        jsonDataEnv["Estrategia"] = value_sl_estrategia;
        jsonDataEnv["Grupo"] = texto_sl_grupo;
        // ----------------------------------------------
        // ----------------------------------------------
        // Invocación ajax
        (function(funcRetorno) {
            // Variable de petición
            var miPeticionAjax = new XMLHttpRequest(),
                paramsURL = 'jsonData=' + JSON.stringify(jsonDataEnv);
            // Definción de ejecusión AJAX
            miPeticionAjax.onload = function() {
                if (this.readyState == 4 && this.status == 200) {
                    funcRetorno(this.responseText);
                }
            };
            // Solicitar recursos por AJAX
            miPeticionAjax.open(
                "POST",
                ('sistema/conforttermicogrupal'), true
            );
            miPeticionAjax.setRequestHeader(
                'Content-type',
                'application/x-www-form-urlencoded'
            );
            miPeticionAjax.send(paramsURL);
            // Función de retorno
        })(function(rData) {
            // Validar datos de retorno
            if (rData && rData.trim()) {
                // Dividir resultados obtenidos
                var strResp = rData.trim().split("<|>");
                if (strResp.length > 1) {
                    // Convertir resultado a un objeto JSON
                    var jsonDataR = JSON.parse("{" + strResp[0] + "}");
                    console.log(jsonDataR);
                    // ----------------------------------------------
                    // ----------------------------------------------
                    // *
                    // Recorrer usuarios
                    tablasACTGU["Usuarios"].forEach(function(itemF, indexF) {
                        // Recorrer JSON - Usuarios 
                        var jsonUsrs = jsonDataR["Usuarios"],
                            jsonUss = null;
                        for (cmpIdx in jsonUsrs) {
                            jsonUss = jsonUsrs[cmpIdx];
                            if (itemF['id_usuario'] === jsonUss['id_usuario']) {
                                break;
                            }
                        }
                        // Confort Térmico:
                        itemF['ashrae_pmv'] = jsonUss['ashrae_pmv'];
                        // Estado:
                        itemF['ashrae_confort'] = jsonUss['ashrae_confort'];
                        // Confort Térmico:
                        itemF['actgu_pmv'] = jsonUss['actgu_pmv'];
                        // Estado:
                        itemF['actgu_confort'] = jsonUss['actgu_confort'];
                    });

                    // ----------------------------------------------
                    // ----------------------------------------------
                    // *

                    var actualizarHvac = false,
                        jsonTCGrupal = jsonDataR["Confort_Grupal"],
                        ctrl_paf_detalle = document.getElementById("ctrl_paf_detalle"),
                        ctrl_input_tcgrupal = document.getElementById("ctrl_input_tcgrupal"),
                        ctrl_describe_tcgrupal = document.getElementById("ctrl_describe_tcgrupal");

                    // Limpiar espacio de texto
                    while (ctrl_paf_detalle.lastChild) {
                        ctrl_paf_detalle.removeChild(ctrl_paf_detalle.lastChild);
                    }
                    ctrl_paf_detalle.textContent = "";

                    // Limpiar espacio de texto
                    while (ctrl_input_tcgrupal.lastChild) {
                        ctrl_input_tcgrupal.removeChild(ctrl_input_tcgrupal.lastChild);
                    }
                    ctrl_input_tcgrupal.value = "";

                    while (ctrl_describe_tcgrupal.lastChild) {
                        ctrl_describe_tcgrupal.removeChild(ctrl_describe_tcgrupal.lastChild);
                    }
                    ctrl_describe_tcgrupal.innerHTML = "";

                    // Validar
                    if (jsonTCGrupal["actgu_configuracion"]) {
                        // Variables que apuntan a los resultados de accion
                        var jsonConfigTCG = jsonTCGrupal["actgu_configuracion"],
                            detalleR = jsonConfigTCG["detalle"] || "";
                        // Asignar mensaje como detalle de los resultados
                        ctrl_paf_detalle.appendChild(document.createTextNode(detalleR));

                        // "id:" + 1 + ",edo:" + e|a + ",tipo:" + --- + ",temp:" + 0.0 + ",aire:" + 0.0 + ",hum:" + 0.0 + ",gas:" + 0.0 + "|";
                        var configR = (jsonConfigTCG["acciones"] || "").trim();
                        // Validar configuración
                        if (configR) {
                            // Tabla
                            var tblHVACs = tablasACTGU["MecanismosHVAC"];
                            // Dividir configuración
                            var aryConfigPIE = configR.split("|"),
                                numAyConfPIE = aryConfigPIE.length;
                            // Recorrer ary de Configuración
                            for (var py = 0; py < numAyConfPIE; py++) {
                                // Recuperar items
                                var aryConfCOMA = aryConfigPIE[py].split(","),
                                    numAyCfCOMA = aryConfCOMA.length;
                                // Variables
                                var itemID = "",
                                    itemEdo = "",
                                    itemTemp = "",
                                    itemHume = "",
                                    itemGas = "",
                                    itemAir = "";
                                // Recorrer ary de Configuración
                                for (var pc = 0; pc < numAyCfCOMA; pc++) {
                                    // Extraer información
                                    var aryCont = aryConfCOMA[pc].split(":");
                                    // Asignar variables
                                    switch (aryCont[0]) {
                                        case "id":
                                            itemID = aryCont[1];
                                            break;
                                        case "edo":
                                            itemEdo = aryCont[1];
                                            break;
                                        case "temp":
                                            itemTemp = aryCont[1];
                                            break;
                                        case "hum":
                                            itemHume = aryCont[1];
                                            break;
                                        case "gas":
                                            itemGas = aryCont[1];
                                            break;
                                        case "aire":
                                            itemAir = aryCont[1];
                                            break;
                                    }
                                }
                                // Recorrer HVACs
                                tblHVACs.forEach(function(itemF, indexF) {
                                    // Validar ID de HVAC
                                    if (itemID === itemF["id_mecanismo_hvac"]) {
                                        // Actualizar valores
                                        tblHVACs[indexF]["estado"] = itemEdo;
                                        // Variables
                                        var tipoHHH = itemF["tipo_de_mecanismo"].toLowerCase();
                                        // Validar el tipo de HVAC
                                        switch (tipoHHH) {
                                            case 'aire acondicionado':
                                                tblHVACs[indexF]["concentracion_de_gas"] = (itemEdo === "e") ? itemGas : "0.0";
                                                tblHVACs[indexF]["temperatura"] = itemTemp;
                                                tblHVACs[indexF]["humedad"] = (itemEdo === "e") ? itemHume : "0.0";
                                                tblHVACs[indexF]["velocidad_del_aire"] = (itemEdo === "e") ? itemAir : "0.0";
                                                break;
                                            case 'calefactor':
                                                tblHVACs[indexF]["concentracion_de_gas"] = (itemEdo === "e") ? itemGas : "0.0";
                                                tblHVACs[indexF]["temperatura"] = itemTemp;
                                                tblHVACs[indexF]["humedad"] = (itemEdo === "e") ? itemHume : "0.0";
                                                tblHVACs[indexF]["velocidad_del_aire"] = (itemEdo === "e") ? itemAir : "0.0";
                                                break;
                                            case 'ventilador':
                                                tblHVACs[indexF]["concentracion_de_gas"] = "";
                                                tblHVACs[indexF]["temperatura"] = "";
                                                tblHVACs[indexF]["humedad"] = "";
                                                tblHVACs[indexF]["velocidad_del_aire"] = (itemEdo === "e") ? itemAir : "0.0";
                                                break;
                                            case 'ventana':
                                                tblHVACs[indexF]["concentracion_de_gas"] = "";
                                                tblHVACs[indexF]["temperatura"] = "";
                                                tblHVACs[indexF]["humedad"] = "";
                                                tblHVACs[indexF]["velocidad_del_aire"] = (itemEdo === "e") ? itemAir : "0.0";
                                                break;
                                            case 'puerta':
                                                tblHVACs[indexF]["concentracion_de_gas"] = "";
                                                tblHVACs[indexF]["temperatura"] = "";
                                                tblHVACs[indexF]["humedad"] = "";
                                                tblHVACs[indexF]["velocidad_del_aire"] = (itemEdo === "e") ? itemAir : "0.0";
                                                break;
                                        }
                                    }
                                });
                            }
                            // Actualizar tabla
                            tablasACTGU["MecanismosHVAC"] = tblHVACs;
                            actualizarHvac = true;

                            // Validar si hay nuevos resultados
                            if (jsonConfigTCG["objetivo"] && (jsonConfigTCG["objetivo"]["cambio"] === "SI")) {
                                // Actualizar valores
                                objsDeAmbit["temperatura"] = parseFloat(jsonConfigTCG["objetivo"]["temperatura"] || "0.0");
                                objsDeAmbit["humedad"] = parseFloat(jsonConfigTCG["objetivo"]["humedad"] || "0.0");
                                objsDeAmbit["velaire"] = parseFloat(jsonConfigTCG["objetivo"]["velaire"] || "0.0");
                                objsDeAmbit["gas"] = parseFloat(jsonConfigTCG["objetivo"]["gas"] || "0.0");
                                // ----------------------------------------------
                                // ----------------------------------------------
                                // Buscar cambios
                                objsDeAmbit["buscar"] = true;
                                // ----------------------------------------------
                                // ----------------------------------------------
                                // Bloquear botón
                                var ctrl_btn_calcular = document.getElementById("ctrl_btn_calcular");
                                // Bloquear botón
                                ctrl_btn_calcular.setAttribute('disabled', 'true');
                                ctrl_btn_calcular.value = "Calculando...";
                            }
                        }
                    }

                    // Asignar valores de CONFORT GRUPAL
                    ctrl_input_tcgrupal.value = jsonTCGrupal["valor_pmv"] || '';
                    ctrl_describe_tcgrupal.appendChild(document.createTextNode("El grupo se encuentra \"" + jsonTCGrupal["valor_confort"] + "\" y la sensación del lugar indica que es \"" + jsonTCGrupal["valor_sensacion"] + "\", para una " + jsonTCGrupal["ambiente_revisado"]));

                    // *
                    // ----------------------------------------------
                    // ----------------------------------------------

                    // Re-Iniciar carga de información ambiental
                    // cargar_valores_ambientales();

                    // Rellenar tabla de usuarios
                    rellenar_tabla_usuarios();
                    // Validar si actualizar HVAC's
                    if (actualizarHvac) {
                        // Actualizar HVAC's
                        rellenar_tabla_de_hvacs();
                    }
                    // Finalizar proceso
                    closeLoader();
                } else {
                    // Lanzar mensaje de error
                    launchMessage("Error", "Ha ocurrido un error, en el proceso para determinar las condiciones de Confort Térmico en el entorno", function(closeMessageBox) {
                        // Rellenar tabla de usuarios
                        rellenar_tabla_usuarios();
                        // Cerrar mensaje de error
                        closeMessageBox();
                        // Finalizar proceso
                        closeLoader();
                    });
                }
            } else {
                // Lanzar mensaje de error
                launchMessage("Error", "Ha ocurrido un error, al intentar determinar las condiciones de Confort Térmico en el entorno", function(closeMessageBox) {
                    // Rellenar tabla de usuarios
                    rellenar_tabla_usuarios();
                    // Cerrar mensaje de error
                    closeMessageBox();
                    // Finalizar proceso
                    closeLoader();
                });
            }
        });
    });
};