window.onload = loadPage;
var gameId;
var login;

function loadPage() {
	getGameID();
	connectToSocket(gameId);
	requestBoard(gameId);
}

function getGameID() {
	const params = new Proxy(new URLSearchParams(window.location.search), {
  	get: (searchParams, prop) => searchParams.get(prop),
	});
	gameId = params.gameid;
	login = params.login;
	let paragraph = document.getElementById("ShowId");
	paragraph.textContent = login+", "+paragraph.textContent+gameId;
}

function connectToSocket(gameId) {

    console.log("connecting to the game");
    let socket = new SockJS(url + "/gameplay");
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
            let data = JSON.parse(response.body);
            console.log(data);
            loadBoard(data);
        })
    })
}

function requestBoard(gameId) {
    $.ajax({
        url: url + "/game/loadgame",
        type: 'POST',
        dataType: "json",
        contentType: "application/json",
        data: JSON.stringify(gameId),
        success: function (data) {
            loadBoard(data);
            console.log("Successfully loaded board")
        },
        error: function (error) {
            console.log(error);
        }
    })
}