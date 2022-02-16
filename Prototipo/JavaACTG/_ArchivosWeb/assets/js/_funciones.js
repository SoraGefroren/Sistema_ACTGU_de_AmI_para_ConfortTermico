// **** Funciones globales
// Función que pone un panel de carga sobre la interfaz
function openLoader(retorno) {
    // Recuperar contenedor
    var modalLoader = document.getElementById("my-modal-loader"),
        bodyContent = null;
    // Si no existe el contenedor de errores
    if (!modalLoader) {
        // Buscar body
        bodyContent = document.getElementsByTagName("body");
        // Preparar elementos
        var divPricipal = document.createElement("div"),
            divDeApy = document.createElement("div"),
            img = document.createElement("img"),
            h3 = document.createElement("h3");
        // Asignar atributos
        divPricipal.setAttribute('class', 'tiny reveal callout actgu-style-modal-loader');
        divPricipal.setAttribute('data-close-on-click', 'false'); // Para evitar su cierre
        divPricipal.setAttribute('data-close-on-esc', 'false'); // Para evitar su cierre
        divPricipal.setAttribute('id', 'my-modal-loader');
        divPricipal.setAttribute('data-reveal', '');
        // Asignar atributos
        divDeApy.setAttribute('class', 'actgu-align-center');
        // Establecer imagen
        img.setAttribute('class', 'actgu-align-center actgu-style-img-loader');
        img.setAttribute('src', 'assets/img/Loader.png');
        // Establecer leyenda
        h3.appendChild(document.createTextNode("Cargando..."));
        h3.setAttribute('class', 'actgu-align-center');
        // Agregar contenido al div de apoyo
        divDeApy.appendChild(img);
        divDeApy.appendChild(h3);
        // Agregar contenido al div pricipal
        divPricipal.appendChild(divDeApy);
        // Agregar elementos al contenedor
        bodyContent[0].appendChild(divPricipal);
        // Preparación de elemento modal
        $(divPricipal).foundation();
        modalLoader = divPricipal;
    }
    // Revelar elemento modal
    $(modalLoader).foundation('open');
    // Función de retorno
    if (retorno) {
        retorno(function() {
            $(modalLoader).foundation('close');
            // Borrar evidencia de haber existido
            padreDeModalL = modalLoader.parentNode;
            padreDeModalL.remove(padreDeModalL);
        });
    } else {
        return function() {
            $(modalLoader).foundation('close');
            // Borrar evidencia de haber existido
            padreDeModalL = modalLoader.parentNode;
            padreDeModalL.remove(padreDeModalL);
        };
    }
}
// Función que lanza un mensaje de: Error, Exito, Pregunta o Confirmacion
function launchMessage(tipo, mensaje, retorno) {
    // Recuperar contenedor
    var modalMessage = document.getElementById("my-modal-message"),
        axContent = null;
    // Si no existe el contenedor de errores
    if (!modalMessage) {
        // Buscar body
        axContent = document.getElementsByTagName("body");
        // Preparar elementos
        var divPricipal = document.createElement("div"),
            divDeApy = document.createElement("div"),
            divBtns = document.createElement("div"),
            h3 = document.createElement("h3"),
            p = document.createElement("p");
        // Asignar diseño
        if (tipo === "Error") {
            divPricipal.setAttribute('class', 'reveal callout alert');
        } else if (tipo === "Exito") {
            divPricipal.setAttribute('class', 'reveal callout success');
        } else if (tipo === "Pregunta") {
            divPricipal.setAttribute('class', 'reveal callout warning');
        } else if (tipo === "Confirmacion") {
            divPricipal.setAttribute('class', 'reveal callout primary');
        }
        // Asignar atributos comunes al contenendor principal
        divPricipal.setAttribute('data-close-on-click', 'false'); // Para evitar su cierre
        divPricipal.setAttribute('data-close-on-esc', 'false'); // Para evitar su cierre
        divPricipal.setAttribute('id', 'my-modal-message');
        divPricipal.setAttribute('data-reveal', '');
        // Asignar atributos comunes al contenendor de apoyo
        divDeApy.setAttribute('class', 'actgu-align-center');
        divBtns.setAttribute('class', 'actgu-align-center');
        // Asignar leyenda principal
        if (tipo === "Error") {
            h3.appendChild(document.createTextNode("Error."));
        } else if (tipo === "Exito") {
            h3.appendChild(document.createTextNode("Éxito."));
        } else if (tipo === "Pregunta") {
            h3.appendChild(document.createTextNode("Pregunta."));
        } else if (tipo === "Confirmacion") {
            h3.appendChild(document.createTextNode("Confirme la acción por favor."));
        }
        // Asignar atributos comunes al mensaje
        p.setAttribute('class', 'lead');
        p.setAttribute('id', 'my-modal-message-text');
        // Adjuntar elementos
        divDeApy.appendChild(p);
        divPricipal.appendChild(h3);
        divPricipal.appendChild(divDeApy);
        // Agregar botones
        if (tipo === "Error") {
            // Variable de botón
            var btnC = document.createElement("button");
            // Configurar botón
            btnC.setAttribute('class', 'button alert');
            btnC.setAttribute('id', 'my-modal-message-btn-c');
            btnC.appendChild(document.createTextNode("Continuar"));
            // Agregar botón a Div de Botones
            divBtns.appendChild(btnC);
        } else if (tipo === "Exito") {
            // Variable de botón
            var btnC = document.createElement("button");
            // Configurar botón
            btnC.setAttribute('class', 'button success');
            btnC.setAttribute('id', 'my-modal-message-btn-c');
            btnC.appendChild(document.createTextNode("Continuar"));
            // Agregar botón a Div de Botones
            divBtns.appendChild(btnC);
        } else if (tipo === "Pregunta") {
            // Variables botón
            var btnA = document.createElement("button"),
                btnC = document.createElement("button");
            // Configurar botón
            btnA.setAttribute('class', 'button success expanded');
            btnA.setAttribute('id', 'my-modal-message-btn-a');
            btnA.appendChild(document.createTextNode("Aceptar"));
            btnC.setAttribute('class', 'button alert expanded');
            btnC.setAttribute('id', 'my-modal-message-btn-c');
            btnC.appendChild(document.createTextNode("Cancelar"));
            // Div's adicionales para botones
            var div_nA = document.createElement("div"),
                div_nB = document.createElement("div"),
                div_nC = document.createElement("div"),
                div_nD_1 = document.createElement("div"),
                div_nD_2 = document.createElement("div"),
                div_nD_3 = document.createElement("div"),
                div_nE = document.createElement("div"),
                div_nF_1 = document.createElement("div"),
                div_nF_2 = document.createElement("div"),
                div_nF_3 = document.createElement("div");
            // Asignar atributos a Div's adicionales de botones
            div_nA.setAttribute('class', 'grid-x grid-padding-x');
            div_nB.setAttribute('class', 'cell small-12 align-self-middle');
            div_nC.setAttribute('class', 'grid-x');
            div_nD_1.setAttribute('class', 'small-3 medium-3');
            div_nD_2.setAttribute('class', 'small-6 medium-6');
            div_nD_3.setAttribute('class', 'small-3 medium-3');
            div_nE.setAttribute('class', 'grid-x');
            div_nF_1.setAttribute('class', 'small-5 medium-5');
            div_nF_2.setAttribute('class', 'small-2 medium-2');
            div_nF_3.setAttribute('class', 'small-5 medium-5');
            // Agregar botónes a Div de Botones
            div_nF_1.appendChild(btnA);
            div_nF_3.appendChild(btnC);
            // Agregar ordenar divs
            div_nE.appendChild(div_nF_1);
            div_nE.appendChild(div_nF_2);
            div_nE.appendChild(div_nF_3);
            div_nD_2.appendChild(div_nE);
            div_nC.appendChild(div_nD_1);
            div_nC.appendChild(div_nD_2);
            div_nC.appendChild(div_nD_3);
            div_nB.appendChild(div_nC);
            div_nA.appendChild(div_nB);
            divBtns.appendChild(div_nA);
        } else if (tipo === "Confirmacion") {
            // Variables botón
            var btnA = document.createElement("button"),
                btnC = document.createElement("button");
            // Configurar botón
            btnA.setAttribute('class', 'button success expanded');
            btnA.setAttribute('id', 'my-modal-message-btn-a');
            btnA.appendChild(document.createTextNode("Aceptar"));
            btnC.setAttribute('class', 'button alert expanded');
            btnC.setAttribute('id', 'my-modal-message-btn-c');
            btnC.appendChild(document.createTextNode("Cancelar"));
            // Div's adicionales para botones
            var div_nA = document.createElement("div"),
                div_nB = document.createElement("div"),
                div_nC = document.createElement("div"),
                div_nD_1 = document.createElement("div"),
                div_nD_2 = document.createElement("div"),
                div_nD_3 = document.createElement("div"),
                div_nE = document.createElement("div"),
                div_nF_1 = document.createElement("div"),
                div_nF_2 = document.createElement("div"),
                div_nF_3 = document.createElement("div");
            // Asignar atributos a Div's adicionales de botones
            div_nA.setAttribute('class', 'grid-x grid-padding-x');
            div_nB.setAttribute('class', 'cell small-12 align-self-middle');
            div_nC.setAttribute('class', 'grid-x');
            div_nD_1.setAttribute('class', 'small-3 medium-3');
            div_nD_2.setAttribute('class', 'small-6 medium-6');
            div_nD_3.setAttribute('class', 'small-3 medium-3');
            div_nE.setAttribute('class', 'grid-x');
            div_nF_1.setAttribute('class', 'small-5 medium-5');
            div_nF_2.setAttribute('class', 'small-2 medium-2');
            div_nF_3.setAttribute('class', 'small-5 medium-5');
            // Agregar botónes a Div de Botones
            div_nF_1.appendChild(btnA);
            div_nF_3.appendChild(btnC);
            // Agregar ordenar divs
            div_nE.appendChild(div_nF_1);
            div_nE.appendChild(div_nF_2);
            div_nE.appendChild(div_nF_3);
            div_nD_2.appendChild(div_nE);
            div_nC.appendChild(div_nD_1);
            div_nC.appendChild(div_nD_2);
            div_nC.appendChild(div_nD_3);
            div_nB.appendChild(div_nC);
            div_nA.appendChild(div_nB);
            divBtns.appendChild(div_nA);
        }
        // Adjuntar elementos
        divPricipal.appendChild(divBtns);
        // Agregar elementos al contenedor
        axContent[0].appendChild(divPricipal);
        // Preparación de elemento modal
        $(document).foundation();
        modalMessage = divPricipal;
    }
    // Recuperar espacio de texto
    axContent = document.getElementById("my-modal-message-text");
    // Limpiar espacio de texto
    while (axContent.lastChild) {
        axContent.removeChild(axContent.lastChild);
    }
    while (mensaje.indexOf("\n") >= 0) {
        mensaje = mensaje.replace("\n", "<br/>");
    }
    axContent.innerHTML = (mensaje || '');
    // Revelar elemento modal
    $(modalMessage).foundation('open');
    // Asignar función de retorno
    if (tipo === "Error") {
        // Variables botón
        var botonC = document.getElementById("my-modal-message-btn-c");
        // Asignar evento
        botonC.addEventListener('click', function(sender) {
            sender.preventDefault();
            retorno(function() {
                $(modalMessage).foundation('close');
                // Borrar evidencia de haber existido
                padreDeModalM = modalMessage.parentNode;
                padreDeModalM.remove(padreDeModalM);
            });
        });
    } else if (tipo === "Exito") {
        // Variables botón
        var botonC = document.getElementById("my-modal-message-btn-c");
        // Asignar evento
        botonC.addEventListener('click', function(sender) {
            sender.preventDefault();
            retorno(function() {
                $(modalMessage).foundation('close');
                // Borrar evidencia de haber existido
                padreDeModalM = modalMessage.parentNode;
                padreDeModalM.remove(padreDeModalM);
            });
        });
    } else if (tipo === "Pregunta") {
        // Variables botón
        var botonA = document.getElementById("my-modal-message-btn-a"),
            botonC = document.getElementById("my-modal-message-btn-c");
        // Asignar eventos
        botonA.addEventListener('click', function(sender) {
            sender.preventDefault();
            retorno(true, function() {
                $(modalMessage).foundation('close');
                // Borrar evidencia de haber existido
                padreDeModalM = modalMessage.parentNode;
                padreDeModalM.remove(padreDeModalM);
            });
        });
        botonC.addEventListener('click', function(sender) {
            sender.preventDefault();
            retorno(false, function() {
                $(modalMessage).foundation('close');
                // Borrar evidencia de haber existido
                padreDeModalM = modalMessage.parentNode;
                padreDeModalM.remove(padreDeModalM);
            });
        });
    } else if (tipo === "Confirmacion") {
        // Variables botón
        var botonA = document.getElementById("my-modal-message-btn-a"),
            botonC = document.getElementById("my-modal-message-btn-c");
        // Asignar eventos
        botonA.addEventListener('click', function(sender) {
            sender.preventDefault();
            retorno(true, function() {
                $(modalMessage).foundation('close');
                // Borrar evidencia de haber existido
                padreDeModalM = modalMessage.parentNode;
                padreDeModalM.remove(padreDeModalM);
            });
        });
        botonC.addEventListener('click', function(sender) {
            sender.preventDefault();
            retorno(false, function() {
                $(modalMessage).foundation('close');
                // Borrar evidencia de haber existido
                padreDeModalM = modalMessage.parentNode;
                padreDeModalM.remove(padreDeModalM);
            });
        });
    }
}
// Función que limpía el contenido de un contenedor
function clearContainer(contenedor) {
    // Tomar elementos hijos
    var last_item = contenedor.lastElementChild;
    // Validar si hay elementos hijos
    while (last_item) {
        // Borrar elemento hijo
        contenedor.removeChild(last_item);
        last_item = contenedor.lastElementChild;
    }
    // Limpíar contenedor
    contenedor.textContent = "";
}
// Función que genera una cadena separada por "," e "y", de un arreglo
function makeTextFromArray(arrayObj, fieldArry) {
    // Recuperar tamaño de arreglo
    var tamArry = arrayObj.length,
        texArry = ""; 
    // Recorrer elementos del arreglo
    for (var i = 0; i < tamArry; i++) {
        // Valida si usar o no campo
        if (fieldArry) {
            // Concatenar el contenido del arreglo
            texArry += arrayObj[i][fieldArry];
        } else {
            // Concatenar el contenido del arreglo
            texArry += arrayObj[i];
        }
        // Valida si usar o no una ","
        if ((i + 2) < tamArry) {
            // Concatenar una ","
            texArry += ", ";
        } else {
            // Valida si usar o no una ","
            if ((i + 1) < tamArry) {
                // Concatenar una "y"
                texArry += " y ";
            }
        }
    }
    // Devolver texto
    return texArry;
}

