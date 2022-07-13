
const myRedirect = function(newAbsoluteLocation) {
    const base = window.location.origin + "/UiWeb_Web_exploded";
    window.location = base + newAbsoluteLocation;
}

const disableElement = function (elem) {
    elem.setAttribute("disabled", "true");
}

const enableElement = function (elem) {
    elem.removeAttribute("disabled");
}

// validators

const validateIntegerSpinnerOnChange = function (event, defaultValue) {
    const newVal = Number(event.target.value);
    let legal = Number.isInteger(newVal);

    if(legal)
    {
        const max = $(event.target).attr("max");

        if(typeof max !== 'undefined' && max !== false)
        {
            legal = newVal <= Number(max);
            if(!legal)
                alert("value must be smaller or equal to " + Number(max));
        }


        const min = $(event.target).attr("min");

        if(legal && typeof min !== 'undefined' && min !== false)
        {
            legal = newVal >= Number(min);
            if(!legal)
                alert("value must be larger or equal to " + Number(min));
        }
    }
    else
        alert("value must be an integer");

    event.target.value = legal ? newVal : defaultValue;
}

const validateDoubleSpinnerOnChange = function (event, defaultValue) {
    const newVal = Number(event.target.value);
    let legal = isFloat(newVal) || Number.isInteger(newVal);

    if(legal)
    {
        const max = $(event.target).attr("max");

        if(typeof max !== 'undefined' && max !== false)
        {
            legal = newVal <= Number(max);
            if(!legal)
                alert("value must be smaller or equal to " + Number(max));
        }

        const min = $(event.target).attr("min");

        if(legal && typeof min !== 'undefined' && min !== false)
        {
            legal = newVal >= Number(min);
            if(!legal)
                alert("value must be larger or equal to " + Number(min));
        }
    }
    else
        alert("value must be a number");

    event.target.value = legal ? newVal : defaultValue;
}

const isFloat = function(n){
    return Number(n) === n && n % 1 !== 0;
}

const removeTableBody = function (table) {
    const rowCount = table.rows.length;
    for (let i = 1; i < rowCount; i++) {
        table.deleteRow(1);
    }
}

const createLabel = function (elemId, text) {
    const label = document.createElement("label");

    label.for = elemId;
    label.id = elemId + "Label";
    label.innerHTML = text;

    return label;
}