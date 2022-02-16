// **** Variables generales o de utileria
var usarTablas = true;
var tablasACTGU = {};
var tagTpsEquips = "TiposEquipamientos";
var tagUsuarios = "Usuarios";
var tagGrupos = "GrpsUsrs";
var tagRoles = "Roles";

var dataFilaUsrSel = "";
var dataFilaGrpSel = "";

var classFilaCon = "actgu-box-fila";
var classFilaSel = "actgu-row-select";
var classDataEnFila = "actgu-data-en-fila";
var classDeReferMET = "actgu-refermetde_";

var dataGrpCheck = "data-grpchck";
var dataUsrCheck = "data-usrchck";

var dataUsrParaMET = "data-usrmet";
var dataUsrRefiere = "data-usrrefer_";

// **** Funciones generales o de utileria

// Función que reinicia los datos contenidos en las tablas
function reiniciarDataDeTablas(preClosLoader) {
    // Cargar loader
    openLoader(function(closeLoader) {
        // Función que reserva las filas seleccionadas en tablas
        guardarFilasSelecciones();
        // Función en caso de error durante la consulta de tablas
        var reConsultarDataDeTablas = function (nomTablaDeE) {
            // Mostrar error
            console.log("Ocurrido un error duruante la consulta de la Tabla \"" + nomTablaDeE + "\"");
            // Frente a un error con las tab las, se reinicia el proceso de consulta del sistema
            setTimeout(function() {
                // Se cierra el loadeer
                closeLoader();
                // Se reinicia el sistema
                reiniciarDataDeTablas(preClosLoader);
                // Ejecutar luego de...
            }, 1000);
        }
        // Lista de tablas a ser consultadas y función para consultarlas
        var aryTablas = [tagUsuarios, tagGrupos, tagRoles, tagTpsEquips],
            consultarBD = function(indexAT, maxInAT, arrTablas, finalizarProceso) {
                // Validar continuación del proceso de consulta
                if (indexAT < maxInAT) {
                    // Invocación AJAX
                    (function(ajaxRetorno) {
                        // Variable de petición
                        var miPeticionAjax = new XMLHttpRequest(),
                            paramsURL = 'jsonData=' + '{Tabla:"' + arrTablas[indexAT] + '"}';
                        // Definción de ejecusión AJAX
                        miPeticionAjax.onload = function() {
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
                    })(function(rData) {
                        // Validar datos de retorno
                        if (rData && rData.trim()) {
                            // Dividir resultados obtenidos
                            var strResp = rData.trim().split("|");
                            if (strResp.length > 1) {
                                // ----------------------------------------------
                                // ----------------------------------------------
                                // Esperar a que se pueda hacer uso de las tablas
                                while (!usarTablas) {
                                    console.log("reiniciarDataDeTablas: " + usarTablas);
                                }
                                // Bloquear uso de las tablas
                                usarTablas = false;
                                // ----------------------------------------------
                                // ----------------------------------------------
                                // Convertir resultado a un objeto JSON
                                var jsonData = JSON.parse("{" + strResp[0] + "}");
                                // Tramiento de los datos consultados
                                for (cmpLlave in jsonData) {
                                    tablasACTGU[cmpLlave] = jsonData[cmpLlave] || [];
                                }
                                // ----------------------------------------------
                                // ----------------------------------------------
                                // Permitir hacer uso de las tablas
                                usarTablas = true;
                                // ----------------------------------------------
                                // ----------------------------------------------
                                // Continuar con el proceso de consulta de tablas
                                consultarBD(indexAT + 1, maxInAT, arrTablas, finalizarProceso);
                            } else {
                                // Reconsulta data de datablas debido a error
                                reConsultarDataDeTablas(arrTablas[indexAT]);
                            }
                        } else {
                            // Reconsulta data de datablas debido a error
                            reConsultarDataDeTablas(arrTablas[indexAT]);
                        }
                    });
                } else {
                    finalizarProceso();
                }
            };
        // Ejecutar función para consulta de BD
        consultarBD(0, aryTablas.length, aryTablas, function() {
            // ----------------------------------------------
            // ----------------------------------------------
            // Esperar a que se pueda hacer uso de las tablas
            while (!usarTablas) {
                console.log("reiniciarDataDeTablas: " + usarTablas);
            }
            // Bloquear uso de las tablas
            usarTablas = false;
            // ----------------------------------------------
            // ----------------------------------------------
            // TABLA: USUARIOS
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
            tablasACTGU[tagUsuarios] = newTablaUsrs;
            // TABLA: GRUPOS
            // Nueva tabla de usuarios
            var newTablaGrups = [];
            // Recorrer usuarios
            tablasACTGU[tagGrupos].forEach(function(itemOriGrp /*OBJ*/ , indexItemOriG /*INT*/ ) {
                // Indica si el item se asigno a la nueva tabla de usuarios
                var asigEnTabla = false;
                // Revisa si el item actual se agrego a la nueva tabla de usuarios
                newTablaGrups.forEach(function(itemNewGrp /*OBJ*/ , indexItemNewG /*INT*/ ) {
                    // Valida si el item actual se agrego a la nueva tabla de usuarios    
                    if (itemOriGrp["id_grupo"] === itemNewGrp["id_grupo"]) {
                        // Validar existencia de usuario
                        if (itemOriGrp["id_usuario"] && itemOriGrp["etiqueta_usuario"]) {
                            // Agregar nueva relación <<Grupo - Lugar>> en arreglo
                            // **** ¿Porque? Un usuario puede pertener a "n" grupos
                            newTablaGrups[indexItemNewG]["usuarios_del_grupo"].push({
                                id_usuario: itemOriGrp["id_usuario"],
                                etiqueta_usuario: itemOriGrp["etiqueta_usuario"]
                            });
                        }
                        // Cambiar estado de bandera
                        asigEnTabla = true;
                    }
                });
                // Validar como agregar item en arreglo AX
                if (!asigEnTabla) {
                    // Intancia de fila de usuario para tabla
                    var newFilaGrp = {};
                    // Recorrer item G y llenar obj Temporal
                    for (var cmpItemOriU /*STR:*/ in itemOriGrp /*:OBJ*/ ) {
                        // Si "cmpItemOriU" no es igual a "id_grupo, nombre_grupo" entonces agregar al objeto "newFilaGrp"
                        if (!((cmpItemOriU === "id_usuario") || (cmpItemOriU === "etiqueta_usuario"))) {
                            // Se pasan todos los campos, el ID GRUPO y NOMBRE GRUPO, se pasan despues, en GRUPO LUGAR
                            newFilaGrp[cmpItemOriU] = itemOriGrp[cmpItemOriU];
                        }
                    }
                    // Agregar nueva relación <<USUARIO - GRUPO>> en la tabla nueva
                    // **** ¿Porque? Un usuario puede pertener a "n" grupos
                    newFilaGrp["usuarios_del_grupo"] = [];
                    // Validar existencia de usuario
                    if (itemOriGrp["id_usuario"] && itemOriGrp["etiqueta_usuario"]) {
                        // Agregar nueva relación <<USUARIO - GRUPO>> en la tabla nueva
                        newFilaGrp["usuarios_del_grupo"].push({
                            id_usuario: itemOriGrp["id_usuario"],
                            etiqueta_usuario: itemOriGrp["etiqueta_usuario"]
                        });
                    }
                    // Agregar nueva fila de usuario a la nueva tabla
                    newTablaGrups.push(newFilaGrp);
                }
            });
            tablasACTGU[tagGrupos] = newTablaGrups;
            // ********************
            // Configurar tablas de usuarios y grupos
            reconfigurarTablaDeUsuarios();
            reconfigurarTablaDeGrupos();
            // Restaura las filas selecciones en las tablas
            restaurarFilasSelecciones();
            // ----------------------------------------------
            // ----------------------------------------------
            // Permitir hacer uso de las tablas
            usarTablas = true;
            // ----------------------------------------------
            // ----------------------------------------------
            // Validar si existe una función previa a cerrar el loader
            if (preClosLoader) {
                // Ejecuta función antes de cerrar el Loader
                preClosLoader();
            }
            // Cierra el icono de carga
            closeLoader();
        });
    });
}

// Función que guardar en memoria las filas seleccionadas
function guardarFilasSelecciones() {
    // Recuperar registro seleccionado
    var itemsEnTb = document.getElementById("ctrl_tabla_usuarios").getElementsByClassName(classFilaSel),
        idDatItem = "";
    // Validar si hay registros seleccionados
    if (itemsEnTb.length > 0) {
        // Recuperar data resguardada en item seleccionado
        idDatItem = itemsEnTb[0].getElementsByClassName(classDataEnFila)[0].value || "";
    }
    // Validar si se logro recuperar la data del item seleccionado
    if (!idDatItem) {
        // Reiniciar data del item seleccionado
        idDatItem = "";
        // Validar que se tenga datos en tabla
        if (tablasACTGU[tagUsuarios]) {
            // Recorrer tabla de usuarios
            tablasACTGU[tagUsuarios].forEach(function(itemOriUsr /*OBJ*/ , indexItemOriU /*INT*/ ) {
                // Validar data del item seleccionado
                if (!idDatItem) {
                    // Asignar identificador del usuario
                    idDatItem = itemOriUsr["id_usuario"];
                } else {
                    // Validar si el nuevo item de usuario es menor
                    if (parseInt(idDatItem) > parseInt(itemOriUsr["id_usuario"])) {
                        // Asignar identificador del usuario
                        idDatItem = itemOriUsr["id_usuario"];
                    }
                }
            });   
        }
    }
    // Asignar data de la fila seleccionada
    dataFilaUsrSel = idDatItem || "";
    // -------------------------------------------------------------------------
    // Recuperar registro seleccionado
    itemsEnTb = document.getElementById("ctrl_tabla_grupos").getElementsByClassName(classFilaSel);
    idDatItem = "";
    // Validar si hay registros seleccionados
    if (itemsEnTb.length > 0) {
        // Recuperar data resguardada en item seleccionado
        idDatItem = itemsEnTb[0].getElementsByClassName(classDataEnFila)[0].value || "";
    }
    // Validar si se logro recuperar la data del item seleccionado
    if (!idDatItem) {
        // Reiniciar data del item seleccionado
        idDatItem = "";
        // Validar que se tenga datos en tabla
        if (tablasACTGU[tagGrupos]) {
            // Recorrer tabla de usuarios
            tablasACTGU[tagGrupos].forEach(function(itemOriGrp /*OBJ*/ , indexItemOriG /*INT*/ ) {
                // Validar data del item seleccionado
                if (!idDatItem) {
                    // Asignar identificador del usuario
                    idDatItem = itemOriGrp["id_grupo"];
                } else {
                    // Validar si el nuevo item de usuario es menor
                    if (parseInt(idDatItem) > parseInt(itemOriGrp["id_grupo"])) {
                        // Asignar identificador del usuario
                        idDatItem = itemOriGrp["id_grupo"];
                    }
                }
            });   
        }
    }
    // Asignar data de la fila seleccionada
    dataFilaGrpSel = idDatItem || "";
}

// Función que restaura en las tablas a las filas seleccionadas
function restaurarFilasSelecciones() {
    // Mecanismo comun para restaurar filas
    var resFilaSeleccionada = function (idCtrlUsr, dataFilaSel) {
        // Tomar instancia de tabla y sus filas seleccionadas
        var filaSelRestaurada = false
            tabActual = document.getElementById(idCtrlUsr),
            itemsEnTb = tabActual.getElementsByClassName(classFilaSel);
        // Recorrer filas seleccionadas en tabla
        [].forEach.call(itemsEnTb, function(objSeld, indcSel) {
            // Remover clase que indica fila seleccionado
            objSeld.classList.remove(classFilaSel);
        });
        // Recuperar filas en tabla
        itemsEnTb = tabActual.getElementsByClassName(classFilaCon);
        // Validar que exista data de fila a seleccionar
        if (dataFilaSel) {
            // Recorrer filas en tabla
            [].forEach.call(itemsEnTb, function(filaCon, indcFila) {
                // Verificar si la data es de la fila a seleccionar
                if (dataFilaSel === filaCon.getElementsByClassName(classDataEnFila)[0].value) {
                    // Seleccionar fila - currentTarget
                    filaCon.classList.add(classFilaSel);
                    filaSelRestaurada = true;
                }
            });
        }
        // Validar restauración de fila
        if (!filaSelRestaurada) {
            // Recorrer filas en tabla
            [].forEach.call(itemsEnTb, function(filaCon, indcFila) {
                // Verificar si la data es de la fila a seleccionar
                if (!filaSelRestaurada) {
                    // Seleccionar fila - currentTarget
                    filaCon.classList.add(classFilaSel);
                    filaSelRestaurada = true;
                }
            });
        }
    };
    // -------------------------------------------------------------------------
    // Restaurar fila seleccionada en tabla de usuarios
    resFilaSeleccionada("ctrl_tabla_usuarios", dataFilaUsrSel);
    // Restaurar fila seleccionada en tabla de grupos
    resFilaSeleccionada("ctrl_tabla_grupos", dataFilaGrpSel);
    // -------------------------------------------------------------------------
}

// Función que configurar tabla de usuarios
function reconfigurarTablaDeUsuarios() {
    // Generar instancia de tabla
    var tabActual = document.getElementById("ctrl_tabla_usuarios"),
        aryFilas = [];
    // Limpiar contenido en tabla
    clearContainer(tabActual);
    // Validar que se tenga datos en tabla
    if (tablasACTGU[tagUsuarios]) {
        // Recorrer tabla de usuarios
        tablasACTGU[tagUsuarios].forEach(function(itemOriUsr /*OBJ*/ , indexItemOriU /*INT*/ ) {
            // Variables para la construcción de elementos web
            var divFila = document.createElement("div"),
                divGrid = document.createElement("div");
            // Construcción de elementos web (DIV's de fila)
            divFila.setAttribute('class', 'small-12 actgu-box-fila');
            divGrid.setAttribute('class', 'grid-x');
            
            // Variables para la construcción de labels web
            var divCon_01 = document.createElement("div"),
                lblCon_01 = document.createElement("label");
            // Construcción de labels web
            divCon_01.setAttribute('class', 'small-2 align-self-middle');
            lblCon_01.setAttribute('class', 'text-left middle actgu-ctrl-label actgu-label-truncate');
            lblCon_01.appendChild(document.createTextNode(itemOriUsr["etiqueta_usuario"]));
            // Agregar label a su div
            divCon_01.appendChild(lblCon_01);
            
            // Variables para la construcción de labels web
            var divCon_02 = document.createElement("div"),
                lblCon_02 = document.createElement("label");
            // Construcción de labels web
            divCon_02.setAttribute('class', 'small-1 align-self-middle');
            lblCon_02.setAttribute('class', 'text-left middle actgu-ctrl-label actgu-label-truncate');
            // Validar icono a mostrar
            if (itemOriUsr["sexo"] === "h") {
                lblCon_02.appendChild(document.createTextNode("Hombre"));
            } else {
                lblCon_02.appendChild(document.createTextNode("Mujer"));
            }
            // Agregar label a su div
            divCon_02.appendChild(lblCon_02);
            
            // Variables para la construcción de labels web
            var divCon_03 = document.createElement("div"),
                lblCon_03 = document.createElement("label");
            // Construcción de labels web
            divCon_03.setAttribute('class', 'small-1 align-self-middle');
            lblCon_03.setAttribute('class', 'text-left middle actgu-ctrl-label actgu-label-truncate');
            lblCon_03.appendChild(document.createTextNode(itemOriUsr["edad"] + " años"));
            // Agregar label a su div
            divCon_03.appendChild(lblCon_03);
            
            // Variables para la construcción de labels web
            var divCon_04 = document.createElement("div"),
                lblCon_04 = document.createElement("label");
            // Construcción de labels web
            divCon_04.setAttribute('class', 'small-1 align-self-middle');
            lblCon_04.setAttribute('class', 'text-left middle actgu-ctrl-label actgu-label-truncate');
            lblCon_04.appendChild(document.createTextNode(itemOriUsr["altura"] + " m"));
            // Agregar label a su div
            divCon_04.appendChild(lblCon_04);
            
            // Variables para la construcción de labels web
            var divCon_05 = document.createElement("div"),
                lblCon_05 = document.createElement("label");
            // Construcción de labels web
            divCon_05.setAttribute('class', 'small-1 align-self-middle');
            lblCon_05.setAttribute('class', 'text-left middle actgu-ctrl-label actgu-label-truncate');
            lblCon_05.appendChild(document.createTextNode(itemOriUsr["peso"] + " kg"));
            // Agregar label a su div
            divCon_05.appendChild(lblCon_05);
            
            // Variables para la construcción de labels web
            var divCon_06 = document.createElement("div"),
                lblCon_06 = document.createElement("label");
            // Construcción de labels web
            divCon_06.setAttribute('class', 'small-1 align-self-middle');
            lblCon_06.setAttribute('class', 'text-left middle actgu-ctrl-label actgu-label-truncate');
            lblCon_06.appendChild(document.createTextNode(itemOriUsr["rol"]));
            // Agregar label a su div
            divCon_06.appendChild(lblCon_06);
            
            // Variables para la construcción de labels web
            var divCon_07 = document.createElement("div"),
                lblCon_07 = document.createElement("label");
            // Construcción de labels web
            divCon_07.setAttribute('class', 'small-2 align-self-middle');
            lblCon_07.setAttribute('class', 'text-left middle actgu-ctrl-label actgu-label-truncate');
            lblCon_07.appendChild(document.createTextNode(describirEquipamiento(itemOriUsr["id_equipamiento"], itemOriUsr["sexo"])));
            // Agregar label a su div
            divCon_07.appendChild(lblCon_07);
            
            // Variables para la construcción de labels web
            var divCon_08 = document.createElement("div"),
                lblCon_08 = document.createElement("label");
            // Construcción de labels web
            divCon_08.setAttribute('class', 'small-1 align-self-middle');
            lblCon_08.setAttribute('class', 'text-left middle actgu-ctrl-label actgu-label-truncate ' + classDeReferMET + itemOriUsr["id_usuario"]);
            lblCon_08.setAttribute(dataUsrParaMET, itemOriUsr["id_usuario"]);
            lblCon_08.setAttribute(dataUsrRefiere + itemOriUsr["id_usuario"], "");
            lblCon_08.appendChild(document.createTextNode(itemOriUsr["met"] || ""));
            // Agregar label a su div
            divCon_08.appendChild(lblCon_08);
            
            // Variables para la construcción de labels web
            var divCon_09 = document.createElement("div"),
                lblCon_09 = document.createElement("label");
            // Construcción de labels web
            divCon_09.setAttribute('class', 'small-2 align-self-middle');
            lblCon_09.setAttribute('class', 'text-left middle actgu-ctrl-label actgu-label-truncate');
            var gruposDelUsr = makeTextFromArray(itemOriUsr["grupos_del_usuario"], "nombre_grupo");
            // console.log("U: " + itemOriUsr["etiqueta_usuario"] + ", Gs: " + gruposDelUsr + ".")
            lblCon_09.appendChild(document.createTextNode(gruposDelUsr));
            // Agregar label a su div
            divCon_09.appendChild(lblCon_09);
            
            // Variables para la construcción de input oculto web
            var inputCon_0X = document.createElement("input");
            // Construcción de input oculto web
            inputCon_0X.setAttribute('type', 'text');
            inputCon_0X.setAttribute('value', itemOriUsr["id_usuario"]);
            inputCon_0X.setAttribute('class', 'actgu-disappear ' + classDataEnFila);
            
            // Agregar divs de labels a div grid
            divGrid.appendChild(divCon_01);
            divGrid.appendChild(divCon_02);
            divGrid.appendChild(divCon_03);
            divGrid.appendChild(divCon_04);
            divGrid.appendChild(divCon_05);
            divGrid.appendChild(divCon_06);
            divGrid.appendChild(divCon_07);
            divGrid.appendChild(divCon_08);
            divGrid.appendChild(divCon_09);
            // Agregar input invisible a div grid
            divGrid.appendChild(inputCon_0X);
            
            // Agregar div grid a div fila
            divFila.appendChild(divGrid);
            
            // ----------------------------------
            // ----------------------------------
            divFila.addEventListener("click", function(sender) {
                // Seleccionar fila de tabla
                seleccionarFilaEnTabla(sender, "ctrl_tabla_usuarios");
            });
            // ----------------------------------
            // ----------------------------------

            // Agregar div fila en arreglo de filas
            aryFilas.push(divFila);
        });
    }
    // Recorrer filas contruidas
    aryFilas.forEach(function(filaCon, indexF) {
        // Agregar fila a tabla
        tabActual.appendChild(filaCon);
    });
}

// Función que configurar tabla de grupos
function reconfigurarTablaDeGrupos() {
    // Generar instancia de tabla
    var tabActual = document.getElementById("ctrl_tabla_grupos"),
        aryFilas = [];
    // Limpiar contenido en tabla
    clearContainer(tabActual);
    // Validar que se tenga datos en tabla
    if (tablasACTGU[tagGrupos]) {
        // Recorrer tabla de usuarios
        tablasACTGU[tagGrupos].forEach(function(itemOriGrp /*OBJ*/ , indexItemOriG /*INT*/ ) {
            // Variables para la construcción de elementos web
            var divFila = document.createElement("div"),
                divGrid = document.createElement("div");
            // Construcción de elementos web (DIV's de fila)
            divFila.setAttribute('class', 'small-12 actgu-box-fila');
            divGrid.setAttribute('class', 'grid-x');
            
            // Variables para la construcción de labels web
            var divCon_01 = document.createElement("div"),
                lblCon_01 = document.createElement("label");
            // Construcción de labels web
            divCon_01.setAttribute('class', 'small-3 align-self-middle');
            lblCon_01.setAttribute('class', 'text-left middle actgu-ctrl-label actgu-label-truncate');
            lblCon_01.appendChild(document.createTextNode(itemOriGrp["nombre"]));
            // Agregar label a su div
            divCon_01.appendChild(lblCon_01);
            
            // Variables para la construcción de labels web
            var divCon_02 = document.createElement("div"),
                lblCon_02 = document.createElement("label");
            // Construcción de labels web
            divCon_02.setAttribute('class', 'small-9 align-self-middle');
            lblCon_02.setAttribute('class', 'text-left middle actgu-ctrl-label actgu-label-truncate');
            var usursDelGrp = makeTextFromArray(itemOriGrp["usuarios_del_grupo"], "etiqueta_usuario");
            // console.log("G: " + itemOriGrp["nombre"] + ", U: " + usursDelGrp + ".");
            lblCon_02.appendChild(document.createTextNode(usursDelGrp));
            // Agregar label a su div
            divCon_02.appendChild(lblCon_02);
            
            // Variables para la construcción de input oculto web
            var inputCon_0X = document.createElement("input");
            // Construcción de input oculto web
            inputCon_0X.setAttribute('type', 'text');
            inputCon_0X.setAttribute('value', itemOriGrp["id_grupo"]);
            inputCon_0X.setAttribute('class', 'actgu-disappear ' + classDataEnFila);
            
            // Agregar divs de labels a div grid
            divGrid.appendChild(divCon_01);
            divGrid.appendChild(divCon_02);
            // Agregar input invisible a div grid
            divGrid.appendChild(inputCon_0X);
            
            // Agregar div grid a div fila
            divFila.appendChild(divGrid);
            
            // ----------------------------------
            // ----------------------------------
            divFila.addEventListener("click", function(sender) {
                // Seleccionar fila de tabla
                seleccionarFilaEnTabla(sender, "ctrl_tabla_grupos");
            });
            // ----------------------------------
            // ----------------------------------

            // Agregar div fila en arreglo de filas
            aryFilas.push(divFila);
        });
    }
    // Recorrer filas contruidas
    aryFilas.forEach(function(filaCon, indexF) {
        // Agregar fila a tabla
        tabActual.appendChild(filaCon);
    });
}

// Función que describe el equipamiento vestido
function describirEquipamiento (strIdTE, strSexo) {
    // Variables
    var strDescrp = "";
    // Recorrer items de la tabla de tipos de equipamientos
    tablasACTGU[tagTpsEquips].forEach(function(itemTE, indexTE) {
        // Validar coincidencia entre id's de los equipamientos
        if (itemTE["id_tipo_de_equipamiento"] === strIdTE) {
            // Validar sexo
            if (strSexo === 'h') {
                // Construir descripción del equipamiento]
                strDescrp = itemTE["descripcion"] + ", " + itemTE["descripcion_para_h"];
            } else {
                // Construir descripción del equipamiento]
                strDescrp = itemTE["descripcion"] + ", " + itemTE["descripcion_para_m"];
            }
        }
    });
    // Ajuste de la descripción
    if (strDescrp !== "") {
        // Volver masyuscula la primer letra de la descripción del equipamiento
        strDescrp = strDescrp[0].toUpperCase() + strDescrp.substring(1, strDescrp.length);
    }
    // Devolver descripción
    return strDescrp;
}

// Función que selecciona una fila de una tabla
function seleccionarFilaEnTabla (sender, tablaNombre) {
    // Recupera tabla y sus filas seleccionadas
    var tablaObjv = document.getElementById(tablaNombre),
        itemsEnTb = tablaObjv.getElementsByClassName(classFilaSel);
    // Recorrer filas seleccionadas en tabla
    [].forEach.call(itemsEnTb, function(objetoSels, indiceSel) {
        // Deseleccionar filas de tabla
        objetoSels.classList.remove(classFilaSel);
    });
    // Seleccionar la fila actual
    sender.currentTarget.classList.add(classFilaSel);
}
