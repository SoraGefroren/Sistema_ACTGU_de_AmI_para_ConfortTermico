// Variables globales de la página
var tab_seleccionado = "panel_usuarios";

// Evento ejecutado al cambiar de tab
$('#tabs_usuarios_y_grupos').on('change.zf.tabs', function(event, tab) {
    // Generar referencia a icono de botones
    var icon_btn_ver = document.getElementById("icon_btn_ver"),
        icon_btn_agregar = document.getElementById("icon_btn_agregar"),
        icon_btn_editar = document.getElementById("icon_btn_editar"),
        icon_btn_elim = document.getElementById("icon_btn_eliminar"),
        ctrl_titulo = document.getElementById("ctrl_titulo");
    // Remover atributo clase
    icon_btn_ver.removeAttribute("class");
    icon_btn_agregar.removeAttribute("class");
    icon_btn_editar.removeAttribute("class");
    icon_btn_elim.removeAttribute("class");
    // Limpiar control de titulo
    clearContainer(ctrl_titulo);
    // Recuperar el ID del tab seleccionado
    var tabId = tab.attr("data-id");
    // Validar el tab
    if (tabId === "panel_usuarios") {
        // Agregar atributo de clase a botones
        icon_btn_ver.setAttribute("class", "fi-torso large");
        icon_btn_agregar.setAttribute("class", "fi-torso large");
        icon_btn_editar.setAttribute("class", "fi-torso large");
        icon_btn_elim.setAttribute("class", "fi-torso large");
        // Asignar tab seleccionado
        tab_seleccionado = tabId;
        // Asingar titulo a pagina
        ctrl_titulo.appendChild(document.createTextNode("Configuración de usuarios"));
    } else if (tabId === "panel_grupos") {
        // Agregar atributo de clase a botones
        icon_btn_ver.setAttribute("class", "fi-torsos-all large");
        icon_btn_agregar.setAttribute("class", "fi-torsos-all large");
        icon_btn_editar.setAttribute("class", "fi-torsos-all large");
        icon_btn_elim.setAttribute("class", "fi-torsos-all large");
        // Asignar tab seleccionado
        tab_seleccionado = tabId;
        // Asingar titulo a pagina
        ctrl_titulo.appendChild(document.createTextNode("Configuración de grupos"));
    }
});

// Función asincrona que recarga los valores enviados por los sensores, cada n tiempo
var reCargarValoresDeSensores = function() {
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
            ('sistema/datos_de_sensor'), true
        );
        miPeticionAjax.setRequestHeader(
            'Content-type',
            'application/x-www-form-urlencoded'
        );
        miPeticionAjax.send(paramsURL);
    // Función de retorno AJAX
    })(function(rData) {
        // Función que hace actuar a este proceso como un hilo
        var reSolicitarValoresDeSensores = function() {
            // Ejecutar luego de...
            setTimeout(reCargarValoresDeSensores, 5000);
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
                    var jsonData = JSON.parse("{" + strResp[0] + "}");
                    // Tratar data de usuarios
                    var aryData = jsonData["sensores_usuarios"];
                    // ----------------------------------------------
                    // ----------------------------------------------
                    // Esperar a que se pueda hacer uso de las tablas
                    while (!usarTablas) {
                        console.log("reCargarValoresDeSensores: " + usarTablas);
                    }
                    // Bloquear uso de las tablas
                    usarTablas = false;
                    // ----------------------------------------------
                    // ----------------------------------------------
                    // Validar datos de sensores de usuario
                    if (aryData) {
                        // ** Recorrer datos de sensor de usuario
                        aryData.forEach(function(itemUsr, indexUsr) {
                            // Validar existencia de la tabla de usuarios
                            if (tablasACTGU[tagUsuarios]) {
                                // Recorrer tabla de usuarios
                                tablasACTGU[tagUsuarios].forEach(function(itemUS, indxUS) {
                                    // Validar los ides de usuarios
                                    if (itemUS["id_usuario"] === itemUsr["id_usuario"]) {
                                        // Actualizar valor MET en tabla de usuarios
                                        tablasACTGU[tagUsuarios][indxUS]["met"] = itemUsr["met"] || "0.0";
                                    }
                                });
                            }
                        });
                    }
                    // Validar existencia de la tabla de usuarios
                    if (tablasACTGU[tagUsuarios]) {
                        // Recorrer tabla de usuarios
                        tablasACTGU[tagUsuarios].forEach(function(itemUsr, indexUsr) {
                            // Recuperar label para asignación de valor MET de un determinado usuario
                            var lblMet = document.getElementById("ctrl_tabla_usuarios").getElementsByClassName(classDeReferMET + itemUsr["id_usuario"]);
                            // Validar si se pudo recuperar la label de MET del usuario
                            if (lblMet.length > 0) {
                                // Actualizar valor contenido en la label de MET
                                lblMet[0].textContent = "";
                                lblMet[0].appendChild(document.createTextNode(parseFloat(itemUsr["met"] || "0.0").toFixed(2)));
                            }
                        });
                    }
                    // ----------------------------------------------
                    // ----------------------------------------------
                    // Permitir hacer uso de las tablas
                    usarTablas = true;
                    // ----------------------------------------------
                    // ----------------------------------------------
                    // ** Reejecuar función que carga valores ambientales
                    reSolicitarValoresDeSensores();
                } else {
                    // Lanzar mensaje de error
                    console.log("Ocurrido un error, en la consulta de los datos de los sensores");
                    // ** Reejecuar función que carga valores ambientales
                    reSolicitarValoresDeSensores();
                }
            } else {
                // Lanzar mensaje de error
                console.log("Ocurrido un error, en la consulta de los datos de los sensores");
                // ** Reejecuar función que carga valores ambientales
                reSolicitarValoresDeSensores();
            }
        } else {
            // Lanzar mensaje de error
            console.log("Ocurrido un error, en la consulta de los datos de los sensores");
            // ** Reejecuar función que carga valores ambientales
            reSolicitarValoresDeSensores();
        }
    });
};

// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv
// vvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvvv

// MAIN
window.addEventListener('load', function() {
    // Inicializar Elementos Foundation
    $(document).foundation();
    // -------------------------------------------------------------------
    // -------------------------------------------------------------------
    reiniciarDataDeTablas(function () {
        // Generar referencia a controles botones
        var ctrl_btn_ver = document.getElementById("ctrl_btn_ver"),
            ctrl_btn_agregar = document.getElementById("ctrl_btn_agregar"),
            ctrl_btn_editar = document.getElementById("ctrl_btn_editar"),
            ctrl_btn_elim = document.getElementById("ctrl_btn_eliminar");
        // Relacionar controles a funciones
        ctrl_btn_ver.addEventListener("click", function(sender) {
            // Validar el tab
            if (tab_seleccionado === "panel_usuarios") {
                // Abrir modulo de usuarios
                abrir_modulo_usuario("ver");
            } else {
                // Abrir modulo de grupos
                abrir_modulo_grupo("ver");
            }
            // Detener proceso normal del botón
            sender.preventDefault();
        });
        ctrl_btn_agregar.addEventListener("click", function(sender) {
            // Validar el tab
            if (tab_seleccionado === "panel_usuarios") {
                // Abrir modulo de usuarios
                abrir_modulo_usuario("nuevo");
            } else {
                // Abrir modulo de grupos
                abrir_modulo_grupo("nuevo");
            }
            // Detener proceso normal del botón
            sender.preventDefault();
        });
        ctrl_btn_editar.addEventListener("click", function(sender) {
            // Validar el tab
            if (tab_seleccionado === "panel_usuarios") {
                // Abrir modulo de usuarios
                abrir_modulo_usuario("editar");
            } else {
                // Abrir modulo de grupos
                abrir_modulo_grupo("editar");
            }
            // Detener proceso normal del botón
            sender.preventDefault();
        });
        ctrl_btn_elim.addEventListener("click", function(sender) {
            // Validar el tab
            if (tab_seleccionado === "panel_usuarios") {
                // Invoca proceso para borrar usuario
                eliminar_usuario();
            } else {
                // Invoca proceso para borrar grupo
                eliminar_grupo();
            }
            // Detener proceso normal del botón
            sender.preventDefault();
        });
        // USUARIOS
        // Recuperar instancia de controles del modulo
        var ctrl_btn_user_guardar = document.getElementById("ctrl_btn_user_guardar"),
            ctrl_btn_user_cancelar = document.getElementById("ctrl_btn_user_cancelar");
        // Relacionar botón de modulo con su función
        ctrl_btn_user_guardar.addEventListener("click", function(sender) {
            // Cerrar la ventana actual
            salvar_usuario();
            // Detener proceso normal del botón
            sender.preventDefault();
        });
        // Relacionar botón de modulo con su función
        ctrl_btn_user_cancelar.addEventListener("click", function(sender) {
            // Cerrar la ventana actual
            cerrar_modulo_usuario();
            // Detener proceso normal del botón
            sender.preventDefault();
        });
        // Recuperar instancia de controles del modulo
        var ctrl_input_user_altura = document.getElementById("ctrl_input_user_altura"),
            ctrl_input_user_peso = document.getElementById("ctrl_input_user_peso"),
            ctrl_sl_user_sexo = document.getElementById("ctrl_sl_user_sexo"),
            ctrl_sl_user_clo = document.getElementById("ctrl_sl_user_clo");
        // Configurar controles del modulo para calcular el valor BMI
        ctrl_input_user_altura.addEventListener('change', (sender) => {
            // Invoca función de cambio relacionada al valor BMI
            calcular_bmi();
        });
        ctrl_input_user_peso.addEventListener('change', (sender) => {
            // Invoca función de cambio relacionada al valor BMI
            calcular_bmi();
        });
        // Configurar controles del modulo para describir el equipamiento
        ctrl_sl_user_sexo.addEventListener('change', (sender) => {
            // Invoca función de cambio del equipamiento
            informar_sobre_equipamiento();
        });
        ctrl_sl_user_clo.addEventListener('change', (sender) => {
            // Invoca función de cambio del equipamiento
            informar_sobre_equipamiento();
        });
        // GRUPO
        // Recuperar instancia de controles del modulo
        var ctrl_btn_grupo_guardar = document.getElementById("ctrl_btn_grupo_guardar"),
            ctrl_btn_grupo_cancelar = document.getElementById("ctrl_btn_grupo_cancelar");
        // Relacionar botón de modulo con su función
        ctrl_btn_grupo_guardar.addEventListener("click", function(sender) {
            // Cerrar la ventana actual
            salvar_grupo();
            // Detener proceso normal del botón
            sender.preventDefault();
        });
        // Relacionar botón de modulo con su función
        ctrl_btn_grupo_cancelar.addEventListener("click", function(sender) {
            // Cerrar la ventana actual
            cerrar_modulo_grupo();
            // Detener proceso normal del botón
            sender.preventDefault();
        });
        // USUARIOS
        // Recuperar instancia de controles del modulo
        var ctrl_sld_user_tendencia_top = document.getElementById("ctrl_sld_user_tendencia_top"),
            ctrl_sld_user_tendencia_bot = document.getElementById("ctrl_sld_user_tendencia_bot");
        // Relacionar botón de modulo con su función
        ctrl_sld_user_tendencia_top.addEventListener("change", function(sender) {
            // Ajustar valor de control
            var valrTendDeCtrl = parseFloat(document.getElementById("ctrl_sld_user_tendencia_top").value || "50") - 50;
            // Validar como escribir valor
            if (valrTendDeCtrl < 0) {
                // Escribir valor de tendencia
                document.getElementById("ctrl_sld_user_tendencia_top_valor").value = "" + valrTendDeCtrl;
            } else {
                // Escribir valor de tendencia
                document.getElementById("ctrl_sld_user_tendencia_top_valor").value = "+" + valrTendDeCtrl;
            }
        });
        // Relacionar botón de modulo con su función
        ctrl_sld_user_tendencia_bot.addEventListener("change", function(sender) {
            // Ajustar valor de control
            var valrTendDeCtrl = parseFloat(document.getElementById("ctrl_sld_user_tendencia_bot").value || "50") - 50;
            // Validar como escribir valor
            if (valrTendDeCtrl < 0) {
                // Escribir valor de tendencia
                document.getElementById("ctrl_sld_user_tendencia_bot_valor").value = "" + valrTendDeCtrl;
            } else {
                // Escribir valor de tendencia
                document.getElementById("ctrl_sld_user_tendencia_bot_valor").value = "+" + valrTendDeCtrl;
            }
        });
            
        // SENSORES
        // Solicitar valores MET de usuario
        reCargarValoresDeSensores();
    });
    // -------------------------------------------------------------------
    // -------------------------------------------------------------------
});