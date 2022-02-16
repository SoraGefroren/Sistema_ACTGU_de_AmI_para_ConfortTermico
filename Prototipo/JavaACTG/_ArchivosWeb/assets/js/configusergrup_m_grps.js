// Variables del modulo
var filGRUPinObjv = {}; /* { id_grupo: "", nombre: "", usuarios_del_grupo: [{id_usuario: "", etiqueta_usuario: ""}, ... ] } */
var objGRUPinEdit = {}; /* Formato de la data JSON: { id_grupo: "", nombre: "", usuarios: [{id_usuario: ""}, ... ] } */

// Función que abre un modulo para: Ver, Editar o Crear un grupo
function abrir_modulo_grupo(strModo) {
    // Reiniciar la fila objetivo
    filGRUPinObjv = null;
    // 1.-
    // -------------------------------------------
    // Recuperar instancia de controles del modulo
    var ctrl_input_number = document.getElementById("ctrl_input_grupo_number"),
        ctrl_input_nombre = document.getElementById("ctrl_input_grupo_nombre");
    // Revisar el modo de configuración del modulo
    if ((strModo === "ver") || (strModo === "editar")) {
        // Recuperar registro seleccionado en tabla
        var itemsEnTb = document.getElementById("ctrl_tabla_grupos").getElementsByClassName(classFilaSel),
            idDatItem = "";
        // Validar si hay registros seleccionados
        if (itemsEnTb.length > 0) {
            // Recuperar data resguardada en item seleccionado
            idDatItem = itemsEnTb[0].getElementsByClassName(classDataEnFila)[0].value || "";
        }
        // Validar si se logro recuperar la data del item seleccionado y exiiste la tabla
        if (idDatItem && tablasACTGU[tagGrupos]) {
            // Recorrer tabla de usuarios
            tablasACTGU[tagGrupos].forEach(function(itemOriGrp /*OBJ*/ , indexItemOriG /*INT*/ ) {
                // Validar data del item seleccionado
                if (idDatItem === itemOriGrp["id_grupo"]) {
                    // Recupera fila objetivo
                    filGRUPinObjv = itemOriGrp;
                }
            });
        }
        // Validar que se recuperara la fila objetivo
        if (!filGRUPinObjv) {
            // Lanzar mensaje de error
            launchMessage("Error", "No ha seleccionado algún \"Grupo\"",
                function(closeMessageBox) {
                    // Cerrar caja del mensaje
                    closeMessageBox();
                });
            // Si no se recupero la fila objetivo se termina el proceso
            return;
        }
        // Asignar valor de controles
        ctrl_input_number.value = filGRUPinObjv["id_grupo"];
        ctrl_input_nombre.value = filGRUPinObjv["nombre"];
    } else {
        // Asignar valor de controles
        ctrl_input_number.value = "";
        ctrl_input_nombre.value = "";
    }
    // Revisar el modo de configuración del modulo
    if ((strModo === "nuevo") || (strModo === "editar")) {
        // Habilitar control
        ctrl_input_nombre.removeAttribute('disabled');
    } else {
        // Deshabilitar control
        ctrl_input_nombre.setAttribute('disabled', 'true');
    }
    // 2.-
    // -------------------------------------------
    // Recuperar instancia de controles del modulo
    var ctrl_titulo = document.getElementById("ctrl_titulo_grupo");
    // Limpiar control de titulo
    clearContainer(ctrl_titulo);
    // Revisar el modo de configuración del modulo
    if (strModo === "nuevo") {
        // Asingar titulo al modulo
        ctrl_titulo.appendChild(document.createTextNode("Crear un nuevo grupo"));
    } else if (strModo === "editar") {
        // Asingar titulo al modulo
        ctrl_titulo.appendChild(document.createTextNode("Editar la información de grupo"));
    } else {
        // Asingar titulo al modulo
        ctrl_titulo.appendChild(document.createTextNode("Información del grupo seleccionado"));
    }
    // 3.-
    // -------------------------------------------
    reconfigurarTablaDeUsuariosDelModuloGrupos(strModo, filGRUPinObjv? filGRUPinObjv["usuarios_del_grupo"]: []);
    // 4.-
    // -------------------------------------------
    // Recuperar instancia de controles del modulo
    var ctrl_btn_guardar = document.getElementById("ctrl_btn_grupo_guardar"),
        ctrl_btn_cancel = document.getElementById("ctrl_btn_grupo_cancelar");
    // Revisar el modo de configuración del modulo
    if ((strModo === "nuevo") || (strModo === "editar")) {
        // Validar si el botón tiene la clase que lo oculta
        if (ctrl_btn_guardar.classList.contains('actgu-disappear')) {
            // Remover clase que oculta al botón
            ctrl_btn_guardar.classList.remove('actgu-disappear');
        }
        // Modificar texto del botón para salir
        ctrl_btn_cancel.value = "Cancelar";
    } else {
        // Validar si el botón no tiene la clase que lo oculta
        if (!ctrl_btn_guardar.classList.contains('actgu-disappear')) {
            // Agregar clase que oculta al botón
            ctrl_btn_guardar.classList.add('actgu-disappear');
        }
        // Modificar texto del botón para salir
        ctrl_btn_cancel.value = "Regresar";
    }
    // 5.-
    // -------------------------------------------
    // Recuperar instancia del modulo del grupo
    var modalVtn = document.getElementById("vtn_modal_grupo");
    // Abrir modulo
    $(modalVtn).foundation('open');
}

// Función que configurar tabla de usuarios del modulo de grupos
function reconfigurarTablaDeUsuariosDelModuloGrupos(strModo, usuariosDelGrupo) {
    // Generar instancia de tabla
    var tabActual = document.getElementById("ctrl_tabla_grupo_usuarios"),
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
            var divCon_00 = document.createElement("div"),
                inputCon_00 = document.createElement("input");
            // Construcción de labels web
            divCon_00.setAttribute('class', 'small-1 align-self-middle');
            // Revisar el modo de configuración del modulo
            if ((strModo === "nuevo") || (strModo === "editar")) {
                // Habilitar control
                inputCon_00.removeAttribute('disabled');
            } else {
                // Deshabilitar control
                inputCon_00.setAttribute('disabled', 'true');
            }
            inputCon_00.setAttribute('type', 'checkbox');
            inputCon_00.setAttribute('class', 'actgu-ctrl-checkbox');
            inputCon_00.setAttribute(dataUsrCheck, itemOriUsr["id_usuario"]);
            inputCon_00.setAttribute(dataUsrRefiere + itemOriUsr["id_usuario"], "");
            // Agregar label a su div
            divCon_00.appendChild(inputCon_00);
            
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
            lblCon_02.setAttribute('class', 'text-left middle actgu-ctrl-label');
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
            lblCon_03.setAttribute('class', 'text-left middle actgu-ctrl-label');
            lblCon_03.appendChild(document.createTextNode(itemOriUsr["edad"] + " años"));
            // Agregar label a su div
            divCon_03.appendChild(lblCon_03);
            
            // Variables para la construcción de labels web
            var divCon_04 = document.createElement("div"),
                lblCon_04 = document.createElement("label");
            // Construcción de labels web
            divCon_04.setAttribute('class', 'small-1 align-self-middle');
            lblCon_04.setAttribute('class', 'text-left middle actgu-ctrl-label');
            lblCon_04.appendChild(document.createTextNode(itemOriUsr["altura"] + " m"));
            // Agregar label a su div
            divCon_04.appendChild(lblCon_04);
            
            // Variables para la construcción de labels web
            var divCon_05 = document.createElement("div"),
                lblCon_05 = document.createElement("label");
            // Construcción de labels web
            divCon_05.setAttribute('class', 'small-1 align-self-middle');
            lblCon_05.setAttribute('class', 'text-left middle actgu-ctrl-label');
            lblCon_05.appendChild(document.createTextNode(itemOriUsr["peso"] + " kg"));
            // Agregar label a su div
            divCon_05.appendChild(lblCon_05);
            
            // Variables para la construcción de labels web
            var divCon_06 = document.createElement("div"),
                lblCon_06 = document.createElement("label");
            // Construcción de labels web
            divCon_06.setAttribute('class', 'small-2 align-self-middle');
            lblCon_06.setAttribute('class', 'text-left middle actgu-ctrl-label actgu-label-truncate');
            lblCon_06.appendChild(document.createTextNode(itemOriUsr["rol"]));
            // Agregar label a su div
            divCon_06.appendChild(lblCon_06);
            
            // Variables para la construcción de labels web
            var divCon_07 = document.createElement("div"),
                lblCon_07 = document.createElement("label");
            // Construcción de labels web
            divCon_07.setAttribute('class', 'small-3 align-self-middle');
            lblCon_07.setAttribute('class', 'text-left middle actgu-ctrl-label actgu-label-truncate');
            lblCon_07.appendChild(document.createTextNode(describirEquipamiento(itemOriUsr["id_equipamiento"], itemOriUsr["sexo"])));
            // Agregar label a su div
            divCon_07.appendChild(lblCon_07);
            
            // Variables para la construcción de input oculto web
            var inputCon_0X = document.createElement("input");
            // Construcción de input oculto web
            inputCon_0X.setAttribute('type', 'text');
            inputCon_0X.setAttribute('value', itemOriUsr["id_usuario"]);
            inputCon_0X.setAttribute('class', 'actgu-disappear ' + classDataEnFila);
            
            // Agregar divs de labels a div grid
            divGrid.appendChild(divCon_00);
            divGrid.appendChild(divCon_01);
            divGrid.appendChild(divCon_02);
            divGrid.appendChild(divCon_03);
            divGrid.appendChild(divCon_04);
            divGrid.appendChild(divCon_05);
            divGrid.appendChild(divCon_06);
            divGrid.appendChild(divCon_07);
            // Agregar input invisible a div grid
            divGrid.appendChild(inputCon_0X);
            
            // Agregar div grid a div fila
            divFila.appendChild(divGrid);
            
            // Agregar div fila en arreglo de filas
            aryFilas.push(divFila);
        });
    }
    // Recorrer filas contruidas
    aryFilas.forEach(function(filaCon, indexF) {
        // Agregar fila a tabla
        tabActual.appendChild(filaCon);
    });
    // Recorrer usuarios que pertenzcan al grupo
    usuariosDelGrupo.forEach(function(itemUsr, indexU) {
        // Recuperar control CHECKBOX referente a usuario en tabla
        var itemsCheck = tabActual.querySelectorAll('[' + dataUsrCheck + '="' + itemUsr["id_usuario"] + '"]');
        // Validar recuperación del control CHECKBOX de usuario
        if (itemsCheck.length > 0) {
            // Recuperar CHECKBOX de usuario
            var itemCheckU = itemsCheck[0];
            // Checkear item recuperado
            itemCheckU.checked = true;
        }
    });
}

// Función que elimina el grupo actualmente seleccionado
function eliminar_grupo() {
    // Reiniciar la fila objetivo
    filGRUPinObjv = null;
    // -------------------------------------------
    // Recuperar registro seleccionado en tabla
    var itemsEnTb = document.getElementById("ctrl_tabla_grupos").getElementsByClassName(classFilaSel),
        idDatItem = "";
    // Validar si hay registros seleccionados
    if (itemsEnTb.length > 0) {
        // Recuperar data resguardada en item seleccionado
        idDatItem = itemsEnTb[0].getElementsByClassName(classDataEnFila)[0].value || "";
    }
    // Validar si se logro recuperar la data del item seleccionado y exiiste la tabla
    if (idDatItem && tablasACTGU[tagGrupos]) {
        // Recorrer tabla de usuarios
        tablasACTGU[tagGrupos].forEach(function(itemOriGrp /*OBJ*/ , indexItemOriG /*INT*/ ) {
            // Validar data del item seleccionado
            if (idDatItem === itemOriGrp["id_grupo"]) {
                // Recupera fila objetivo
                filGRUPinObjv = itemOriGrp;
            }
        });
    }
    // Validar que se recuperara la fila objetivo
    if (!filGRUPinObjv) {
        // Lanzar mensaje de error
        launchMessage("Error", "No ha seleccionado algún \"Grupo\"",
            function(closeMessageBox) {
                // Cerrar caja del mensaje
                closeMessageBox();
            });
        // Si no se recupero la fila objetivo se termina el proceso
        return;
    }
    // -------------------------------------------
    // Lanzar mensaje de error
    launchMessage("Pregunta",
        "¿Esta seguro de eliminar al grupo \"" + filGRUPinObjv["nombre"] + "\"?",
        // Función que recibe la respuesta dada a la pregunta hecha
        function(respVtnBox, cerrarVtnBox) {
            // Cerrar la caja de la pregunta
            cerrarVtnBox();
            // Validar respuesta
            if (respVtnBox) {
                // Iniciar loader en interfaz
                openLoader(function(closeLoader) {
                    // Iniciar proceso AJAX
                    (function(funcRetorno) {
                        // Variable de petición AJAX
                        var miPeticionAjax = new XMLHttpRequest(),
                            paramsURL = 'jsonData=' + JSON.stringify({
                                id_grupo: filGRUPinObjv["id_grupo"]
                            });
                        // Definción de ejecusión AJAX
                        miPeticionAjax.onload = function() {
                            // Validación del retorno AJAX
                            if (this.readyState == 4 && this.status == 200) {
                                // Función que continua con el proceso
                                funcRetorno(this.responseText);
                            }
                        };
                        // Solicitar recursos por AJAX
                        miPeticionAjax.open(
                            "POST",
                            ('grupo/eliminar'), true
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
                            // Validar respuesta
                            if (strResp.length > 1) {
                                // Recuperar datos resultantes
                                var jsonData = JSON.parse(strResp[0]);
                                // Validar respuesta
                                if (jsonData["todo"] === "ok") {
                                    // Cerrar loader
                                    closeLoader();
                                    // Recarga la información del sistema
                                    reiniciarDataDeTablas(null);
                                } else {
                                    // Lanzar mensaje de error
                                    launchMessage("Error", jsonData["mensaje"],
                                        function(closeMessageBox) {
                                            // Cerrar mensaje
                                            closeMessageBox();
                                            // Cerrar loader
                                            closeLoader();
                                        });
                                }
                            } else {
                                // Lanzar mensaje de error
                                launchMessage("Error", "Ocurrido un error al intentar realizar la siguiente acción: \"Eliminar\"",
                                    function(closeMessageBox) {
                                        // Cerrar mensaje
                                        closeMessageBox();
                                        // Cerrar loader
                                        closeLoader();
                                    });
                            }
                        } else {
                            // Lanzar mensaje de error
                            launchMessage("Error", "Ocurrido un error al intentar realizar la siguiente acción: \"Eliminar\"",
                                function(closeMessageBox) {
                                    // Cerrar mensaje
                                    closeMessageBox();
                                    // Cerrar loader
                                    closeLoader();
                                });
                        }
                    });
                });
            }
        });
}

// Función que guarda el grupo: Editado o Creado
function salvar_grupo() {
    // Cargar loader
    openLoader(function(closeLoader) {
        // Recuperar valor de controles del modulo
        var val_grupo_number = document.getElementById("ctrl_input_grupo_number").value || "",
            val_grupo_nombre = document.getElementById("ctrl_input_grupo_nombre").value || "",
            accion_ejecuta = val_grupo_number? "actualizar": "registrar";
        // Reiniciar variable con data del grupo a guardar
        objGRUPinEdit = {};
        // Inicializar variable con data de grupo a guardar
        objGRUPinEdit["id_grupo"] = val_grupo_number || "";
        objGRUPinEdit["nombre"] = val_grupo_nombre || "";
        // Validaciones
        if (!objGRUPinEdit["nombre"]) {
            // Lanzar mensaje de error
            launchMessage("Error", "No ha indicado el campo \"Nombre del grupo\"",
                function(closeMessageBox) {
                    // Cerrar mensaje
                    closeMessageBox();
                    // Cerrar loader
                    closeLoader();
                });
            return;
        }
        // Validaciones
        if (objGRUPinEdit["nombre"].length > 20) {
            // Lanzar mensaje de error
            launchMessage("Error", "Lo escrito en el campo \"Nombre del grupo\" no debe ser mayor a 20 caracteres",
                function(closeMessageBox) {
                    // Cerrar mensaje
                    closeMessageBox();
                    // Cerrar loader
                    closeLoader();
                });
            return;
        }
        // Preparar variable con data del grupo a guardar para recibir usuarios seleccionados
        objGRUPinEdit["usuarios"] = [];
        // Generar instancia de tabla
        var arryCntrl = [],
            tabActual = document.getElementById("ctrl_tabla_grupo_usuarios"),
            chkBxSels = tabActual.querySelectorAll('input[type="checkbox"]:checked');
        // Recorrer CheckBoxes seleccionadas en tabla
        [].forEach.call(chkBxSels, function(cbSel, indcSel) {
            // Recuperar data de usuario en CheckBox
            var valIdUsr = cbSel.getAttribute(dataUsrCheck);
            // Validar data de usuario recuperada
            if (valIdUsr && !arryCntrl.includes(valIdUsr)) {
                // Se agrega id de usuario para control y evitar repetidos
                arryCntrl.push(valIdUsr);
                // Agregar id de usuario a data del grupo a guardar
                objGRUPinEdit["usuarios"].push({
                    id_usuario: valIdUsr,
                });
            }
        });
        // Invocación AJAX
        (function(funcRetorno) {
            // Variable de petición AJAX
            var miPeticionAjax = new XMLHttpRequest(),
                paramsURL = 'jsonData=' + JSON.stringify(objGRUPinEdit);
            // Definción de ejecusión AJAX
            miPeticionAjax.onload = function() {
                // Valida retorno de la ejecusión AJAX
                if (this.readyState == 4 && this.status == 200) {
                    // Continua con el proceso
                    funcRetorno(this.responseText);
                }
            };
            // Solicitar recursos por AJAX
            miPeticionAjax.open(
                "POST",
                ('grupo/' + accion_ejecuta), true
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
                // Valida la respuesta obtenida
                if (strResp.length > 1) {
                    // Recuperar datos resultantes
                    var jsonData = JSON.parse(strResp[0]);
                    // Validar respuesta
                    if (jsonData["todo"] === "ok") {
                        // Cerrar loader
                        closeLoader();
                        // Cerrar el modulo grupo
                        cerrar_modulo_grupo();
                        // Recarga de la información del sistema
                        reiniciarDataDeTablas(null);
                    } else {
                        // Lanzar mensaje de error
                        launchMessage("Error", (jsonData["mensaje"] || "Ocurrido un error al intentar realizar la siguiente acción: \"" + accion_ejecuta + "\""),
                            function(closeMessageBox) {
                                // Cerrar mensaje
                                closeMessageBox();
                                // Cerrar loader
                                closeLoader();
                            });
                    }
                } else {
                    // Lanzar mensaje de error
                    launchMessage("Error", "Ocurrido un error al intentar realizar la siguiente acción: \"" + accion_ejecuta + "\"",
                        function(closeMessageBox) {
                            // Cerrar mensaje
                            closeMessageBox();
                            // Cerrar loader
                            closeLoader();
                        });
                }
            } else {
                // Lanzar mensaje de error
                launchMessage("Error", "Ocurrido un error al intentar realizar la siguiente acción: \"" + accion_ejecuta + "\"",
                    function(closeMessageBox) {
                        // Cerrar mensaje
                        closeMessageBox();
                        // Cerrar loader
                        closeLoader();
                    });
            }
        });
    });
}
// Función que encargada de cerrar el modulo actual
function cerrar_modulo_grupo() {
    // Recuperar instancia de la ventana
    var modalVtn = document.getElementById("vtn_modal_grupo");
    // cerrar ventana modal
    $(modalVtn).foundation('close');
}