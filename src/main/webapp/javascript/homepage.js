var context;
var width;
var height;
var currentActive;

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function start() {
    currentActive = document.getElementById("white");
    drawBackground();
    updateCanvas();
    setInterval(updateCanvas, 5000);
}

function updateCanvas() {
    drawImage()
}

function drawBackground() {
    var canvas = document.getElementById("homepage_canvas");
    width = canvas.width;
    height = canvas.height;
    context = canvas.getContext('2d');
    context.msImageSmoothingEnabled = false;
    context.mozImageSmoothingEnabled = false;
    context.webkitImageSmoothingEnabled = false;
    context.imageSmoothingEnabled = false;

    drawImage();
}

function drawImage() {
    var w = width * 1000
    var h = height * 1000
    var image = new Image();

    var xhr = new XMLHttpRequest();
    xhr.open('GET', 'http://localhost:8080/api/v1/pixels', true);
    xhr.responseType = 'blob'; //Blob
    xhr.onload = function () {
        image.onload = function () {
            context.drawImage(image, 0, 0, 100, 100, 0, 0, width, height);
            drawChanges()
        };
        image.src = URL.createObjectURL(xhr.response)
    };
    xhr.send();
}

function drawChanges() {
    for (key in changes) {
        var pixel = changes[key];
        drawPixel(pixel);
    }
    for (key in backupPixels) {
        var pixel = backupPixels[key];
        drawPixel(pixel);
    }
}

function submitChanges() {
    pixels = [];
    for (key in changes) {
        var pixel = changes[key];
        pixels.push({
            "x": pixel.x,
            "y": pixel.y,
            "red": pixel.color.red,
            "green": pixel.color.green,
            "blue": pixel.color.blue
        });
    }
    requestObject = getRequestObject(pixels);
    var authName = "Authorization"
    var token = getCookie(authName);

    var xhr = new XMLHttpRequest();
    xhr.open('POST', 'http://localhost:8080/api/v1/pixels', true);
    xhr.setRequestHeader("Authorization", "Bearer " + token)
    xhr.setRequestHeader("Content-type", "application/json");
    var data = JSON.stringify(requestObject);
    xhr.send(data);

    updateCanvas();
    backupPixels = changes;
    setTimeout(() => backupPixels = {}, 5000)
    changes = {};
}

function getRequestObject(pixes) {
    var username = getCookie("username");
    return {
        "username": username,
        "pixels": pixes
    }
}

class Color {
    constructor(red, green, blue) {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }
}

class Pixel {
    constructor(x, y, color) {
        this.x = x;
        this.y = y;
        this.color = new Color(color.red, color.green, color.blue);
    }
}

currentColor = new Color(255, 255, 255);
changes = {}
backupPixels = {}

function setCurrentColor(red, green, blue, elementId) {
    console.log(currentActive);
    currentActive.classList.remove("active");
    currentActive = document.getElementById(elementId);
    currentActive.classList.add("active");
    currentColor = new Color(red, green, blue);
}

function storeChange(event) {
    var x = Math.floor(event.offsetX / 10);
    var y = Math.floor(event.offsetY / 10);
    var pixel = new Pixel(x, y, currentColor);
    changes[String([x, y])] = pixel;
    drawPixel(pixel);
}

function drawPixel(pixel) {
    var x = pixel.x * 10;
    var y = pixel.y * 10;
    var color = pixel.color;
    context.fillStyle = "rgba(" + color.red + "," + color.green +
        "," + color.blue + "," + 1 + ")";
    context.fillRect(x, y, 10, 10);

}