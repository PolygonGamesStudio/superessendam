var ws = new WebSocket("ws://localhost:8080/chat");
// страница навешивает на новый объект три колл-бека:

// первый вызовется, когда соединение будет установлено:
ws.onopen = function() { alert("Connection opened...") };

// второй - когда соединено закроется
ws.onclose = function() { alert("Connection closed...") };

// и, наконец, третий - каждый раз, когда браузер получает какие-то данные через веб-сокет
ws.onmessage = function(evt) {
     x = document.createElement("p");
     x.innerHTML = evt.data;
     document.getElementById("chatbox").appendChild(x);
};

function dispatchText(){
    var userInput = document.getElementById("message").value;
    document.getElementById("message").value = "";
    x = document.createElement("p");
    x.innerHTML = "You sent: " + userInput;
    document.getElementById("chatbox").appendChild(x);

    var objDiv = document.getElementById("chatbox");
    objDiv.scrollTop = objDiv.scrollHeight;

    ws.send(userInput);
}

function showInterface() {
    document.getElementById('newChatBtn').style.display = 'none';
    document.getElementById('connectToChatBtn').style.display = 'none';

    document.getElementById('chatbox').style.display = 'block';
    document.getElementById('conversation').style.display = 'block';
}

function createNewChat() {
    ws.send('service:create');
    showInterface();
}

function connectToChat() {
    ws.send('service:connect');
    showInterface();
}