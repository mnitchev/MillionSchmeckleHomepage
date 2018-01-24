function load() {
    registerEnterListener("username");
    registerEnterListener("password");
}

function registerEnterListener(username) {
    document.getElementById(username).addEventListener("keydown", function (e) {
        if (e.keyCode == 13) {
            submitForm();
        }
    }, false);
}

function submitForm() {
    var username = document.getElementById("username").value
    var password = document.getElementById("password").value
    var user = {
        "username": username,
        "password": password
    }

    xhr = new XMLHttpRequest();
    xhr.open('POST', "api/v1/login", true);
    xhr.setRequestHeader("Content-type", "application/json");
    requestObject = {
        "username": username,
        "password": password
    }
    xhr.onreadystatechange = () => {
        if (xhr.readyState == 4 && xhr.status == 200) {
            var baseUrl = getBaseUrl();
            var token = xhr.responseText;
            setCookie("Authorization", token, 1);
            setCookie("username", username, 1);

            var element = document.getElementById("success");
            element.innerHTML = '<span class="success_span">Login Failed</span>'

            window.location.href = baseUrl + "homepage.html";
        } else if (xhr.readyState == 4 && xhr.status >= 400) {
            var element = document.getElementById("success");
            element.innerHTML = '<span class="failure_span">Login Failed</span>'
        }
    }
    var data = JSON.stringify(requestObject);
    xhr.send(data);
}

function setCookie(cname, cvalue, exhours) {
    var d = new Date();
    d.setTime(d.getTime() + (exhours * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expires + ";path=/";
}

function eraseCookie(name) {
    document.cookie = name + '=; Max-Age=-99999999;';
}

function getBaseUrl() {
    var getUrl = window.location;
    var baseUrl = getUrl.protocol + "//" + getUrl.host + "/";
    return baseUrl;
}