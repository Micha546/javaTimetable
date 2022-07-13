

let counter = 0;

let onButtonClick = function() {
    const divToChange = document.getElementById("hello-world-div");
    divToChange.innerHTML = "Clicked " + (counter++) + " times";
}

$(function(){
    alert("jquery in js file");
});

$(function(){

});