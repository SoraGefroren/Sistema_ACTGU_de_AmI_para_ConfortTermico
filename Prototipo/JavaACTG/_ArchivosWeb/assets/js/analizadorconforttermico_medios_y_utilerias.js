// Variables para referir a condiciones ambientales
var cmpHumedad = "humedad";
var cmpTempera = "temperatura";
var cmpVelAire = "velocidad_del_aire";
var cmpConcGas = "concentracion_de_gas";

// Variables de control
var configDeItos = null,
    resultDeItos = [],
    grupoSel = "",
    tagGrupos = "Grupos",
    tagUsuarios = "Usuarios",
    tagUsuariosSel = "UsuariosSel",
    tablasACTGU = {};

// Controles de la pagina y ventana modal interna
var ctrl_btn_resultado_regresar = document.getElementById("ctrl_btn_resultado_regresar"),
    ctrl_btn_iniciarcalculo = document.getElementById('ctrl_btn_iniciarcalculo'),
    ctrl_sl_estrategia = document.getElementById('ctrl_sl_estrategia'),
    ctrl_cb_politica = document.getElementById("ctrl_cb_politica"),
    ctrl_sl_grupo = document.getElementById('ctrl_sl_grupo');

// Función que recarga el contenido de la tabla grupos
function reCargarTablaGrupos (posClosLoader) {
    // Cargar loader
    openLoader(function(closeLoader) {
        // Invocación AJAX
        (function(ajaxRetorno) {
            // Variable de petición
            var miPeticionAjax = new XMLHttpRequest(),
                paramsURL = 'jsonData=' + '{Tabla:"' + tagGrupos + '"}';
            // Definción de ejecusión AJAX
            miPeticionAjax.onload = function() {
                // Valida retorno de la ejecusión AJAX
                if (this.readyState === 4 && this.status === 200) {
                    // Al recibir respuesta AJAX
                    ajaxRetorno(this.responseText);
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
        // Función de retorno AJAX
        })(function(rDataTabla) {
            // Función en caso de error durante la consulta de tabla
            var reReiniciarCargaDeTablaGrupos = function () {
                // Mostrar error
                console.log("Ocurrido un error duruante la consulta de la Tabla \"" + tagGrupos + "\"");
                // Frente a un error con las tab las, se reinicia el proceso de consulta del sistema
                setTimeout(function() {
                    // Se cierra el loadeer
                    closeLoader();
                    // Se reinicia el sistema
                    reCargarTablaGrupos(posClosLoader);
                    // Ejecutar luego de...
                }, 1000);
            };
            // Validar datos de retorno
            if (rDataTabla && rDataTabla.trim()) {
                // Dividir resultados obtenidos
                var strResp = rDataTabla.trim().split("|");
                if (strResp.length > 1) {
                    // Convertir resultado a un objeto JSON
                    var jsonData = JSON.parse("{" + strResp[0] + "}");
                    // Recuperación de los datos consultados
                    tablasACTGU[tagGrupos] = jsonData[tagGrupos] || [];
                    // ********************
                    // Limpiar control con grupos
                    clearContainer(ctrl_sl_grupo);
                    // Agregar items del control de grupos
                    tablasACTGU[tagGrupos].forEach(function(itemG, indexG) {
                        // Crear opcion
                        var itemParaSel = document.createElement("option");
                        // Configurar opcion creada
                        itemParaSel.setAttribute("value", itemG["id_grupo"]);
                        itemParaSel.appendChild(document.createTextNode(itemG["nombre"]));
                        // Agregar opcion a selector
                        ctrl_sl_grupo.appendChild(itemParaSel);
                    });
                    // Recuperar items en control de grupos
                    var itemsInSl = ctrl_sl_grupo.options,
                        nmItmsDeSl = itemsInSl.length;
                    // Validar existencia de elementos
                    if (itemsInSl > 0) {
                        // Recorrer items y desseleccionar items
                        for (var ix = 0; ix < nmItmsDeSl; ix++) {
                            // Deseleccionar items
                            itemsInSl[ix].selected = false;
                        }
                        // Seleccionar el primer item en el control
                        itemsInSl[0].selected = true;
                        grupoSel = itemsInSl[0].value;
                    } else {
                        grupoSel = "";
                    }
                    // ********************
                    // Cerrar loader
                    closeLoader();
                    // Validar si existe una función para luego de cerrar el loader
                    if (posClosLoader) {
                        // Ejecuta función despues de cerrar el Loader
                        posClosLoader();
                    }
                } else {
                    // Reconsulta data de tablas debido a error
                    reReiniciarCargaDeTablaGrupos();
                }
            } else {
                // Reconsulta data de tablas debido a error
                reReiniciarCargaDeTablaGrupos();
            }
        });
    });
}

// Función que recarga el contenido de la tabla usuarios
function reCargarTablaUsuarios (posClosLoader) {
    // Cargar loader
    openLoader(function(closeLoader) {
        // Invocación AJAX
        (function(ajaxRetorno) {
            // Variable de petición
            var miPeticionAjax = new XMLHttpRequest(),
                paramsURL = 'jsonData=' + '{Tabla:"' + tagUsuarios + '"}';
            // Definción de ejecusión AJAX
            miPeticionAjax.onload = function() {
                // Valida retorno de la ejecusión AJAX
                if (this.readyState === 4 && this.status === 200) {
                    // Al recibir respuesta AJAX
                    ajaxRetorno(this.responseText);
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
        // Función de retorno AJAX
        })(function(rDataTabla) {
            // Función en caso de error durante la consulta de tabla
            var reReiniciarCargaDeTablaUsuarios = function () {
                // Mostrar error
                console.log("Ocurrido un error duruante la consulta de la Tabla \"" + tagUsuarios + "\"");
                // Frente a un error con las tab las, se reinicia el proceso de consulta del sistema
                setTimeout(function() {
                    // Se cierra el loadeer
                    closeLoader();
                    // Se reinicia el sistema
                    reCargarTablaUsuarios(posClosLoader);
                    // Ejecutar luego de...
                }, 1000);
            };
            // Validar datos de retorno
            if (rDataTabla && rDataTabla.trim()) {
                // Dividir resultados obtenidos
                var strResp = rDataTabla.trim().split("|");
                if (strResp.length > 1) {
                    // Convertir resultado a un objeto JSON
                    var jsonData = JSON.parse("{" + strResp[0] + "}");
                    // Recuperación de los datos consultados
                    tablasACTGU[tagUsuarios] = jsonData[tagUsuarios] || [];
                    // Nueva tabla de usuarios
                    var newTablaUsrs = [];
                    // Recorrer usuarios
                    tablasACTGU[tagUsuarios].forEach(function(itemOriUsr /*OBJ*/ , indexItemOriU /*INT*/ ) {
                        // Indica si el item se asigno a la nueva tabla de usuarios
                        var asigEnTabla = false;
                        // Revisa si el item actual se agrego a la nueva tabla de usuarios
                        newTablaUsrs.forEach(function(itemNewUsr /*OBJ*/ , indexItemNewU /*INT*/ ) {
                            // Valida si el item actual se agrego a la nueva tabla de usuarios    
                            if (itemOriUsr["id_usuario"] === itemNewUsr["id_usuario"]) {
                                // Agregar nueva relación <<Grupo - Lugar>> en arreglo
                                // **** ¿Porque? Un usuario puede pertener a "n" grupos
                                newTablaUsrs[indexItemNewU]["grupos_del_usuario"].push({
                                    id_grupo: itemOriUsr["id_grupo"],
                                    nombre_grupo: itemOriUsr["nombre_grupo"]
                                });
                                // Cambiar estado de bandera
                                asigEnTabla = true;
                            }
                        });
                        // Validar como agregar item en arreglo AX
                        if (!asigEnTabla) {
                            // Intancia de fila de usuario para tabla
                            var newFilaUsr = {};
                            // Recorrer item G y llenar obj Temporal
                            for (var cmpItemOriU /*STR:*/ in itemOriUsr /*:OBJ*/ ) {
                                // Si "cmpItemOriU" no es igual a "id_grupo, nombre_grupo" entonces agregar al objeto "newFilaUsr"
                                if (!((cmpItemOriU === "id_grupo") || (cmpItemOriU === "nombre_grupo"))) {
                                    // Se pasan todos los campos, el ID GRUPO y NOMBRE GRUPO, se pasan despues, en GRUPO LUGAR
                                    newFilaUsr[cmpItemOriU] = itemOriUsr[cmpItemOriU];
                                }
                            }
                            // Agregar nueva relación <<USUARIO - GRUPO>> en la tabla nueva
                            // **** ¿Porque? Un usuario puede pertener a "n" grupos
                            newFilaUsr["grupos_del_usuario"] = [];
                            // Agregar nueva relación <<USUARIO - GRUPO>> en la tabla nueva
                            newFilaUsr["grupos_del_usuario"].push({
                                id_grupo: itemOriUsr["id_grupo"],
                                nombre_grupo: itemOriUsr["nombre_grupo"]
                            });
                            // Agregar nueva fila de usuario a la nueva tabla
                            newTablaUsrs.push(newFilaUsr);
                        }
                    });
                    // Actualziar con nueva tabla de usuarios
                    tablasACTGU[tagUsuarios] = newTablaUsrs;
                    // Cerrar loader
                    closeLoader();
                    // Validar si existe una función para luego de cerrar el loader
                    if (posClosLoader) {
                        // Ejecuta función despues de cerrar el Loader
                        posClosLoader();
                    }
                } else {
                    // Reconsulta data de tablas debido a error
                    reReiniciarCargaDeTablaUsuarios();
                }
            } else {
                // Reconsulta data de tablas debido a error
                reReiniciarCargaDeTablaUsuarios();
            }
        });
    });
}

// Función que recarga la configuración actual
function reCargarConfiguracion (preClosLoader, posClosLoader) {
    // Cargar loader
    openLoader(function(closeLoader) {
        // Invocación AJAX
        (function(ajaxRetorno) {
            // Variable de petición
            var miPeticionAjax = new XMLHttpRequest(),
                paramsURL = 'jsonData=' + '{mensaje:""}';
            // Definción de ejecusión AJAX
            miPeticionAjax.onload = function() {
                // Valida retorno de la ejecusión AJAX
                if (this.readyState === 4 && this.status === 200) {
                    // Al recibir respuesta AJAX
                    ajaxRetorno(this.responseText);
                }
            };
            // Solicitar recursos por AJAX
            miPeticionAjax.open(
                "POST",
                ('configuracion/esquema'), true
            );
            miPeticionAjax.setRequestHeader(
                'Content-type',
                'application/x-www-form-urlencoded'
            );
            miPeticionAjax.send(paramsURL);
        // Función de retorno AJAX
        })(function(rData) {
            // Función en caso de error durante la consulta de tabla
            var reReiniciarCargaDeConfiguracion = function () {
                // Mostrar error
                console.log("Ocurrido un error al intentar recuperar la configuración actual");
                // Frente a un error con las tab las, se reinicia el proceso de consulta del sistema
                setTimeout(function() {
                    // Se cierra el loadeer
                    closeLoader();
                    // Se reinicia el sistema
                    reCargarConfiguracion(preClosLoader, posClosLoader);
                    // Ejecutar luego de...
                }, 1000);
            };
            // Validar datos de retorno
            if (rData && rData.trim()) {
                // Dividir resultados obtenidos
                var strResp = rData.trim().split("|");
                // Validar respuesta
                if (strResp.length > 1) {
                    // Recuperar configuración de iteraciones
                    configDeItos = JSON.parse(strResp[0]);
                    // ********************
                    // Asignar data a controles de configuración
                    ctrl_sl_estrategia.value = configDeItos["estrategia"] || "001";
                    // Validar el valor de la politica
                    if ((configDeItos["politica"] || "ACTGU").toUpperCase() === "ACTGU") {
                        // Asignar valor de politica
                        ctrl_cb_politica.checked = true;
                    } else {
                        // Asignar valor de politica
                        ctrl_cb_politica.checked = false;
                    }
                    // Recuperar lista de grupos
                    var itemsInSl = ctrl_sl_grupo.options,
                        nmItmsDeSl = itemsInSl.length,
                        grupoExiste = false;
                    // Recorrer items
                    for (var ix = 0; ix < nmItmsDeSl; ix++) {
                        // Verificar si el grupo a seleccionar existe
                        if (itemsInSl[ix].value === configDeItos["grupo_investigado"]) {
                            // Indicar que exite el grupo
                            grupoExiste = true;
                            break;
                        }
                    }
                    // Validar el grupo a ser agregado
                    if (grupoExiste) {
                        // Asignar grupo en control de grupos
                        ctrl_sl_grupo.value = configDeItos["grupo_investigado"] || grupoSel || "";
                        // Actualizar valor de grupo seleccionado
                        grupoSel = ctrl_sl_grupo.value;
                    }
                    // ********************
                    // Nueva tabla de usuarios
                    var newTablaUsrsSel = [],
                        ctrlDeIdeUsrs = [];
                    // Recorrer items de la tabla de usuarios
                    tablasACTGU[tagUsuarios].forEach(function(itemOriUsr, indexU) {
                        // Recorrer usuarios a los que pertenece este usuario
                        itemOriUsr["grupos_del_usuario"].forEach(function(itemOriG, indexG) {
                            // Validar grupo del usuario
                            if ((itemOriG["id_grupo"] === grupoSel) &&
                                !ctrlDeIdeUsrs.includes(itemOriUsr["id_usuario"])) {
                                // Agregar item de usuario a tabla de seleccionados
                                newTablaUsrsSel.push(itemOriUsr);
                                ctrlDeIdeUsrs.push(itemOriUsr["id_usuario"]);
                            }
                        });
                    });
                    // Actualziar con tabla de usuarios seleccionados
                    tablasACTGU[tagUsuariosSel] = newTablaUsrsSel;
                    // ********************
                    // Validar si existe una función para antes de cerrar el loader
                    if (preClosLoader) {
                        // Ejecuta función antes de cerrar el Loader
                        preClosLoader();
                    }
                    // Cerrar loader
                    closeLoader();
                    // Validar si existe una función previa a cerrar el loader
                    if (posClosLoader) {
                        // Ejecuta función antes de cerrar el Loader
                        posClosLoader();
                    }
                } else {
                    // Reconsultar configuración
                    reReiniciarCargaDeConfiguracion();
                }
            } else {
                // Reconsultar configuración
                reReiniciarCargaDeConfiguracion();
            }
        });
    });
}

// Marcadores para la grafica de Confort Grupal
var markerCG_Y = {value:0, color: "#FFAE00", lineDashType: "dot", thickness:3},
    markerCG_X = {value:0, color: "#FFAE00", lineDashType: "dot", thickness:3},
    dataPointsCG = [/*{x: 0, y: 0, iteracion: 0 },...*/];

// Marcadores para la grafica de Confort Personal
var markerCP_Y = {value:0, color: "#1779BA", lineDashType: "dot", thickness:3},
    markerCP_X = {value:0, color: "#1779BA", lineDashType: "dot", thickness:3},
    dataPointsCP = {/*
        id_usuario: [
            {x: 0, y: 0, iteracion: 0, id_usuario: 0}, ...
        ], ...
    */},
    dataUsuariosCP = [/*{
        showInLegend: true,
        markerType: "square",
        type: "spline",
        markerSize: 15,
        fillOpacity: 0.2,
        xValueFormatString: "Iteración: #",
        yValueFormatString: "",
        name: "Usuario XXK",
        color: "#668C4E",
        dataPoints: dataPointsCP[n],
        click: function(e){
            markerCP_Y.value = e.dataPoint.y;
            markerCP_X.value = e.dataPoint.x;
            $("#ctrl_confortpersonal_grafica").CanvasJSChart().render();
            // alert(e.dataSeries.type+ ", dataPoint { x:" + e.dataPoint.x + ", y: "+ e.dataPoint.y + " }" );
        }
    }, ... */];

// Función que configura el arreglode datos de usuario que utilizara la grafica de confort personal
function configDataDeUsrs_paraCPyCG () {
    // Reiniciar valor de marcadores de CONFORT GRUPAL
    markerCG_Y.value = 0;
    markerCG_X.value = 0;
    // Reiniciar valor de marcadores de CONFORT PERSONAL
    markerCP_Y.value = 0;
    markerCP_X.value = 0;
    // Limpiar datos para grafica de CONFORT GRUPAL
    // Mientras el arreglo tenga items
    while(dataPointsCG.length > 0) {
         // Sacar (Remover) item
        dataPointsCG.pop();
    }
    // Limpiar datos de grafica de CONFORT PERSONAL
    // Recorrer arreglo de datos de usuarios
    for (var [ideUsr, dataUsr] of Object.entries(dataPointsCP)) {
        // Mientras el arreglo tenga items
        while(dataPointsCP[ideUsr].length > 0) {
            // Sacar (Remover) item
            dataPointsCP[ideUsr].pop();
        }
    }
    // Mientras el arreglo tenga data de usuario
    while(dataUsuariosCP.length > 0) {
         // Sacar (Remover) item
        dataUsuariosCP.pop();
    }
    // Configurar data de grafica de CONFORT PERSONAL
    // Recorrer items de la tabla de usuarios
    tablasACTGU[tagUsuariosSel].forEach(function(itemOriUsr, indexU) {
        // Inicializada variables para almacenamiento de datos
        dataPointsCP[itemOriUsr["id_usuario"]] = [/* {x: 0, y: 0, iteracion: 0, id_usuario: 0} */];
        // Agregar configuración de linea de usuario
        dataUsuariosCP.push({
                // Mostrar leyenda inferior
                showInLegend: true,
                // Define el tipo de punto
                markerType: "square",
                // Degfine el tipo de grafico
                type: "spline",
                // Grosor del punto en el grafico y opacidad del relleno
                markerSize: 10,
                fillOpacity: 0,
                lineThickness: 2.5,
                // Define como se veran las cosas en el Tooltip
                xValueFormatString: "# iteración.",
                yValueFormatString: "",
                name: itemOriUsr["etiqueta_usuario"] || "",
                // Configuración de los puntos
                color: "#"+((1<<24)*Math.random()|0).toString(16),
                dataPoints: dataPointsCP[itemOriUsr["id_usuario"]],
                // Data de usuario
                dataRegistro: {
                    id_usuario: itemOriUsr["id_usuario"] || "",
                    etiqueta_usuario: itemOriUsr["etiqueta_usuario"] || ""
                },
                // Acción ejecutada al dar click sobre el punto
                click: function(e){
                    // Recuperar el valor de los ejes marcados
                    markerCP_Y.value = e.dataPoint.y;
                    markerCP_X.value = e.dataPoint.x;
                    // Actualiza información de controles relacionados
                    actualizarInfoConfortPersonal(
                        e.dataPoint["iteracion"],
                        e.dataPoint["id_usuario"],
                        markerCP_X.value, markerCP_Y.value);
                }
            });
    });
}
                    