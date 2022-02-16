// Variables para la reserva de selecciones
var dataReservada = {
    text_sl_grupo: "",
    value_sl_grupo: "",
    array_usrs_data: []
};

// Función que reserva las selecciones
var guardarSelecciones = function() {
    // Reiniciar objeto de reserva
    dataReservada.array_usrs_data = [];
    // Variables
    var ctrl_sl_grupo = document.getElementById("ctrl_sl_grupo"),
        text_sl_grupo = ctrl_sl_grupo.options[ctrl_sl_grupo.selectedIndex].text,
        value_sl_grupo = ctrl_sl_grupo.options[ctrl_sl_grupo.selectedIndex].value,
        tabla_usuarios = tablasACTGU["Usuarios"];
    // Asignar data a reserva
    dataReservada.text_sl_grupo = text_sl_grupo;
    dataReservada.value_sl_grupo = value_sl_grupo;
    // Recorrer tabla
    tabla_usuarios.forEach(function(usrItem, indexItem) {
        // Asignar data a reserva
        dataReservada.array_usrs_data.push({
            id_usuario: usrItem["id_usuario"] || "",
            data_sensores: usrItem["data_sensores"] || null
        });
    });
};

// PENDIENTE >> ACTUALIZAR
// Función que restaura las selecciones
var restaurarSelecciones = function() {
    // Variables
    var ctrl_sl_grupo = document.getElementById("ctrl_sl_grupo"),
        tabla_usuarios = tablasACTGU["Usuarios"],
        text_sl_grupo = dataReservada.text_sl_grupo;
    // Seleccionar el SL de grupo
    // **** Tomar items del selector grupo
    var items_sl_sels = ctrl_sl_grupo.options,
        num_ssl_sels = items_sl_sels.length;
    // **** Seleccionar-Desseleccionar items
    for (var w = 0; w < num_ssl_sels; w++) {
        // Validar contenido
        if (items_sl_sels[w].text === text_sl_grupo) {
            items_sl_sels[w].selected = true;
        } else {
            items_sl_sels[w].selected = false;
        }
    }
    // Restaurar data en tabla
    tabla_usuarios.forEach(function(usrItem, indexItem) {
        // Recorrer reserva
        [].forEach.call(dataReservada.array_usrs_data, function(elemX, indeX) {
            // Validar coincidencia
            if (usrItem["id_usuario"] === elemX["id_usuario"]) {
                // Validar falta de datos de sensor
                if (!usrItem["data_sensores"] && elemX["data_sensores"]) {
                    // Asignar datos de sensor a usuario
                    tabla_usuarios[indexItem]["data_sensores"] = elemX["data_sensores"];
                }
            }
        });
    });
    // Reiniciar objeto de reserva
    dataReservada.text_sl_grupo = "";
    dataReservada.value_sl_grupo = "";
    dataReservada.array_usrs_data = [];
};

// Función de prueba
var reiniciar_sistema = function() {
    // Validar si reservar selecciones
    if (reservarSelecciones) {
        // Función que reserva las selecciones
        guardarSelecciones();
    }
    // No usar tablar para determinados procesos
    usarTablas = false;
    // Cargar loader
    openLoader(function(closeLoader) {
        // Variables
        var aryStrTablas = ["Usuarios", "MecanismosHVAC", "Grupos", "Prioridades", "TiposEquipamientos", "TiposHVAC", "Grupos_ocupan_Momentos"],
            funConsultarBD = function(indexAT, maxInAT, aryTablasC, funTerminarProceso) {
                // Validar continuación del proceso de consulta
                if (indexAT < maxInAT) {
                    // Invocación ajax
                    (function(funcRetorno) {
                        // Variable de petición
                        var miPeticionAjax = new XMLHttpRequest(),
                            paramsURL = 'jsonData=' + '{Tabla:"' + aryTablasC[indexAT] + '"}';
                        // Definción de ejecusión AJAX
                        miPeticionAjax.onload = function() {
                            if (this.readyState == 4 && this.status == 200) {
                                funcRetorno(this.responseText);
                            }
                        };
                        // Solicitar recursos por AJAX
                        miPeticionAjax.open(
                            "POST",
                            ('sistema/informacion'), true
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
                            var strResp = rData.trim().split("|");
                            if (strResp.length > 1) {
                                // Convertir resultado a un objeto JSON
                                var jsonData = JSON.parse("{" + strResp[0] + "}");
                                // Tramiento de los datos consultados
                                for (cmpLlave in jsonData) {
                                    tablasACTGU[cmpLlave] = jsonData[cmpLlave] || [];
                                }
                                // Continuar con el proceso de consulta
                                funConsultarBD(indexAT + 1, maxInAT, aryTablasC, funTerminarProceso);
                            } else {
                                // Mostrar error
                                console.log("Ha ocurrido un error, en la carga de los \"" + aryTablasC[indexAT] + "\"");
                                // Frente a un error con las tab las, se reinicia el proceso de consulta del sistema
                                setTimeout(function() {
                                    // Se cierra el loadeer
                                    closeLoader();
                                    // Se reinicia el sistema
                                    reiniciar_sistema();
                                    // Ejecutar luego de...
                                }, 1000);
                            }
                        } else {
                            // Mostrar error
                            console.log("Ha ocurrido un error, en la consulta de los \"" + aryTablasC[indexAT] + "\"");
                            // Frente a un error con las tab las, se reinicia el proceso de consulta del sistema
                            setTimeout(function() {
                                // Se cierra el loadeer
                                closeLoader();
                                // Se reinicia el sistema
                                reiniciar_sistema();
                                // Ejecutar luego de...
                            }, 1000);
                        }
                    });
                } else {
                    funTerminarProceso();
                }
            };
        // Ejecutar función para consulta de BD
        funConsultarBD(0, aryStrTablas.length, aryStrTablas, function() {
            // ---------------------------------------
            // ---------------------------------------
            // ---------------------------------------
            // Ajuste de la tabla de usuarios
            var tablaUsuarios = tablasACTGU["Usuarios"] || [],
                aryUsrsAx = [];
            // Recorrer usuarios
            tablaUsuarios.forEach(function(itemUsr /*OBJ*/ , indexG /*INT*/ ) {
                // Variables
                var exisInAryUX = false;
                // Validar presencia de usuario en Arreglo
                aryUsrsAx.forEach(function(axUsr /*OBJ*/ , axIU /*INT*/ ) {
                    if (itemUsr["id_usuario"] === axUsr["id_usuario"]) {
                        // Agregar nueva relación <<Grupo - Lugar>> en arreglo
                        // ¿Porque? Un usuario puede pertener a "n" grupos
                        //      y en cada grupo, cada usuario puede ocupar un sitio distinto
                        aryUsrsAx[axIU]["grupo_lugar"].push({
                            id_grupo: itemUsr["id_grupo"],
                            nombre_grupo: itemUsr["nombre_grupo"]
                        });
                        // Cambiar estado de bandera
                        exisInAryUX = true;
                    }
                });
                // Validar como agregar item en arreglo AX
                if (!exisInAryUX) {
                    // Variables
                    var tempObj = {};
                    // Recorrer item G y llenar obj Temporal
                    for (var subIxG /*STR:*/ in itemUsr /*:OBJ*/ ) {
                        // Si "subIxG" o "Campo" no es igual a "id_grupo, nombre_grupo, ..., columna_lugar_en_clase"
                        //      entonces se agrega al objeto "tempObj"
                        if (!((subIxG === "id_grupo") || (subIxG === "nombre_grupo"))) {
                            // Se pasan todos los campos, el ID GRUPO y NOMBRE GRUPO, se pasan despues, en GRUPO LUGAR
                            tempObj[subIxG] = itemUsr[subIxG];
                        }
                    }
                    // Agregar nueva relación <<Grupo - Lugar>> en arreglo
                    // ¿Porque? Un usuario puede pertener a "n" grupos
                    //      y en cada grupo, cada usuario puede ocupar un sitio distinto
                    tempObj["grupo_lugar"] = [];
                    // Agregar nueva relación <<Grupo - Lugar>> en arreglo
                    tempObj["grupo_lugar"].push({
                        id_grupo: itemUsr["id_grupo"],
                        nombre_grupo: itemUsr["nombre_grupo"]
                    });
                    aryUsrsAx.push(tempObj);
                }
            });
            tablasACTGU["Usuarios"] = aryUsrsAx;
            // Configurar sistema
            configurar_sistema();
            closeLoader();

            // Ya pueso usar tablar para determinados procesos
            usarTablas = true;
        });
    });
};

// Función de prueba
var configurar_sistema = function() {
    // Variables
    var tablaGrupos = tablasACTGU["Grupos"],
        tablaTiposHVAC = tablasACTGU["TiposHVAC"],
        tablaPrioridades = tablasACTGU["Prioridades"],
        tablaTiposEquipamientos = tablasACTGU["TiposEquipamientos"];
    // ********************
    // Validar búsqueda de N condiciones
    if (!objsDeAmbit["buscar"]) {
        // Recorrer HVACs
        tablasACTGU["MecanismosHVAC"].forEach(function(itemXZY, indexXZY) {
            // Actualizar valores
            tablasACTGU["MecanismosHVAC"][indexXZY]["estado"] = "a";
        });
    }
    // ********************
    // Ajuste de la configuración general
    var ctrl_selector = null,
        ctrl_input = null,
        items_sl = null,
        num_items_sl = null;
    // Validar accion
    if (cambiarConfigGral) {
        // ********************
        // ** Selector estrategia
        ctrl_selector = document.getElementById("ctrl_sl_estrategia");
        items_sl = ctrl_selector.options;
        num_items_sl = items_sl.length;
        // Validar existencia de elementos
        if (num_items_sl > 0) {
            // **** Recorrer items y desseleccionar items
            for (var ix = 0; ix < num_items_sl; ix++) {
                items_sl[ix].selected = false;
            }
            // **** Seleccionar item inicial
            items_sl[0].selected = true;
        }
        // ********************
        // ** Selector dia
        // ** Elegir día
        var actualDate = new Date(),
            actualMonth = actualDate.getMonth() + 1,
            actualNumDay = actualDate.getDate(),
            currentDate = new Date(
                actualDate.getFullYear() + "-" +
                (actualMonth < 10 ? "0" + actualMonth.toString() : actualMonth.toString()) + "-" +
                (actualNumDay < 10 ? "0" + actualNumDay.toString() : actualNumDay.toString()) + " 12:00:00"),
            actualDay = currentDate.getDay() - 1;
        // ** Instanciar controles
        ctrl_selector = document.getElementById("ctrl_sl_dia");
        items_sl = ctrl_selector.options;
        num_items_sl = items_sl.length;

        // **** Recorrer items y desseleccionar items
        for (var ix = 0; ix < num_items_sl; ix++) {
            items_sl[ix].selected = false;
        }
        // **** Seleccionar item actual
        items_sl[((actualDay < num_items_sl) && (actualDay >= 0) ? actualDay : (num_items_sl - 1))].selected = true;

        // ********************
        // ** Input time (Intanciar y calcular tiempo)
        var timeHor = actualDate.getHours(),
            timeMin = actualDate.getMinutes(),
            ctrl_input = document.getElementById("ctrl_input_hora");

        // **** Insertar tiempo actual
        ctrl_input.value = ((timeHor < 10) ? "0" + timeHor.toString() :
            timeHor.toString()) + ":" + ((timeMin < 10) ? "0" + timeMin.toString() : timeMin.toString());
    }

    // Se prohibe cambiar la configuración general en las siguientes vueltas
    cambiarConfigGral = false;

    // ---------------------------------------
    // ---------------------------------------
    // ---------------------------------------

    // Ajuste de los elementos del ambiente
    // ********************
    // ** Selector grupos (instancia)
    ctrl_selector = document.getElementById("ctrl_sl_grupo");
    items_sl = ctrl_selector.lastElementChild;
    // **** Remover items de selector
    while (items_sl) {
        ctrl_selector.removeChild(items_sl);
        items_sl = ctrl_selector.lastElementChild;
    }
    // **** Agregar items de selector grupos
    tablaGrupos.forEach(function(itemG, indexG) {
        // Crear opcion
        var itemParaSel = document.createElement("option");
        // Configurar opcion creada
        itemParaSel.setAttribute("value", itemG["id_grupo"]);
        itemParaSel.appendChild(document.createTextNode(itemG["nombre"]));
        // Agregar opcion a selector
        ctrl_selector.appendChild(itemParaSel);
    });
    items_sl = ctrl_selector.options;
    num_items_sl = items_sl.length;
    // Validar existencia de elementos
    if (num_items_sl > 0) {
        // **** Recorrer items y desseleccionar items
        for (var ix = 0; ix < num_items_sl; ix++) {
            items_sl[ix].selected = false;
        }
        // **** Seleccionar item inicial
        items_sl[0].selected = true;
    }
    // ********************
    // ** Input parrafo - resultados
    ctrl_input = document.getElementById("ctrl_paf_detalle");
    var item_paf = ctrl_input.lastElementChild;
    // **** Remover items de input
    while (item_paf) {
        ctrl_input.removeChild(item_paf);
        item_paf = ctrl_input.lastElementChild;
    }
    ctrl_input.textContent = '';

    // ---------------------------------------
    // ---------------------------------------
    // ---------------------------------------

    // Ajuste general para las ventanas modales
    // ********************
    // ** Selector grupos - ALL - de Modal Usuarios
    ctrl_selector = document.getElementById("ctrl_sl_user_grupo_all");
    items_sl = ctrl_selector.lastElementChild;
    // **** Remover items de selector
    while (items_sl) {
        ctrl_selector.removeChild(items_sl);
        items_sl = ctrl_selector.lastElementChild;
    }
    // **** Agregar items de selector
    tablaGrupos.forEach(function(itemG, indexG) {
        // Crear opcion
        var itemParaSel = document.createElement("option");
        // Configurar opcion creada
        itemParaSel.setAttribute("value", itemG["id_grupo"]);
        itemParaSel.appendChild(document.createTextNode(itemG["nombre"]));
        // Agregar opcion a selector
        ctrl_selector.appendChild(itemParaSel);
    });
    items_sl = ctrl_selector.options;
    num_items_sl = items_sl.length;
    // Validar existencia de elementos
    if (num_items_sl > 0) {
        // **** Recorrer items y desseleccionar items
        for (var ix = 0; ix < num_items_sl; ix++) {
            items_sl[ix].selected = false;
        }
        // **** Seleccionar item inicial
        items_sl[0].selected = true;
    }
    // ********************
    // ** Selector grupos - SELS - de Modal Usuarios
    ctrl_selector = document.getElementById("ctrl_sl_user_grupo_sels");
    items_sl = ctrl_selector.lastElementChild;
    // **** Remover items de selector
    while (items_sl) {
        ctrl_selector.removeChild(items_sl);
        items_sl = ctrl_selector.lastElementChild;
    }
    items_sl = ctrl_selector.options;
    num_items_sl = items_sl.length;
    // Validar existencia de elementos
    if (num_items_sl > 0) {
        // **** Recorrer items y desseleccionar items
        for (var ix = 0; ix < num_items_sl; ix++) {
            items_sl[ix].selected = false;
        }
        // **** Seleccionar item inicial
        items_sl[0].selected = true;
    }

    // ********************
    // ** Selector prioridades
    ctrl_selector = document.getElementById("ctrl_sl_user_prioridad");
    items_sl = ctrl_selector.lastElementChild;
    // **** Remover items de selector
    while (items_sl) {
        ctrl_selector.removeChild(items_sl);
        items_sl = ctrl_selector.lastElementChild;
    }
    // **** Agregar items de selector
    tablaPrioridades.forEach(function(itemG, indexG) {
        // Crear opcion
        var itemParaSel = document.createElement("option");
        // Configurar opcion creada
        itemParaSel.setAttribute("value", itemG["id_prioridad"]);
        itemParaSel.appendChild(document.createTextNode(itemG["prioridad"]));
        // Agregar opcion a selector
        ctrl_selector.appendChild(itemParaSel);
    });
    items_sl = ctrl_selector.options;
    num_items_sl = items_sl.length;
    // Validar existencia de elementos
    if (num_items_sl > 0) {
        // **** Recorrer items y desseleccionar items
        for (var ix = 0; ix < num_items_sl; ix++) {
            items_sl[ix].selected = false;
        }
        // **** Seleccionar item inicial
        items_sl[0].selected = true;
    }
    // ********************
    // ** Selector CLO
    ctrl_selector = document.getElementById("ctrl_sl_user_clo");
    items_sl = ctrl_selector.lastElementChild;
    // **** Remover items de selector
    while (items_sl) {
        ctrl_selector.removeChild(items_sl);
        items_sl = ctrl_selector.lastElementChild;
    }
    // **** Agregar items de selector
    tablaTiposEquipamientos.forEach(function(itemG, indexG) {
        // Crear opcion
        var itemParaSel = document.createElement("option"),
            idEqui = itemG["id_tipo_de_equipamiento"];
        // Configurar opcion creada
        itemParaSel.setAttribute("value", idEqui);
        itemParaSel.appendChild(document.createTextNode("00" + idEqui));
        // Agregar opcion a selector
        ctrl_selector.appendChild(itemParaSel);
    });
    items_sl = ctrl_selector.options;
    num_items_sl = items_sl.length;
    // Validar existencia de elementos
    if (num_items_sl > 0) {
        // **** Recorrer items y desseleccionar items
        for (var ix = 0; ix < num_items_sl; ix++) {
            items_sl[ix].selected = false;
        }
        // **** Seleccionar item inicial
        items_sl[0].selected = true;
    }
    // ********************
    // ** Selector Tipo HVAC
    ctrl_selector = document.getElementById("ctrl_sl_hvac_tipo");
    items_sl = ctrl_selector.lastElementChild;
    // **** Remover items de selector
    while (items_sl) {
        ctrl_selector.removeChild(items_sl);
        items_sl = ctrl_selector.lastElementChild;
    }
    // **** Agregar items de selector
    tablaTiposHVAC.forEach(function(itemG, indexG) {
        // Crear opcion
        var itemParaSel = document.createElement("option");
        // Configurar opcion creada
        itemParaSel.setAttribute("value", itemG["id_tipo_de_mecanismo_hvac"]);
        // Recupera valor
        var valTipoMeca = ajustarTextoDeTipoHVAC(itemG["tipo_de_mecanismo"] || "");
        // Agregar item
        itemParaSel.appendChild(document.createTextNode(valTipoMeca));
        // Agregar opcion a selector
        ctrl_selector.appendChild(itemParaSel);
    });
    items_sl = ctrl_selector.options;
    num_items_sl = items_sl.length;
    // Validar existencia de elementos
    if (num_items_sl > 0) {
        // **** Recorrer items y desseleccionar items
        for (var ix = 0; ix < num_items_sl; ix++) {
            items_sl[ix].selected = false;
        }
        // **** Seleccionar item inicial
        items_sl[0].selected = true;
    }
    // ---------------------------------------
    // ---------------------------------------
    // ---------------------------------------
    // Validar si se reservaron las selecciones
    if (reservarSelecciones) {
        // Función que restaura las selecciones
        restaurarSelecciones();
    } else {
        // Todas las siguientes veces se reservaran las selecciones
        reservarSelecciones = true;
    }
    // ********************
    // ** Iniciar/Reiniciar elementos de Tablas
    rellenar_tabla_usuarios();
    rellenar_tabla_de_hvacs();
};


// Función que re-rellena los PMV de la tabla de usuarios
var revals_en_tabla_usuarios = function() {
    // Recuperar elementos de analisis
    var tablaUsuarios = tablasACTGU["Usuarios"],
        lbls_pmv = document.getElementById("ctrl_tabla_usuarios").getElementsByClassName('actgu-es-lbl-pmv');
    // Recorrer labels
    [].forEach.call(lbls_pmv, function(lblX, indexL) {
        // Recupera ID de usuario
        var strIdUsr = lblX.getAttribute('data-user-id');
        // Buscar datos de usuario
        tablaUsuarios.forEach(function(usrItem, indexItem) {
            // Validar que se el usuario buscando
            if (strIdUsr === usrItem['id_usuario']) {
                // Actualizar label PMV
                lblX.textContent = "";
                lblX.appendChild(document.createTextNode((usrItem['data_sensores'] ? parseFloat(usrItem['data_sensores']["met"] || "0").toFixed(2) : "---")));
            }
        });
    });
};

// Función asincrona que carga los valores ambientales cada n tiempo
var cargar_valores_ambientales = function() {
    // Invocación ajax
    (function(funcRetorno) {
        // Variables
        var objToSend = {},
            ctrl_sl_grupo = document.getElementById("ctrl_sl_grupo"),
            texto_sl_grupo = ctrl_sl_grupo.options[ctrl_sl_grupo.selectedIndex].text;
        // Agregar valores a ser enviados
        objToSend["Grupo"] = texto_sl_grupo;
        // Variable de petición
        var miPeticionAjax = new XMLHttpRequest(),
            paramsURL = 'jsonData=' + JSON.stringify(objToSend);
        // Definción de ejecusión AJAX
        miPeticionAjax.onload = function() {
            if (this.readyState == 4 && this.status == 200) {
                funcRetorno(this.responseText);
            } else {
                // debugger;
            }
        };
        // Solicitar recursos por AJAX
        miPeticionAjax.open(
            "POST",
            ('sistema/datos_de_sensor'), true
        );
        miPeticionAjax.setRequestHeader(
            'Content-type',
            'application/x-www-form-urlencoded'
        );
        miPeticionAjax.send(paramsURL);
        // Función de retorno
    })(function(rData) {
        // Función que hace actuar a este proceso como un hilo
        var recargar_valores_ambientales = function() {
            // Ejecutar luego de...
            setTimeout(cargar_valores_ambientales, 5000);
        };
        // Validar datos de retorno
        if (rData && rData.trim()) {
            // Dividir resultados obtenidos
            var strResp = rData.trim().split("|");
            if (strResp.length > 1) {
                // Validar que la respuesta traiga algo
                if (strResp[0].trim()) {
                    // Convertir resultado a un objeto JSON
                    var jsonData = JSON.parse("{" + strResp[0] + "}"),
                        ctrl_input = null;
                    console.log(jsonData);
                    // Tramiento de los datos consultados
                    // ** Input Temperatura
                    ctrl_input = document.getElementById("ctrl_input_temperatura");
                    ctrl_input.value = jsonData["temperatura"] || "";
                    // ** Input Humedad
                    ctrl_input = document.getElementById("ctrl_input_humedad");
                    ctrl_input.value = jsonData["humedad"] || "";
                    // ** Input Humedad
                    ctrl_input = document.getElementById("ctrl_input_gas");
                    ctrl_input.value = jsonData["concentracion_de_gas"] || "";
                    // ** Input Humedad
                    ctrl_input = document.getElementById("ctrl_input_vel_aire");
                    ctrl_input.value = jsonData["velocidad_del_aire"] || "";
                    // Tratar data de usuarios
                    var aryData = jsonData["sensores_usuarios"];
                    // Validar datos de sensores de usuario
                    if (aryData) {
                        // ** Recorrer datos de sensor de usuario
                        aryData.forEach(function(itemUsr, indexUsr) {
                            // Actualizar datos en tabla de usuarios
                            if (usarTablas) {
                                // Recorrer usuarios
                                tablasACTGU["Usuarios"].forEach(function(itemF, indexF) {
                                    // Validar si actualizar
                                    if (itemF["id_usuario"] === itemUsr["id_usuario"]) {
                                        tablasACTGU["Usuarios"][indexF]["data_sensores"] = {
                                            met: itemUsr["met"] || "0.0"
                                        };
                                    }
                                });
                            }
                        });
                    }
                    // ----------------------------------------------
                    // ----------------------------------------------
                    // Validar búsqueda de N condiciones
                    if (objsDeAmbit["buscar"]) {
                        // Validar coincidencia de números
                        if ((parseFloat(jsonData["temperatura"] || "0") === parseFloat(objsDeAmbit["temperatura"] || "0")) ||
                            (parseFloat(jsonData["velocidad_del_aire"] || "0") === parseFloat(objsDeAmbit["velaire"] || "0"))) {
                            // Recuperar botón
                            var ctrl_btn_calcular = document.getElementById("ctrl_btn_calcular");
                            // DesBloquear botón
                            ctrl_btn_calcular.value = "Calcular";
                            ctrl_btn_calcular.removeAttribute('disabled');
                            // Ya no buscar cambios
                            objsDeAmbit["buscar"] = false;
                            // Recorrer HVACs
                            tablasACTGU["MecanismosHVAC"].forEach(function(itemXZY, indexXZY) {
                                // Actualizar valores
                                tablasACTGU["MecanismosHVAC"][indexXZY]["estado"] = "a";
                            });
                            // Actualizar HVAC's
                            rellenar_tabla_de_hvacs();
                        }
                    }
                    // ----------------------------------------------
                    // ----------------------------------------------
                    // ** Reejecuar función que carga valores ambientales
                    revals_en_tabla_usuarios();
                    recargar_valores_ambientales();
                } else {
                    // ** Reejecuar función que carga valores ambientales
                    recargar_valores_ambientales();
                }
            } else {
                // Lanzar mensaje de error
                console.log("Ha ocurrido un error, en la consulta de los datos de los sensores");
                // ** Reejecuar función que carga valores ambientales
                recargar_valores_ambientales();
            }
        } else {
            // ** Reejecuar función que carga valores ambientales
            recargar_valores_ambientales();
        }
    });
};

// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

// MAIN
window.addEventListener('load', function() {
    // Cargar loader
    openLoader(function(closeLoader) {
        // -------------------------------------------------------------------
        // -------------------------------------------------------------------

        /*
        // Controles de la Interfaz
        // (CheckBox, SelectorEstrategia, SelectorDía, InputHora)
        var // Configuración general
            ctrl_btn_calcular = document.getElementById("ctrl_btn_calcular"),
            ctrl_input_hora = document.getElementById("ctrl_input_hora"),
            ctrl_sl_dia = document.getElementById("ctrl_sl_dia"),
            // Control de grupo
            ctrl_sl_grupo = document.getElementById("ctrl_sl_grupo");
        // Controles generales
        ctrl_btn_calcular.addEventListener("click", function(sender) {
            calcular_confort_termico_grupal();
            sender.preventDefault();
        });
        ctrl_input_hora.addEventListener("change", function(sender) {
            actualizar_configuracion_general();
            sender.preventDefault();
        });
        ctrl_sl_dia.addEventListener("change", function(sender) {
            actualizar_configuracion_general();
            sender.preventDefault();
        });
        // Control de grupo
        ctrl_sl_grupo.addEventListener("change", function(sender) {
            cambiar_de_grupo();
            sender.preventDefault();
        });
        */

        closeLoader();
    });

    // Inicializar Elementos Foundation
    $(document).foundation();
});