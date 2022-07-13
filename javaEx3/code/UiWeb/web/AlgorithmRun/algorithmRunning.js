
let globalBestSolutionOfThisGeneration = null;
let globalRuleNameEnforcementMap;

$(function () {
    $.ajax({
        data: {id: new URLSearchParams(window.location.search).get("id")},
        url: "/UiWeb_Web_exploded/AlgorithmView/algorithmWrapperServlet",
        success: function (algorithmInformation) {
            handleStatusChange(algorithmInformation.status);
            updateAlgorithmData(algorithmInformation);
            updateCheckBoxes(algorithmInformation);
            globalBestSolutionOfThisGeneration = algorithmInformation.bestSolution;
            globalTeacherViewList = algorithmInformation.teachers;
            globalGradeViewList = algorithmInformation.grades;
            globalDays = algorithmInformation.bestSolution.timeTable.days;
            globalHours = algorithmInformation.bestSolution.timeTable.hours;
            globalRuleNameEnforcementMap = algorithmInformation.ruleNameToEnforcementMap;
        }
    });
})

const goToAlgorithm = function () {
    myRedirect("/AlgorithmView/Algorithm.html?id=" + new URLSearchParams(window.location.search).get("id"));
}

const sendStatusChangeAjax = function (ajaxData) {
    $.ajax({
        data: ajaxData,
        url: "/UiWeb_Web_exploded/AlgorithmRun/changeStatusServlet",
        success: function (newStatus) {
            handleStatusChange(newStatus);
        },
        error: function (errorObj){
            console.log("in sendStatusChangeAjax error")
        }
    });
}

const handleStatusChange = function (newStatus) {

    const startButton = $("#startAlgorithmButton")[0];
    const pauseButton = $("#pauseAlgorithmButton")[0];
    const stopButton = $("#stopAlgorithmButton")[0];
    const settingsButton = $("#goToSettingsPageButton")[0];
    const generationCheckBox = $("#stopByGenerationCheckBox")[0];
    const fitnessCheckBox = $("#stopByFitnessCheckBox")[0];
    const timeCheckBox = $("#stopByTimeCheckBox")[0];
    const generationSpinner = $("#byGenerationSpinner")[0];
    const fitnessSpinner = $("#byFitnessSpinner")[0];
    const timeSpinner = $("#byTimeSpinner")[0];

    switch (newStatus)
    {
        case "Running":
            handleRunning(startButton, pauseButton, stopButton,
                settingsButton, generationCheckBox, fitnessCheckBox, timeCheckBox,
                generationSpinner, fitnessSpinner, timeSpinner);
            break;
        case "Paused":
            handlePaused(startButton, pauseButton, stopButton,
                settingsButton, generationCheckBox, fitnessCheckBox, timeCheckBox,
                generationSpinner, fitnessSpinner, timeSpinner);
            break;
        case "Stopped":
            handleStopped(startButton, pauseButton, stopButton,
                settingsButton, generationCheckBox, fitnessCheckBox, timeCheckBox,
                generationSpinner, fitnessSpinner, timeSpinner);
            break;
    }
}

const handleRunning = function (startButton, pauseButton, stopButton,
                                settingsButton, generationCheckBox, fitnessCheckBox, timeCheckBox,
                                generationSpinner, fitnessSpinner, timeSpinner) {
    console.log("in handleRunning");

    disableElement(startButton);
    enableElement(pauseButton);
    enableElement(stopButton);
    disableElement(settingsButton);

    disableElement(generationCheckBox);
    disableElement(fitnessCheckBox);
    disableElement(timeCheckBox);
    disableElement(generationSpinner);
    disableElement(fitnessSpinner);
    disableElement(timeSpinner);

    startFetchInterval();
}

const handlePaused = function (startButton, pauseButton, stopButton,
                               settingsButton, generationCheckBox, fitnessCheckBox, timeCheckBox,
                               generationSpinner, fitnessSpinner, timeSpinner) {
    console.log("in handlePaused");

    enableElement(startButton);
    disableElement(pauseButton);
    enableElement(stopButton);
    enableElement(settingsButton);

    enableElement(generationCheckBox);
    enableElement(fitnessCheckBox);
    enableElement(timeCheckBox);
    enableElement(generationSpinner);
    enableElement(fitnessSpinner);
    enableElement(timeSpinner);

    stopFetchInterval();
}

const handleStopped = function (startButton, pauseButton, stopButton,
                                settingsButton, generationCheckBox, fitnessCheckBox, timeCheckBox,
                                generationSpinner, fitnessSpinner, timeSpinner) {
    console.log("in handleStopped");

    enableElement(startButton);
    disableElement(pauseButton);
    disableElement(stopButton);
    enableElement(settingsButton);

    enableElement(generationCheckBox);
    enableElement(fitnessCheckBox);
    enableElement(timeCheckBox);
    enableElement(generationSpinner);
    enableElement(fitnessSpinner);
    enableElement(timeSpinner);

    stopFetchInterval();
}

const areAllCheckBoxesUnchecked = function () {
    const generationCheckBox = $("#stopByGenerationCheckBox")[0];
    const fitnessCheckBox = $("#stopByFitnessCheckBox")[0];
    const timeCheckBox = $("#stopByTimeCheckBox")[0];

    return !(generationCheckBox.checked || fitnessCheckBox.checked || timeCheckBox.checked);
}

const onStartPress = function () {
    if(areAllCheckBoxesUnchecked())
    {
        alert("you have to choose at least 1 stop condition");
    }
    else
    {
        const ajaxData = {
            id: new URLSearchParams(window.location.search).get("id"),
            status: "Running"
        };

        sendStatusChangeAjax(ajaxData);
    }
}

const onPausePress = function () {
    const ajaxData = {
        id: new URLSearchParams(window.location.search).get("id"),
        status: "Paused"
    };

    sendStatusChangeAjax(ajaxData);
}

const onStopPress = function () {
    const ajaxData = {
        id: new URLSearchParams(window.location.search).get("id"),
        status: "Stopped"
    };

    sendStatusChangeAjax(ajaxData);
}

// algorithm data fetching logic

let algorithmFetchingInterval;
const millisBetweenFetching = 200;

const startFetchInterval = function () {
    algorithmFetchingInterval = setInterval(fetchAlgorithmData, millisBetweenFetching);
}

const stopFetchInterval = function () {
    clearInterval(algorithmFetchingInterval);
}

const fetchAlgorithmData = function () {
    $.ajax({
        data: {id: new URLSearchParams(window.location.search).get("id")},
        url: "/UiWeb_Web_exploded/AlgorithmView/algorithmWrapperServlet",
        success: function (algorithmInformation) {
            updateAlgorithmData(algorithmInformation);
            updateProgressBars(algorithmInformation);
            globalBestSolutionOfThisGeneration = algorithmInformation.bestSolution;
            globalRuleNameEnforcementMap = algorithmInformation.ruleNameToEnforcementMap;
        }
    });
}

const updateAlgorithmData = function (algorithmInformation) {
    const generationLabel = $("#generationLabel")[0];
    const fitnessLabel = $("#fitnessLabel")[0];

    generationLabel.innerHTML = algorithmInformation.generation;
    fitnessLabel.innerHTML = algorithmInformation.fitness;
}

const updateProgressBars = function (algorithmInformation) {
    const progressByGeneration = $("#progressByGeneration")[0];
    const progressByFitness = $("#progressByFitness")[0];
    const progressByTime = $("#progressByTime")[0];

    const generationSpinner = $("#byGenerationSpinner")[0];
    const fitnessSpinner = $("#byFitnessSpinner")[0];
    const timeSpinner = $("#byTimeSpinner")[0];

    const generationValue = Number(algorithmInformation.generation) / Number(generationSpinner.value);
    const fitnessValue = Number(algorithmInformation.fitness) / Number(fitnessSpinner.value);
    const timeValue = Number(algorithmInformation.timeRunningInSeconds) / (Number(timeSpinner.value) * 60);

    progressByGeneration.value = generationValue;
    progressByFitness.value = fitnessValue;
    progressByTime.value = timeValue;
}

const updateCheckBoxes = function (algorithmInformation) {
    $("#stopByGenerationCheckBox").attr("checked", algorithmInformation.stopByGeneration);
    $("#stopByFitnessCheckBox").attr("checked", algorithmInformation.stopByFitness);
    $("#stopByTimeCheckBox").attr("checked", algorithmInformation.stopByTime);
    $("#byGenerationSpinner")[0].value = algorithmInformation.byGenerationStopValue;
    $("#byFitnessSpinner")[0].value = algorithmInformation.byFitnessStopValue;
    $("#byTimeSpinner")[0].value = algorithmInformation.byTimeStopValue;
}

// stop conditions

// ByGeneration, ByFitness, ByTime;

const onByGenerationClick = function () {
    const ajaxData = {
        id: new URLSearchParams(window.location.search).get("id"),
        action: $("#stopByGenerationCheckBox").is(":checked") ? "add" : "remove",
        type: "ByGeneration",
        value: $("#byGenerationSpinner")[0].value
    }

    sentStopConditionUpdateAjax(ajaxData);
}

const onByFitnessClick = function () {
    const ajaxData = {
        id: new URLSearchParams(window.location.search).get("id"),
        action: $("#stopByFitnessCheckBox").is(":checked") ? "add" : "remove",
        type: "ByFitness",
        value: $("#byFitnessSpinner")[0].value
    }

    sentStopConditionUpdateAjax(ajaxData);
}

const onByTimeClick = function () {
    const ajaxData = {
        id: new URLSearchParams(window.location.search).get("id"),
        action: $("#stopByTimeCheckBox").is(":checked") ? "add" : "remove",
        type: "ByTime",
        value: $("#byTimeSpinner")[0].value
    }

    sentStopConditionUpdateAjax(ajaxData);
}

const sentStopConditionUpdateAjax = function (ajaxData) {

    console.log("in sentStopConditionUpdateAjax, ajaxData:");
    console.log(ajaxData);

    $.ajax({
        data: ajaxData,
        url: "/UiWeb_Web_exploded/AlgorithmRun/stopConditionUpdateServlet",
        success: function () {
            console.log("update was good");
        },
        error: function (obj) {
            console.log("Error in sentStopConditionUpdateAjax");
            console.log(obj.responseText);
        }
    })
}

// onChange for spinners

$(function (){

    $("#byGenerationSpinner").on("change", function (e) {
        validateIntegerSpinnerOnChange(e, 100);
        if($("#stopByGenerationCheckBox").is(":checked"))
            onByGenerationClick();
    })

    $("#byFitnessSpinner").on("change", function (e) {
        validateIntegerSpinnerOnChange(e, 100);
        if($("#stopByFitnessCheckBox").is(":checked"))
            onByFitnessClick();
    })

    $("#byTimeSpinner").on("change", function (e) {
        validateIntegerSpinnerOnChange(e, 1);
        if($("#stopByTimeCheckBox").is(":checked"))
            onByTimeClick();
    })
})

// other users working list update

$(function () {
    setInterval(createAjaxCallForOtherUserList, 500);
})

const createAjaxCallForOtherUserList = function () {
    $.ajax({
        data: {id: new URLSearchParams(window.location.search).get("id")},
        url: "/UiWeb_Web_exploded/AlgorithmRun/usersWorkingListServlet",
        success: function (response) {
            refreshOtherWorkingList(response);
        },
        error: function () {
            console.log("there was an error in createAjaxDataForOtherUserList")
        }
    });
}

const refreshOtherWorkingList = function (otherUserList) {
    $("#otherWorkingUsersList").empty();

    $.each(otherUserList || [], function (index, user) {
        const listRow = document.createElement('li');
        listRow.appendChild(document.createTextNode("username = " + user.username));
        listRow.appendChild(document.createTextNode(", fitness = " + user.fitness));
        listRow.appendChild(document.createTextNode(", generation = " + user.generation));
        $(listRow).appendTo($("#otherWorkingUsersList"));
    })
}

// show best solution logic

let globalBestSolutionToShowAsTuples = null;  // null means that getBestSolutionButton was not clicked yet
let globalRuleNameEnforcementMapToShow;
let globalTeacherViewList;
let globalGradeViewList;
let globalDays;
let globalHours;

const onGetBestSolutionButtonClick = function () {
    console.log("in onGetBestSolutionButtonClick");
    if(globalBestSolutionOfThisGeneration === null)
        globalBestSolutionToShowAsTuples = null;
    else
    {
        globalBestSolutionToShowAsTuples = globalBestSolutionOfThisGeneration.timeTable.tuples;
        globalRuleNameEnforcementMapToShow = globalRuleNameEnforcementMap;
        $("#showBestSolutionTypeSelect").trigger("change");
        updateRuleEnforcementTable();
    }
}

const updateRuleEnforcementTable = function () {
    const ruleEnforcementTableJQuarryElem = $("#ruleEnforcementTable");
    removeTableBody(ruleEnforcementTableJQuarryElem[0]);

    for(let [key, value] of Object.entries(globalRuleNameEnforcementMapToShow))
    {
        const tableRow = document.createElement("tr");
        const nameColumn = document.createElement("td");
        const enforcementColumn = document.createElement("td");

        nameColumn.innerHTML = key;
        $(nameColumn).appendTo(tableRow);

        enforcementColumn.innerHTML = value + "%";
        $(enforcementColumn).appendTo(tableRow);

        $(tableRow).appendTo(ruleEnforcementTableJQuarryElem);
    }
}

$(function () {
    $("#showBestSolutionTypeSelect").on("change", function (e) {
        const newVal = e.target.value;

        updateTeacherOrGradeChooseDiv(newVal);

        switch(newVal)
        {
            case "Raw":
                handleShowBestSolutionByRaw();
                break;
            case "Teacher":
                handleShowBestSolutionByTeacher();
                break;
            case "Grade":
                handleShowBestSolutionByGrade();
                break;
        }
    })
})

const handleShowBestSolutionByRaw = function () {
    if(globalBestSolutionToShowAsTuples !== null)
    {
        $("#teacherOrGradeChooseDiv").empty();
        const solutionDivJQuarryElem = $("#solutionShowDiv");
        solutionDivJQuarryElem.empty();

        globalBestSolutionToShowAsTuples.sort((t1, t2) => {
            if(t1.day !== t2.day)
                return t1.day - t2.day;
            else if (t1.hour !== t2.hour)
                return t1.hour - t2.hour;
            else if (t1.teacher.id !== t2.teacher.id)
                return t1.teacher.id - t2.teacher.id;
            else
                return t1.grade.id - t2.grade.id;
        });

        const rawList = document.createElement("ul");

        $.each(globalBestSolutionToShowAsTuples || [], function (index, tuple) {
            const listRow = document.createElement('li');
            listRow.appendChild(document.createTextNode("day = " + tuple.day));
            listRow.appendChild(document.createTextNode(", hour = " + tuple.hour));
            listRow.appendChild(document.createTextNode(", teacher = " + tuple.teacher.name));
            listRow.appendChild(document.createTextNode(", grade = " + tuple.grade.name));
            listRow.appendChild(document.createTextNode(", subject = " + tuple.subject.name));
            $(listRow).appendTo($(rawList));
        });

        $(rawList).appendTo(solutionDivJQuarryElem);
    }
}

const handleShowBestSolutionByTeacher = function () {
    if(globalBestSolutionToShowAsTuples !== null)
    {
        const solutionDivJQuarryElem = $("#solutionShowDiv");
        solutionDivJQuarryElem.empty();

        const teacherToShowId = Number($("#teacherGradeSelect")[0].value);
        let tuplesToShow = [];

        $.each(globalBestSolutionToShowAsTuples || [], function (index, tuple) {
            if(tuple.teacher.id === teacherToShowId)
                tuplesToShow.push(tuple);
        });

        const table = createTable("teacher", tuplesToShow);

        $(table).appendTo(solutionDivJQuarryElem);
    }
}

const createTable = function (by, tuplesToShow) {
    const table = document.createElement("table");

    const firstRow = document.createElement("tr");
    const firstCell = document.createElement("td");
    firstCell.innerHTML = "H / D";
    firstRow.appendChild(firstCell);

    for(let day = 1; day <= globalDays; ++day)
    {
        const cell = document.createElement("td");
        cell.innerHTML = day.toString();
        firstRow.appendChild(cell);
    }

    table.appendChild(firstRow);

    for(let hour = 1; hour <= globalHours; ++hour)
    {
        const row = document.createElement("tr");
        const firstCellOfRow = document.createElement("td");
        firstCellOfRow.innerHTML = hour.toString();
        row.appendChild(firstCellOfRow);

        for(let day = 1; day <= globalDays; ++day)
        {
            const cell = document.createElement("td");
            cell.innerHTML = createCellInnerHtml(by, tuplesToShow, day, hour);
            row.appendChild(cell);
        }

        table.appendChild(row);
    }

    return table;
}

const createCellInnerHtml = function (by, tuplesToShow, day, hour) {
    let html = "";

    $.each(tuplesToShow || [], function (index, tuple) {
        if(tuple.day === day && tuple.hour === hour)
        {
            html += (by === "teacher"
                ? "grade: " + tuple.grade.name + "<br/>subject: " + tuple.subject.name + "<br/>"
                : "teacher: " + tuple.teacher.name + "<br/>subject: " + tuple.subject.name + "<br/>");
        }
    });

    return html;
}

const handleShowBestSolutionByGrade = function () {
    if(globalBestSolutionToShowAsTuples !== null)
    {
        const solutionDivJQuarryElem = $("#solutionShowDiv");
        solutionDivJQuarryElem.empty();

        const gradeToShowId = Number($("#teacherGradeSelect")[0].value);
        let tuplesToShow = [];

        $.each(globalBestSolutionToShowAsTuples || [], function (index, tuple) {
            if(tuple.grade.id === gradeToShowId)
                tuplesToShow.push(tuple);
        });

        const table = createTable("grade", tuplesToShow);

        $(table).appendTo(solutionDivJQuarryElem);
    }
}

const updateTeacherOrGradeChooseDiv = function (newVal) {

    $("#teacherOrGradeChooseDiv").empty();

    switch(newVal)
    {
        case "Raw":
            break;
        case "Teacher":
            updateTeacherOrGradeSelect("teacher");
            break;
        case "Grade":
            updateTeacherOrGradeSelect("grade");
            break;
    }
}

updateTeacherOrGradeSelect = function (by) {
    const teacherGradeSelect = document.createElement("select");
    teacherGradeSelect.id = "teacherGradeSelect";

    const label = (by === "teacher"
        ? createLabel("teacherGradeSelect", "Choose teacher to watch:")
        : createLabel("teacherGradeSelect", "Choose grade to watch:"));

    const lst = (by === "teacher" ? globalTeacherViewList : globalGradeViewList);

    $.each(lst || [], function (index, view) {
        const option = document.createElement("option");
        option.value = view.ID;
        option.innerHTML = view.name;
        $(option).appendTo($(teacherGradeSelect));
    });

    $(teacherGradeSelect).on("change", function () {
        if(by === "teacher")
            handleShowBestSolutionByTeacher();
        else
            handleShowBestSolutionByGrade();
    })

    const div = $("#teacherOrGradeChooseDiv");
    $(label).appendTo(div);
    $(teacherGradeSelect).appendTo(div);
}