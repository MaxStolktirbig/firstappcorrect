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
            loadWeatherData(latitude.innerText, longtitude.innerText, document.getElementById("cty").innerText);
        });

}

function loadWeatherData(lat, lon, city) {
    headertext.innerText = "Het weer in "+city;
    var fetchstring = 'https://api.openweathermap.org/data/2.5/weather?lat='+lat+'&lon='+lon+'&units=metric&appid='+weatherkey.toString();
    fetch(fetchstring).then(function (response) {
        return response.json();
    }).then(
        function (weatherData) {
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
    )
}


function reloadWeatherData(event){
    // console.log(event.target.parentNode.childNodes[1]);
    var target = event.target.parentNode;
    var city = target.childNodes[1].textContent;
    var latlon = target.values.split(';');
    console.log(target.values);
    var lat = latlon[0];
    var lon = latlon[1];
    if(city.includes('DC')){
        city = 'Washington';
    }
    loadWeatherData(lat, lon, city);
}


function homeWeatherData(event){
    var city = stad.innerText;
    var lat = latitude.innerText;
    var lon = longtitude.innerText;
    loadWeatherData(lat, lon, city);
}


function loadDestinations(){
    var table = document.getElementById("bestemmingen");
    // if(localStorage.getItem("countryRetrieve"))
    fetch("http://localhost:1337/restservices/countries/")
        .then(function (response) {
            return response.json();
        })
        .then(function (data) {

            var today = new Date();
            var time = today.getHours() + ":" + today.getMinutes();

            localStorage.setItem("countryList", data+"");
            console.log(data);
            console.log(time);
            localStorage.setItem("countryRetrieve", time);

            for(var i = 0; i < Object.keys(data).length; i++){
                var currdata = data[i];
                var tr = table.insertRow(i+1);
                tr.className ='clickable';
                tr.id = currdata['code'];
                tr.values = currdata['lat'] +';'+currdata['lng'];
                var tdList = ["name", "capital", "region", "surface", "population"];
                console.log(tdList.length);
                for(var j = 0; j < tdList.length; j++){
                    var cell = tr.insertCell(j);
                    var type = tdList[j];
                    cell.innerText = currdata[type];
                    if(type.includes('region')){
                        cell.className+=' reg';
                    } else if(type.includes('surface')){
                        cell.className+=' sur';
                    } else if(type.includes('population')){
                        cell.className+=' pop';
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
