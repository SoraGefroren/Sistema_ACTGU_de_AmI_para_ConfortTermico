// Variables del Script
var tablasACTGU = {};
var tagGrupos = "Grupos";
// Controles de la pagina
var ctrl_sl_estrategia = document.getElementById("ctrl_sl_estrategia"),
    ctrl_lbl_politica = document.getElementById("ctrl_lbl_politica"),
    ctrl_cb_politica = document.getElementById("ctrl_cb_politica"),
    ctrl_input_saltos = document.getElementById("ctrl_input_saltos"),
    ctrl_input_tiempo = document.getElementById("ctrl_input_tiempo"),
    ctrl_input_val_temperatura = document.getElementById("ctrl_input_val_temperatura"),
    ctrl_input_incr_temperatura = document.getElementById("ctrl_input_incr_temperatura"),
    ctrl_input_val_humedad = document.getElementById("ctrl_input_val_humedad"),
    ctrl_input_incr_humedad = document.getElementById("ctrl_input_incr_humedad"),
    ctrl_input_val_gas = document.getElementById("ctrl_input_val_gas"),
    ctrl_input_incr_gas = document.getElementById("ctrl_input_incr_gas"),
    ctrl_input_val_vel_aire = document.getElementById("ctrl_input_val_vel_aire"),
    ctrl_input_incr_vel_aire = document.getElementById("ctrl_input_incr_vel_aire"),
    ctrl_sl_grupo = document.getElementById("ctrl_sl_grupo");
// Variables de apoyo
var grupoSel = "";
var ultEdoCheck = false;

// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

// Función que recarga el contenido de la tabla grupos
function reCargarTablaGrupos (posClosLoader) {
    // Cargar loader
    openLoader(function(closeLoader) {
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
                    clearContainer(ctrl_sl_grupo)
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
                        numItemsDeSl = itemsInSl.length;
                    // Validar existencia de elementos
                    if (itemsInSl > 0) {
                        // Recorrer items y desseleccionar items
                        for (var ix = 0; ix < numItemsDeSl; ix++) {
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

// Función que recarga la configuración actual
function reCargarConfiguracion (preClosLoader) {
    // Cargar loader
    openLoader(function(closeLoader) {
        // Función en caso de error durante la consulta de tabla
        var reReiniciarCargaDeConfiguracion = function () {
            // Mostrar error
            console.log("Ocurrido un error al intentar recuperar la configuración actual");
            // Frente a un error con las tab las, se reinicia el proceso de consulta del sistema
            setTimeout(function() {
                // Se cierra el loadeer
                closeLoader();
                // Se reinicia el sistema
                reCargarConfiguracion(preClosLoader);
                // Ejecutar luego de...
            }, 1000);
        };
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
            // Validar datos de retorno
            if (rData && rData.trim()) {
                // Dividir resultados obtenidos
                var strResp = rData.trim().split("|");
                // Validar respuesta
                if (strResp.length > 1) {
                    // Recuperar datos resultantes
                    var jsonData = JSON.parse(strResp[0]);
                    // Validar respuesta
                    if (jsonData) {
                        // Validar el valor de la politica
                        if ((jsonData["politica"] || "ACTGU").toUpperCase() === "ACTGU") {
                            // Asignar valor de politica
                            ctrl_cb_politica.checked = true;
                            ultEdoCheck = true;
                        } else {
                            // Asignar valor de politica
                            ctrl_cb_politica.checked = false;
                            ultEdoCheck = false;
                        }
                        // Asignar data a controles de configuración
                        ctrl_sl_estrategia.value = jsonData["estrategia"] || "001";
                        ctrl_input_saltos.value = jsonData["numero_de_iteraciones"] || 0;
                        ctrl_input_tiempo.value = jsonData["duracion_de_iteracion"] || 0;
                        ctrl_input_val_temperatura.value = jsonData["temperatura"]? jsonData["temperatura"]["valor_inicial"] || 0: 0;
                        ctrl_input_incr_temperatura.value = jsonData["temperatura"]? jsonData["temperatura"]["valor_incremento"] || 0: 0;
                        ctrl_input_val_humedad.value = jsonData["humedad"]? jsonData["humedad"]["valor_inicial"] || 0: 0;
                        ctrl_input_incr_humedad.value = jsonData["humedad"]? jsonData["humedad"]["valor_incremento"] || 0: 0;
                        ctrl_input_val_gas.value = jsonData["concentracion_gas"]? jsonData["concentracion_gas"]["valor_inicial"] || 0: 0;
                        ctrl_input_incr_gas.value = jsonData["concentracion_gas"]? jsonData["concentracion_gas"]["valor_incremento"] || 0: 0;
                        ctrl_input_val_vel_aire.value = jsonData["velocidad_aire"]? jsonData["velocidad_aire"]["valor_inicial"] || 0: 0;
                        ctrl_input_incr_vel_aire.value = jsonData["velocidad_aire"]? jsonData["velocidad_aire"]["valor_incremento"] || 0: 0;
                        // Recuperar lista de grupos
                        var itemsInSl = ctrl_sl_grupo.options,
                            numItemsDeSl = itemsInSl.length,
                            grupoExiste = false;
                        // Recorrer items
                        for (var ix = 0; ix < numItemsDeSl; ix++) {
                            // Verificar si el grupo a seleccionar existe
                            if (itemsInSl[ix].value === jsonData["grupo_investigado"]) {
                                // Indicar que exite el grupo
                                grupoExiste = true;
                                break;
                            }
                        }
                        // Validar el grupo a ser agregado
                        if (grupoExiste) {
                            // Asignar grupo en control de grupos
                            ctrl_sl_grupo.value = jsonData["grupo_investigado"] || grupoSel || "";
                        } else {
                            // Asignar grupo en control de grupos
                            ctrl_sl_grupo.value = grupoSel || "";
                        }
                        // Validar si existe una función previa a cerrar el loader
                        if (preClosLoader) {
                            // Ejecuta función antes de cerrar el Loader
                            preClosLoader();
                        }
                        // Cerrar loader
                        closeLoader();
                    } else {
                        // Reconsultar configuración
                        reReiniciarCargaDeConfiguracion();
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

// Función que recarga la configuración actual
function salvarConfiguracion () {
    // Cargar loader
    openLoader(function(closeLoader) {
        // Variable JSON con data
        var objCONFIGinEdit = {};
        // Asignar primer nivel de valores JSON
        objCONFIGinEdit["iniciar"] = "false";
        objCONFIGinEdit["politica"] = ctrl_cb_politica.checked? "ACTGU": "ASHRAE";
        objCONFIGinEdit["estrategia"] = ctrl_sl_estrategia.value || "";
        objCONFIGinEdit["numero_de_iteraciones"] = parseInt((ctrl_input_saltos.value || "0") + "");
        objCONFIGinEdit["duracion_de_iteracion"] = parseInt((ctrl_input_tiempo.value || "0") + "");
        objCONFIGinEdit["grupo_investigado"] = ctrl_sl_grupo.value || "";
        // Validaciones
        if (!objCONFIGinEdit["numero_de_iteraciones"] && (objCONFIGinEdit["numero_de_iteraciones"] != 0)) {
            // Lanzar mensaje de error
            launchMessage("Error", "Es necesario indicar un \"Número de iteraciones\" valido",
                function(closeMessageBox) {
                    // Cerrar mensaje
                    closeMessageBox();
                    // Cerrar loader
                    closeLoader();
                });
            return;
        }
        // Validaciones
        if (objCONFIGinEdit["numero_de_iteraciones"] > 10) {
            // Lanzar mensaje de error
            launchMessage("Error", "El \"Número de iteraciones\" no puede ser mayor a 10",
                function(closeMessageBox) {
                    // Cerrar mensaje
                    closeMessageBox();
                    // Cerrar loader
                    closeLoader();
                });
            return;
        }
        // Validaciones
        if (objCONFIGinEdit["numero_de_iteraciones"] < 0) {
            // Lanzar mensaje de error
            launchMessage("Error", "El \"Número de iteraciones\" no puede ser menor a 0",
                function(closeMessageBox) {
                    // Cerrar mensaje
                    closeMessageBox();
                    // Cerrar loader
                    closeLoader();
                });
            return;
        }
        // Validaciones
        if (!objCONFIGinEdit["duracion_de_iteracion"] && (objCONFIGinEdit["duracion_de_iteracion"] != 0)) {
            // Lanzar mensaje de error
            launchMessage("Error", "Es necesario indicar una \"Duración de la iteración\" valida",
                function(closeMessageBox) {
                    // Cerrar mensaje
                    closeMessageBox();
                    // Cerrar loader
                    closeLoader();
                });
            return;
        }
        // Validaciones
        if (objCONFIGinEdit["duracion_de_iteracion"] > 300) {
            // Lanzar mensaje de error
            launchMessage("Error", "La \"Duración de la iteración\" no puede ser mayor a 300 segundos",
                function(closeMessageBox) {
                    // Cerrar mensaje
                    closeMessageBox();
                    // Cerrar loader
                    closeLoader();
                });
            return;
        }
        // Validaciones
        if (objCONFIGinEdit["duracion_de_iteracion"] < 5) {
            // Lanzar mensaje de error
            launchMessage("Error", "La \"Duración de la iteración\" no puede ser menor a 5 segundos",
                function(closeMessageBox) {
                    // Cerrar mensaje
                    closeMessageBox();
                    // Cerrar loader
                    closeLoader();
                });
            return;
        }
        // Asignar segundo nivel de valores JSON
        objCONFIGinEdit["temperatura"] = {};
        objCONFIGinEdit["temperatura"]["valor_inicial"] = parseFloat((ctrl_input_val_temperatura.value || "0") + "");
        objCONFIGinEdit["temperatura"]["valor_incremento"] = parseFloat((ctrl_input_incr_temperatura.value || "0") + "");
        objCONFIGinEdit["humedad"] = {};
        objCONFIGinEdit["humedad"]["valor_inicial"] = parseFloat((ctrl_input_val_humedad.value || "0") + "");
        objCONFIGinEdit["humedad"]["valor_incremento"] = parseFloat((ctrl_input_incr_humedad.value || "0") + "");
        objCONFIGinEdit["concentracion_gas"] = {};
        objCONFIGinEdit["concentracion_gas"]["valor_inicial"] = parseFloat((ctrl_input_val_gas.value || "0") + "");
        objCONFIGinEdit["concentracion_gas"]["valor_incremento"] = parseFloat((ctrl_input_incr_gas.value || "0") + "");
        objCONFIGinEdit["velocidad_aire"] = {};
        objCONFIGinEdit["velocidad_aire"]["valor_inicial"] = parseFloat((ctrl_input_val_vel_aire.value || "0") + "");
        objCONFIGinEdit["velocidad_aire"]["valor_incremento"] = parseFloat((ctrl_input_incr_vel_aire.value || "0") + "");
        
        // Mecanismo para validar los valores ambientales
        var msjErroDeValAmbt = "",
            validadorDeAmbtVals = function (nomAmbt, cmpAmbt, cmpSecu, minValu, maxValu, sufxAmbt) {
                // Validaciones
                if (!objCONFIGinEdit[cmpAmbt][cmpSecu] && (objCONFIGinEdit[cmpAmbt][cmpSecu] != 0)) {
                    // Regresa mensaje de error
                    return "Error", "Es necesario indicar una \"" + nomAmbt + "\" valida";
                }
                // Validaciones
                if (!((minValu <= objCONFIGinEdit[cmpAmbt][cmpSecu]) && (objCONFIGinEdit[cmpAmbt][cmpSecu] <= maxValu))) {
                    // Regresa mensaje de error
                    return "Error", "La \"" + nomAmbt + "\" debe estar entre los " + minValu + sufxAmbt + " y " + maxValu + sufxAmbt;
                }
                // No regresa mensaje de error 
                return "";
            };
        // Validar temperatura
        msjErroDeValAmbt = validadorDeAmbtVals("Temperatura", "temperatura", "valor_inicial", 0, 50, "°C");
        if (msjErroDeValAmbt) {
            // Lanzar mensaje de error
            launchMessage("Error", msjErroDeValAmbt, function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }
        msjErroDeValAmbt = validadorDeAmbtVals("Temperatura", "temperatura", "valor_incremento", 0, 50, "°C");
        if (msjErroDeValAmbt) {
            // Lanzar mensaje de error
            launchMessage("Error", msjErroDeValAmbt, function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }
        // Validar humedad
        msjErroDeValAmbt = validadorDeAmbtVals("Humedad", "humedad", "valor_inicial", 0, 100, "%");
        if (msjErroDeValAmbt) {
            // Lanzar mensaje de error
            launchMessage("Error", msjErroDeValAmbt, function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }
        msjErroDeValAmbt = validadorDeAmbtVals("Humedad", "humedad", "valor_incremento", 0, 100, "%");
        if (msjErroDeValAmbt) {
            // Lanzar mensaje de error
            launchMessage("Error", msjErroDeValAmbt, function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }
        // Validar concentración de gas
        msjErroDeValAmbt = validadorDeAmbtVals("Concentración de gas", "concentracion_gas", "valor_inicial", 0, 10000, " ppm");
        if (msjErroDeValAmbt) {
            // Lanzar mensaje de error
            launchMessage("Error", msjErroDeValAmbt, function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }
        msjErroDeValAmbt = validadorDeAmbtVals("Concentración de gas", "concentracion_gas", "valor_incremento", 0, 10000, " ppm");
        if (msjErroDeValAmbt) {
            // Lanzar mensaje de error
            launchMessage("Error", msjErroDeValAmbt, function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }
        // Validar velocidad del aire
        msjErroDeValAmbt = validadorDeAmbtVals("Velocidad del aire", "velocidad_aire", "valor_inicial", 0, 5, " m/s");
        if (msjErroDeValAmbt) {
            // Lanzar mensaje de error
            launchMessage("Error", msjErroDeValAmbt, function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }
        msjErroDeValAmbt = validadorDeAmbtVals("Velocidad del aire", "velocidad_aire", "valor_incremento", 0, 5, " m/s");
        if (msjErroDeValAmbt) {
            // Lanzar mensaje de error
            launchMessage("Error", msjErroDeValAmbt, function(closeMessageBox) {
                // Cerrar mensaje
                closeMessageBox();
                // Cerrar loader
                closeLoader();
            });
            return;
        }
        // Invocación AJAX
        (function(ajaxRetorno) {
            // Variable de petición
            var miPeticionAjax = new XMLHttpRequest(),
                paramsURL = 'jsonData=' + JSON.stringify(objCONFIGinEdit);
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
                ('configuracion/actualizar'), true
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
                        // Recarga de la información del sistema
                        reCargarConfiguracion(null);
                    } else {
                        // Lanzar mensaje de error
                        launchMessage("Error", (jsonData["mensaje"] || "Ocurrido un error al intentar salvar la configuración actual"),
                            function(closeMessageBox) {
                                // Cerrar mensaje
                                closeMessageBox();
                                // Cerrar loader
                                closeLoader();
                            });
                    }
                } else {
                    // Lanzar mensaje de error
                    launchMessage("Error", "Ocurrido un error al intentar salvar la configuración actual",
                        function(closeMessageBox) {
                            // Cerrar mensaje
                            closeMessageBox();
                            // Cerrar loader
                            closeLoader();
                        });
                }
            } else {
                // Lanzar mensaje de error
                launchMessage("Error", "Ocurrido un error al intentar salvar la configuración actual",
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

// Función que corrige el estado del checkbox de politica
function onChangeCBX_Polica() {
    // Recupera valor seleccionado
    var val_estrategia = parseInt(ctrl_sl_estrategia.value || "0");
    // Validar la estrategia seleccionada
    if (val_estrategia != 6) {
        // Puede ser TRUE o FALSE
        ultEdoCheck = ctrl_cb_politica.checked;
    }
}

// Función que ajusta el estado del checkbox de politica
function onChangeSEL_Estrategia() {
    // Recupera valor seleccionado
    var val_estrategia = parseInt(ctrl_sl_estrategia.value || "0");
    // Validar la estrategia seleccionada
    if (val_estrategia === 6) {
        // Deshabilitar el control ChekBox
        ctrl_cb_politica.setAttribute('disabled', 'true');
        // Siempre sera TRUE con la estegia 6
        ctrl_cb_politica.checked = false;
        // Validar si tiene la clase de texto gris
        if (!ctrl_lbl_politica.classList.contains("actgu-texto-descriptivo")) {
            // Agregar clase de texto gris
            ctrl_lbl_politica.classList.add("actgu-texto-descriptivo");
        }
    } else {
        // Habilitar el control ChekBox
        ctrl_cb_politica.removeAttribute('disabled');
        // Puede ser TRUE o FALSE
        ctrl_cb_politica.checked = ultEdoCheck;
        // Validar si tiene la clase de texto gris
        if (ctrl_lbl_politica.classList.contains("actgu-texto-descriptivo")) {
            // Agregar clase de texto gris
            ctrl_lbl_politica.classList.remove("actgu-texto-descriptivo");
        }
    }
}

// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

// MAIN
window.addEventListener('load', function() {
    // Inicializar Elementos Foundation
    $(document).foundation();
    // Cargar tabla de grupos
    reCargarTablaGrupos(function () {
        // Cargar la última configuración
        reCargarConfiguracion(function() {
            // -------------------------------------------------------------------
            // -------------------------------------------------------------------
            // Generar referencia a controles botones
            var ctrl_btn_guardarconfig = document.getElementById("ctrl_btn_guardarconfig");
            // Relacionar controles a funciones
            ctrl_btn_guardarconfig.addEventListener("click", function(sender) {
                // Salvar configuración actual
                salvarConfiguracion();
                // Detener proceso normal del botón
                sender.preventDefault();
            });
            // Asigna función auto-ejecutada al cambiar el valor de checkbox
            ctrl_cb_politica.addEventListener('change', (sender) => {
                // Corrige el estado del control politica
                onChangeCBX_Polica();
            });
            // Asigna función auto-ejecutada al cambiar de estrategia
            ctrl_sl_estrategia.addEventListener('change', (sender) => {
                // Ajusta el estado del selector de estrategias
                onChangeSEL_Estrategia();
            });
            // Corrige el estado del control politica
            onChangeCBX_Polica();
            // Ajusta el estado del selector de estrategias
            onChangeSEL_Estrategia();
            // -------------------------------------------------------------------
            // -------------------------------------------------------------------
        });
    });
});