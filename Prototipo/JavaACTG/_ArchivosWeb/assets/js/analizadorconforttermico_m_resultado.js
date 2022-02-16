
                    
// Función para abrir una ventana
function abrir_modulo_resultado(results) {
    // Recupera el valor de los controles
    var vlr_ito = parseInt(document.getElementById('ctrl_confortgrupal_iteracion').value + ""),
        vlr_valor = parseFloat(document.getElementById('ctrl_confortgrupal_valor').value + "");

    // Validar contenido del control de iteración
    if (!vlr_ito) {
        // Lanzar mensaje de error
        launchMessage("Error", "Por favor, seleccioné alguna iteración",
            function(closeMessageBox) {
                // Cerrar caja del mensaje
                closeMessageBox();
            });
        // Salir y no mostrar ventana
        return;
    }
    
    // Recuperar instancia del control titulo
    var ctrl_titulo_grupo = document.getElementById('ctrl_titulo_grupo');
    // Limpiar control de titulo
    clearContainer(ctrl_titulo_grupo);
    // Asingar titulo al modulo
    ctrl_titulo_grupo.appendChild(document.createTextNode("Detalle de la iteración \"" + vlr_ito + "\""));
    
    // Ajustar iteracion
    vlr_ito -= 1;
    vlr_valor -= 3;
    
    // Variables para recuperar la data de Iteración
    var seLogroActl = false,
        resuConGrp = {},
        conEnfs = {};

    // Validar existencia de resultados
    if (results.length > 0) {
        // Variables para recorrer iteraciones
        var numIto = 0,
            reDeCTG = {};
        // Recorrer resultados de CONFORT TERMICO
        results.forEach(function(itemResIto, indexF) {
            // Recuperar número de la iteración
            numIto = parseInt(itemResIto["iteracion"]);
            reDeCTG = itemResIto["resultado"]["confort_grupal"] || {};
            // Validar si la iteración coincide
            if ((numIto === vlr_ito) && (parseFloat(reDeCTG["valor_pmv"]) === vlr_valor)) {
                // Reservar data de CONFORT GRUPAL
                resuConGrp = reDeCTG;
                conEnfs = itemResIto["resultado"]["condiciones_enfrentadas"] || {};
                // Se avisa que se logro actualizar los controles
                seLogroActl = true;
            }
        });
    }
    
    // Validar si se logro actualizar los controles
    if (!seLogroActl) {
        // Lanzar mensaje de error
        launchMessage("Error", "No se consiguió recuperar información sobre la iteración seleccionada",
            function(closeMessageBox) {
                // Cerrar caja del mensaje
                closeMessageBox();
            });
        // Salir y no mostrar ventana
        return;
    }
    
    // Recuperar data de configuración
    var resOjConfig = resuConGrp["actgu_configuracion"];
    
    // Generar referencia a controles botones de la ventana
    var ctrl_resultado_ctg_valor = document.getElementById("ctrl_input_resultado_ctg_valor"),
        ctrl_resultado_ctg_descrip = document.getElementById("ctrl_input_resultado_ctg_descrip"),
        
        ctrl_resultado_enfrentada_temperatura = document.getElementById("ctrl_input_resultado_enfrentada_temperatura"),
        ctrl_resultado_enfrentada_humedad = document.getElementById("ctrl_input_resultado_enfrentada_humedad"),
        ctrl_resultado_enfrentada_gas = document.getElementById("ctrl_input_resultado_enfrentada_gas"),
        ctrl_resultado_enfrentada_vel_aire = document.getElementById("ctrl_input_resultado_enfrentada_vel_aire"),
        
        ctrl_resultado_esperada_temperatura = document.getElementById("ctrl_input_resultado_esperada_temperatura"),
        ctrl_resultado_esperada_humedad = document.getElementById("ctrl_input_resultado_esperada_humedad"),
        ctrl_resultado_esperada_gas = document.getElementById("ctrl_input_resultado_esperada_gas"),
        ctrl_resultado_esperada_vel_aire = document.getElementById("ctrl_input_resultado_esperada_vel_aire");
    
    // Asignar valor PMV en control
    ctrl_resultado_ctg_valor.value = resuConGrp["valor_pmv"] || "";
    
    // Limpiar control de descripción
    clearContainer(ctrl_resultado_ctg_descrip);
        
    // Validar existencia de descripción en resultado de iteración
    if (resOjConfig) {    
        // Asignar descripción en control
        ctrl_resultado_ctg_descrip.textContent = resOjConfig["descripcion"] || "";
    } else {
        // Asignar descripción en control
        ctrl_resultado_ctg_descrip.textContent = "";
    }
    
    // Actualizar valor de condiciones enfrentadas
    ctrl_resultado_enfrentada_temperatura.value = conEnfs[cmpTempera] || 0;
    ctrl_resultado_enfrentada_humedad.value = conEnfs[cmpHumedad] || 0;
    ctrl_resultado_enfrentada_gas.value = conEnfs[cmpConcGas] || 0;
    ctrl_resultado_enfrentada_vel_aire.value = conEnfs[cmpVelAire] || 0;
    
    // Validar existencia de descripción en resultado de iteración
    if (resOjConfig && resOjConfig["condiciones_buscadas"] && (resOjConfig["condiciones_buscadas"]["cambio"] === "SI")) {
        // Actualizar valor de condiciones buscadas
        ctrl_resultado_esperada_temperatura.value = resOjConfig["condiciones_buscadas"][cmpTempera] || 0;
        ctrl_resultado_esperada_humedad.value = resOjConfig["condiciones_buscadas"][cmpHumedad] || 0;
        ctrl_resultado_esperada_gas.value = resOjConfig["condiciones_buscadas"][cmpConcGas] || 0;
        ctrl_resultado_esperada_vel_aire.value = resOjConfig["condiciones_buscadas"][cmpVelAire] || 0;
    } else {
        // Actualizar valor de condiciones buscadas
         ctrl_resultado_esperada_temperatura.value = conEnfs[cmpTempera] || 0;
        ctrl_resultado_esperada_humedad.value = conEnfs[cmpHumedad] || 0;
        ctrl_resultado_esperada_gas.value = conEnfs[cmpConcGas] || 0;
        ctrl_resultado_esperada_vel_aire.value = conEnfs[cmpVelAire] || 0;
    }
    
    // Configurar tabla de actuadores
    reconfigurarTablaDeActuadores(resOjConfig["acciones"] || "");
    
    
    
    // Recuperar instancia de la ventana
    var modalVtn = document.getElementById("vtn_modal_resultado");
    // Abrir ventana modal
    $(modalVtn).foundation('open');
    return;
}

// Función que configurar tabla de actuadores
function reconfigurarTablaDeActuadores(accConfgActs) {
    // Generar instancia de tabla
    var tabActual = document.getElementById("ctrl_tabla_resultado_confighvacs"),
        aryFilas = [];
    // Limpiar contenido en tabla
    clearContainer(tabActual);
    
    // Dividir data
    var aryStrDAT = accConfgActs.split("*"),
        numberAryDAT = aryStrDAT.length,
        cnfgDiv = null;
    
    // Recorrer data
    for (var i = 0; i < numberAryDAT; i++) {
        
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
        divCon_01.setAttribute('class', 'small-5 align-self-middle');
        lblCon_01.setAttribute('class', 'text-center middle actgu-ctrl-label');
        
        // Variables para la construcción de labels web
        var divCon_02 = document.createElement("div"),
            lblCon_02 = document.createElement("label");
        // Construcción de labels web
        divCon_02.setAttribute('class', 'small-3 align-self-middle');
        lblCon_02.setAttribute('class', 'text-center middle actgu-ctrl-label');
        
        // Variables para la construcción de labels web
        var divCon_03 = document.createElement("div"),
            lblCon_03 = document.createElement("label");
        // Construcción de labels web
        divCon_03.setAttribute('class', 'small-4 align-self-middle text-center');
        lblCon_03.setAttribute('class', 'text-center middle actgu-ctrl-label');
        
        // MSJ = "id:ideAct, edo:a, tipo:tipoAct, temp:nwTemp, aire:nwVelAir, hum:nwHume, gas:nwCGas"
        cnfgDiv = aryStrDAT[i].split(",");
        
        // >> De acuerdo al tipo de actuador
        switch (cnfgDiv[2].split(":")[1].toLowerCase()){
            
            case "aire acondicionado":
                // Asigna contenido a label
                lblCon_01.appendChild(document.createTextNode("Aire acondicionado"));
                // Agregar label a su div
                divCon_01.appendChild(lblCon_01);

                // Validar icono a mostrar
                if (cnfgDiv[1].split(":")[1].toLowerCase() === "a") {
                    // Asigna contenido a label
                    lblCon_02.appendChild(document.createTextNode("Apagado"));
                    // Asigna contenido a label
                    // >> Recuperar y actualizar valor configurado de temperatura
                    lblCon_03.appendChild(document.createTextNode("0.00°C"));
                } else {
                    // Asigna contenido a label
                    lblCon_02.appendChild(document.createTextNode("Encendido"));
                    // Asigna contenido a label
                    // >> Recuperar y actualizar valor configurado de temperatura
                    lblCon_03.appendChild(document.createTextNode( parseFloat(cnfgDiv[3].split(":")[1].toLowerCase() || "0").toFixed(2) + "°C"));
                }
                // Agregar label a su div
                divCon_02.appendChild(lblCon_02);
                // Agregar label a su div
                divCon_03.appendChild(lblCon_03);
                break;
            case "calefactor":
                // Asigna contenido a label
                lblCon_01.appendChild(document.createTextNode("Calefactor"));
                // Agregar label a su div
                divCon_01.appendChild(lblCon_01);

                // Validar icono a mostrar
                if (cnfgDiv[1].split(":")[1].toLowerCase() === "a") {
                    // Asigna contenido a label
                    lblCon_02.appendChild(document.createTextNode("Apagado"));
                    // Asigna contenido a label
                    // >> Recuperar y actualizar valor configurado de temperatura
                    lblCon_03.appendChild(document.createTextNode("0.00°C"));
                } else {
                    // Asigna contenido a label
                    lblCon_02.appendChild(document.createTextNode("Encendido"));
                    // Asigna contenido a label
                    // >> Recuperar y actualizar valor configurado de temperatura
                    lblCon_03.appendChild(document.createTextNode( parseFloat(cnfgDiv[3].split(":")[1].toLowerCase() || "0").toFixed(2) + "°C"));
                }
                // Agregar label a su div
                divCon_02.appendChild(lblCon_02);
                // Agregar label a su div
                divCon_03.appendChild(lblCon_03);
                
                break;
                
            case "ventilador":
                // Asigna contenido a label
                lblCon_01.appendChild(document.createTextNode("Ventilador"));
                // Agregar label a su div
                divCon_01.appendChild(lblCon_01);

                // Validar icono a mostrar
                if (cnfgDiv[1].split(":")[1].toLowerCase() === "a") {
                    // Asigna contenido a label
                    lblCon_02.appendChild(document.createTextNode("Apagado"));
                    // Asigna contenido a label
                    // >> Recuperar y actualizar valor configurado de vel_aire
                    // lblCon_03.appendChild(document.createTextNode("0.00 m/s"));
                    lblCon_03.appendChild(document.createTextNode(""));
                } else {
                    // Asigna contenido a label
                    lblCon_02.appendChild(document.createTextNode("Encendido"));
                    // Asigna contenido a label
                    // >> Recuperar y actualizar valor configurado de vel_aire
                    // lblCon_03.appendChild(document.createTextNode( parseFloat(cnfgDiv[4].split(":")[1].toLowerCase() || "0").toFixed(2) + " m/s"));
                    lblCon_03.appendChild(document.createTextNode(""));
                }
                // Agregar label a su div
                divCon_02.appendChild(lblCon_02);
                // Agregar label a su div
                divCon_03.appendChild(lblCon_03);
                
                break;
                
            case "ventana": case "ventanas":
                // Asigna contenido a label
                lblCon_01.appendChild(document.createTextNode("Ventanas"));
                // Agregar label a su div
                divCon_01.appendChild(lblCon_01);

                // Validar icono a mostrar
                if (cnfgDiv[1].split(":")[1].toLowerCase() === "a") {
                    // Asigna contenido a label
                    lblCon_02.appendChild(document.createTextNode("Cerradas"));
                } else {
                    // Asigna contenido a label
                    lblCon_02.appendChild(document.createTextNode("Abiertas"));
                }
                // Agregar label a su div
                divCon_02.appendChild(lblCon_02);
                
                // Asigna contenido a label
                lblCon_03.appendChild(document.createTextNode(""));
                // Agregar label a su div
                divCon_03.appendChild(lblCon_03);
                
                break;
                
            case "puerta":
                // Asigna contenido a label
                lblCon_01.appendChild(document.createTextNode("Puerta"));
                // Agregar label a su div
                divCon_01.appendChild(lblCon_01);

                // Validar icono a mostrar
                if (cnfgDiv[1].split(":")[1].toLowerCase() === "a") {
                    // Asigna contenido a label
                    lblCon_02.appendChild(document.createTextNode("Cerrada"));
                } else {
                    // Asigna contenido a label
                    lblCon_02.appendChild(document.createTextNode("Abierta"));
                }
                // Agregar label a su div
                divCon_02.appendChild(lblCon_02);
                
                // Asigna contenido a label
                lblCon_03.appendChild(document.createTextNode(""));
                // Agregar label a su div
                divCon_03.appendChild(lblCon_03);
                
                break;
        }
        
        // Agregar divs de labels a div grid
        divGrid.appendChild(divCon_01);
        divGrid.appendChild(divCon_02);
        divGrid.appendChild(divCon_03);

        // Agregar div grid a div fila
        divFila.appendChild(divGrid);
        
        // >> De acuerdo al tipo de actuador
        switch (cnfgDiv[2].split(":")[1].toLowerCase()){
            case "aire acondicionado":
            case "calefactor":
                // Agregar div fila en arreglo de filas
                aryFilas.push(divFila);
                break;
        }
    }
    
    // Recorrer filas contruidas
    aryFilas.forEach(function(filaCon, indexF) {
        // Agregar fila a tabla
        tabActual.appendChild(filaCon);
    });
}

// Se ejecutar al dar click sobre cancelar y cierra el modulo (ventana) actual
function cerrar_modulo_resultado() {
    // Recuperar instancia de la ventana
    var modalVtn = document.getElementById("vtn_modal_resultado");
    // cerrar ventana modal
    $(modalVtn).foundation('close');
}