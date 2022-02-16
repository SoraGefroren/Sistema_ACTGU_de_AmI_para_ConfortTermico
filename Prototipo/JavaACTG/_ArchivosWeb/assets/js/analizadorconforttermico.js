
// Controles de la grafica de Confort Grupal
var ctrl_confortgrupal_iteracion = document.getElementById('ctrl_confortgrupal_iteracion'),
    ctrl_confortgrupal_sensacion = document.getElementById('ctrl_confortgrupal_sensacion'),
    ctrl_confortgrupal_estado = document.getElementById('ctrl_confortgrupal_estado'),
    ctrl_confortgrupal_valor = document.getElementById('ctrl_confortgrupal_valor'),
    ctrl_confortgrupal_valor_numero = document.getElementById('ctrl_confortgrupal_valor_numero'),
    ctrl_confortgrupal_btn_detalle = document.getElementById('ctrl_confortgrupal_btn_detalle'),
    // Controles de la grafica de Confort Personal
    ctrl_confortpersonal_etiqueta = document.getElementById('ctrl_confortpersonal_etiqueta'),
    ctrl_confortpersonal_iteracion = document.getElementById('ctrl_confortpersonal_iteracion'),
    ctrl_confortpersonal_sensacion = document.getElementById('ctrl_confortpersonal_sensacion'),
    ctrl_confortpersonal_estado = document.getElementById('ctrl_confortpersonal_estado'),
    ctrl_confortpersonal_valor = document.getElementById('ctrl_confortpersonal_valor'),
    ctrl_confortpersonal_valor_numero = document.getElementById('ctrl_confortpersonal_valor_numero');

// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

// Función que actualiza información de controles relacionados al Confort Personal
function actualizarInfoConfortPersonal(vlrIto, vlrIdUsr, vlrEnX, vlrDePMV) {
    // Variables para actualización de controles
    var seLogroActl = false;
    // Actualizar gráfico
    $("#ctrl_confortpersonal_grafica").CanvasJSChart().render();
    // Validar existencia de resultados
    if (resultDeItos.length > 0) {
        // Variables para recorrer iteraciones
        var numIto = 0,
            usrsCT = [];
        // Recorrer resultados de CONFORT TERMICO
        resultDeItos.forEach(function(itemResIto, indexF) {
            // Recuperar número de la iteración
            numIto = parseInt(itemResIto["iteracion"]);
            usrsCT = itemResIto["resultado"]["usuarios"] || [];
            // Recorrer resultados de CONFORT PERSONAL
            usrsCT.forEach(function(itemUsrs, indexF) {
                // Validar si la iteración coincide
                if ((numIto === vlrIto) && (itemUsrs["id_usuario"] === vlrIdUsr) && (parseFloat(itemUsrs["pmv"]) === vlrDePMV)) {
                    // Actualziar controles de CONFORT PERSONAL
                    ctrl_confortpersonal_iteracion.value = (numIto + 1);
                    ctrl_confortpersonal_sensacion.value = itemUsrs["sensacion"] || "";
                    ctrl_confortpersonal_estado.value = itemUsrs["confort"] || "";
                    ctrl_confortpersonal_valor.value = parseFloat(itemUsrs["pmv"]) + 3;
                    ctrl_confortpersonal_valor_numero.value = itemUsrs["pmv"] || "";
                    ctrl_confortpersonal_etiqueta.value = recuperarEtiquetaDeUsr(itemUsrs["id_usuario"]);
                    // Se avisa que se logro actualizar los controles
                    seLogroActl = true;
                }
            });
        });
    }
    // Validar si se logro actualizar los controles
    if (!seLogroActl) {
        // Actualziar controles de CONFORT GRUPAL
        ctrl_confortpersonal_iteracion.value = "";
        ctrl_confortpersonal_sensacion.value = "";
        ctrl_confortpersonal_estado.value = "";
        ctrl_confortpersonal_valor.value = 3;
        ctrl_confortpersonal_valor_numero.value = "0";
        ctrl_confortpersonal_etiqueta.value = "";
    }
}

// Function que actualiza información de controles relacionados al Confort Grupal
function actualizarInfoConfortGrupal(vlrIto, vlrEnX, vlrDePMV) {
    // Variables para actualización de controles
    var seLogroActl = false;
    // Actualizar gráfico
    $("#ctrl_confortgrupal_grafica").CanvasJSChart().render();
    // Validar existencia de resultados
    if (resultDeItos.length > 0) {
        // Variables para recorrer iteraciones
        var numIto = 0,
            resultDeCTG = {};
        // Recorrer resultados de CONFORT TERMICO
        resultDeItos.forEach(function(itemResIto, indexF) {
            // Recuperar número de la iteración
            numIto = parseInt(itemResIto["iteracion"]);
            resultDeCTG = itemResIto["resultado"]["confort_grupal"] || {};
            // Validar si la iteración coincide
            if ((numIto === vlrIto) && (parseFloat(resultDeCTG["valor_pmv"]) === vlrDePMV)) {
                // Actualziar controles de CONFORT GRUPAL
                ctrl_confortgrupal_iteracion.value = (numIto + 1);
                ctrl_confortgrupal_sensacion.value = resultDeCTG["valor_sensacion"] || "";
                ctrl_confortgrupal_estado.value = resultDeCTG["valor_confort"] || "";
                ctrl_confortgrupal_valor.value = parseFloat(resultDeCTG["valor_pmv"]) + 3;
                ctrl_confortgrupal_valor_numero.value = resultDeCTG["valor_pmv"] || "";
                // Se avisa que se logro actualizar los controles
                seLogroActl = true;
            }
        });
    }
    // Validar si se logro actualizar los controles
    if (!seLogroActl) {
        // Actualziar controles de CONFORT GRUPAL
        ctrl_confortgrupal_iteracion.value = "";
        ctrl_confortgrupal_sensacion.value = "";
        ctrl_confortgrupal_estado.value = "";
        ctrl_confortgrupal_valor.value = 3;
        ctrl_confortgrupal_valor_numero.value = "0";
    }
}

// Función que modifica configuración de control para calcular Confort Térmcio
function ajustarConfigDeControlDeCalculo () {
    // Validar si iniciar la solicitud de resultados
    if (configDeItos["iniciar"].toLowerCase() === "true") {
        // Actualizar leyenda de botón
        ctrl_btn_iniciarcalculo.innerText = "Reiniciar cálculo";
        // Deshabilitar control para iniciar calculo
        ctrl_btn_iniciarcalculo.setAttribute('disabled', 'true');
    } else {
        // Actualizar leyenda de botón
        ctrl_btn_iniciarcalculo.innerText = "Iniciar cálculo";
        // Habilitar control para iniciar calculo
        ctrl_btn_iniciarcalculo.removeAttribute('disabled');
    }
}

// Función que recupera la etiqueta de un usuario
function recuperarEtiquetaDeUsr (ideUsr) {
    // Variable de etiqueta
    var valEtiqueta = "";
    // Recorrer items de la tabla de usuarios
    tablasACTGU[tagUsuariosSel].forEach(function(itemOriUsr, indexG) {
        // Validar coincidencia con el usuario buscado
        if (itemOriUsr["id_usuario"] === ideUsr) {
            // Recuperar el valor de la etiqueta
            valEtiqueta = itemOriUsr["etiqueta_usuario"] || "";
        }
    });
    // Devolver etiqueta
    return valEtiqueta;
}

// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

// Función que reiniciar/inicia el calculo del Confort Térmico
function reiniciarCalculoDeCT () {
    // Calculart el tiempo de espera para recuperación de resultados
    var tiempoEspera = (parseFloat((configDeItos["duracion_de_iteracion"] || 0) + "") * 1000) + 2500;
    // Mecanismo que inicia proceso de solicitud de resultados de Confort
    var solicitarResultadosDeConfort = function () {
        // Cargar loader
        openLoader(function(closeLoader) {
            // Iniciar calculo de Confort luego de
            setTimeout(function () {
                // Cerrar loader
                closeLoader();
                // Recuperar resultados del calculo de Confort
                iniRecuperacionDeResultadosDeConfort(null);
            }, /* Se indican n tiempo de espera */ tiempoEspera);
        });
    };
    // Validar si iniciar la solicitud de resultados
    if (configDeItos["iniciar"].toLowerCase() === "false") {
        // Mecanismo que cambia el valor de configuración "iniciar" a TRUE
        cambiarIndicadorParaIniCalculoDeConfort("true",
            function () {
                // Actualizar configuración de control de calculo de Confort Termico
                // *** Cambia control a: REINICIAR y se de DESHABILITA
                ajustarConfigDeControlDeCalculo();
                // Inicia proceso de solicitud de resultados
                solicitarResultadosDeConfort();
            });
    } else {
        // Se DESHABILITA control para iniciar calculo
        ctrl_btn_iniciarcalculo.setAttribute('disabled', 'true');
        // Mecanismo que cambia el valor de configuración "iniciar" a FALSE
        cambiarIndicadorParaIniCalculoDeConfort("false",
            function () {
                // Cargar loader
                openLoader(function(closeLoader) {
                    // Iniciar calculo de Confort luego de
                    setTimeout(function () {
                        // Cerrar loader
                        closeLoader();
                        // Mecanismo que cambia el valor "iniciar" a TRUE
                        cambiarIndicadorParaIniCalculoDeConfort("true",
                            function () {
                                // Inicia proceso de solicitud de resultados
                                solicitarResultadosDeConfort();
                            });
                    }, /* Se indican n tiempo de espera */ tiempoEspera);
                });
            });
    }
}

// Función que recarga la configuración actual
function cambiarIndicadorParaIniCalculoDeConfort (iniCalCon, posClosLoader) {
    // Cargar loader
    openLoader(function(closeLoader) {
        // Ajustar indicador
        iniCalCon = iniCalCon.toLowerCase();
        // Asignar primer nivel de valores JSON
        configDeItos["iniciar"] = iniCalCon;
        // Invocación AJAX
        (function(ajaxRetorno) {
            // Variable de petición
            var miPeticionAjax = new XMLHttpRequest(),
                paramsURL = 'jsonData=' + JSON.stringify(configDeItos);
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
            // Función que hace actuar a este proceso como un hilo
            var detenerCambDeIndicadorDeIniCalculoDeConfort = function(mensajeError) {
                // Lanzar mensaje de error
                launchMessage("Error", (mensajeError ||
                    ("Ocurrido un error al intentar " + (iniCalCon === "false"? "iniciar": "reiniciar") + " el calculo")),
                    function(closeMessageBox) {
                        // Cerrar mensaje
                        closeMessageBox();
                        // Cerrar loader
                        closeLoader();
                        // debugger;
                        // -------------------------------------------------------------------
                        // -------------------------------------------------------------------
                        // Recargar la última configuración
                        reCargarConfiguracion(function() {// Cambiar configuración de control de calculo de Confort Termico
                            // Configura data de usuarios para CONFORT PERSONAL
                            configDataDeUsrs_paraCPyCG();
                            // Austar configuración de control iniciar
                            ajustarConfigDeControlDeCalculo();
                            // debugger;
                            // Actualizar información seleccionada en grafico de CONFORT GRUPAL
                            actualizarInfoConfortGrupal(0, markerCG_X.value, markerCG_Y.value);
                            // Actualizar información seleccionada en grafico de CONFORT PERSONAL
                            actualizarInfoConfortPersonal(0, 0, markerCP_X.value, markerCP_Y.value);
                        }, function () {
                            // Validar si iniciar la solicitud de resultados
                            if (configDeItos["iniciar"].toLowerCase() === "true") {
                                // Deshabilitar control para iniciar calculo
                                ctrl_btn_iniciarcalculo.removeAttribute('disabled');
                            }
                        });
                        // -------------------------------------------------------------------
                        // -------------------------------------------------------------------
                    });
            };
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
                        // Validar si existe una función para luego de cerrar el loader
                        if (posClosLoader) {
                            // Ejecuta función despues de cerrar el Loader
                            posClosLoader();
                        }
                    } else {
                        // Detener proceso de cambio del indicador debio a error
                        detenerCambDeIndicadorDeIniCalculoDeConfort(jsonData["mensaje"] || "");
                    }
                } else {
                    // Detener proceso de cambio del indicador debio a error
                    detenerCambDeIndicadorDeIniCalculoDeConfort("");
                }
            } else {
                // Detener proceso de cambio del indicador debio a error
                detenerCambDeIndicadorDeIniCalculoDeConfort("");
            }
        });
    });
}

// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

// Función que recupera los resultados del calculo de Confort
function iniRecuperacionDeResultadosDeConfort(metdToContinue) {
    // Invocación AJAX
    (function(funcRetorno) {
        // Variable de petición AJAX
        var miPeticionAjax = new XMLHttpRequest(),
            paramsURL = 'jsonData=' + JSON.stringify({"mensaje":""});
        // Definción de ejecusión AJAX
        miPeticionAjax.onload = function() {
            // Validar respuesta AJAX
            if (this.readyState == 4 && this.status == 200) {
                // Continuar con el proceso
                funcRetorno(this.responseText);
            }
        };
        // Solicitar recursos por AJAX
        miPeticionAjax.open(
            "POST",
            ('configuracion/resultados'), true
        );
        miPeticionAjax.setRequestHeader(
            'Content-type',
            'application/x-www-form-urlencoded'
        );
        miPeticionAjax.send(paramsURL);
    // Función de retorno AJAX
    })(function(rData) {
        // Función que hace actuar a este proceso como un hilo
        var reRecuperarResultadosDeConfort = function(mensajeError) {
            // Lanzar mensaje de error
            console.log(mensajeError || "");
            // Ejecutar luego de...
            setTimeout(function () {
                // Se ejecuta recuperador de resultados del calculo de Confort
                iniRecuperacionDeResultadosDeConfort(metdToContinue);
            }, /* Se indican 5 */ 5000);
        };
        // Validar datos de retorno
        if (rData && rData.trim()) {
            // Dividir resultados obtenidos
            var strResp = rData.trim().split("|");
            // Validar resultados obtenidos
            if (strResp.length > 1) {
                // Validar que la respuesta traiga algo
                if (strResp[0].trim()) {
                    // Convertir resultado a un objeto JSON
                    resultDeItos = JSON.parse(strResp[0]);
                    // Limpiar datos para grafica de CONFORT GRUPAL
                    // Mientras el arreglo tenga items
                    while(dataPointsCG.length > 0) {
                         // Sacar (Remover) item
                        dataPointsCG.pop();
                    }
                    // Limpiar datos para grafica de CONFORT PERSONAL
                    // Recorrer arreglo de datos de usuarios
                    for (var [ideUsr, dataUsr] of Object.entries(dataPointsCP)) {
                        // Mientras el arreglo tenga items
                        while(dataPointsCP[ideUsr].length > 0) {
                            // Sacar (Remover) item
                            dataPointsCP[ideUsr].pop();
                        }
                    }
                    // Variables para recorrer iteraciones
                    var numIto = 0,
                        usrsCT = [],
                        condiActs = {},
                        resultDeCTG = {};
                    // Variables que validan los marcadores
                    var itoCG = 0,
                        itoCP = 0,
                        ideUsrCP = "",
                        esValidoMarkCG = false,
                        esValidoMarkCP = false;
                    // Recorrer resultados de CONFORT
                    resultDeItos.forEach(function(itemResIto, indexF) {
                        // Recuperar número de la iteración
                        numIto = parseInt(itemResIto["iteracion"]);
                        usrsCT = itemResIto["resultado"]["usuarios"] || [];
                        condiActs = itemResIto["resultado"]["condiciones_enfrentadas"] || {};
                        resultDeCTG = itemResIto["resultado"]["confort_grupal"] || {};
                        // Armar datos de CONFORT GRUPAL
                        dataPointsCG.push({
                            // Agregar data de iteracion
                            iteracion: numIto,
                            // X: Considerando iteraciones de "0" a "n", se resta "1"
                            x: (numIto + 1),
                            // Y: Valor PMV del CONFORT GRUPAL
                            y: parseFloat(resultDeCTG["valor_pmv"])
                        });
                        // Validar marcador de CONFORT GRUPAL
                        if (!esValidoMarkCG &&
                           ((markerCG_X.value === (numIto + 1)) &&
                           (markerCG_Y.value === parseFloat(resultDeCTG["valor_pmv"])))) {
                            // Reservar iteración valida
                            itoCG = numIto;
                            // Validar marcador de CONFORT GRUPAL
                            esValidoMarkCG = true;
                        }
                        // Recorrer resultado de CONFORT PERSONAL
                        usrsCT.forEach(function(itemUsrs, indexF) {
                            // Armar datos de CONFORT PERSONAL
                            dataPointsCP[itemUsrs["id_usuario"]].push({
                                // Agregar data de usuario
                                id_usuario: itemUsrs["id_usuario"],
                                iteracion: numIto,
                                // X: Considerando iteraciones de "0" a "n", se resta "1"
                                x: (numIto + 1),
                                // Y: Valor PMV del CONFORT PERSONAL
                                y: parseFloat(itemUsrs["pmv"])
                            });
                            // Validar marcador de CONFORT PERSONAL
                            if (!esValidoMarkCP &&
                               ((markerCP_X.value === (numIto + 1)) &&
                               (markerCP_Y.value === parseFloat(itemUsrs["pmv"])))) {
                                // Reservar iteración valida
                                itoCP = numIto;
                                ideUsrCP = itemUsrs["id_usuario"];
                                // Validar marcador de CONFORT PERSONAL
                                esValidoMarkCP = true;
                            }
                        });
                    });
                    // Validar marcador de CONFORT GRUPAL
                    if (!esValidoMarkCG) {
                        // Recorrer resultados de CONFORT
                        resultDeItos.forEach(function(itemResIto, indexF) {
                            // Recuperar número de la iteración
                            numIto = parseInt(itemResIto["iteracion"]);
                            resultDeCTG = itemResIto["resultado"]["confort_grupal"] || {};
                            // Reservar iteración valida
                            // ** Siempre sera el ultimo
                            itoCG = numIto;
                            // Actualizar valor de marcadores
                            markerCG_X.value = (numIto + 1);
                            markerCG_Y.value = parseFloat(resultDeCTG["valor_pmv"]);
                        });
                    }
                    // Validar marcador de CONFORT PERSONAL
                    if (!esValidoMarkCP) {
                        // Recorrer resultado de CONFORT PERSONAL
                        usrsCT.forEach(function(itemUsrs, indexF) {
                            // Reservar iteración valida
                            // ** Siempre sera el ultimo
                            itoCP = numIto;
                            ideUsrCP = itemUsrs["id_usuario"];
                            // Validar marcador de CONFORT PERSONAL
                            markerCP_X.value = (numIto + 1);
                            markerCP_Y.value = parseFloat(itemUsrs["pmv"]);
                        });
                    }
                    // Actualizar información seleccionada en grafico de CONFORT GRUPAL
                    actualizarInfoConfortGrupal(itoCG, markerCG_X.value, markerCG_Y.value);
                    // Actualizar información seleccionada en grafico de CONFORT PERSONAL
                    actualizarInfoConfortPersonal(itoCP, ideUsrCP, markerCP_X.value, markerCP_Y.value);
                    // Vefiricar si ya se ha alcanzado el maximo de iteraciones definidas
                    if (resultDeItos.length < parseInt(configDeItos["numero_de_iteraciones"])) {
                        // Validar si existe un método de continuación
                        if (metdToContinue) {
                            // Se indica fin del proceso de recuperación
                            console.log(" >> Se recuperaron " + resultDeItos.length + " de " + configDeItos["numero_de_iteraciones"] + " resultados de iteración");
                            // Se ejecuta método de continuación
                            metdToContinue();
                        } else {
                            // ** Reejecuar función que recupera los resultados de Confort
                            reRecuperarResultadosDeConfort(" >> Resultados recuperados: " + resultDeItos.length + " de " + parseInt(configDeItos["numero_de_iteraciones"]));
                        }
                    } else {
                        // Se indica fin del proceso de recuperación
                        console.log(" >> Se recuperadon todos los resultados posibles de conseguir");
                        // ** Habilitar control para reiniciar calculo
                        ctrl_btn_iniciarcalculo.removeAttribute('disabled');
                    }
                } else {
                    // ** Reejecuar función que recupera los resultados de Confort
                    reRecuperarResultadosDeConfort("Ocurrido un error al intentar recuperar los resultados del calculo de Confort");
                }
            } else {
                // ** Reejecuar función que recupera los resultados de Confort
                reRecuperarResultadosDeConfort("Ocurrido un error al intentar recuperar los resultados del calculo de Confort");
            }
        } else {
            // ** Reejecuar función que recupera los resultados de Confort
            reRecuperarResultadosDeConfort("Ocurrido un error al intentar recuperar los resultados del calculo de Confort");
        }
    });
}

// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

// MAIN
window.addEventListener('load', function() {
    // Inicializar Elementos Foundation
    $(document).foundation();
    // Cargar tabla de grupos
    reCargarTablaGrupos(function () {
        // Cargar tabla de usuarios
        reCargarTablaUsuarios(function (){
            // Cargar la última configuración
            reCargarConfiguracion(function() {
                // -------------------------------------------------------------------
                // -------------------------------------------------------------------
                // Configura data de usuarios para CONFORT PERSONAL y GRUPAL
                configDataDeUsrs_paraCPyCG();
                // Construir grafica de CONFORT GRUPAL
                $("#ctrl_confortgrupal_grafica").CanvasJSChart({
                    animationEnabled: true,
                    exportEnabled: true,
                    theme: "light",
                    zoomEnabled: true, 
                    zoomType: "xy",
                    title:{
                        text: "Confort Térmico Grupal por Iteración"
                    },
                    axisY: {
                        // Grosor de la linea 0 en Y
                        lineThickness: 1.5,
                        // Color de la linea 0 en Y
                        lineColor: "#668C4E",
                        // Configuración del titulo y los valores en Y
                        titleFontColor: "#668C4E",
                        title: "Niveles de Confort Térmico",
                        suffix: " pmv",
                        interval:1,
                        includeZero: true,
                        valueFormatString: "",
                        // Define el formato de la linea punteada en Y
                        stripLines:[markerCG_Y]
                    },
                    axisX:{
                        // Grosor de la linea 0 en X
                        lineThickness: 1.5,
                        // Color de la linea 0 en X
                        lineColor: "#668C4E",
                        // Configuración del titulo y los valores en X
                        titleFontColor: "#668C4E",
                        title: "Iteraciones",
                        interval:1,
                        valueFormatString: "",
                        // Define el formato de la linea punteada en X
                        stripLines:[markerCG_X]
                    },
                    toolTip:{
                        shared: true
                    },
                    data: [{
                        // Mostrar leyenda inferior
                        showInLegend: false,
                        // Define el tipo de punto
                        markerType: "square",
                        // Degfine el tipo de grafico
                        type: "splineArea",
                        // Grosor del punto en el grafico y opacidad del relleno
                        markerSize: 15,
                        fillOpacity: 0.2,
                        lineThickness: 5.0,
                        // Define como se veran las cosas en el Tooltip
                        xValueFormatString: "# iteración",
                        yValueFormatString: "",
                        name: "Confort",
                        // Configuración de los puntos
                        color: "#668C4E",
                        dataPoints: dataPointsCG,
                        // Acción ejecutada al dar click sobre el punto
                        click: function(e){
                            // Recuperar el valor de los ejes marcados
                            markerCG_Y.value = e.dataPoint.y;
                            markerCG_X.value = e.dataPoint.x;
                            // Actualiza información de controles relacionados
                            actualizarInfoConfortGrupal(
                                e.dataPoint["iteracion"],
                                markerCG_X.value, markerCG_Y.value);
                        }
                    }]
                });
                // Construir grafica de CONFORT PERSONAL
                $("#ctrl_confortpersonal_grafica").CanvasJSChart({
                    animationEnabled: true,
                    exportEnabled: true,
                    theme: "light",
                    zoomEnabled: true, 
                    zoomType: "xy",
                    title:{
                        text: "Confort Térmico Personal por Iteración"
                    },
                    axisY: {
                        // Grosor de la linea 0 en Y
                        lineThickness: 1.5,
                        // Color de la linea 0 en Y
                        lineColor: "#668C4E",
                        // Configuración del titulo y los valores en Y
                        titleFontColor: "#668C4E",
                        title: "Niveles de Confort Térmico",
                        suffix: " pmv",
                        interval:1,
                        valueFormatString: "",
                        // Define el formato de la linea punteada en Y
                        stripLines:[markerCP_Y]
                    },
                    axisX:{
                        // Grosor de la linea 0 en X
                        lineThickness: 1.5,
                        // Color de la linea 0 en X
                        lineColor: "#668C4E",
                        // Configuración del titulo y los valores en X
                        titleFontColor: "#668C4E",
                        title: "Iteraciones",
                        interval:1,
                        valueFormatString: "",
                        // Define el formato de la linea punteada en X
                        stripLines:[markerCP_X]
                    },
                    toolTip:{
                        shared: true
                    },
                    legend: {
                        // Evento al dar click en linea de usuario
                        itemclick: function (e){
                            // Validar estado de la linea en la grafica
                            if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
                                // Ocultar linea en la grafica
                                e.dataSeries.visible = false;
                            } else{
                                // Hacer visible la linea en la grafica
                                e.dataSeries.visible = true;
                            }
                            // Redibujar grafica
                            $("#ctrl_confortpersonal_grafica").CanvasJSChart().render();
                        }
                    },
                    data: dataUsuariosCP
                });
                // -------------------------------------------------------------------
                // -------------------------------------------------------------------
                // Asginar función para reiniciar calculo de Confort Termico
                ctrl_btn_iniciarcalculo.addEventListener("click", function(sender) {
                    // Función para reiniciar calculo de Confort Termico
                    reiniciarCalculoDeCT();
                    // Detener proceso normal del botón
                    sender.preventDefault();
                });
                // Asginar función para abrir el detalle del resultado
                ctrl_confortgrupal_btn_detalle.addEventListener("click", function(sender) {
                    // Abrir modulo para ver detalle del resultado
                    abrir_modulo_resultado(resultDeItos);
                    // Detener proceso normal del botón
                    sender.preventDefault();
                });
                // Relacionar controles de ventana model a función para cerrarla
                ctrl_btn_resultado_regresar.addEventListener("click", function(sender) {
                    // Cerrar la ventana actual
                    cerrar_modulo_resultado();
                    // Detener proceso normal del botón
                    sender.preventDefault();
                });
                // Cambiar configuración de control de calculo de Confort Termico
                ajustarConfigDeControlDeCalculo();
                // -------------------------------------------------------------------
                // -------------------------------------------------------------------
            }, function () {
                // -------------------------------------------------------------------
                // -------------------------------------------------------------------
                // Validar si iniciar la solicitud de resultados
                if (configDeItos["iniciar"].toLowerCase() === "true") {
                    // Recuperar resultados del calculo de Confort
                    iniRecuperacionDeResultadosDeConfort(function () {
                        // ** Habilitar control para reiniciar calculo
                        ctrl_btn_iniciarcalculo.removeAttribute('disabled');
                    });
                }
                // -------------------------------------------------------------------
                // -------------------------------------------------------------------
            });
        });
    });
});