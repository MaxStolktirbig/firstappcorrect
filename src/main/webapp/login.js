
document.getElementById("button").addEventListener("click", login());
function login(event) {
    let formData = new FormData(document.querySelector('#loginForm'));
    let encData = new URLSearchParams(formData);
    console.log(encData);
    fetch("/restservices/authentication", {method: 'POST', body: encData})
        .then(function (response) {
             if (response.ok) {
                 window.sessionStorage.setItem('found', 'yes');
                 return response.json();
            }else {
                window.sessionStorage.setItem('found', 'no');
             }
        })
            // .then(myJson => window.sessionStorage.setItem("sessionToken", myJson.JWT))
            //     .then(window.location.replace("worldservice/index.html"))
        .catch(error => sessionStorage.setItem('error', error));
};