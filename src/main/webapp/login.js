window.onload = function() {
    document.getElementById("loginForm").addEventListener("submit", login)
    }


function login(event) {
    let formData = new FormData(document.querySelector('#loginForm'));
    let encData = new URLSearchParams(formData.entries());

    fetch("/restservices/authentication", {method: 'POST', body: encData})
        .then(function (response) {
             if (response.ok) {
                 window.sessionStorage.setItem('found', 'yes');
                 return response.json();
            }else {
                window.sessionStorage.setItem('found', 'no');
             }
        })
        .then(myJson => window.sessionStorage.setItem("sessionToken", myJson.JWT))
        .catch(error => sessionStorage.setItem('error', error));
};