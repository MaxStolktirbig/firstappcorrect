window.onload = function() {
    document.getElementById("loginForm").addEventListener("submit", login)
    }
function login(event) {
    let formData = new FormData(document.querySelector('#loginForm'));
    let encData = new URLSearchParams(formData.entries());

    fetch("http://localhost:1337/restservices/authentication", {method: 'POST', body: encData})
        .then(function (response) {
             if (response.ok) {
                document.querySelector('#loginForm').append("<p style='color:green'>Login successful!</p>");
                return response.json();
            }else {
                document.querySelector('#loginForm').append("<p style='color:red'>No user found with that username and password.</p>");
            }
        })
        .then(myJson => window.sessionStorage.setItem("sessionToken", myJson.JWT))
        .catch(error => sessionStorage.setItem('error', error));
};