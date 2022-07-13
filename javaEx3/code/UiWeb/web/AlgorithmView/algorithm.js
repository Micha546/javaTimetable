
$(function () {
    refreshPageInformation();
})

const refreshPageInformation = function () {
    const queryString = window.location.search;
    const urlParams = new URLSearchParams(queryString);

    if(urlParams.has("id"))
    {
        const myData = {"id" : urlParams.get("id")};

        $.ajax({
            data: myData,
            url: "/UiWeb_Web_exploded/AlgorithmView/algorithmWrapperServlet",
            success : function (algorithmInformation) {
                updatePopulationSize(algorithmInformation.population);
                pullTableInformation(algorithmInformation);
                pullSelectionCrossoverInformation(algorithmInformation);
            },
            error : function (errorObj) {
                console.log("ERROR!");
                console.log(myData);
            }
        });
    }
}

const updatePopulationSize = function (populationSize) {
    $("#populationSizeSpinner").val(populationSize);
}

const pullTableInformation = function (algorithmInformation) {
    updateSubjectInformation(algorithmInformation.subjects);
    updateTeacherInformation(algorithmInformation.teachers);
    updateGradeInformation(algorithmInformation.grades);
    updateRuleInformation(algorithmInformation.rules);
    updateMutationInformation(algorithmInformation.mutations);
}

const pullSelectionCrossoverInformation = function (algorithmInformation) {
    updateSelectionInformation(algorithmInformation);
    updateCrossoverInformation(algorithmInformation);
}

const updateSubjectInformation = function (subjects) {

    removeTableBody($("#subjectTable")[0]);

    $.each(subjects || [], function (index, subjectView) {
        const tableRow = document.createElement("tr");
        const idColumn = document.createElement("td");
        const nameColumn = document.createElement("td");

        idColumn.innerHTML = subjectView.ID;
        $(idColumn).appendTo(tableRow);

        nameColumn.innerHTML = subjectView.name;
        $(nameColumn).appendTo(tableRow);

        $(tableRow).appendTo($("#subjectTable"));
    })
}

const updateTeacherInformation = function (teachers) {
    removeTableBody($("#teacherTable")[0]);

    $.each(teachers || [], function (index, teacherView) {
        const tableRow = document.createElement("tr");
        const idColumn = document.createElement("td");
        const nameColumn = document.createElement("td");
        const subjectTeachingColumn = document.createElement("td");

        idColumn.innerHTML = teacherView.ID;
        $(idColumn).appendTo(tableRow);

        nameColumn.innerHTML = teacherView.name;
        $(nameColumn).appendTo(tableRow);

        subjectTeachingColumn.innerHTML = createSubjectCell(teacherView.subjects);
        $(subjectTeachingColumn).appendTo(tableRow);

        $(tableRow).appendTo($("#teacherTable"));
    })
}

const updateGradeInformation = function (grades) {
    removeTableBody($("#gradeTable")[0]);

    $.each(grades || [], function (index, gradeView) {
        const tableRow = document.createElement("tr");
        const idColumn = document.createElement("td");
        const nameColumn = document.createElement("td");
        const subjectsColumn = document.createElement("td");
        const hoursColumn = document.createElement("td");

        idColumn.innerHTML = gradeView.ID;
        $(idColumn).appendTo(tableRow);

        nameColumn.innerHTML = gradeView.name;
        $(nameColumn).appendTo(tableRow);

        let subjectCell = "";
        let hoursCell = "";

        gradeView.subjectsToHours.sort((pair1, pair2) => pair1.key.ID - pair2.key.ID);

        $.each(gradeView.subjectsToHours || [], function (index, pair) {
            subjectCell += (pair.key.name + " (id=" + pair.key.ID + ")<br/>");
            hoursCell += (pair.value + "<br/>");
        })

        subjectsColumn.innerHTML = subjectCell;
        hoursColumn.innerHTML = hoursCell;

        $(subjectsColumn).appendTo(tableRow);
        $(hoursColumn).appendTo(tableRow);

        $(tableRow).appendTo($("#gradeTable"));
    })
}

const updateRuleInformation = function (rules) {
    removeTableBody($("#ruleTable")[0]);

    $.each(rules || [], function (index, ruleView) {
        const tableRow = document.createElement("tr");
        const nameColumn = document.createElement("td");
        const severityColumn = document.createElement("td");

        nameColumn.innerHTML = ruleView.name;
        $(nameColumn).appendTo(tableRow);

        severityColumn.innerHTML = ruleView.severity;
        $(severityColumn).appendTo(tableRow);

        $(tableRow).appendTo($("#ruleTable"));
    })
}

const updateMutationInformation = function (mutations) {
    removeTableBody($("#mutationTable")[0]);

    $.each(mutations || [], function (index, mutationView) {
        const tableRow = document.createElement("tr");
        const nameColumn = document.createElement("td");
        const chanceColumn = document.createElement("td");
        const argumentsColumn = document.createElement("td");
        const deleteMutationColumn = document.createElement("td");

        nameColumn.innerHTML = mutationView.name;
        $(nameColumn).appendTo(tableRow);

        chanceColumn.innerHTML = mutationView.chance + "%";
        $(chanceColumn).appendTo(tableRow);

        argumentsColumn.innerHTML = createArgumentsCell(mutationView.argumentsDescription);
        $(argumentsColumn).appendTo(tableRow);

        deleteMutationColumn.appendChild(createDeleteMutationButton(mutationView));
        $(deleteMutationColumn).appendTo(tableRow);

        $(tableRow).appendTo($("#mutationTable"));
    })
}

const createArgumentsCell = function (arguments) {
    let cellText = "";

    $.each(arguments || [], function (index, arg) {
        cellText += arg + "<br/>";
    })

    return cellText;
}

const createDeleteMutationButton = function (mutationView) {

    const deleteMutationButton = document.createElement("BUTTON");

    deleteMutationButton.innerHTML = "delete";

    $(deleteMutationButton).on("click", function (event) {

        let ajaxData = {
            id: new URLSearchParams(window.location.search).get("id"),
            type: mutationView.name,
            chance: mutationView.chance,
            arguments: mutationView.arguments
        }

        console.log("deleting mutation!");
        console.log("delete data: ");
        console.log(ajaxData);

        $.ajax({
            data: ajaxData,
            url: "/UiWeb_Web_exploded/AlgorithmView/mutationDeletingServlet",
            success: function () {
                refreshPageInformation();
            }
        })
    })

    return deleteMutationButton;
}

const createSubjectCell = function (subjects) {
    let innerHtmlToReturn = "";

    $.each(subjects || [], function (index, subjectView) {
        innerHtmlToReturn += (subjectView.name + " (id=" + subjectView.ID + ")<br/>");
    })

    return innerHtmlToReturn;
}

const updateSelectionInformation = function (algorithmInformation) {

    const dtoSelection = algorithmInformation.selection;
    const selectionChoiceBoxJQuarryElem = $("#selectionChoiceBox");
    const elitismSpinnerJQuarryElem = $("#elitismSpinner");

    elitismSpinnerJQuarryElem[0].max = algorithmInformation.population;

    selectionChoiceBoxJQuarryElem.on("change", e => selectionBoxChangeEvent(e));

    selectionChoiceBoxJQuarryElem.val(dtoSelection.timeTableSelection);
    selectionChoiceBoxJQuarryElem.trigger("change");

    elitismSpinnerJQuarryElem.val(dtoSelection.elitism);

    if(dtoSelection.timeTableSelection === "Truncation") {
        $("#topPercentSpinner").val(dtoSelection.argsList[0]);
    }
    else if(dtoSelection.timeTableSelection === "Tournament") {
        $("#pteSpinner").val(dtoSelection.argsList[0]);
    }
}

const updateCrossoverInformation = function (algorithmInformation) {
    const dtoCrossover = algorithmInformation.crossOver;
    const crossoverChoiceBoxJQuarryElem = $("#crossoverChoiceBox");
    const cutSizeSpinnerJQuarryElem = $("#cutSizeSpinner");

    crossoverChoiceBoxJQuarryElem.on("change", e => crossoverBoxChangeEvent(e));

    crossoverChoiceBoxJQuarryElem.val(dtoCrossover.crossOver);
    crossoverChoiceBoxJQuarryElem.trigger("change");

    cutSizeSpinnerJQuarryElem.val(dtoCrossover.cutSize);

    if(dtoCrossover.crossOver === "AspectOriented") {
        $("#selectOrientation").val(dtoCrossover.args[0]);
    }
}

//SECTION
// mutation logic

$(function (){
    const mutationChoiceBoxJQuarryElem = $("#mutationChoiceBox");

    mutationChoiceBoxJQuarryElem.on("change", function (event) {
        const newChoice = event.target.value;

        switch(newChoice) {
            case "Flipping":
                handleFlipping();
                break;
            case "Sizer":
                handleSizer();
                break;
        }
    })

    mutationChoiceBoxJQuarryElem.trigger("change");

    $("#mutationAddingButton").on("click", function () {
        const selectedMutationValue = $("#mutationChoiceBox")[0].value;

        let ajaxData = {};

        switch(selectedMutationValue) {
            case "Flipping":
                ajaxData = createFlippingData();
                break;
            case "Sizer":
                ajaxData = createSizerData();
                break;
        }

        console.log(ajaxData);

        $.ajax({
            data: ajaxData,
            url: "/UiWeb_Web_exploded/AlgorithmView/mutationAddingServlet",
            success: function () {
                refreshPageInformation();
            }
        })
    })
})

const clearOptionalInputTypesFromMutationDiv = function () {
    const mutationDiv = $("#mutationAddingDiv")[0];
    const mutationTitle = $("#mutationTitle")[0];
    const mutationChoiceBoxLabel = $("#mutationChoiceBoxLabel")[0];
    const mutationChoiceBox = $("#mutationChoiceBox")[0];
    const mutationChanceSpinnerLabel = $("#mutationChanceSpinnerLabel")[0];
    const mutationChanceSpinner = $("#mutationChanceSpinner")[0];
    mutationDiv.textContent = "";
    $(mutationTitle).appendTo($(mutationDiv));
    $(mutationChoiceBoxLabel).appendTo($(mutationDiv));
    $(mutationChoiceBox).appendTo($(mutationDiv));
    $(mutationChanceSpinnerLabel).appendTo($(mutationDiv));
    $(mutationChanceSpinner).appendTo($(mutationDiv));
}

const handleFlipping = function () {
    clearOptionalInputTypesFromMutationDiv();
    const mutationDiv = $("#mutationAddingDiv")[0];
    const maxTuplesSpinner = createMaxTuplesSpinner();
    const maxTuplesSpinnerLabel = createLabel(maxTuplesSpinner.id, "Max tuples:");
    const ComponentSelect = createComponentSelect();
    const ComponentSelectLabel = createLabel(ComponentSelect.id, "Component:");
    $(maxTuplesSpinnerLabel).appendTo($(mutationDiv));
    $(maxTuplesSpinner).appendTo($(mutationDiv));
    $(ComponentSelectLabel).appendTo($(mutationDiv));
    $(ComponentSelect).appendTo($(mutationDiv));
}

const createMaxTuplesSpinner = function () {
    const spinner = document.createElement("input");

    spinner.type = "number";
    spinner.name = "maxTuplesSpinner";
    spinner.id = "maxTuplesSpinner";
    spinner.min = "0";
    spinner.max = Number.MAX_SAFE_INTEGER.toString();
    spinner.step = "1";
    spinner.value = "5";

    $(spinner).on("change", function (e) {
        validateIntegerSpinnerOnChange(e, 5);
    });

    return spinner;
}

const createComponentSelect = function () {
    const selectComponent = document.createElement("select");
    selectComponent.id = "selectComponent";

    const optionFlippingDay = document.createElement("option");
    optionFlippingDay.value = "FlippingDay";
    optionFlippingDay.innerHTML = "D";

    const optionFlippingHour = document.createElement("option");
    optionFlippingHour.value = "FlippingHour";
    optionFlippingHour.innerHTML = "H";

    const optionFlippingGrade = document.createElement("option");
    optionFlippingGrade.value = "FlippingGrade";
    optionFlippingGrade.innerHTML = "C";

    const optionFlippingTeacher = document.createElement("option");
    optionFlippingTeacher.value = "FlippingTeacher";
    optionFlippingTeacher.innerHTML = "T";

    const optionFlippingSubject = document.createElement("option");
    optionFlippingSubject.value = "FlippingSubject";
    optionFlippingSubject.innerHTML = "S";

    $(optionFlippingDay).appendTo(selectComponent);
    $(optionFlippingHour).appendTo(selectComponent);
    $(optionFlippingGrade).appendTo(selectComponent);
    $(optionFlippingTeacher).appendTo(selectComponent);
    $(optionFlippingSubject).appendTo(selectComponent);

    return selectComponent;
}

const handleSizer = function () {
    clearOptionalInputTypesFromMutationDiv();
    const mutationDiv = $("#mutationAddingDiv")[0];
    const TotalTuplesSpinner = createTotalTuplesSpinner();
    const TotalTuplesSpinnerLabel = createLabel(TotalTuplesSpinner.id, "Total tuples:");
    $(TotalTuplesSpinnerLabel).appendTo($(mutationDiv));
    $(TotalTuplesSpinner).appendTo($(mutationDiv));
}

const createTotalTuplesSpinner = function () {
    const spinner = document.createElement("input");

    spinner.type = "number";
    spinner.name = "totalTuplesSpinner";
    spinner.id = "totalTuplesSpinner";
    spinner.min = Number.MIN_SAFE_INTEGER.toString();
    spinner.max = Number.MAX_SAFE_INTEGER.toString();
    spinner.step = "1";
    spinner.value = "3";

    $(spinner).on("change", function (e) {
        validateIntegerSpinnerOnChange(e, 3);
    });

    return spinner;
}

const createFlippingData = function () {
    const maxTuplesValue = $("#maxTuplesSpinner")[0].value;
    const componentValue = $("#selectComponent")[0].value;
    const chance = $("#mutationChanceSpinner")[0].value;

    const algorithmID = new URLSearchParams(window.location.search).get("id");

    return {
        "id" : algorithmID,
        "type" : "Flipping",
        "chance" : chance,
        "maxTuples" : maxTuplesValue,
        "component" : componentValue
    };
}

const createSizerData = function () {
    const totalTuplesValue = $("#totalTuplesSpinner")[0].value;
    const chance = $("#mutationChanceSpinner")[0].value;

    const algorithmID = new URLSearchParams(window.location.search).get("id");

    return {
        "id" : algorithmID,
        "type" : "Sizer",
        "chance" : chance,
        "totalTuples" : totalTuplesValue
    };
}

//SECTION
// selection logic

$(function () {
    $("#saveSelectionButton").on("click", function (event) {
        let ajaxData = {
            id : new URLSearchParams(window.location.search).get("id"),
            type : $("#selectionChoiceBox")[0].value,
            elitism : $("#elitismSpinner")[0].value,
            arguments: []
        };

        if(ajaxData.type === "Truncation") {
            ajaxData.arguments.push($("#topPercentSpinner")[0].value);
        }
        else if(ajaxData.type === "Tournament") {
            ajaxData.arguments.push($("#pteSpinner")[0].value);
        }

        console.log("selection save ajaxData:");
        console.log(ajaxData);

        $.ajax({
            data: ajaxData,
            url: "/UiWeb_Web_exploded/AlgorithmView/changeSelectionServlet",
            success: function () {
                refreshPageInformation();
            }
        })
    })
})

const selectionBoxChangeEvent = function (event) {
    const newSelectedSelection = event.target.value;

    switch(newSelectedSelection)
    {
        case "Truncation":
            handleTruncation();
            break;
        case "RouletteWheel":
            handleRouletteWheel();
            break;
        case "Tournament":
            handleTournament();
            break;
    }
};

const clearOptionalInputTypesFromSelectionDiv = function () {
    const selectionDiv = $("#selectionDiv")[0];
    const selectionChoiceBoxLabel = $("#selectionChoiceBoxLabel")[0];
    const selectionChoiceBox = $("#selectionChoiceBox")[0];
    const elitismSpinnerLabel = $("#elitismSpinnerLabel")[0];
    const elitismSpinner = $("#elitismSpinner")[0];
    selectionDiv.textContent = "";
    $(selectionChoiceBoxLabel).appendTo($(selectionDiv));
    $(selectionChoiceBox).appendTo($(selectionDiv));
    $(elitismSpinnerLabel).appendTo($(selectionDiv));
    $(elitismSpinner).appendTo($(selectionDiv));
}

const handleTruncation = function () {
    clearOptionalInputTypesFromSelectionDiv();

    const selectionDiv = $("#selectionDiv")[0];
    const topPercentSpinner = createTopPercentSpinner();
    const topPercentSpinnerLabel = createLabel(topPercentSpinner.id, "Top percent:");

    $(topPercentSpinnerLabel).appendTo($(selectionDiv));
    $(topPercentSpinner).appendTo($(selectionDiv));
}

const handleRouletteWheel = function () {
    clearOptionalInputTypesFromSelectionDiv();
}

const handleTournament = function () {
    clearOptionalInputTypesFromSelectionDiv();

    const selectionDiv = $("#selectionDiv")[0];
    const pteSpinner = createPteSpinner();
    const pteSpinnerLabel = createLabel(pteSpinner.id, "Pte:");

    $(pteSpinnerLabel).appendTo($(selectionDiv));
    $(pteSpinner).appendTo($(selectionDiv));
}

const createTopPercentSpinner = function () {
    const spinner = document.createElement("input");

    spinner.type = "number";
    spinner.name = "topPercentSpinner";
    spinner.id = "topPercentSpinner";
    spinner.min = "0";
    spinner.max = "100";
    spinner.step = "1";
    spinner.value = "10";

    $(spinner).on("change", function (e) {
        validateIntegerSpinnerOnChange(e, 10);
    });

    return spinner;
}

const createPteSpinner = function () {
    const spinner = document.createElement("input");

    spinner.type = "number";
    spinner.name = "pteSpinner";
    spinner.id = "pteSpinner";
    spinner.min = "0";
    spinner.max = "1";
    spinner.step = "0.1";
    spinner.value = "0.2";

    $(spinner).on("change", function (e) {
        validateDoubleSpinnerOnChange(e, 0.2);
    });

    return spinner;
}

//SECTION
// crossover logic

$(function () {
    $("#saveCrossOverButton").on("click", function (event) {
        let ajaxData = {
            id : new URLSearchParams(window.location.search).get("id"),
            type : $("#crossoverChoiceBox")[0].value,
            cutSize : $("#cutSizeSpinner")[0].value,
            arguments: []
        };

        if(ajaxData.type === "AspectOriented") {
            ajaxData.arguments.push($("#selectOrientation")[0].value);
        }

        console.log("crossover save ajaxData:");
        console.log(ajaxData);

        $.ajax({
            data: ajaxData,
            url: "/UiWeb_Web_exploded/AlgorithmView/changeCrossoverServlet",
            success: function () {
                refreshPageInformation();
            }
        })
    })
})

const crossoverBoxChangeEvent = function (event) {
    const newSelectedCrossover = event.target.value;

    switch(newSelectedCrossover)
    {
        case "DayTimeOriented":
            handleDayTimeOriented();
            break;
        case "AspectOriented":
            handleAspectOriented();
            break;
    }
};

const clearOptionalInputTypesFromCrossoverDiv = function () {
    const crossoverDiv = $("#crossoverDiv")[0];
    const crossoverChoiceBoxLabel = $("#crossoverChoiceBoxLabel")[0];
    const crossoverChoiceBox = $("#crossoverChoiceBox")[0];
    const cutSizeSpinnerLabel = $("#cutSizeSpinnerLabel")[0];
    const cutSizeSpinner = $("#cutSizeSpinner")[0];
    crossoverDiv.textContent = "";
    $(crossoverChoiceBoxLabel).appendTo($(crossoverDiv));
    $(crossoverChoiceBox).appendTo($(crossoverDiv));
    $(cutSizeSpinnerLabel).appendTo($(crossoverDiv));
    $(cutSizeSpinner).appendTo($(crossoverDiv));
}

const handleDayTimeOriented = function () {
    clearOptionalInputTypesFromCrossoverDiv();
}

const handleAspectOriented = function () {
    clearOptionalInputTypesFromCrossoverDiv();

    const crossoverDiv = $("#crossoverDiv")[0];
    const orientationSelect = createOrientationSelect();
    const orientationSelectLabel = createLabel(orientationSelect.id, "Orientation:");

    $(orientationSelectLabel).appendTo($(crossoverDiv));
    $(orientationSelect).appendTo($(crossoverDiv));
}

const createOrientationSelect = function () {
    const selectOrientation = document.createElement("select");
    selectOrientation.id = "selectOrientation";

    const optionCLASS = document.createElement("option");
    optionCLASS.value = "CLASS";
    optionCLASS.innerHTML = "CLASS";

    const optionTEACHER = document.createElement("option");
    optionTEACHER.value = "TEACHER";
    optionTEACHER.innerHTML = "TEACHER";

    $(optionCLASS).appendTo(selectOrientation);
    $(optionTEACHER).appendTo(selectOrientation);

    return selectOrientation;
}

//SECTION
// population size change logic

$(function () {
    $("#savePopulationSizeButton").on("click", function () {

        let ajaxData = {
            id: new URLSearchParams(window.location.search).get("id"),
            populationSize: $("#populationSizeSpinner")[0].value
        }

        console.log("population size change data:");
        console.log(ajaxData);

        $.ajax({
            data: ajaxData,
            url: "/UiWeb_Web_exploded/AlgorithmView/populationChangeServlet",
            success: function () {
                refreshPageInformation();
            }
        })
    })
})

const getBackToAlgorithmRunning = function () {
    myRedirect("/AlgorithmRun/AlgorithmRunning.html?id=" +  new URLSearchParams(window.location.search).get("id"));
}

// validators for spinners

$(function () {
    $("#populationSizeSpinner").on("change", function (e){
        validateIntegerSpinnerOnChange(e, 100);
    });

    $("#cutSizeSpinner").on("change", function (e){
        validateIntegerSpinnerOnChange(e, 2);
    });

    $("#elitismSpinner").on("change", function (e){
        validateIntegerSpinnerOnChange(e, 0);
    });

    $("#mutationChanceSpinner").on("change", function (e){
        validateIntegerSpinnerOnChange(e, 20);
    });
})