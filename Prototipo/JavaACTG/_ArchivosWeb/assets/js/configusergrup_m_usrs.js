// Variables del modulo
var filUSERinObjv = {}; /* { id_usuario: "", sexo: "", edad: "", peso: "", altura: "", id_rol: "", etiqueta: "", id_equipamiento: "", grupos: [{ id_grupo: "" }, ...] } */
var objUSERinEdit = {}; /* Formato de la data JSON: { id_usuario: "", sexo: "", edad: "", peso: "", altura: "", id_rol: "", etiqueta: "", id_equipamiento: "", grupos: [{ id_grupo: "" }, ...] } */


// Se ejecuta para abrir una ventana
function abrir_modulo_usuario(strModo) {
    // Reiniciar la fila objetivo
    filUSERinObjv = null;
    // 1.-
    // -------------------------------------------
    // Recuperar instancia de controles del modulo
    var ctrl_input_number = document.getElementById("ctrl_input_user_number"),
        ctrl_input_etiqueta = document.getElementById("ctrl_input_user_etiqueta"),
        ctrl_sel_prioridad = document.getElementById("ctrl_sl_user_prioridad"),
        ctrl_sel_sexo = document.getElementById("ctrl_sl_user_sexo"),
        ctrl_input_edad = document.getElementById("ctrl_input_user_edad"),
        ctrl_input_altura = document.getElementById("ctrl_input_user_altura"),
        ctrl_input_peso = document.getElementById("ctrl_input_user_peso"),
        ctrl_input_bmi = document.getElementById("ctrl_input_user_bmi"),
        ctrl_sel_clo = document.getElementById("ctrl_sl_user_clo"),
        ctrl_sel_clo_value = document.getElementById("ctrl_sl_user_clo_value"),
        ctrl_sel_clo_descrp = document.getElementById("ctrl_sl_user_clo_descripcion"),
        ctrl_sld_condicion_top = document.getElementById("ctrl_sld_user_condicion_top"),
        ctrl_sld_tendencia_top = document.getElementById("ctrl_sld_user_tendencia_top"),
        ctrl_sld_user_tendencia_top_valor = document.getElementById("ctrl_sld_user_tendencia_top_valor"),
        ctrl_sld_condicion_bot = document.getElementById("ctrl_sld_user_condicion_bot"),
        ctrl_sld_tendencia_bot = document.getElementById("ctrl_sld_user_tendencia_bot"),
        ctrl_sld_user_tendencia_bot_valor = document.getElementById("ctrl_sld_user_tendencia_bot_valor");
    // Limpiar control de roles
    clearContainer(ctrl_sel_prioridad);
    // Agregar items del control de grupos
    tablasACTGU[tagRoles].forEach(function(itemE, indexG) {
        // Crear opcion
        var itemParaSel = document.createElement("option");
        // Configurar opcion creada
        itemParaSel.setAttribute("value", itemE["id_rol"]);
        itemParaSel.appendChild(document.createTextNode(itemE["rol"]));
        // Agregar opcion a selector
        ctrl_sel_prioridad.appendChild(itemParaSel);
    });
    // Recuperar items en control de grupos
    var itemsInSl = ctrl_sel_prioridad.options,
        numItemsDeSl = itemsInSl.length;
    // Validar existencia de elementos
    if (numItemsDeSl > 0) {
        // Recorrer items y desseleccionar items
        for (var ix = 0; ix < numItemsDeSl; ix++) {
            // Deseleccionar items
            itemsInSl[ix].selected = false;
        }
        // Seleccionar el primer item en el control
        itemsInSl[0].selected = true;
    }
    // Limpiar control de equipamiento
    clearContainer(ctrl_sel_clo);
    // Agregar items del control de grupos
    tablasACTGU[tagTpsEquips].forEach(function(itemE, indexG) {
        // Crear opcion
        var itemParaSel = document.createElement("option");
        // Configurar opcion creada
        itemParaSel.setAttribute("value", itemE["id_tipo_de_equipamiento"]);
        itemParaSel.appendChild(document.createTextNode(itemE["nombre_equipamiento"]));
        // Agregar opcion a selector
        ctrl_sel_clo.appendChild(itemParaSel);
    });
    // Recuperar items en control de grupos
    itemsInSl = ctrl_sel_clo.options;
    numItemsDeSl = itemsInSl.length;
    // Validar existencia de elementos
    if (numItemsDeSl > 0) {
        // Recorrer items y desseleccionar items
        for (var ix = 0; ix < numItemsDeSl; ix++) {
            // Deseleccionar items
            itemsInSl[ix].selected = false;
        }
        // Seleccionar el primer item en el control
        itemsInSl[0].selected = true;
    }
    // 2.-
    // -------------------------------------------
    // Revisar el modo de configuración del modulo
    if ((strModo === "ver") || (strModo === "editar")) {
        // Recuperar registro seleccionado en tabla
        var itemsEnTb = document.getElementById("ctrl_tabla_usuarios").getElementsByClassName(classFilaSel),
            idDatItem = "";
        // Validar si hay registros seleccionados
        if (itemsEnTb.length > 0) {
            // Recuperar data resguardada en item seleccionado
            idDatItem = itemsEnTb[0].getElementsByClassName(classDataEnFila)[0].value || "";
        }
        // Validar si se logro recuperar la data del item seleccionado y exiiste la tabla
        if (idDatItem && tablasACTGU[tagUsuarios]) {
            // Recorrer tabla de usuarios
            tablasACTGU[tagUsuarios].forEach(function(itemOriUsr /*OBJ*/ , indexItemOriU /*INT*/ ) {
                // Validar data del item seleccionado
                if (idDatItem === itemOriUsr["id_usuario"]) {
                    // Recupera fila objetivo
                    filUSERinObjv = itemOriUsr;
                }
            });
        }
        // Validar que se recuperara la fila objetivo
        if (!filUSERinObjv) {
            // Lanzar mensaje de error
            launchMessage("Error", "No ha seleccionado algún \"Usuario\"",
                function(closeMessageBox) {
                    // Cerrar caja del mensaje
                    closeMessageBox();
                });
            // Si no se recupero la fila objetivo se termina el proceso
            return;
        }
        // Asignar valor de controles
        ctrl_input_number.value = filUSERinObjv["id_usuario"];
        ctrl_input_etiqueta.value = filUSERinObjv["etiqueta_usuario"];
        ctrl_sel_prioridad.value = filUSERinObjv["id_rol"];
        ctrl_sel_sexo.value = filUSERinObjv["sexo"];
        ctrl_input_edad.value = filUSERinObjv["edad"];
        ctrl_input_bmi.value = 0;
        ctrl_input_peso.value = filUSERinObjv["peso"];
        ctrl_input_altura.value = filUSERinObjv["altura"];
        ctrl_sel_clo_descrp.textContent = "";
        ctrl_sel_clo_value.textContent = "";
        ctrl_sel_clo.value = filUSERinObjv["id_equipamiento"];
        ctrl_sld_condicion_top.value = filUSERinObjv["condicion_top"];
        ctrl_sld_tendencia_top.value = parseFloat(filUSERinObjv["tendencia_top"] || "0") + 50;
        ctrl_sld_user_tendencia_top_valor.value = (parseFloat(filUSERinObjv["tendencia_top"] || "0") > 0)? "+" + parseFloat(filUSERinObjv["tendencia_top"] || "0"): parseFloat(filUSERinObjv["tendencia_top"] || "0");
        ctrl_sld_condicion_bot.value = filUSERinObjv["condicion_bottom"];
        ctrl_sld_tendencia_bot.value = parseFloat(filUSERinObjv["tendencia_bottom"] || "0") + 50;
        ctrl_sld_user_tendencia_bot_valor.value = (parseFloat(filUSERinObjv["tendencia_bottom"] || "0") > 0)? "+" + parseFloat(filUSERinObjv["tendencia_bottom"] || "0"): parseFloat(filUSERinObjv["tendencia_bottom"] || "0");
    } else {
        // Asignar valor de controles
        ctrl_input_number.value = "";
        ctrl_input_etiqueta.value = "";
        ctrl_sel_prioridad.value = "1";
        ctrl_sel_sexo.value = "h";
        ctrl_input_bmi.value = 0;
        ctrl_input_edad.value = 0;
        ctrl_input_peso.value = 0;
        ctrl_input_altura.value = 0;
        ctrl_sel_clo_descrp.textContent = "";
        ctrl_sel_clo_value.textContent = "";
        ctrl_sel_clo.value = "1";
        ctrl_sld_condicion_top.value = 15;
        ctrl_sld_tendencia_top.value = 50;
        ctrl_sld_user_tendencia_top_valor.value = 0;
        ctrl_sld_condicion_bot.value = 28;
        ctrl_sld_tendencia_bot.value = 50;
        ctrl_sld_user_tendencia_bot_valor.value = 0;
    }
    // Invocar cambio relacionada al valor BMI
    calcular_bmi();
    // Invoca cambio del equipamiento
    informar_sobre_equipamiento();
    // 3.-
    // -------------------------------------------
    // Revisar el modo de configuración del modulo
    if ((strModo === "nuevo") || (strModo === "editar")) {
        // Habilitar controles
        ctrl_input_etiqueta.removeAttribute('disabled');
        ctrl_sel_prioridad.removeAttribute('disabled');
        ctrl_sel_sexo.removeAttribute('disabled');
        ctrl_input_edad.removeAttribute('disabled');
        ctrl_input_peso.removeAttribute('disabled');
        ctrl_input_altura.removeAttribute('disabled');
        ctrl_sel_clo_descrp.removeAttribute('disabled');
        ctrl_sel_clo_value.removeAttribute('disabled');
        ctrl_sel_clo.removeAttribute('disabled');
        ctrl_sld_tendencia_top.removeAttribute('disabled');
        ctrl_sld_tendencia_bot.removeAttribute('disabled');
        // Deshabilitar controles
        ctrl_input_bmi.setAttribute('disabled', 'true');
        ctrl_sld_condicion_top.setAttribute('disabled', 'true');
        ctrl_sld_condicion_bot.setAttribute('disabled', 'true');
    } else {
        // Deshabilitar controles
        ctrl_input_etiqueta.setAttribute('disabled', 'true');
        ctrl_sel_prioridad.setAttribute('disabled', 'true');
        ctrl_sel_sexo.setAttribute('disabled', 'true');
        ctrl_input_bmi.setAttribute('disabled', 'true');
        ctrl_input_edad.setAttribute('disabled', 'true');
        ctrl_input_peso.setAttribute('disabled', 'true');
        ctrl_input_altura.setAttribute('disabled', 'true');
        ctrl_sel_clo_descrp.setAttribute('disabled', 'true');
        ctrl_sel_clo_value.setAttribute('disabled', 'true');
        ctrl_sel_clo.setAttribute('disabled', 'true');
        ctrl_sld_condicion_top.setAttribute('disabled', 'true');
        ctrl_sld_tendencia_top.setAttribute('disabled', 'true');
        ctrl_sld_condicion_bot.setAttribute('disabled', 'true');
        ctrl_sld_tendencia_bot.setAttribute('disabled', 'true');
    }
    // 4.-
    // -------------------------------------------
    // Recuperar instancia de controles del modulo
    var ctrl_titulo = document.getElementById("ctrl_titulo_usuario");
    // Limpiar control de titulo
    clearContainer(ctrl_titulo);
    // Revisar el modo de configuración del modulo
    if (strModo === "nuevo") {
        // Asingar titulo al modulo
        ctrl_titulo.appendChild(document.createTextNode("Crear un usuario nuevo"));
    } else if (strModo === "editar") {
        // Asingar titulo al modulo
        ctrl_titulo.appendChild(document.createTextNode("Editar la información del usuario"));
    } else {
        // Asingar titulo al modulo
        ctrl_titulo.appendChild(document.createTextNode("Información del usuario seleccionado"));
    }
    // 5.-
    // -------------------------------------------
    reconfigurarTablaDeGruposDelModuloUsuarios(strModo, filUSERinObjv? filUSERinObjv["grupos_del_usuario"]: []);
    // 6.-
    // -------------------------------------------
    // Recuperar instancia de controles del modulo
    var ctrl_btn_guardar = document.getElementById("ctrl_btn_user_guardar"),
        ctrl_btn_cancel = document.getElementById("ctrl_btn_user_cancelar");
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
    // 7.-
    // -------------------------------------------
    // Recuperar instancia del modulo del usuario
    var modalVtn = document.getElementById("vtn_modal_user");
    // Abrir ventana modal
    $(modalVtn).foundation('open');
}

// Función que se auto-ejecuta para calcular y actualizar el valor de BMI
function calcular_bmi() {
    // Recuperar el valor contenido en los controles Peso y Altura
    var val_altura = document.getElementById("ctrl_input_user_altura").value || "0",
        val_peso = document.getElementById("ctrl_input_user_peso").value || "0";
    // Parseo de variables como FLOAT para calcular el BMI
    var bmi = 0,
        peso = parseFloat(val_peso),
        altura = parseFloat(val_altura);
    // Validar datos con los cuales calcular el BMI
    if ((peso === 0) && (altura === 0)) {
        // Definir el valor de BMI
        bmi = 0;
    } else if (altura === 0) {
        // Definir el valor de BMI
        bmi = 0;
    } else {
        // Calcular el valor de BMI
        bmi = peso / altura;
    }
    // Actualizar valor de BMI
    document.getElementById("ctrl_input_user_bmi").value = bmi.toFixed(2);
}

// Función que se auto-ejecuta para describir el equipamiento vestido
function informar_sobre_equipamiento() {
    // Crear, instancia de controles
    var val_sel_sexo = document.getElementById("ctrl_sl_user_sexo").value,
        val_sel_clo = document.getElementById("ctrl_sl_user_clo").value,
        ctrl_sel_clo_value = document.getElementById("ctrl_sl_user_clo_value"),
        ctrl_sel_clo_descripcion = document.getElementById("ctrl_sl_user_clo_descripcion");
    // Recuperar valores de selector de equipamiento
    var regEquip = null;
    // Recorrer registros en tabla
    tablasACTGU[tagTpsEquips].forEach(function(itemF, indexF) {
        // Validar existencia de equipamiento seleccionado
        if (val_sel_clo === itemF["id_tipo_de_equipamiento"]) {
            // Reservar registro seleccionado
            regEquip = itemF;
        }
    });
    // Validar recuperación de registro
    if (regEquip) {
        // Variables
        var valorCLO = parseFloat(regEquip["valor_clo"] || "0"),
            descrCLO = regEquip["descripcion"] || "";
        // Validar el sexo del usuario
        if (val_sel_sexo === 'h') {
            // Ajustar valor CLO de acuerdo al sexo
            valorCLO += parseFloat(regEquip["suma_clo_para_h"] || "0");
            // Ajustar descripción del equipamiento]
            descrCLO += ", " + regEquip["descripcion_para_h"];
        } else {
            // Ajustar valor CLO de acuerdo al sexo
            valorCLO += parseFloat(regEquip["suma_clo_para_m"] || "0");
            // Ajustar descripción del equipamiento]
            descrCLO += ", " + regEquip["descripcion_para_m"];
        }
        // Asignar valor CLO a control de valor CLO
        ctrl_sel_clo_value.textContent = valorCLO.toFixed(2) + " clo";
        // Asignar descripción CLO a control de descripción de CLO
        if (descrCLO !== "") {
            // Volver masyuscula la primer letra de la descripción del equipamiento y al asignarla a control de descripción
            ctrl_sel_clo_descripcion.textContent = descrCLO[0].toUpperCase() + descrCLO.substring(1, descrCLO.length);
        } else {
            // Limpiar control
            ctrl_sel_clo_descripcion.textContent = "";
        }
    } else {
        ctrl_sel_clo_value.textContent = "";
        ctrl_sel_clo_descripcion.textContent = "";
    }
}


// Función que configurar tabla de grupos del modulo de usuarios
function reconfigurarTablaDeGruposDelModuloUsuarios(strModo, gruposDelUsuario) {
    // Generar instancia de tabla
    var tabActual = document.getElementById("ctrl_tabla_usuario_grupos"),
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
            var divCon_00 = document.createElement("div"),
                inputCon_00 = document.createElement("input");
            // Construcción de labels web
            divCon_00.setAttribute('class', 'small-2 align-self-middle text-center');
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
            inputCon_00.setAttribute(dataUsrCheck, itemOriGrp["id_grupo"]);
            inputCon_00.setAttribute(dataUsrRefiere + itemOriGrp["id_grupo"], "");
            // Agregar label a su div
            divCon_00.appendChild(inputCon_00);
            
            // Variables para la construcción de labels web
            var divCon_01 = document.createElement("div"),
                lblCon_01 = document.createElement("label");
            // Construcción de labels web
            divCon_01.setAttribute('class', 'small-10 align-self-middle');
            lblCon_01.setAttribute('class', 'text-left middle actgu-ctrl-label actgu-label-truncate');
            lblCon_01.appendChild(document.createTextNode(itemOriGrp["nombre"]));
            // Agregar label a su div
            divCon_01.appendChild(lblCon_01);
            
            // Variables para la construcción de input oculto web
            var inputCon_0X = document.createElement("input");
            // Construcción de input oculto web
            inputCon_0X.setAttribute('type', 'text');
            inputCon_0X.setAttribute('value', itemOriGrp["id_grupo"]);
            inputCon_0X.setAttribute('class', 'actgu-disappear ' + classDataEnFila);
            
            // Agregar divs de labels a div grid
            divGrid.appendChild(divCon_00);
            divGrid.appendChild(divCon_01);
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
    gruposDelUsuario.forEach(function(itemUsr, indexU) {
        // Recuperar control CHECKBOX referente a usuario en tabla
        var itemsCheck = tabActual.querySelectorAll('[' + dataUsrCheck + '="' + itemUsr["id_grupo"] + '"]');
        // Validar recuperación del control CHECKBOX de usuario
        if (itemsCheck.length > 0) {
            // Recuperar CHECKBOX de usuario
            var itemCheckU = itemsCheck[0];
            // Checkear item recuperado
            itemCheckU.checked = true;
        }
    });
}

// Función que elimina el usuario actualmente seleccionado
function eliminar_usuario() {
    // Reiniciar la fila objetivo
    filUSERinObjv = null;
    // -------------------------------------------
    // Recuperar registro seleccionado en tabla
    var itemsEnTb = document.getElementById("ctrl_tabla_usuarios").getElementsByClassName(classFilaSel),
        idDatItem = "";
    // Validar si hay registros seleccionados
    if (itemsEnTb.length > 0) {
        // Recuperar data resguardada en item seleccionado
        idDatItem = itemsEnTb[0].getElementsByClassName(classDataEnFila)[0].value || "";
    }
    // Validar si se logro recuperar la data del item seleccionado y exiiste la tabla
    if (idDatItem && tablasACTGU[tagUsuarios]) {
        // Recorrer tabla de usuarios
        tablasACTGU[tagUsuarios].forEach(function(itemOriUsr /*OBJ*/ , indexItemOriU /*INT*/ ) {
            // Validar data del item seleccionado
            if (idDatItem === itemOriUsr["id_usuario"]) {
                // Recupera fila objetivo
                filUSERinObjv = itemOriUsr;
            }
        });
    }
    // Validar que se recuperara la fila objetivo
    if (!filUSERinObjv) {
        // Lanzar mensaje de error
        launchMessage("Error", "No ha seleccionado algún \"Usuario\"",
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
        "¿Esta seguro de eliminar al usuario \"" + filUSERinObjv["etiqueta_usuario"] + "\"?",
        function(respVtnBox, cerrarVtnBox) {
            // Cerrar mensaje
            cerrarVtnBox();
            // Validar respuesta
            if (respVtnBox) {
                // Cargar loader
                openLoader(function(closeLoader) {
                    // Iniciar proceso AJAX
                    (function(funcRetorno) {
                        // Variable de petición AJAX
                        var miPeticionAjax = new XMLHttpRequest(),
                            paramsURL = 'jsonData=' + JSON.stringify({
                                id_usuario: filUSERinObjv["id_usuario"]
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
                            ('usuario/eliminar'), true
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
                                launchMessage("Error", "Ocurrido un error al intentar realizar la siguiente acción: \"eliminar\"",
                                    function(closeMessageBox) {
                                        // Cerrar mensaje
                                        closeMessageBox();
                                        // Cerrar loader
                                        closeLoader();
                                    });
                            }
                        } else {
                            // Lanzar mensaje de error
                            launchMessage("Error", "Ocurrido un error al intentar realizar la siguiente acción: \"eliminar\"",
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

// Función que guarda el usuario: Editado o Creado
function salvar_usuario() {
    // Cargar loader
    openLoader(function(closeLoader) {
        // Controles de horario de clases
        var val_user_number = document.getElementById("ctrl_input_user_number").value,
            val_user_etiqueta = document.getElementById("ctrl_input_user_etiqueta").value,
            val_user_sexo = document.getElementById("ctrl_sl_user_sexo").value,
            val_user_edad = parseFloat(document.getElementById("ctrl_input_user_edad").value),
            val_user_peso = document.getElementById("ctrl_input_user_peso").value,
            val_user_altura = parseFloat(document.getElementById("ctrl_input_user_altura").value),
            val_user_prioridad = document.getElementById("ctrl_sl_user_prioridad").value,
            val_user_clo = document.getElementById("ctrl_sl_user_clo").value,
            val_user_condicion_top = parseFloat(document.getElementById("ctrl_sld_user_condicion_top").value),
            val_user_tendencia_top = parseFloat(document.getElementById("ctrl_sld_user_tendencia_top").value) - 50,
            val_user_condicion_bot = parseFloat(document.getElementById("ctrl_sld_user_condicion_bot").value),
            val_user_tendencia_bot = parseFloat(document.getElementById("ctrl_sld_user_tendencia_bot").value) - 50,
            accion_ejecuta = val_user_number? "actualizar": "registrar";
        // Reiniciar variable con data del grupo a guardar
        objUSERinEdit = {};
        // Inicializar variable con data de grupo a guardar
        objUSERinEdit["id_usuario"] = val_user_number || "";
        objUSERinEdit["etiqueta"] = val_user_etiqueta || "";
        objUSERinEdit["sexo"] = val_user_sexo || "";
        objUSERinEdit["edad"] = val_user_edad || "";
        objUSERinEdit["peso"] = val_user_peso || "";
        objUSERinEdit["altura"] = val_user_altura || "";
        objUSERinEdit["id_rol"] = val_user_prioridad || "";
        objUSERinEdit["id_equipamiento"] = val_user_clo || "";
        objUSERinEdit["condicion_top"] = val_user_condicion_top || "";
        objUSERinEdit["tendencia_top"] = val_user_tendencia_top || "";
        objUSERinEdit["condicion_bottom"] = val_user_condicion_bot || "";
        objUSERinEdit["tendencia_bottom"] = val_user_tendencia_bot || "";
        // Validaciones
        if (!objUSERinEdit["etiqueta"]) {
            // Lanzar mensaje de error
            launchMessage("Error", "Es necesario asignar una \"Etiqueta\" única para el usuario", function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }
        // Validaciones
        if (objUSERinEdit["etiqueta"].length >= 50) {
            // Lanzar mensaje de error
            launchMessage("Error", "La \"Etiqueta\" no debe superar los 50 caracteres", function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }
        // Validaciones
        if (!objUSERinEdit["edad"] || (objUSERinEdit["edad"] < 0)) {
            // Lanzar mensaje de error
            launchMessage("Error", "Es necesario indicar una \"Edad\" valida", function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }

        // Validaciones
        if (objUSERinEdit["edad"] > 100) {
            // Lanzar mensaje de error
            launchMessage("Error", "La \"Edad\" no puede superar los 100 años", function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }

        // Validaciones
        if (!objUSERinEdit["peso"] || (objUSERinEdit["peso"] < 0)) {
            // Lanzar mensaje de error
            launchMessage("Error", "Es necesario indicar un \"Peso\" valido", function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }

        // Validaciones
        if (objUSERinEdit["peso"] > 200) {
            // Lanzar mensaje de error
            launchMessage("Error", "El \"Peso\" no puede superar los 200 kg", function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }

        // Validaciones
        if (!objUSERinEdit["altura"] || (objUSERinEdit["altura"] < 0)) {
            // Lanzar mensaje de error
            launchMessage("Error", "Es necesario indicar una \"Altura\" valida", function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }

        // Validaciones
        if (objUSERinEdit["altura"] > 2.5) {
            // Lanzar mensaje de error
            launchMessage("Error", "La \"Altura\" no puede superar los 2.5 m", function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }
        
        // Validaciones
        if (!((0 <= objUSERinEdit["condicion_top"]) && (objUSERinEdit["condicion_top"] <= 50))) {
            // Lanzar mensaje de error
            launchMessage("Error", "La primera \"Temperatura\" se sale de un rango aceptable, por favor, ingrese un valor entre 0 y 50", function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }

        // Validaciones
        if (!((0 <= objUSERinEdit["condicion_bottom"]) && (objUSERinEdit["condicion_bottom"] <= 50))) {
            // Lanzar mensaje de error
            launchMessage("Error", "La segunda \"Temperatura\" se sale de un rango aceptable, por favor, ingrese un valor entre 0 y 50", function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }

        // Validaciones
        if (!((-50 <= objUSERinEdit["tendencia_top"]) && (objUSERinEdit["tendencia_top"] <= 50))) {
            // Lanzar mensaje de error
            launchMessage("Error", "La primera \"Tendencia\" no presenta un valor aceptable, por favor revísela", function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }

        // Validaciones
        if (!((-50 <= objUSERinEdit["tendencia_bottom"]) && (objUSERinEdit["tendencia_bottom"] <= 50))) {
            // Lanzar mensaje de error
            launchMessage("Error", "La segunda \"Tendencia\" no presenta un valor aceptable, por favor revísela", function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }

        // Validaciones
        if (objUSERinEdit["condicion_top"] >= objUSERinEdit["condicion_bottom"]) {
            // Lanzar mensaje de error
            launchMessage("Error", "El valor de primera \"Temperatura\" no puede ser mayor o igual al valor de la segunda \"Temperatura\"", function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }

        // Validaciones (Ya que la condicion_top siempre debe ser MENOR)
        if (objUSERinEdit["tendencia_top"] < 0) {
            // Lanzar mensaje de error
            launchMessage("Error", "La primer \"Tendencia\" no puede ser dirigida a un ambiente frío", function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }

        // Validaciones (Ya que la condicion_bot siempre debe ser MAYOR)
        if (objUSERinEdit["tendencia_bottom"] > 0) {
            // Lanzar mensaje de error
            launchMessage("Error", "La segunda \"Tendencia\" no puede ser dirigida a un ambiente cálido", function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }
        // Preparar variable con data del grupo a guardar para recibir usuarios seleccionados
        objUSERinEdit["grupos"] = [];
        // Generar instancia de tabla
        var arryCntrl = [],
            tabActual = document.getElementById("ctrl_tabla_usuario_grupos"),
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
                objUSERinEdit["grupos"].push({
                    id_grupo: valIdUsr,
                });
            }
        });
        // Validaciones
        if (objUSERinEdit["grupos"].length <= 0) {
            // Lanzar mensaje de error
            launchMessage("Error", "Por favor, indique a que \"Grupo\" o \"Grupos\" pertenece este usuario",
                function(closeMessageBox) {
                    // Cerrar mensaje
                    closeMessageBox();
                    // Cerrar loader
                    closeLoader();
                });
            return;
        } else {
            // Variable
            var sonGrupsValido = true,
                aryDeBuenos = [];
            // Validar relaciones
            objUSERinEdit["grupos"].forEach(function(itemO, indexO) {
                // Validar que existan registros
                if (!itemO["id_grupo"]) {
                    // Se indica que no son validos los grupos
                    sonGrupsValido = false;
                } else {
                    aryDeBuenos.push(itemO);
                }
            });
            // Validar validez de grupos
            if ((!sonGrupsValido) || (aryDeBuenos.length <= 0)) {
                // Lanzar mensaje de error
                launchMessage("Error", "Por favor, indique a que \"Grupo\" o \"Grupos\" pertenece este usuario",
                    function(closeMessageBox) {
                        // Cerrar mensaje
                        closeMessageBox();
                        // Cerrar loader
                        closeLoader();
                    });
                return;
            }
        }
        // Invocación ajax
        (function(funcRetorno) {
            // Variable de petición
            var miPeticionAjax = new XMLHttpRequest(),
                paramsURL = 'jsonData=' + JSON.stringify(objUSERinEdit);
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
                ('usuario/' + accion_ejecuta), true
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
                    // Recuperar datos resultantes
                    var jsonData = JSON.parse(strResp[0]);
                    // Validar respuesta
                    if (jsonData["todo"] === "ok") {
                        // Cerrar loader
                        closeLoader();
                        // Cerrar modulo de usuarios
                        cerrar_modulo_usuario();
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

// Se ejecutar al dar click sobre cancelar y cierra el modulo (ventana) actual
function cerrar_modulo_usuario() {
    // Recuperar instancia de la ventana
    var modalVtn = document.getElementById("vtn_modal_user");
    // cerrar ventana modal
    $(modalVtn).foundation('close');
}

