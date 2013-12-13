/**
 * Created with JetBrains WebStorm.
 * User: flexo
 * Date: 12/11/13
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
var ball = document.querySelector('.ball');
var garden = document.querySelector('.garden');
var output = document.querySelector('.output');

var maxX = garden.clientWidth - ball.clientWidth;
var maxY = garden.clientHeight - ball.clientHeight;

var ws = new WebSocket("ws://localhost:8080/ping");
// страница навешивает на новый объект три колл-бека:

// первый вызовется, когда соединение будет установлено:
ws.onopen = function() { alert("Connection opened...") };

// второй - когда соединено закроется
ws.onclose = function() { alert("Connection closed...") };

// и, наконец, третий - каждый раз, когда браузер получает какие-то данные через веб-сокет
// ws.onmessage = function(evt) {
// };

function handleOrientation(event) {
    var x = event.beta; // In degree in the range [-180,180]
    var y = event.gamma; // In degree in the range [-90,90]

    output.innerHTML = "beta : " + x + "\n";
    output.innerHTML += "gamma: " + y + "\n";

    // Because we don't want to have the device upside down
    // We constrain the x value to the range [-90,90]
//    if (x > 90) { x = 90};
//    if (x < -90) { x = -90};

    // To make computation easier we shift the range of
    // x and y to [0,180]
    //x += 90;
    //y += 90;

    // 10 is half the size of the ball
    // It center the positionning point to the center of the ball
    ball.style.top = (maxX*x/180 - 10) + "px";
    ball.style.left = (maxY*y/180 - 10) + "px";
//
//    var angels = {}
//    angels.beta = x;
//    angels.gamma = y;


    ws.send(y.toString());
}

function handleTouch(event) {
    ws.send("gameON");
}

window.addEventListener('deviceorientation', handleOrientation);

window.addEventListener('touchend', handleTouch);
