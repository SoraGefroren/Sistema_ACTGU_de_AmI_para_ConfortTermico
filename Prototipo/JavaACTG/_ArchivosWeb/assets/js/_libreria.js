// Convertir string a fecha
function strToDate(str, separador, invert) {
    str = str.split(separador || '/');
    str[1] = parseInt(str[1]) - 1;
    var date = null;
    if (invert) {
        date = new Date(str[0], str[1], str[2]);
    } else {
        date = new Date(str[2], str[1], str[0]);
    }
    return date;
}

// Cambiar formato string fecha
function formatStrDate(str, separOld, separNew, invert) {
    str = str.split(separOld || '/');
    var date = null;
    if (invert) {
        date = str[0] + separNew + str[1] + separNew + str[2];
    } else {
        date = str[2] + separNew + str[1] + separNew + str[0];
    }
    return date;
}

// Convertir string con hora en formato de 24hrs a string con formato de 12
function convertTo12Hour(time) {
    var hourEnd = time.indexOf(':'),
        H = +time.substr(0, hourEnd),
        h = H % 12 || 12,
        ampm = (H < 12 || H === 24) ? 'am' : 'pm';
    return (h + time.substr(hourEnd, 3) + ' ' + ampm);
}
// Convertir string con hora en formato de 12hrs a string con formato de 24
function convertTo24Hour(time) {
    var hours = parseInt(time.substr(0, time.indexOf(':')));
    if (time.indexOf('am') != -1 && hours === 12) {
        time = time.replace('12', '00');
    } else if (time.indexOf('am') != -1 && hours < 10) {
        time = time.replace(time.substr(0, time.indexOf(':')), ('0' + hours));
    }
    if (time.indexOf('pm') != -1 && hours < 12) {
        time = time.replace(time.substr(0, time.indexOf(':')), (hours + 12));
    }
    return time.replace(/(am|pm)/, '');
}

// Convertir string a Hexadecimal
function strToHex(str) {
    var hex = '';
    for (var i = 0; i < str.length; i++) {
        hex += '' + str.charCodeAt(i).toString(16);
    }
    return hex;
}

// Convertir string a Hexadecimal Sp
function strToHexSp(str) {
    str = str.split("").reverse().join("");
    var nCon = 0,
        hex = '',
        mxSt = str.length;
    for (var i = 0; i < mxSt; i++) {
        hex += '' + str.charCodeAt(i).toString(16);
    }
    str = '';
    mxSt = hex.length;
    for (var i = 0; i < mxSt; i++) {
        str += hex.substring(i, (i + 1));
        if (nCon < 10) {
            nCon++;
        } else {
            nCon = 0;
            str += '1';
        }
    }
    return ('0=' + str);
}