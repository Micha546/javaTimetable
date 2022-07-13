
// user list refresh logic

const refreshUsersList = function(usernames) {
    $("#connectedUsersList").empty();

    console.log(usernames);

    $.each(usernames || [], function(index, username) {
        $('<li>' + username + '</li>')
            .appendTo($("#connectedUsersList"));
    });
}

const ajaxUsersList = function() {
    $.ajax({
        url: "/UiWeb_Web_exploded/MainPage/userListServlet",
        success: function(usersListFromServer) {
            refreshUsersList(usersListFromServer);
        },
        error: function (errorObj) {
            console.log(errorObj);
            console.log(errorObj.responseText);
        }
    });
}

$(function() {
    setInterval(ajaxUsersList, 1000);
});

// file upload logic

$(function () {
    $("#fileForm").on("submit", function () {

        let formData = new FormData();

        formData.append( 'file', $( '#fileInput' )[0].files[0] );

        $.ajax({
            data: formData,
            method: "POST",
            url: this.action,
            timeout: 2000,
            processData: false,
            contentType: false,
            error: function (errorObj) {
                $("#fileUploadErrorDiv").text("ERROR " + errorObj.responseText)
            },
            success: function (goodXmlFile) {
                $("#fileUploadErrorDiv").text("GOOD " +goodXmlFile)
            }
        });

        return false;
    });
})

// files uploaded list refresh logic

const refreshFileList = function(fileInfos) {
    $("#uploadedFilesList").empty();

    $.each(fileInfos || [], function(index, fileInfo) {
        const listRow = document.createElement('li');
        listRow.appendChild(document.createTextNode("id = " + fileInfo.id));
        listRow.appendChild(document.createTextNode(", uploader name = " + fileInfo.uploaderName));
        listRow.appendChild(document.createTextNode(", days = " + fileInfo.D));
        listRow.appendChild(document.createTextNode(", hours = " + fileInfo.H));
        listRow.appendChild(document.createTextNode(", grades = " + fileInfo.C));
        listRow.appendChild(document.createTextNode(", teachers = " + fileInfo.T));
        listRow.appendChild(document.createTextNode(", subjects = " + fileInfo.S));
        listRow.appendChild(document.createTextNode(", number of users working on = " + fileInfo.numberOfUsersWorking));
        listRow.appendChild(document.createTextNode(", hard rules = " + fileInfo.hardRules));
        listRow.appendChild(document.createTextNode(", soft rules = " + fileInfo.softRules));
        listRow.appendChild(document.createTextNode(", max fitness = " + fileInfo.maxFitness));
        listRow.appendChild(createWorkOnAlgorithmButton(fileInfo.id));
        $(listRow).appendTo($("#uploadedFilesList"));
    });
}

const refreshFileTable = function (fileInfos) {
    removeTableBody($("#uploadedFilesTable")[0]);

    $.each(fileInfos || [], function(index, fileInfo) {
        const tableRow = document.createElement("tr");
        const uploaderNameColumn = document.createElement("td");
        const daysColumn = document.createElement("td");
        const hoursColumn = document.createElement("td");
        const gradesColumn = document.createElement("td");
        const teachersColumn = document.createElement("td");
        const subjectsColumn = document.createElement("td");
        const hardRulesColumn = document.createElement("td");
        const softRulesColumn = document.createElement("td");
        const numOfWorkingColumn = document.createElement("td");
        const maxFitnessColumn = document.createElement("td");
        const workOnThisColumn = document.createElement("td");

        uploaderNameColumn.innerHTML = fileInfo.uploaderName;
        $(uploaderNameColumn).appendTo(tableRow);

        daysColumn.innerHTML = fileInfo.D;
        $(daysColumn).appendTo(tableRow);

        hoursColumn.innerHTML = fileInfo.H;
        $(hoursColumn).appendTo(tableRow);

        gradesColumn.innerHTML = fileInfo.C;
        $(gradesColumn).appendTo(tableRow);

        teachersColumn.innerHTML = fileInfo.T;
        $(teachersColumn).appendTo(tableRow);

        subjectsColumn.innerHTML = fileInfo.S;
        $(subjectsColumn).appendTo(tableRow);

        hardRulesColumn.innerHTML = fileInfo.hardRules;
        $(hardRulesColumn).appendTo(tableRow);

        softRulesColumn.innerHTML = fileInfo.softRules;
        $(softRulesColumn).appendTo(tableRow);

        numOfWorkingColumn.innerHTML = fileInfo.numberOfUsersWorking;
        $(numOfWorkingColumn).appendTo(tableRow);

        maxFitnessColumn.innerHTML = fileInfo.maxFitness;
        $(maxFitnessColumn).appendTo(tableRow);

        workOnThisColumn.appendChild(createWorkOnAlgorithmButton(fileInfo.id));
        $(workOnThisColumn).appendTo(tableRow);

        $(tableRow).appendTo($("#uploadedFilesTable"));
    });
}

const ajaxFileInfoList = function() {
    $.ajax({
        url: "/UiWeb_Web_exploded/MainPage/fileInfoListServlet",
        success: function(fileInfoListFromServer) {
            //refreshFileList(fileInfoListFromServer);
            refreshFileTable(fileInfoListFromServer);
        }
    });
}

$(function() {
    setInterval(ajaxFileInfoList, 1000);
});

const createWorkOnAlgorithmButton = function (algorithmId) {
    let btn = document.createElement("BUTTON");

    $(btn).text("work on this algorithm");

    btn.onclick = function () {
        myRedirect("/AlgorithmRun/AlgorithmRunning.html?id=" + algorithmId);
    };

    return btn;
}