
document.getElementById("button").addEventListener("click", login);
function login(event) {
    let formData = new FormData(document.querySelector('#loginForm'));
    let encData = new URLSearchParams(formData);
    console.log(encData);
    fetch("/restservices/authentication", {method: 'POST', body: encData})
        .then(function (response) {
             if (response.ok) {
                 window.localStorage.setItem('found', 'yes');
                 return response.json();
            }else {
                window.localStorage.setItem('found', 'no');
             }
        })
            .then(myJson => window.sessionStorage.setItem("sessionToken", myJson.JWT))
        .catch(error => sessionStorage.setItem('error', error));
};