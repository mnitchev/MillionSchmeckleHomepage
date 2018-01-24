function load() {
    registerEnterListener("username");
    registerEnterListener("password");
    registerEnterListener("password-repeat");
}

function registerEnterListener(username) {
    document.getElementById(username).addEventListener("keydown", function (e) {
        if (e.keyCode == 13) {
            submitForm();
        }
    }, false);
}


function validateForm() {
    var password = document.getElementById("password").value
    var passwordPassword = document.getElementById("password-repeat").value

    if (password != repeatPassword) {
        alert("Passwords do not match!");
        return false;
    }
    return true;
}

function submitForm() {
    var username = document.getElementById("username").value
    var password = document.getElementById("password").value

    xhr = new XMLHttpRequest();
    xhr.open('POST', "api/v1/user", true);
    xhr.setRequestHeader("Content-type", "application/json");
    requestObject = {
        "username": username,
        "password": password
    }
    xhr.onreadystatechange = () => {
        if (xhr.readyState == 4 && xhr.status == 200) {
            var baseUrl = getBaseUrl();
            var element = document.getElementById("success");
            element.innerHTML = '<span class="success_span">Login Failed</span>'
            window.location.href = baseUrl + "login.html";
        } else if (xhr.readyState == 4 && xhr.status >= 400) {
            var element = document.getElementById("success");
            element.innerHTML = '<span class="failure_span">Login Failed</span>'
        }
    }
    var data = JSON.stringify(requestObject);
    xhr.send(data);
}

function getBaseUrl() {
    var getUrl = window.location;
    var baseUrl = getUrl.protocol + "//" + getUrl.host + "/";
    return baseUrl;
}