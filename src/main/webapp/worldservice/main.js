var landcode =document.getElementById("lndc");
var land = document.getElementById("lnnm");
var regio = document.getElementById("region");
var stad = document.getElementById("cty");
var postalcode = document.getElementById("pstcd");
var latitude = document.getElementById("lat");
var longtitude = document.getElementById("lon");
var ip = document.getElementById("ip");
var temp = document.getElementById("temp");
var luchtv = document.getElementById("luchtv");
var winds = document.getElementById("winds");
var windr = document.getElementById("windr");
var zonop = document.getElementById("sunup");
var zonon = document.getElementById("sundown");
var headertext = document.getElementById("weertitel");
var weatherkey = 'c4247fb82eb9464c9a2b43b7bc2f28c3';

window.onload= function(){
    initPage();
};

document.getElementById('myLocation').addEventListener("click", homeWeatherData);
document.getElementById("saveButton").addEventListener("click", saveData)
function initPage(){
    initLoadData();
    loadDestinations();
}

function initLoadData() {
    fetch("https://ipapi.co/json").then(function (response) {
        return response.json();
    })
        .then(function (ipData) {
            console.log(ipData);
            landcode.innerText = ipData["country"];
            land.innerText = ipData["country_name"];
            regio.innerText = ipData["region"];
            stad.innerText = ipData["city"];
            postalcode.innerText = ipData["postal"];
            latitude.innerText = ipData["latitude"];
            longtitude.innerText = ipData["longitude"];
            ip.innerText = ipData["ip"];
            headertext.innerText = "Het weer in "+stad.innerText;
            loadWeatherData(latitude.innerText, longtitude.innerText, stad.innerText, land.innerText);
        });

}
function loadWeatherData(lat, lon, city, country) {
    if(city != "") {
        headertext.innerText = "Het weer in " + city;
    }else{
        headertext.innerText = "Het weer in "+ country;
    }
    var fetchstring = 'https://api.openweathermap.org/data/2.5/weather?lat='+lat+'&lon='+lon+'&units=metric&appid='+weatherkey.toString();
    var today=new Date();
    console.log(today);
    var checkdate = today.setMinutes(today.getMinutes()-10);
    console.log(checkdate);
    var storageDate = localStorage.getItem("countryRetrieve"+lat);
    if(storageDate<=checkdate || storageDate == undefined) {
        fetch(fetchstring).then(function (response) {
            return response.json();
        }).then(function (weatherData) {
            console.log("Response "+ weatherData);
            localStorage.setItem(lat, JSON.stringify(weatherData));
            localStorage.setItem("countryRetrieve"+lat, today);
            insertWeatherData(weatherData);
        });
    } else {
        insertWeatherData(JSON.parse(localStorage.getItem(lat)));
    }


    function insertWeatherData(weatherData) {
        console.log(weatherData);
        temp.innerText      = weatherData["main"]["temp"];
        luchtv.innerText    = weatherData["main"]["humidity"];
        winds.innerText     = weatherData["wind"]["speed"];
        windr.innerText     = weatherData["wind"]["deg"];

        // zonon.innerText     = weatherData["sys"]["sunrise"];    //needs formatting
        // zonop.innerText     = weatherData["sys"]["sunset"];
        zonop.innerText     = formatWeatherTime( weatherData["sys"]["sunrise"]);
        zonon.innerText     = formatWeatherTime( weatherData["sys"]["sunset"]);
    }
}


function reloadWeatherData(event){
    var target = event.target.parentNode;
    console.log(target);
    var country = target.childNodes[0].textContent;
    var city = target.childNodes[1].textContent;
    var latlon = target.values.split(';');

    console.log(target.values);
    var lat = latlon[0];
    var lon = latlon[1];
    loadWeatherData(lat, lon, city, country);
}


function homeWeatherData(event){
    var city = stad.innerText;
    var lat = latitude.innerText;
    var lon = longtitude.innerText;
    var country = land.innerText;
    loadWeatherData(lat, lon, city);
}


function loadDestinations(){
    var table = document.getElementById("bestemmingen");
    fetch("http://localhost:1337/restservices/countries/", {method: 'GET'})
        .then(function (response) {
            return response.json();
        })
        .then(function (data) {
            for(var i = 0; i < Object.keys(data).length; i++){
                var currdata = data[i];
                var tr = table.insertRow(i+1);
                tr.className ='clickable';
                tr.id = currdata['code'];
                tr.values = currdata['lat'] +';'+currdata['lng'];
                var tdList = ["name", "capital", "region", "surface", "population", "update", "delete"];
                console.log(tdList.length);
                for(var j = 0; j < tdList.length; j++){
                    var cell = tr.insertCell(j);
                    var type = tdList[j];
                    cell.innerText = currdata[type];
                    if(!type.includes('update') && !type.includes('delete')){
                        cell.addEventListener("click", editFormData);
                    }
                    if(type.includes('region')){
                        cell.className+=' reg';
                    } else if(type.includes('surface')){
                        cell.className+=' sur';
                    } else if(type.includes('population')){
                        cell.className+=' pop';
                    } else if(type.includes('update')){
                        cell.className+=' update';
                        cell.innerText = 'update';
                        cell.addEventListener("click", updateData);
                    } else if(type.includes('delete')){
                        cell.className+=' delete';
                        cell.innerText = 'delete';
                        cell.addEventListener("click", deleteData);
                    }
                }
                tr.addEventListener("click", reloadWeatherData);
            }
            console.log(data);
        });
}
function formatWeatherTime(seconds){
    var miliseconds = seconds*1000;
    var datum = new Date(miliseconds);
    var hours = datum.getHours();
    var minutes = datum.getMinutes();
    if(hours<10){
        hours = '0'+hours;
    }
    return hours+':'+minutes;
}


function editFormData(event){
    var target = event.target.parentNode;
    console.log('hello I am the taget: '+target.childNodes[0].textContent);
    var country = document.getElementById("updateCountry");
    var capital = document.getElementById('updateCapital');
    var region = document.getElementById('updateRegion');
    var surface = document.getElementById('updateSurface');
    var population = document.getElementById('updatePopulation');
    country.value = target.childNodes[0].textContent;
    capital.value = target.childNodes[1].textContent;
    region.value = target.childNodes[2].textContent;
    surface.value = target.childNodes[3].textContent;
    population.value  = target.childNodes[4].textContent;
}

function updateData(event) {
    var target = event.target.parentNode;
    console.log(target.id);
    // var formData = document.querySelector('updateForm');
    var formData = new FormData(document.getElementById('updateForm'));
    var encData = new URLSearchParams(formData.entries());
    console.log('formdata' + formData);
    fetch("http://localhost:1337/restservices/countries/"+target.id, {method: 'PUT', body: encData})
        .then(function(response){
            return response.json();
        }).then(function (boolean) {
            console.log(boolean);
            alert(boolean);
    }).then(window.location.replace("/worldservice/index.html"))
}

function deleteData(event) {
    var target = event.target.parentNode;
    console.log(target.id);
    fetch("http://localhost:1337/restservices/countries/"+target.id, {method: 'DELETE'})
        .then(function(response){
            return response.json();
        }).then(function (boolean) {
        console.log(boolean);
        //initPage();
        alert(boolean);
    }).then(window.location.replace("/worldservice/index.html"))
}

function saveData(event) {
    var formData = new FormData(document.getElementById('updateForm'));
    var encData = new URLSearchParams(formData.entries());
    console.log('formdata' + formData);
    fetch("http://localhost:1337/restservices/countries/save", {method: 'POST', body: encData})
        .then(function(response){
            return response.json();
        }).then(function (boolean) {
        console.log(boolean);
        alert(boolean);
    }).then(window.location.replace("/worldservice/index.html"))

}
